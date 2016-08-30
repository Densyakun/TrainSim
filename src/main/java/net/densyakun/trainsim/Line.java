package net.densyakun.trainsim;
import java.util.ArrayList;
import java.util.List;
public final class Line {
	private String name;
	private List<Rail> rails = new ArrayList<Rail>();
	private int limitspeed = -1;
	private LineColor color;
	private List<Line> lines_in;//現在未使用
	private List<Line> lines_out;//現在未使用
	private int branch_in = 0;//現在未使用
	private int branch_out = 0;//現在未使用
	public Line(String name, int limitspeed) {
		this.name = name;
		this.limitspeed = limitspeed <= 0 ? 0 : limitspeed;
	}
	public String getName() {
		return name;
	}
	public List<Rail> getRails() {
		return rails;
	}
	public void addRails(Rail... rails) {
		for (int a = 0; a < rails.length; a++) {
			this.rails.add(rails[a]);
		}
	}
	public double getLength() {
		double a = 0;
		for (int b = 0; b < rails.size(); b++) {
			a += rails.get(b).getLength();
		}
		return a;
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
		if ((this.lines_in = lines_in) == null) {
			branch_in = -1;
		} else {
			branch_in = lines_in.size() == 1 ? 0 : -1;
		}
	}
	public List<Line> getLines_in() {
		return lines_in;
	}
	public void setLines_out(List<Line> lines_out) {
		if ((this.lines_out = lines_out) == null) {
			branch_out = -1;
		} else {
			branch_out = lines_out.size() == 1 ? 0 : -1;
		}
	}
	public List<Line> getLines_out() {
		return lines_out;
	}
	public void setBranch_in(int branch_in) {
		this.branch_in = branch_in;
	}
	public int getBranch_in() {
		return branch_in;
	}
	public void setBranch_out(int branch_out) {
		this.branch_out = branch_out;
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
	public void setLoop(boolean loop) {
		if (loop) {
			List<Line> loopline = new ArrayList<Line>();
			loopline.add(this);
			setLines_in(loopline);
			setLines_out(loopline);
		} else {
			setLines_in(null);
			setLines_out(null);
		}
	}
}
