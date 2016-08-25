package net.densyakun.trainsim;
public final class Station extends Rail {
	private String name;
	//private double stoppos;
	public Station(double length, String name/*, double stoppos*/) {
		super(length);
		this.name = name;
		//this.stoppos = stoppos < 0 ? 0 : stoppos > length ? length : stoppos;
	}
	public String getName() {
		return name;
	}
	/*public double getStopPos() {
		return stoppos;
	}*/
	@Override
	public String toString() {
		return name;
	}
}
