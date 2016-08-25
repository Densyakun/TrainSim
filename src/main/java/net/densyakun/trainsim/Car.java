package net.densyakun.trainsim;
public final class Car {
	private double length;
	public Car(double length) {
		this.length = length <= 0 ? Double.MIN_NORMAL : length;
	}
	public double getLength() {
		return length;
	}
}
