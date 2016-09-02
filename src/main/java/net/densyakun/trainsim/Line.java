package net.densyakun.trainsim;
import java.util.ArrayList;
import java.util.List;
public class Line {
	private String name;
	private List<Rail> rails = new ArrayList<Rail>();
	private double length = 0;
	private int gauge;
	private int limitspeed;
	private LineColor color;
	private List<Line> lines_in = new ArrayList<Line>();
	private List<Line> lines_out = new ArrayList<Line>();
	private int branch_in = -1;
	private int branch_out = -1;
	public Line(String name, int gauge, int limitspeed) {
		this.name = name;
		this.gauge = gauge;
		this.limitspeed = limitspeed <= 0 ? 0 : limitspeed;
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
			setLines_in(new ArrayList<Line>());
			setLines_out(new ArrayList<Line>());
		}
	}
}
