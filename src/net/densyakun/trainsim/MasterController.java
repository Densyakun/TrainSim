package net.densyakun.trainsim;

//マスコン
public abstract class MasterController {
	public void setPower(Train train, double accel, double brake) {
		train.setAccel(accel < 0 ? 0 : accel > 1 ? 1 : accel);
		train.setBrake(brake < 0 ? 0 : brake > 1 ? 1 : brake);
	}
}
