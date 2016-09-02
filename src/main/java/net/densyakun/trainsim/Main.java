package net.densyakun.trainsim;
import net.densyakun.trainsim.pack.RailwayPack_Test;
public class Main implements RailwayListener {
	RailwayManager railwaymanager = new RailwayManager();
	public Main() {
		railwaymanager.addListener(this);
		railwaymanager.loadRailwayPack(new RailwayPack_Test());
		railwaymanager.setTimeScale(15);
		railwaymanager.setUpdateinterval(500);
		railwaymanager.play();
	}
	public static void main(String[] args) {
		new Main();
	}
	@Override
	public void played(RailwayManager railwaymanager) {
	}
	@Override
	public void stopped(RailwayManager railwaymanager) {
	}
	@Override
	public void updated(RailwayManager railwaymanager) {
		System.out.println(railwaymanager.getTrains().get(0).getPosition() + ": " + railwaymanager.getTrains().get(0).getSpeed() + ": " + railwaymanager.getTrains().get(0).getAccel() + ": " + railwaymanager.getTrains().get(0).getBrake() + ": " + railwaymanager.getTrains().get(0).getRunningLine() + ": " + railwaymanager.getTrains().get(0).getRunRoute());
	}
	@Override
	public void addLine(RailwayManager railwaymanager, Line line) {
	}
	@Override
	public void addTrain(RailwayManager railwaymanager, Train train) {
	}
	@Override
	public void clearRailway(RailwayManager railwaymanager) {
	}
	@Override
	public void trainAccident(RailwayManager railwaymanager, Train train, AccidentCause accidentcause) {
		railwaymanager.setTimeScale(RailwayManager.REAL_TIMESCALE);
		System.out.println("事故: " + train + ":" + train.getPosition() + ": " + accidentcause);
	}
	@Override
	public void trainStopped(RailwayManager railwaymanager, Train train, Station station) {
		System.out.println("停車: " + train + ":" + station);
	}
}
