package net.densyakun.trainsim;
import net.densyakun.trainsim.pack.RailwayPack;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
public final class RailwayManager implements Runnable {
	public static int NOT_AUTO_UPDATE = 0;
	private boolean playing = false;
	private boolean fast = false;
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
	public boolean isFast() {
		return fast;
	}
	public void setFast(boolean fast) {
		this.fast = fast;
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
			if (old.getTime() != update.getTime()) {
				this.update = update;
				time.setTime(time.getTime() + (update.getTime() - old.getTime()) * (fast ? 3 : 1));
				for (int a = 0; a < (update.getTime() - old.getTime()) * (fast ? 3 : 1); a++) {
					for (int b = 0; b < trains.size(); b++) {
						Train train = trains.get(b);
						TrainDriver driver = train.getDriver();
						if (driver != null) {
							Diagram diagram = driver.getDiagram();
							if (diagram == null) {
								
							} else {
								double c = -1;
								double d = train.getBrakeDistance(0);
								double e = 0;
								double f = 0;
								List<Station> stopstationlist = diagram.getStopStationList();
								if (0 < stopstationlist.size()) {
									Station sta = stopstationlist.get(0);
									for (int g = 0; g < train.getRunningLine().getRails().size(); g++) {
										if (sta == train.getRunningLine().getRails().get(g)) {
											c = sta.getLength() / 2;
											for (int h = 0; h < g; h++) {
												c += train.getRunningLine().getRails().get(h).getLength();
											}
											/*if (train.getSpeed() >= 5) {
												if (train.getPosition() > a + 0.5) {
													a += 5;
												} else if (train.getPosition() < a - 0.5) {
													a -= 5;
												}
											}*/
											break;
										}
									}
								}
								e = TrainSimMath.getSpeed(train.getRunningLine().getLimitSpeed());
								double g = 0;
								for (int h = 0; h < train.getRunningLine().getRails().size(); h++) {
									if (train.getRunningLine().getRails().get(h).getLimitSpeed() != null) {
										if (g + train.getRunningLine().getRails().get(h).getLength() >= train.getPosition() - train.getLength() / 2 && g <= train.getPosition() + train.getLength() / 2) {
											e = Math.min(e, TrainSimMath.getSpeed(train.getRunningLine().getRails().get(h).getLimitSpeed()));
										}
										if (train.getSpeed() < 0 && train.getPosition() - train.getLength() / 2 > g + train.getRunningLine().getRails().get(h).getLength()) {
											double i = train.getBrakeDistance(TrainSimMath.getSpeed(train.getRunningLine().getRails().get(h).getLimitSpeed()));
											if (train.getPosition() - train.getLength() / 2 + i < g + train.getRunningLine().getRails().get(h).getLength()) {
												f = Math.max(f, Math.max(2.0 / 3, -i / (train.getPosition() - g + train.getRunningLine().getRails().get(h).getLength())));
											}
										} else if (train.getSpeed() > 0 && train.getPosition() + train.getLength() / 2 < g) {
											double i = train.getBrakeDistance(TrainSimMath.getSpeed(train.getRunningLine().getRails().get(h).getLimitSpeed()));
											if (train.getPosition() + train.getLength() / 2 + i > g) {
												f = Math.max(f, Math.max(2.0 / 3, i / (g - train.getPosition())));
											}
										}
									}
									g += train.getRunningLine().getRails().get(h).getLength();
								}
								f = Math.min(f, 1);
								double h = train.getBrakeDistance(e);
								if (h < 0) {
									d = Math.min(d, h);
								} else if (h > 0) {
									d = Math.max(d, h);
								}
								double brake = Math.max(c == -1 ? 1 : train.getSpeed() < 0 ? train.getPosition() + d < c ? 1 : -d / (train.getPosition() - c)/* + (train.getSpeed() > 5 && train.getPosition() - a <= 80 ? 0.05 * (Math.min(5, -train.getSpeed()) / 5) : 0)*/ : train.getSpeed() > 0 ? train.getPosition() + d > c ? 1 : d / (c - train.getPosition())/* + (train.getSpeed() > 5 && a - train.getPosition() <= 80 ? 0.05 * (Math.min(5, train.getSpeed() / 5)) : 0)*/ : 0, f);
								if (brake != 1) {
									List<Train> trains = getTrains();
									for (int i = 0; i < trains.size(); i++) {
										Train j = trains.get(i);
										if (j != train && train.getRunningLine().equals(j.getRunningLine()) && (train.getPosition() > d && train.getPosition() > j.getPosition() && (train.getPosition() - train.getLength() / 2) <= (j.getPosition() + 200 + j.getLength() / 2) || (train.getPosition() < d && train.getPosition() < j.getPosition() && (train.getPosition() + train.getLength() / 2) >= (j.getPosition() - 200 - j.getLength() / 2)))) {
											brake = 1;
										}
										j = null;
									}
									trains = null;
								}
								train.getMasterController().setPower(train, brake < 1.0 / 3 && Math.max(train.getSpeed(), -train.getSpeed()) < (e - 2.5) ? 1 : 0, Math.max(train.getSpeed(), -train.getSpeed()) > e ? 1 : brake >= 2.0 / 3 ? brake : 0);
								if (c != -1) {
									if (train.getPosition() > c + 0.5) {
										train.setReverser(Reverser.back);
									} else if (train.getPosition() < c - 0.5) {
										train.setReverser(Reverser.forward);
									}
									if (stopstationlist.size() > 0 && train.getSpeed() == 0 && train.getPosition() >= c - 0.5 && train.getPosition() <= c + 0.5) {
										Station station = diagram.stop();
										for (int i = 0; i < listeners.size(); i++) {
											listeners.get(i).trainStopped(this, train, station);
										}
									}
								}
							}
						}
						boolean broken = train.isBroken();
						double speed = train.getSpeed();
						Reverser reverser = train.getReverser();
						double acceleration = train.getAcceleration();
						double accel = train.getAccel();
						if (accel != 0) {
							if ((reverser == Reverser.back) && (speed < 5)) {
								speed -= acceleration * accel / 1000;
							} else if ((reverser == Reverser.forward) && (speed > -5)) {
								speed += acceleration * accel / 1000;
							}
						}
						train.setAccel(accel);
						double deceleration = train.getDeceleration();
						double brake = train.getBrake();
						if (speed < 0) {
							speed += deceleration * (broken ? 1 : brake) / 1000;
							if (accel == 0) {
								speed += 0.0001;
							}
							if (0 < speed) {
								speed = 0;
							}
						} else if (speed > 0) {
							speed -= deceleration * (broken ? 1 : brake) / 1000;
							if (accel == 0) {
								speed -= 0.0001;
							}
							if (0 > speed) {
								speed = 0;
							}
						}
						train.setBrake(brake);
						train.setSpeed(speed);
						if (speed != 0) {
							train.teleport(train.getPosition() + speed * 1000 / 60 / 60 / 1000);
						}
					}
					for (int b = 0; b < trains.size(); b++) {
						for (int c = 0; c < trains.size(); c++) {
							if (b != c) {
								Train train_a = trains.get(b);
								Train train_b = trains.get(c);
								double train_a_position = train_a.getPosition();
								double train_b_position = train_b.getPosition();
								double train_a_length = train_a.getLength();
								double train_b_length = train_b.getLength();
								//TODO 交点を求める
								if (((train_a_position + train_a_length / 2 >= train_b_position + train_b_length / 2) && (train_a_position - train_a_length / 2 <= train_b_position + train_b_length / 2)) || ((train_a_position - train_a_length / 2 <= train_b_position - train_b_length / 2) && (train_a_position + train_a_length / 2 >= train_b_position - train_b_length / 2))) {
									double train_a_speed = train_a.getSpeed();
									double train_b_speed = train_b.getSpeed();
									train_a_speed = train_b_speed = (train_a_speed + train_b_speed) / 2;
									train_a.broken();
									train_b.broken();
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
		if ((this.updateinterval = updateinterval) != NOT_AUTO_UPDATE) {
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
}
