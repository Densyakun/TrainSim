package net.densyakun.trainsim;

import java.io.Serializable;

//ワンハンドルマスコン
public final class OneAxisMC extends MasterController implements Serializable {
	/**
	 * 0.0.2a以降で使用可能
	 */
	private static final long serialVersionUID = 1L;

	private int maxaccel;// 加速ノッチ数
	private int maxbrake;// ブレーキノッチ数(非常ノッチを除く)

	public OneAxisMC(int maxaccel, int maxbrake) {
		this.maxaccel = maxaccel < 3 ? 3 : maxaccel > 30 ? 30 : maxaccel;
		this.maxbrake = maxbrake < 3 ? 3 : maxbrake > 30 ? 30 : maxbrake;
	}

	@Override
	public void setPower(Train train, double accel, double brake) {
		if ((brake = brake < 0 ? 0 : brake > 1 ? 1 : brake) == 0) {
			train.setAccel(Math.ceil((accel < 0 ? 0 : accel > 1 ? 1 : accel) * maxaccel) / maxaccel);
			train.setBrake(0);
		} else {
			train.setAccel(0);
			train.setBrake(Math.ceil(brake * (maxbrake + 1)) / (maxbrake + 1));
		}
	}
}
