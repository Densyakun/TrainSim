package net.densyakun.trainsim;
import java.util.List;
public final class TrainDriver {
	private String name;
	private Diagram diagram;
	//private List<Station> passedstationlist = new ArrayList<Station>();
	private int passedstations = 0;
	private boolean acceled = false;//再加速済みかどうか（速度を一定に保つために行う頻繁な再加速を安定した再加速にするために必要）
	public TrainDriver(String name) {
		this.name = name;
	}
	public String getName() {
		return name;
	}
	public Diagram getDiagram() {
		return diagram;
	}
	public void setDiagram(Diagram diagram) {
		this.diagram = diagram;
	}
	/*public List<Station> getPassedStationList() {
		return passedstationlist;
	}
	public void clearPassedStationList() {
		passedstationlist.clear();
	}*/
	@Override
	public boolean equals(Object o) {
		return o == null ? false : o instanceof TrainDriver ? name.equals(((TrainDriver) o).getName()) : false;
	}
	public void updatetick(RailwayManager railwaymanager, Train train) {
		if (diagram == null) {
			train.getMasterController().setPower(train, 0, 1);
		} else {
			List<Station> stopstationlist = diagram.getStopStationList();
			int nextstations = stopstationlist.size() - passedstations;
			/*List<Station> nextstationlist = diagram.getStopStationList();
			for (int a = 0; a < passedstationlist.size(); a++) {
				for (int b = 0; b < nextstationlist.size(); b++) {
					if (passedstationlist.get(a).equals(nextstationlist.get(b))) {
						nextstationlist.remove(b);
						break;
					}
				}
			}*/

			//boolean to_in = false;
			//boolean to_out = false;
			double stoppos = -1;
			/*if (0 < nextstationlist.size()) {
				Station sta = nextstationlist.get(0);
				stoppos = train.getRunningLine().getRailStartPosition(sta) + sta.getLength() / 2;
			}*/
			double speed = train.getSpeed();
			if (0 < nextstations) {
				Station sta = stopstationlist.get(stopstationlist.size() - nextstations);
				stoppos = train.getRunningLine().getRailStartPosition(sta) + sta.getLength() / 2;
				if (speed == 0) {
					if (/*nextstationlist.size()*/nextstations > 0) {
						if (train.getPosition() > stoppos + 0.5) {
							train.setReverser(Reverser.back);
						} else if (train.getPosition() < stoppos - 0.5) {
							train.setReverser(Reverser.forward);
						} else {
							railwaymanager.trainStopped(train, sta);
						}
					} else {
						train.setReverser(Reverser.back);
					}
				}
			}
			double accel = 0;
			double brake = 0;
			if (stoppos == -1) {
				brake = 1;
				acceled = false;
			} else {
				double stoppable_distance = train.getBrakeDistance(0);
				int maxspeed = Math.min(train.getMaxSpeed(), train.getRunningLine().getLimitSpeed());
				if (speed >= maxspeed) {
					acceled = true;
				}
				double a = 0;
				for (int b = 0; b < train.getRunningLine().getRails().size(); b++) {
					if (train.getRunningLine().getRails().get(b).getLimitSpeed() != null) {
						if (a + train.getRunningLine().getRails().get(b).getLength() >= train.getPosition() - train.getLength() / 2 && a <= train.getPosition() + train.getLength() / 2) {
							maxspeed = Math.min(maxspeed, train.getRunningLine().getRails().get(b).getLimitSpeed());
						}
						if (speed < 0 && train.getPosition() - train.getLength() / 2 > a + train.getRunningLine().getRails().get(b).getLength()) {
							double i = train.getBrakeDistance(train.getRunningLine().getRails().get(b).getLimitSpeed());
							if (train.getPosition() - train.getLength() / 2 + i < a + train.getRunningLine().getRails().get(b).getLength()) {
								brake = Math.max(brake, Math.max(2.0 / 3, -i / (train.getPosition() - a + train.getRunningLine().getRails().get(b).getLength())));
							}
						} else if (speed != 0 && train.getPosition() + train.getLength() / 2 < a) {
							double i = train.getBrakeDistance(train.getRunningLine().getRails().get(b).getLimitSpeed());
							if (train.getPosition() + train.getLength() / 2 + i > a) {
								brake = Math.max(brake, Math.max(2.0 / 3, i / (a - train.getPosition())));
							}
						}
					}
					a += train.getRunningLine().getRails().get(b).getLength();
				}
				double b = train.getBrakeDistance(maxspeed);
				if (b < 0) {
					stoppable_distance = Math.min(stoppable_distance, b);
				} else if (b != 0) {
					stoppable_distance = Math.max(stoppable_distance, b);
				}

				brake = Math.max(speed < 0 ? train.getPosition() + stoppable_distance < stoppos ? 1 : -stoppable_distance / (train.getPosition() - stoppos)/* + (train.getSpeed() > 5 && train.getPosition() - a <= 80 ? 0.05 * (Math.min(5, -train.getSpeed()) / 5) : 0)*/ : speed != 0 ? train.getPosition() + stoppable_distance > stoppos ? 1 : stoppable_distance / (stoppos - train.getPosition())/* + (train.getSpeed() > 5 && a - train.getPosition() <= 80 ? 0.05 * (Math.min(5, train.getSpeed() / 5)) : 0)*/ : 0, brake);
				if (brake != 1) {
					List<Train> trains = railwaymanager.getTrains();
					for (int c = 0; c < trains.size(); c++) {
						Train d = trains.get(c);
						if (d != train && train.getRunningLine().equals(d.getRunningLine()) &&
								(
								(train.getPosition() > stoppos && train.getPosition() > d.getPosition() && train.getPosition() - 200 - train.getLength() / 2 <= d.getPosition() + d.getLength() / 2) ||
								(train.getPosition() < stoppos && train.getPosition() < d.getPosition() && train.getPosition() + 200 + train.getLength() / 2 >= d.getPosition() - d.getLength() / 2)
								)
							) {
							brake = 1;
							break;
						}
					}

					double collectspeed = Math.max(speed, -speed);
					double targetspeed = acceled ? maxspeed - 5 : maxspeed;
					if ((accel = train.getBrake() < 0.5 && collectspeed < targetspeed ? (targetspeed - collectspeed) / train.getAcceleration() : 0) != 0 |
							(brake = collectspeed >= maxspeed + 2.5 ? 1 : brake >= 0.5 ? brake : 0) != 0) {
						acceled = false;
					}
				}
			}

			train.setReverser(Reverser.forward);
			accel = 1;
			brake = 0;

			train.getMasterController().setPower(train, accel, brake);
		}
	}
	public void stopStation() {
		passedstations++;
		/*List<Station> stopstationlist = diagram.getStopStationList();
		if (0 < stopstationlist.size()) {
			passedstationlist.add(stopstationlist.get(0));
		}*/
	}
}
