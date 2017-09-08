package net.densyakun.trainsim;

import java.io.Serializable;

//線路
public class Rail implements Serializable {
	private static final long serialVersionUID = 1L;

	private double length;// 長さ
	private int limitspeed = 0;// 制限速度(0は未制限)
	// private float gradient = 0;//TODO 勾配（現在この機能は未使用）

	public Rail(double length) {
		this.length = length <= 0 ? Double.MIN_NORMAL : length;
	}

	public Rail(double length, int limitspeed) {
		this.length = length <= 0 ? Double.MIN_NORMAL : length;
		this.limitspeed = limitspeed < 0 ? 0 : limitspeed;
	}

	public final double getLength() {
		return length;
	}

	public final int getLimitSpeed() {
		return limitspeed;
	}

	public void setLimitSpeed(int limitspeed) {
		this.limitspeed = limitspeed < 0 ? 0 : limitspeed;
	}
	/*
	 * public final float getGradient() { return gradient; } public void
	 * setGradient(float gradient) { this.gradient = gradient < 0 ? 0 :
	 * gradient; }
	 */
}
