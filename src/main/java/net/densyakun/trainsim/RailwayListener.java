package net.densyakun.trainsim;
public interface RailwayListener {
	public void played(RailwayManager railwaymanager);
	public void stopped(RailwayManager railwaymanager);
	public void updated(RailwayManager railwaymanager);
	public void addLine(RailwayManager railwaymanager, Line line);
	public void addTrain(RailwayManager railwaymanager, Train train);
	public void clearRailway(RailwayManager railwaymanager);
	public void trainBroken(RailwayManager railwaymanager, Train train);
	public void trainStopped(RailwayManager railwaymanager, Train train, Station station);
}
