package net.densyakun.trainsim;
public class Train {
	private String name;
	private Car[] cars;
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
	private boolean broken = false;
	public Train(String name, Car[] cars, Line runningline, double position, int maxspeed, double acceleration, double deceleration, MasterController mc) {
		this.name = name;
		this.cars = cars;
		this.runningline = runningline;
		teleport(position);
		this.maxspeed = maxspeed < 0 ? 0 : maxspeed;
		this.acceleration = acceleration <= 0 ? Double.MIN_NORMAL : acceleration;
		this.deceleration = deceleration <= 0 ? Double.MIN_NORMAL : deceleration;
		this.mc = mc;
	}
	public String getName() {
		return name;
	}
	public final Car[] getCars() {
		return cars;
	}
	public final double getLength() {
		double a = 0;
		for (int b = 0; b < cars.length; b++) {
			a += cars[b].getLength();
		}
		return a;
	}
	public final Line getRunningLine() {
		return runningline;
	}
	public final double getPosition() {
		return position;
	}
	public final void teleport(double position) {
		if ((runningline != null) && (getLength() <= runningline.getLength())) {
			this.position = position < getLength() / 2 ? getLength() / 2 : position + getLength() / 2 > runningline.getLength() ? runningline.getLength() - getLength() / 2 : position;
		} else {
			runningline = null;
			this.position = getLength() / 2;
		}
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
		return broken ? 0 : accel;
	}
	public final void setAccel(double accel) {
		this.accel = accel < 0 ? 0 : accel > 1 ? 1 : accel;
	}
	public final double getBrake() {
		return broken ? 0 : brake;
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
	public final boolean isBroken() {
		return broken;
	}
	public final void broken() {
		if (!broken) {
			broken = true;
		}
	}
	public double getBrakeDistance(double setspeed) {
		double c = 0;
		if (speed != 0) {
			//for (double d = speed; Math.max(d, -d) > setspeed;/* b += 200*/) {
			/*	double e = deceleration / 5;
				if (d < 0) {
					d = Math.min(0, d + e);
				} else {
					d = Math.max(0, d - e);
				}
				c += d * 1000 / 60 / 60 / 5;
			}*/
			for (double d = Math.max(speed, -speed); d > setspeed; c += d * 1000 / 60 / 60 / RailwayManager.MAXSPEED_AUTO_UPDATE) {
				d -= deceleration / RailwayManager.MAXSPEED_AUTO_UPDATE;
				if (d < 0) {
					d = 0;
				}
			}
		}
		return speed < 0 ? -c : c;
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
		if (!broken) {
			if (accel != 0) {
				if ((reverser == Reverser.back) && (speed < 5)) {
					speed -= (acceleration * accel * RailwayManager.MAXSPEED_AUTO_UPDATE / 1000) * ((500 - speed) / 500);
				} else if ((reverser == Reverser.forward) && (speed > -5)) {
					speed += (acceleration * accel * RailwayManager.MAXSPEED_AUTO_UPDATE / 1000) * ((500 - speed) / 500);
				}
			}
			if (speed < 0) {
				speed += deceleration * brake * RailwayManager.MAXSPEED_AUTO_UPDATE / 1000;
				if (0 < speed) {
					speed = 0;
				}
			} else if (speed > 0) {
				speed -= deceleration * brake * RailwayManager.MAXSPEED_AUTO_UPDATE / 1000;
				if (0 > speed) {
					speed = 0;
				}
			}
		}
		if (speed < 0) {
			if (accel == 0) {
				speed += 0.1 * RailwayManager.MAXSPEED_AUTO_UPDATE / 1000;//TODO 空気抵抗・摩擦については後々正確に作る予定
			}
		} else if (speed > 0) {
			if (accel == 0) {
				speed -= 0.1 * RailwayManager.MAXSPEED_AUTO_UPDATE / 1000;//TODO 〃
			}
		}
		if (speed != 0) {
			teleport(position + speed * RailwayManager.MAXSPEED_AUTO_UPDATE / 60 / 60);
		}
	}
	public void stop() {
		if (driver != null) {
			driver.stop();
		}
	}
}
