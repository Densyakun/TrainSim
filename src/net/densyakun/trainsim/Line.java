package net.densyakun.trainsim;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

//路線データ。すべての方向は左側がin、右側がoutとなり、左側を0として列車の位置が計算される。
//路線同士を接続するには分岐器のような機能で接続する。接続先が一つの場合は単にレールを接続することになる。
//接続を複数にすれば分岐器となり、分岐先を指定することでレールが繋がる。
//基本的には磁石のようにin側とout側を接続するが、デルタ線などでは一部の路線がin側とin側(もしくはout側とout側)で接続することになり、列車が進入すると列車の前後が逆になる。(座標や速度も逆になる)
//現在のレールと進入するレールがin側とin側(もしくはout側とout側)であるかどうかは進入先のレールの分岐器
//Pの字のような鉄道網でPのうち上半分を一つのレールになるように接続をしている場合などには、列車が別の線路に進入する際にはin側にはout側、out側にはin側に優先して進入される。
public class Line implements Serializable {
	/**
	 * 0.0.2a以降で使用可能
	 */
	private static final long serialVersionUID = 1L;

	private String name;// 路線名（識別に使用）
	private List<Rail> rails = new ArrayList<Rail>();// 使用するレール(直線状に接続される)
	private double length = 0;// 路線の総距離(線路が変更されるたびに再計算される)
	private int gauge;// 軌間(左右のレールの距離。列車側の軌間と合わない場合脱線となる)
	private int limitspeed;// 路線最高速度
	private LineColor color;// 路線の帯色(描画系で使用する事ができる)
	private List<Line> lines_in = new ArrayList<Line>();// in側の接続レール
	private List<Line> lines_out = new ArrayList<Line>();// out側の接続レール
	private int branch_in = -1;// in側のレール接続先
	private int branch_out = -1;// out側のレール接続先

	public Line(String name, int gauge, int limitspeed) {
		this.name = name;
		this.gauge = gauge;
		this.limitspeed = limitspeed <= 0 ? 0 : limitspeed;
	}

	public Line(String name, int gauge, int limitspeed, Rail... rails) {
		this(name, gauge, limitspeed);
		addRails(rails);
	}

	public String getName() {
		return name;
	}

	public void addRails(Rail... rails) {
		for (int a = 0; a < rails.length; a++) {
			this.rails.add(rails[a]);
		}
		reloadLength();
	}

	public void setRails(List<Rail> rails) {
		this.rails = rails;
		reloadLength();
	}

	public List<Rail> getRails() {
		return rails;
	}

	public double getLength() {
		return length;
	}

	// 路線の総距離を再計算
	public void reloadLength() {
		length = 0;
		for (int b = 0; b < rails.size(); b++) {
			length += rails.get(b).getLength();
		}
	}

	public int getGauge() {
		return gauge;
	}

	public int getLimitSpeed() {
		return limitspeed;
	}

	public final LineColor getLineColor() {
		return color;
	}

	public final void setLineColor(LineColor linecolor) {
		color = linecolor;
	}

	public void setLines_in(List<Line> lines_in) {
		branch_in = (this.lines_in = lines_in).size() == 1 ? 0 : -1;
	}

	public List<Line> getLines_in() {
		return lines_in;
	}

	public void setLines_out(List<Line> lines_out) {
		branch_out = (this.lines_out = lines_out).size() == 1 ? 0 : -1;
	}

	public List<Line> getLines_out() {
		return lines_out;
	}

	public void setBranch_in(int branch_in) {
		this.branch_in = lines_in.size() <= branch_in ? -1 : branch_in;
	}

	public int getBranch_in() {
		return branch_in;
	}

	public void setBranch_out(int branch_out) {
		this.branch_out = lines_out.size() <= branch_out ? -1 : branch_out;
	}

	public int getBranch_out() {
		return branch_out;
	}

	@Override
	public boolean equals(Object o) {
		return o == null ? false : o instanceof Line ? name.equals(((Line) o).getName()) : false;
	}

	@Override
	public String toString() {
		return name;
	}

	// 指定したレールが始まる位置を返す(レールが始まる位置+レールの長さがレールが終わる位置となる。-1が返された場合はレールがこの路線に存在しない)
	public double getRailStartPosition(Rail rail) {
		double length = 0;
		for (int a = 0; a < rails.size(); a++) {
			if (rails.get(a) == rail) {
				return length;
			}
			length += rails.get(a).getLength();
		}
		return -1;
	}

	// 環状線にする
	public void setLoop(boolean loop) {
		if (loop) {
			List<Line> loopline = new ArrayList<Line>();
			loopline.add(this);
			setLines_in(loopline);
			setLines_out(loopline);
		} else {
			setLines_in(new ArrayList<Line>());
			setLines_out(new ArrayList<Line>());
		}
	}
}
