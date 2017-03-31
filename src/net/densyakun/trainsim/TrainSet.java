package net.densyakun.trainsim;
import java.io.Serializable;
public class TrainSet implements Serializable {
	/**
	 * 0.0.2a以降で使用可能
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * フリーゲージトレイン用の軌間指定値
	 */
	public static final int FREE_GAUGE_TRAIN = -1;

	private String name;//列車形式名（識別に使用）編成構成別に分けるのを推薦。（編成番号は当クラスを避け、Trainクラスに使用することを強く推薦）
	private Car[] cars;//列車編成。配列の0の方が1号車となり、路線のin側が1号車となる。
	private double length = 0;//列車の全長
	private int gauge;//軌間(ゲージ)。単位はmm（ミリメートル）フリーゲージトレインにすることもできる。
	private int maxspeed;//最高速度。いずれは設計最高速度となる予定。
	private double acceleration;//起動加速度
	private double deceleration;//減速度

	private LineColor color;//列車の帯色

	//TODO 前後の運転台・乗務員室の操作を分ける

	private MasterController mc;//マスコンの種類。マスコンの状態ではない。詳しくはTrainDriverクラスを参照。 //TODO 前後の運転台ごとに分ける予定

	public TrainSet(String name, Car[] cars, int gauge, int maxspeed, double acceleration, double deceleration, MasterController mc) {
		this.name = name;
		this.cars = cars;
		for (int a = 0; a < cars.length; a++) {
			length += cars[a].getLength();
		}
		this.gauge = gauge;
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
	public double getLength() {
		return length;
	}
	public int getGauge() {
		return gauge;
	}
	public final int getMaxSpeed() {
		return maxspeed;
	}
	public final double getAcceleration() {
		return acceleration;
	}
	public final double getDeceleration() {
		return deceleration;
	}
	public final MasterController getMasterController() {
		return mc;
	}
	public final LineColor getLineColor() {
		return color;
	}
	public final void setLineColor(LineColor linecolor) {
		color = linecolor;
	}
	@Override
	public boolean equals(Object obj) {
		return obj instanceof TrainSet && name.equals(((TrainSet) obj).name);
	}
}
