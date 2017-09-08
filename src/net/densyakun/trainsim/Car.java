package net.densyakun.trainsim;

import java.io.Serializable;

//車両データ
public final class Car implements Serializable {
	/**
	 * 0.0.2a以降で使用可能
	 */
	private static final long serialVersionUID = 1L;

	private String name; // 車両形式名。車両番号などを入れると良い(例:キハ47 8087)
	private double length; // 車両の長さ(20m車の場合は20)

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
