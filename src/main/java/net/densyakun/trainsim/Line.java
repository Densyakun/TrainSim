package net.densyakun.trainsim;
import java.util.ArrayList;
import java.util.List;
public final class Line {
	private String name;
	private List<Rail> rails = new ArrayList<Rail>();
	private int limitspeed = -1;
	/*private Line inline;
	private Line outline;*/
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
	@Override
	public boolean equals(Object o) {
		return o == null ? false : o instanceof Line ? name.equals(((Line) o).getName()) : false;
	}
	@Override
	public String toString() {
		return name;
	}
}
