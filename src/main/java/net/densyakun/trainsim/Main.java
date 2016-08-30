package net.densyakun.trainsim;
import net.densyakun.trainsim.pack.jreast.RailwayPack_YamanoteLine;
public class Main implements RailwayListener {
	RailwayManager railwaymanager = new RailwayManager();
	public Main() {
		railwaymanager.addListener(this);
		railwaymanager.loadRailwayPack(new RailwayPack_YamanoteLine());
		//railwaymanager.setTimeScale(60);
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
		System.out.println(railwaymanager.getTrains().get(0).getPosition() + ":" + railwaymanager.getTrains().get(0).getSpeed() + ":" + railwaymanager.getTrains().get(0).getAccel() + ":" + railwaymanager.getTrains().get(0).getBrake());
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
	public void trainBroken(RailwayManager railwaymanager, Train train) {
		railwaymanager.setTimeScale(RailwayManager.REAL_TIMESCALE);
		System.out.println("車両故障: " + train + ":" + train.getPosition());
	}
	@Override
	public void trainStopped(RailwayManager railwaymanager, Train train, Station station) {
		System.out.println("停車: " + train + ":" + station);
	}
}
