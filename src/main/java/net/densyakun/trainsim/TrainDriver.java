package net.densyakun.trainsim;
import java.util.List;
//運転士
public final class TrainDriver {
	public static final double DEFAULT_MAX_STOP_DISTANCE = 1000;//停止位置目標までの距離の計算の限界距離の初期値
	private String name;//運転士の名前
	private Diagram diagram;//所持しているダイヤ
	private int passedstations = 0;//停車・通過済みの駅数。次駅を求めたりするために使う。
	private boolean acceled = false;//再加速済みかどうか（速度を一定に保つために行う頻繁な再加速を安定した再加速にするために必要）
	private double maxstopdistance = DEFAULT_MAX_STOP_DISTANCE;//停止位置目標までの距離の計算の限界距離
	private double distanceto = 0;//停止位置目標までの距離（列車の前後に関係）
	public TrainDriver(String name) {
		this(name, DEFAULT_MAX_STOP_DISTANCE);
	}
	public TrainDriver(String name, double maxstopdistance) {
		this.name = name;
		this.maxstopdistance = maxstopdistance;
	}
	public String getName() {
		return name;
	}
	public Diagram getDiagram() {
		return diagram;
	}
	public void setDiagram(Diagram diagram) {
		this.diagram = diagram;
		passedstations = 0;
	}
	public boolean isAcceled() {
		return acceled;
	}
	public void setAcceled(boolean acceled) {
		this.acceled = acceled;
	}
	public double getDistanceto() {
		return distanceto;
	}
	public void setDistanceto(double distanceto) {
		this.distanceto = distanceto;
	}
	@Override
	public boolean equals(Object o) {
		return o == null ? false : o instanceof TrainDriver ? name.equals(((TrainDriver) o).getName()) : false;
	}
	public void updatetick(RailwayManager railwaymanager, Train train) {
		double accel = 0;
		double brake = 1;//ブレーキ量(0~1)。目的地が無い場合は1。
		if (diagram != null) {//ダイヤがあるか
			Station nextstop = getNextStopStation();
			if (nextstop != null) {//次の停車駅があるか
				brake = 0;

				boolean invert = train.isInvert();

				//停止位置目標までの距離を計算
				distanceto = train.getDistance(maxstopdistance, nextstop);
				if (invert) {
					distanceto = -distanceto;
				}

				//停止判定と次駅の停止位置目標までの距離を計算
				double speed = train.getSpeed();
				if (speed == 0) {

					//停止判定
					if (-0.5 <= distanceto && distanceto <= 0.5) {//停止位置範囲内なら停車する
						railwaymanager.trainStopped(train, nextstop);//駅に停車
						if ((nextstop = getNextStopStation()) != null) {//次の駅があるか

							//次の駅の距離を計算
							distanceto = train.getDistance(maxstopdistance, nextstop);
							if (invert) {
								distanceto = -distanceto;
							}
						}
					}

					//逆転器の操作
					if (distanceto < -0.5) {
						train.setReverser(Reverser.back);
					} else {
						train.setReverser(Reverser.forward);
					}
				}

				double trainlengthhalf = train.getLength() / 2;
				double position = train.getPosition();

				//TODO マスコンの操作を頻繁に行わないようにする
				//30キロ超えの場合は今までと変わらず、
				//30キロ以内の場合において、ブレーキが0.5以上の場合は予想よりもブレーキを強くかける。（ただし、常用ブレーキの場合は常用最大以下）

				//TODO
				//最高速度と速度制限区間を計算しブレーキ量を調節
				int maxspeed = train.getMaxSpeed();
				//int nextmaxspeed = maxspeed;
				//double nextspeedlimitdistance = 0;

				List<Line> runroute = train.getRunRoute();
				Line runningline = train.getRunningLine();
				double e = 0;
				boolean f = false;
				boolean right = 0 < speed;
				if (invert) {
					right = !right;
				}
				if (right) {
					for (int g = 0; g < runroute.size() && e <= trainlengthhalf; g++) {
						boolean h = runroute.get(g).equals(runningline);
						if (!f && h) {
							f = true;
						}
						if (f) {
							Line line = runroute.get(g);
							maxspeed = Math.min(maxspeed, line.getLimitSpeed());
							List<Rail> rails = line.getRails();
							for (int i = 0; i < rails.size() && e <= trainlengthhalf; i++) {
								Rail rail = rails.get(i);
								if (h ? position - trainlengthhalf <= line.getRailStartPosition(rail) + rail.getLength() : true) {
									int j = rail.getLimitSpeed();
									if (j != 0) {
										maxspeed = Math.min(maxspeed, j);
									}
									e += rail.getLength();
								}
							}
						}
					}
				} else {
					for (int g = runroute.size() - 1; 0 <= g && e <= trainlengthhalf; g--) {
						boolean h = runroute.get(g).equals(runningline);
						if (!f && h) {
							f = true;
						}
						if (f) {
							Line line = runroute.get(g);
							maxspeed = Math.min(maxspeed, line.getLimitSpeed());
							List<Rail> rails = line.getRails();
							for (int i = rails.size() - 1; 0 <= i && e <= trainlengthhalf; i--) {
								Rail rail = rails.get(i);
								if (h ? line.getRailStartPosition(rail) <= position + trainlengthhalf : true) {
									int j = rail.getLimitSpeed();
									if (j != 0) {
										maxspeed = Math.min(maxspeed, j);
									}
									e += rail.getLength();
								}
							}
						}
					}
				}

				//停止可能最短距離
				double stoppable_distance = Math.max(train.getBrakeDistance(0), train.getBrakeDistance(maxspeed));

				double collectspeed = Math.max(speed, -speed);//速度を正の数にする

				//停止位置目標までの距離からブレーキ量を計算しブレーキをかける
				if (invert) {
					brake = Math.max(-distanceto <= stoppable_distance ? 1 : stoppable_distance / -distanceto, brake);
				} else {
					brake = Math.max(distanceto <= stoppable_distance ? 1 : stoppable_distance / distanceto, brake);
				}

				if (speed >= maxspeed) {
					acceled = true;
				}

				if (brake != 1) {
					//TODO
					//TODO 先行列車を発見しただけでなく、分岐器が繋がっていないなどの線路が切れているなどで脱線することを確認した場合にも急停車するようにする。
					//先行列車を目撃した場合に非常ブレーキをかける
					List<Train> trains = railwaymanager.getTrains();
					for (int c = 0; c < trains.size(); c++) {
						Train d = trains.get(c);
						if (d != train && train.getRunningLine().equals(d.getRunningLine()) && position < d.getPosition() && position + 200 + trainlengthhalf >= d.getPosition() - d.getLength() / 2) {
							brake = 1;
							break;
						}
					}
					double targetspeed = acceled ? maxspeed - 5 : maxspeed;//運転士が出したい目標の速度（再加速を安定させるため、acceledがtrueの場合は目標最高速度 - 5キロになってから再加速するようにしている）

					brake = maxspeed + 1 <= collectspeed ? 1 : brake >= 0.5 ? brake : 0;
					if (train.getBrake() == 0 && collectspeed < targetspeed) {
						accel = (targetspeed - collectspeed) / train.getAcceleration();
					}
				}
				if (accel != 0 || brake != 0) {
					acceled = false;
				}
			}
		}
		train.getMasterController().setPower(train, accel, brake);
	}
	public void stopStation() {
		passedstations++;
	}
	//次の停車駅を返す
	public Station getNextStopStation() {
		List<Station> stopstationlist = diagram.getStopStationList();//停車駅リスト
		int nextstations = stopstationlist.size() - passedstations;//停車済みの駅数
		if (0 < nextstations) {//次の停車駅があるかどうか
			return stopstationlist.get(stopstationlist.size() - nextstations);
		}
		return null;
	}

	//TODO 通過機能が追加されたら、次の停車駅までにある次の通過駅を返すメソッドを作る
}
