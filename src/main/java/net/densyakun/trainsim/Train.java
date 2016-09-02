package net.densyakun.trainsim;
import java.util.ArrayList;
import java.util.List;
public class Train {
	public static final int FREE_GAUGE_TRAIN = -1;
	private String name;
	private Car[] cars;
	private double length = Double.MIN_NORMAL;
	private int gauge;
	private Line runningline;
	private double position;
	private int maxspeed;
	private double speed = 0;
	private double acceleration;
	private double deceleration;
	private double accel = 0;
	private double brake = 0;
	private TrainDriver driver;
	private LineColor color;
	private Reverser reverser = Reverser.off;
	private MasterController mc;
	private AccidentCause accidentcause = AccidentCause.noaccident;
	private List<Line> runroute;
	private List<Line> runninglines;
	private boolean invert = false;
	public Train(String name, Car[] cars, int gauge, Line runningline, double position, int maxspeed, double acceleration, double deceleration, MasterController mc, boolean invert) {
		this.name = name;
		this.cars = cars;
		this.gauge = gauge;
		this.runningline = runningline;
		this.position = position;
		this.maxspeed = maxspeed < 0 ? 0 : maxspeed;
		this.acceleration = acceleration <= 0 ? Double.MIN_NORMAL : acceleration;
		this.deceleration = deceleration <= 0 ? Double.MIN_NORMAL : deceleration;
		this.mc = mc;
		this.invert = invert;
		reloadLength();
		reloadRunRoute();
		reloadRunningLines();
	}
	public String getName() {
		return name;
	}
	public final Car[] getCars() {
		return cars;
	}
	public final double getLength() {
		return length;
	}
	public final void reloadLength() {
		length = 0;
		for (int a = 0; a < cars.length; a++) {
			length += cars[a].getLength();
		}
	}
	public final int getGauge() {
		return gauge;
	}
	private final void setRunningLine(Line runningline) {
		if ((this.runningline = runningline) != null) {
			reloadRunningLines();
		}
	}
	public final Line getRunningLine() {
		return runningline;
	}
	public final double getPosition() {
		return position;
	}
	public final int getMaxSpeed() {
		return maxspeed;
	}
	public final double getSpeed() {
		return speed;
	}
	public final void setSpeed(double speed) {
		this.speed = speed;
	}
	public final double getAcceleration() {
		return acceleration;
	}
	public final double getDeceleration() {
		return deceleration;
	}
	public final double getAccel() {
		return accidentcause.isBroken() ? 0 : accel;
	}
	public final void setAccel(double accel) {
		this.accel = accel < 0 ? 0 : accel > 1 ? 1 : accel;
	}
	public final double getBrake() {
		return accidentcause.isBroken() ? 0 : brake;
	}
	public final void setBrake(double brake) {
		this.brake = brake < 0 ? 0 : brake > 1 ? 1 : brake;
	}
	public final TrainDriver getDriver() {
		return driver;
	}
	public final void setDriver(TrainDriver driver) {
		this.driver = driver;
	}
	public final LineColor getLineColor() {
		return color;
	}
	public final void setLineColor(LineColor linecolor) {
		color = linecolor;
	}
	public final Reverser getReverser() {
		return reverser;
	}
	public final void setReverser(Reverser reverser) {
		this.reverser = reverser;
	}
	public final MasterController getMasterController() {
		return mc;
	}
	public void Accident(AccidentCause accidentcause) {
		this.accidentcause = accidentcause;
	}
	public final AccidentCause getAccidentCause() {
		return accidentcause;
	}
	public final List<Line> getRunRoute() {
		return runroute;
	}
	public List<Line> getRunningLines() {
		return runninglines;
	}
	public final boolean isInvert() {
		return invert;
	}
	@Override
	public String toString() {
		return name;
	}
	//衝突の判定はRailwayManagerが行う
	public void updatetick(RailwayManager railwaymanager) {
		if (driver != null) {
			driver.updatetick(railwaymanager, this);
		}
		if (!accidentcause.isBroken()) {
			if (accel != 0) {
				if (invert) {
					if ((reverser == Reverser.back) && (speed > -5)) {
						speed += (acceleration * accel * RailwayManager.MAXSPEED_AUTO_UPDATE / 1000) * ((500 - speed) / 500);
					} else if ((reverser == Reverser.forward) && (speed < 5)) {
						speed -= (acceleration * accel * RailwayManager.MAXSPEED_AUTO_UPDATE / 1000) * ((500 - speed) / 500);
					}
				} else {
					if ((reverser == Reverser.back) && (speed < 5)) {
						speed -= (acceleration * accel * RailwayManager.MAXSPEED_AUTO_UPDATE / 1000) * ((500 - speed) / 500);
					} else if ((reverser == Reverser.forward) && (speed > -5)) {
						speed += (acceleration * accel * RailwayManager.MAXSPEED_AUTO_UPDATE / 1000) * ((500 - speed) / 500);
					}
				}
			}
		}
		if (speed < 0) {
			speed += deceleration * brake * RailwayManager.MAXSPEED_AUTO_UPDATE / 1000;
			if (accel == 0) {
				speed += 0.1 * RailwayManager.MAXSPEED_AUTO_UPDATE / 1000;//TODO 空気抵抗・摩擦については後々正確に作る予定
			}
			if (0 < speed) {
				speed = 0;
			}
		} else if (speed > 0) {
			speed -= deceleration * brake * RailwayManager.MAXSPEED_AUTO_UPDATE / 1000;
			if (accel == 0) {
				speed -= 0.1 * RailwayManager.MAXSPEED_AUTO_UPDATE / 1000;//TODO 〃
			}
			if (0 > speed) {
				speed = 0;
			}
		}
		if (speed != 0) {
			teleport(railwaymanager, position + speed * RailwayManager.MAXSPEED_AUTO_UPDATE / 60 / 60);
		}
	}
	public final void teleport(RailwayManager railwaymanager, double position) {
		if (position < length / 2) {
			double b = 0;
			int c = 0;
			for (; c < runroute.size(); c++) {
				if (runroute.get(c).equals(runningline)) {
					for (int d = c - 1; 0 <= d; d--) {
						b += runroute.get(d).getLength();
					}
					break;
				}
			}
			if (position < 0) {
				if (0 <= c - 1) {
					boolean d = true;
					Line lineto = runroute.get(c - 1);
					if (0 <= lineto.getBranch_out()) {
						if (runningline.equals(lineto.getLines_out().get(lineto.getBranch_out()))) {
							d = false;
							setRunningLine(lineto);
							teleport(railwaymanager, runningline.getLength() + position);
						}
					}
					if (d) {
						if (0 <= lineto.getBranch_in()) {
							if (runningline.equals(lineto.getLines_in().get(lineto.getBranch_in()))) {
								d = false;
								setRunningLine(lineto);
								teleport(railwaymanager, -position);
								invert = !invert;
								speed = -speed;
							}
						}
					}
					if (d) {
						railwaymanager.trainAccident(this, AccidentCause.derailment);
						speed = 0;
					}
				} else {
					railwaymanager.trainAccident(this, AccidentCause.derailment);
					speed = 0;
				}
			} else if (position < length / 2 - b) {
				teleport(railwaymanager, length / 2 - b);
				railwaymanager.trainAccident(this, AccidentCause.derailment);
				speed = 0;
			} else {
				this.position = position;
			}
		} else if (runningline.getLength() < position + length / 2) {
			double b = 0;
			int c = 0;
			for (; c < runroute.size(); c++) {
				if (runroute.get(c).equals(runningline)) {
					for (int d = c + 1; d < runroute.size(); d++) {
						b += runroute.get(d).getLength();
					}
					break;
				}
			}
			if (runningline.getLength() < position) {
				if (c + 1 < runroute.size()) {
					boolean d = true;
					Line oldline = runningline;
					Line lineto = runroute.get(c + 1);
					if (0 <= lineto.getBranch_in()) {
						if (runningline.equals(lineto.getLines_in().get(lineto.getBranch_in()))) {
							d = false;
							setRunningLine(lineto);
							teleport(railwaymanager, position - oldline.getLength());
						}
					}
					if (d) {
						if (0 <= lineto.getBranch_out()) {
							if (runningline.equals(lineto.getLines_out().get(lineto.getBranch_out()))) {
								d = false;
								setRunningLine(lineto);
								teleport(railwaymanager, lineto.getLength() + oldline.getLength() - position);
								invert = !invert;
								speed = -speed;
							}
						}
					}
					if (d) {
						railwaymanager.trainAccident(this, AccidentCause.derailment);
						speed = 0;
					}
				} else {
					railwaymanager.trainAccident(this, AccidentCause.derailment);
					speed = 0;
				}
			} else if (runningline.getLength() - length / 2 + b < position) {
				teleport(railwaymanager, runningline.getLength() - length / 2 + b);
				railwaymanager.trainAccident(this, AccidentCause.derailment);
				speed = 0;
			} else {
				this.position = position;
			}
		} else {
			this.position = position;
		}
		if (!accidentcause.isBroken()) {
			reloadRunRoute();
			reloadRunningLines();
			for (int a = 0; a < runninglines.size(); a++) {
				if (runninglines.get(a).getGauge() != gauge) {
					railwaymanager.trainAccident(this, AccidentCause.derailment);
					speed = 0;
					break;
				}
			}
		}
	}
	public double getBrakeDistance(double setspeed) {
		double c = 0;
		if (speed != 0) {
			for (double d = Math.max(speed, -speed); d > setspeed; c += d * 1000 / 60 / 60 / RailwayManager.MAXSPEED_AUTO_UPDATE) {
				d -= deceleration / RailwayManager.MAXSPEED_AUTO_UPDATE;
				if (d < 0) {
					d = 0;
				}
			}
		}
		return speed < 0 ? -c : c;
	}
	public void stopStation() {
		if (driver != null) {
			driver.stopStation();
		}
	}
	public void reloadRunRoute() {
		List<Line> lines = new ArrayList<Line>();
		lines.add(runningline);
		aaa(lines, runningline, false, invert);
		aaa(lines, runningline, true, invert);
		runroute = lines;
	}
	private void aaa(List<Line> lines, Line line, boolean right, boolean invert) {
		if (right) {
			if (invert) {
				if (0 <= line.getBranch_in()) {
					Line line_in = line.getLines_in().get(line.getBranch_in());
					if (3 <= lines.size()) {
						for (int a = 0; a < lines.size(); a++) {
							if (lines.get(a).equals(line_in)) {
								return;
							}
						}
					}
					lines.add(line_in);
					aaa(lines, line_in, true, true);
				}
			} else {
				if (0 <= line.getBranch_out()) {
					Line line_out = line.getLines_out().get(line.getBranch_out());
					if (3 <= lines.size()) {
						for (int a = 0; a < lines.size(); a++) {
							if (lines.get(a).equals(line_out)) {
								return;
							}
						}
					}
					lines.add(line_out);
					aaa(lines, line_out, true, false);
				}
			}
		} else {
			if (invert) {
				if (0 <= line.getBranch_out()) {
					Line line_out = line.getLines_out().get(line.getBranch_out());
					if (3 <= lines.size()) {
						for (int a = 0; a < lines.size(); a++) {
							if (lines.get(a).equals(line_out)) {
								return;
							}
						}
					}
					lines.add(0, line_out);
					aaa(lines, line_out, false, true);
				}
			} else {
				if (0 <= line.getBranch_in()) {
					Line line_in = line.getLines_in().get(line.getBranch_in());
					if (3 <= lines.size()) {
						for (int a = 0; a < lines.size(); a++) {
							if (lines.get(a).equals(line_in)) {
								return;
							}
						}
					}
					lines.add(0, line_in);
					aaa(lines, line_in, false, false);
				}
			}
		}
	}
	public void reloadRunningLines() {
		List<Line> lines = new ArrayList<Line>();
		double a = 0;
		double left = position - length / 2;
		double right = position + length / 2;
		for (int b = 0; b < runroute.size(); b++) {
			if (left <= a + runroute.get(b).getLength() && a <= right) {
				lines.add(runroute.get(b));
			}
			a += runroute.get(b).getLength();
		}
		runninglines = lines;
	}
}
