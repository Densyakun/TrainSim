package net.densyakun.trainsim;

//車両データ
public final class Car {
	private String name;//車両形式名。車両番号などを入れると良い(例:キハ47 8087)
	private double length;//車両の長さ(20m車の場合は20)
	public Car(String name, double length) {
		this.name = name;
		this.length = length <= 0 ? Double.MIN_NORMAL : length;
	}
	public String getName() {
		return name;
	}
	public double getLength() {
		return length;
	}
	@Override
	public String toString() {
		return name;
	}
}
