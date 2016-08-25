package net.densyakun.trainsim;
import net.densyakun.trainsim.pack.jreast.RailwayPack_KeihinTohokuLine;
public class Main implements Runnable, RailwayListener {
	RailwayManager railwaymanager = new RailwayManager();
	public Main() {
		railwaymanager.addListener(this);
		railwaymanager.loadRailwayPack(new RailwayPack_KeihinTohokuLine());
		railwaymanager.setFast(true);
		railwaymanager.play();
		new Thread(this).start();
	}
	public static void main(String[] args) {
		new Main();
	}
	@Override
	public void run() {
		while (true) {
			railwaymanager.update();
			//System.out.println(railwaymanager.getTrains().get(0).getPosition() + ":" + railwaymanager.getTrains().get(0).getSpeed() + ":" + railwaymanager.getTrains().get(0).getAccel() + ":" + railwaymanager.getTrains().get(0).getBrake() + ":" + railwaymanager.getTrains().get(0).getDriver().getStopStationList());
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	@Override
	public void played(RailwayManager railwaymanager) {
	}
	@Override
	public void stopped(RailwayManager railwaymanager) {
	}
	@Override
	public void updated(RailwayManager railwaymanager) {
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
		System.out.println("車両故障: " + train);
	}
	@Override
	public void trainStopped(RailwayManager railwaymanager, Train train, Station station) {
		System.out.println("停車: " + train + ":" + station);
	}
}
