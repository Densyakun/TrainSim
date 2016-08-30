package net.densyakun.trainsim;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.densyakun.trainsim.pack.RailwayPack;
public final class RailwayManager implements Runnable {
	public static int NOT_AUTO_UPDATE = 0;
	public static int MAXSPEED_AUTO_UPDATE = 50;
	public static int REAL_TIMESCALE = 1;
	private boolean playing = false;
	private int timescale = REAL_TIMESCALE;
	private Thread thread;
	private int updateinterval = NOT_AUTO_UPDATE;
	private Date update = new Date();
	private Date time = new Date();
	private List<RailwayListener> listeners = new ArrayList<RailwayListener>();
	private List<Line> lines = new ArrayList<Line>();
	private List<Train> trains = new ArrayList<Train>();

	@Override
	public void run() {
		while (updateinterval != NOT_AUTO_UPDATE) {
			if (playing) {
				update();
				try {
					Thread.sleep(updateinterval);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		thread = null;
	}

	public List<Line> getLines() {
		return lines;
	}
	public boolean addLine(Line line) {
		for (int a = 0; a < lines.size(); a++) {
			if (lines.get(a).getName().equals(line.getName())) {
				return false;
			}
		}
		lines.add(line);
		for (int a = 0; a < listeners.size(); a++) {
			listeners.get(a).addLine(this, line);
		}
		return true;
	}
	public List<Train> getTrains() {
		return trains;
	}
	public void addTrain(Train train) {
		trains.add(train);
		for (int a = 0; a < listeners.size(); a++) {
			listeners.get(a).addTrain(this, train);
		}
		update();
	}
	public void clearRailway() {
		lines.clear();
		trains.clear();
		for (int a = 0; a < listeners.size(); a++) {
			listeners.get(a).clearRailway(this);
		}
	}
	public void loadRailwayPack(RailwayPack pack) {
		//clearRailway();
		List<Line> lines = pack.getLines();
		for (int a = 0; a < lines.size(); a++) {
			addLine(lines.get(a));
		}
		List<Train> trains = pack.getTrains();
		for (int a = 0; a < trains.size(); a++) {
			addTrain(trains.get(a));
		}
	}
	public boolean isPlaying() {
		return playing;
	}
	public Date getUpdateTime() {
		return update;
	}
	public void play() {
		if (!playing) {
			update = new Date();
			playing = true;
			for (int a = 0; a < listeners.size(); a++) {
				listeners.get(a).played(this);
			}
		}
	}
	public void stop() {
		if (playing) {
			update();
			playing = false;
			for (int a = 0; a < listeners.size(); a++) {
				listeners.get(a).stopped(this);
			}
		}
	}
	public int getTimeScale() {
		return timescale;
	}
	public void setTimeScale(int timescale) {
		this.timescale = timescale;
	}
	public Date getTime() {
		return time;
	}
	public void setTime(Date time) {
		this.time = time;
	}
	public void update() {
		if (playing) {
			Date old = update;
			Date update = new Date();
			if (MAXSPEED_AUTO_UPDATE <= update.getTime() - old.getTime()) {
				this.update = update;
				long ms = (update.getTime() - old.getTime()) * timescale;
				time.setTime(time.getTime() + ms);
				for (int a = 0; a < ms / MAXSPEED_AUTO_UPDATE; a++) {
					for (int b = 0; b < trains.size(); b++) {
						trains.get(b).updatetick(this);
					}
					//衝突の判定
					//TODO 交点を求める予定
					for (int c = 0; c < trains.size(); c++) {
						for (int d = 0; d < trains.size(); d++) {
							if (c != d) {
								Train train_a = trains.get(c);
								Train train_b = trains.get(d);
								double train_a_position = train_a.getPosition();
								double train_b_position = train_b.getPosition();
								double train_a_length = train_a.getLength();
								double train_b_length = train_b.getLength();
								boolean e = train_a_position + train_a_length / 2 >= train_b_position + train_b_length / 2 && train_a_position - train_a_length / 2 <= train_b_position + train_b_length / 2;
								boolean f = train_a_position - train_a_length / 2 <= train_b_position - train_b_length / 2 && train_a_position + train_a_length / 2 >= train_b_position - train_b_length / 2;
								double collision_point = (train_a_position + train_a_length / 2 + train_b_position - train_b_length / 2) / 2;
								if (e) {
									train_a.teleport(collision_point - train_a_length / 2);
									train_b.teleport(collision_point + train_b_length / 2);
								}
								if (f) {
									train_a.teleport(collision_point + train_a_length / 2);
									train_b.teleport(collision_point - train_b_length / 2);
								}
								if (e || f) {
									double train_a_speed = train_a.getSpeed();
									double train_b_speed = train_b.getSpeed();
									train_a_speed = train_b_speed = (train_a_speed + train_b_speed) / 4;
									trainBroken(train_a);
									trainBroken(train_b);
								}
							}
						}
					}
				}
				for (int a = 0; a < listeners.size(); a++) {
					listeners.get(a).updated(this);
				}
			}
		}
	}
	public void addListener(RailwayListener listener) {
		listeners.add(listener);
	}

	public void setUpdateinterval(int updateinterval) {
		if ((this.updateinterval = (updateinterval < MAXSPEED_AUTO_UPDATE ? MAXSPEED_AUTO_UPDATE : updateinterval)) != NOT_AUTO_UPDATE) {
			if (thread == null) {
				(thread = new Thread(this)).start();
			}
		}
	}

	public void trainBroken(Train train) {
		if (!train.isBroken()) {
			train.broken();
			for (int a = 0; a < listeners.size(); a++) {
				listeners.get(a).trainBroken(this, train);
			}
		}
	}

	public void trainStopped(Train train, Station station) {
		train.stop();
		for (int a = 0; a < listeners.size(); a++) {
			listeners.get(a).trainStopped(this, train, station);
		}
	}
}
