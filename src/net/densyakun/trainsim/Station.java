package net.densyakun.trainsim;

/**
 * 駅(線路を継承)
 * TODO: 結果的にはレールから切り離し、プラットホームと停止位置のクラスにする予定。
 * TODO: また、停止位置のデータはプラットホームが持っており、プラットホームのデータはこのクラスとは別に作る"駅クラス"がまとめる予定
 *
 * @author Densyakun
 */
public final class Station extends Rail {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	private String name;// 駅名
	// private double stoppos;

	//TODO 停止位置

	public Station(double length, String name/* , double stoppos */) {
		super(length);
		this.name = name;
		// this.stoppos = stoppos < 0 ? 0 : stoppos > length ? length : stoppos;
	}

	public String getName() {
		return name;
	}

	/*
	 * public double getStopPos() { return stoppos; }
	 */
	@Override
	public String toString() {
		return name;
	}
}
