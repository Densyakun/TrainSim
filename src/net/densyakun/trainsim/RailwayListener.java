package net.densyakun.trainsim;
public interface RailwayListener {
	public void played(RailwayManager railwaymanager);
	public void stopped(RailwayManager railwaymanager);
	public void updated(RailwayManager railwaymanager);
	public void addLine(RailwayManager railwaymanager, Line line);
	public void addTrain(RailwayManager railwaymanager, Train train);
	public void clearRailway(RailwayManager railwaymanager);
	public void trainDerailment(RailwayManager railwaymanager, Train train, double speed);
	public void trainCrush(RailwayManager railwaymanager, Train train_a, Train train_b);
	public void trainStopped(RailwayManager railwaymanager, Train train, Station station);
}
