package net.densyakun.trainsim;

import java.io.Serializable;

//路線の帯色(二色ある場合はsub***も使用する)
public final class LineColor implements Serializable {
	/**
	 * 0.0.2a以降で使用可能
	 */
	private static final long serialVersionUID = 1L;

	private int mainred;
	private int maingreen;
	private int mainblue;
	private int subred;
	private int subgreen;
	private int subblue;
	public LineColor(int red, int green, int blue) {
		setMainColor(red, green, blue);
	}
	public int getMainRed() {
		return mainred;
	}
	public int getMainGreen() {
		return maingreen;
	}
	public int getMainBlue() {
		return mainblue;
	}
	public void setMainColor(int red, int green, int blue) {
		mainred = red;
		maingreen = green;
		mainblue = blue;
	}
	public int getSubRed() {
		return subred;
	}
	public int getSubGreen() {
		return subgreen;
	}
	public int getSubBlue() {
		return subblue;
	}
	public void setSubColor(int red, int green, int blue) {
		subred = red;
		subgreen = green;
		subblue = blue;
	}
}
