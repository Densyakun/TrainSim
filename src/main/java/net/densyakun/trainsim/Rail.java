package net.densyakun.trainsim;
public class Rail {
	private double length;
	private Integer limitspeed;
	private float gradient = 0;
	public Rail(double length) {
		this.length = length <= 0 ? Double.MIN_NORMAL : length;
	}
	public final double getLength() {
		return length;
	}
	public final Integer getLimitSpeed() {
		return limitspeed;
	}
	public void setLimitSpeed(Integer limitspeed) {
		this.limitspeed = limitspeed == null ? null : limitspeed < 0 ? 0 : limitspeed;
	}
	public final float getGradient() {
		return gradient;
	}
	public void setGradient(float gradient) {
		this.gradient = gradient < 0 ? 0 : gradient;
	}
}
