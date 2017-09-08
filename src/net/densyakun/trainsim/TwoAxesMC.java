package net.densyakun.trainsim;

import java.io.Serializable;

//ツーハンドルマスコン
public final class TwoAxesMC extends MasterController implements Serializable {
	/**
	 * 0.0.2a以降で使用可能
	 */
	private static final long serialVersionUID = 1L;

	public static final int nonstagebrake = -1;// 無段階ブレーキ
	private int maxaccel;// 加速ノッチ数
	private int maxbrake;// ブレーキノッチ数(非常ノッチを除く)

	public TwoAxesMC(int maxaccel, int maxbrake) {
		this.maxaccel = maxaccel < 3 ? 3 : maxaccel > 30 ? 30 : maxaccel;
		this.maxbrake = maxbrake == nonstagebrake ? maxbrake : maxbrake < 3 ? 3 : maxbrake > 30 ? 30 : maxbrake;
	}

	@Override
	public void setPower(Train train, double accel, double brake) {
		brake = brake < 0 ? 0 : brake > 1 ? 1 : brake;
		train.setAccel(Math.ceil((accel < 0 ? 0 : accel > 1 ? 1 : accel) * maxaccel) / maxaccel);
		train.setBrake(maxbrake == nonstagebrake ? brake : Math.ceil(brake * (maxbrake + 1)) / (maxbrake + 1));
	}
}
