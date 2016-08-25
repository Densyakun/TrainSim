package net.densyakun.trainsim;
public final class TwoAxesMC extends MasterController {
	public static final int nonstagebrake = -1;
	private int maxaccel = 3;
	private int maxbrake = 3;
	public TwoAxesMC(int maxaccel, int maxbrake) {
		this.maxaccel = maxaccel < 3 ? 3 : maxaccel > 30 ? 30 : maxaccel;
		this.maxbrake = maxbrake == nonstagebrake ? maxbrake : maxbrake < 3 ? 3 : maxbrake > 30 ? 30 : maxbrake;
	}
	@Override
	public void setPower(Train train, double accel, double brake) {
		brake = brake < 0 ? 0 : brake > 1 ? 1 : brake;
		train.setAccel(1 * Math.ceil(accel < 0 ? 0 : accel > 1 ? 1 : accel * maxaccel) / maxaccel);
		train.setBrake(maxbrake == nonstagebrake ? brake : 1 * Math.ceil(brake * maxbrake) / maxbrake);
	}
}
