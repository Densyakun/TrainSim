package net.densyakun.trainsim;
import java.util.ArrayList;
import java.util.List;
public class Point {
	private List<Rail> to;
	private int tonum = -1;
	private boolean toright;
	public Point(boolean toright) {
		this(new ArrayList<Rail>(), toright);
	}
	public Point(List<Rail> to, boolean toright) {
		this.to = to;
		this.toright = toright;
	}
	public void setTo(int tonum) {
		this.tonum = tonum < -1 ? -1 : tonum >= to.size() ? to.size() - 1 : tonum;
	}
	/*@Override
	public Rail getLeftRail() {
		if ((0 <= tonum) && (tonum < to.size())) {
			return to.get(tonum);
		}
		return null;
	}
	@Override
	public void setLeftRail(Rail rail) {
	}
	@Override
	public Rail getRightRail() {
		return null;
	}
	@Override
	public void setRightRail(Rail rail) {
	}*/
}
