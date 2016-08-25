package net.densyakun.trainsim;
public final class OneAxisMC extends MasterController {
	private int maxaccel = 3;
	private int maxbrake = 3;
	public OneAxisMC(int maxaccel, int maxbrake) {
		this.maxaccel = maxaccel < 3 ? 3 : maxaccel > 30 ? 30 : maxaccel;
		this.maxbrake = maxbrake < 3 ? 3 : maxbrake > 30 ? 30 : maxbrake;
	}
	@Override
	public void setPower(Train train, double accel, double brake) {
		accel = accel < 0 ? 0 : accel > 1 ? 1 : accel;
		brake = brake < 0 ? 0 : brake > 1 ? 1 : brake;
		if (brake == 0) {
			train.setAccel(1 * Math.ceil(accel * maxaccel) / maxaccel);
			train.setBrake(0);
		} else {
			train.setAccel(0);
			train.setBrake(1 * Math.ceil(brake * maxbrake) / (maxbrake + 1));
		}
	}
}
