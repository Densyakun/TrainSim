package net.densyakun.trainsim;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

//列車データ。（実体）
//編成データはTrainSetクラスにある。
//place(Location)を使用して車両を線路に設置する。
public class Train implements Serializable {
	/**
	 * 0.0.2a以降で使用可能
	 */
	private static final long serialVersionUID = 1L;

	private String name;// 編成の名前。（識別に使用）編成番号などを入れると良い（例:トウ520編成）
	private TrainSet trainset;// 列車編成の基本情報
	private Line runningline;// 走行中の路線（positionが使用する路線。列車が設置されていない場合はnull）
	private double position;// 走行位置(runninglineのin側を基準とする)
	private double speed = 0;// 速度(in側に進むと負の数になる)
	private double accel = 0;// 加速(0~1)
	private double brake = 0;// 減速(0~1)
	private boolean broken = false;// 車両が壊れているか
	private boolean derailment = false;// 脱線しているか
	private boolean crush = false;// 衝突したか
	private boolean moved = false;// 車両を動かしたことがあるか

	// TODO 前後の運転台・乗務員室の操作を分ける

	private TrainDriver driver;// 運転士 //TODO 前後どちらの乗務員室にいるかどうかの指定はない
	private Reverser reverser = Reverser.off;// 逆転器 //TODO 前後の運転台ごとに分ける予定

	// 列車が走行するルートの全区間。
	// 分岐器や列車の場所によって可変する。
	// 前後の路線がある場合は必ず入れるため、同じ路線がループしている場合は3つ存在する。
	// 事故が起きると事故時のルートのままになる。
	// 理由は、ポイントを走行中に分岐器が作動したことにより脱線した場合、
	// 分岐器が作動したあとのルートに変わってしまい、列車が別の線路に移動してしまうため。
	private List<Line> runroute;

	private List<Line> runninglines;// 列車が走行している使用中の路線
	private boolean invert = false;// 列車の向きが逆になっているか
	private boolean loop = false;// 走行中の路線が環状線になっているか

	public Train(String name, TrainSet trainset) {
		this.name = name;
		this.trainset = trainset;
	}

	public String getName() {
		return name;
	}

	public final TrainSet getTrainset() {
		return trainset;
	}

	public final Line getRunningLine() {
		return runningline;
	}

	public final double getPosition() {
		return position;
	}

	public final double getSpeed() {
		return speed;
	}

	public final void setSpeed(double speed) {
		this.speed = speed;
	}

	public final double getAccel() {
		return broken ? 0 : accel;
	}

	public final void setAccel(double accel) {
		this.accel = accel < 0 ? 0 : accel > 1 ? 1 : accel;
	}

	public final double getBrake() {
		return broken ? 0 : brake;
	}

	public final void setBrake(double brake) {
		this.brake = brake < 0 ? 0 : brake > 1 ? 1 : brake;
	}

	public final TrainDriver getDriver() {
		return driver;
	}

	public final void setDriver(TrainDriver driver) {
		this.driver = driver;
	}

	public final Reverser getReverser() {
		return reverser;
	}

	public final void setReverser(Reverser reverser) {
		this.reverser = reverser;
	}

	public boolean isBroken() {
		return broken;
	}

	public void repair() {
		broken = derailment = crush = false;
	}

	public boolean isDerailment() {
		return derailment;
	}

	public void derailment() {
		broken = derailment = true;
		speed = 0;
	}

	public boolean isCrush() {
		return crush;
	}

	public void crush() {
		broken = crush = true;
	}

	public final List<Line> getRunRoute() {
		return runroute;
	}

	public List<Line> getRunningLines() {
		return runninglines;
	}

	public final boolean isInvert() {
		return invert;
	}

	public boolean isLoop() {
		return loop;
	}

	@Override
	public String toString() {
		return name;
	}

	// 衝突の判定はRailwayManagerが行う
	public void updatetick(RailwayManager railwaymanager) {
		if (this.runningline != null) {
			if (driver != null) {
				driver.updatetick(railwaymanager, this);
			}
			if (!isBroken()) {
				if (accel != 0) {
					if (invert) {
						if ((reverser == Reverser.back) && (speed > -5)) {
							speed += (trainset.getAcceleration() * accel * RailwayManager.MAXSPEED_AUTO_UPDATE / 1000)
									* ((500 - speed) / 500);
						} else if ((reverser == Reverser.forward) && (speed < 5)) {
							speed -= (trainset.getAcceleration() * accel * RailwayManager.MAXSPEED_AUTO_UPDATE / 1000)
									* ((500 - speed) / 500);
						}
					} else {
						if ((reverser == Reverser.back) && (speed < 5)) {
							speed -= (trainset.getAcceleration() * accel * RailwayManager.MAXSPEED_AUTO_UPDATE / 1000)
									* ((500 - speed) / 500);
						} else if ((reverser == Reverser.forward) && (speed > -5)) {
							speed += (trainset.getAcceleration() * accel * RailwayManager.MAXSPEED_AUTO_UPDATE / 1000)
									* ((500 - speed) / 500);
						}
					}
				}
			}
			if (speed < 0) {
				speed += trainset.getDeceleration() * brake * RailwayManager.MAXSPEED_AUTO_UPDATE / 1000;
				if (accel == 0) {
					speed += 0.1 * RailwayManager.MAXSPEED_AUTO_UPDATE / 1000;// TODO 空気抵抗・摩擦については後々正確に作る予定
				}
				if (0 < speed) {
					speed = 0;
				}
			} else if (speed > 0) {
				speed -= trainset.getDeceleration() * brake * RailwayManager.MAXSPEED_AUTO_UPDATE / 1000;
				if (accel == 0) {
					speed -= 0.1 * RailwayManager.MAXSPEED_AUTO_UPDATE / 1000;// TODO 〃
				}
				if (0 > speed) {
					speed = 0;
				}
			}
			if (moved ? speed != 0 : true) {
				teleport(railwaymanager, position + speed * RailwayManager.MAXSPEED_AUTO_UPDATE / 60 / 60);
				moved = true;
			}
		}
	}

	public final void teleport(RailwayManager railwaymanager, double position) {
		if (this.runningline != null) {
			double length = trainset.getLength();
			if (position < length / 2) {
				double b = 0;
				int c = 1;
				for (; c < runroute.size(); c++) {
					if (runroute.get(c).equals(runningline)) {
						for (int d = c - 1; 0 <= d; d--) {
							b += runroute.get(d).getLength();
						}
						break;
					}
				}
				if (position < 0) {
					if (0 <= c - 1) {
						boolean d = true;
						Line lineto = runroute.get(c - 1);
						if (0 <= lineto.getBranch_out()
								&& runningline.equals(lineto.getLines_out().get(lineto.getBranch_out()))) {
							d = false;
							runningline = lineto;
							reloadRunRoute();
							teleport(railwaymanager, runningline.getLength() + position);
						}
						if (d && 0 <= lineto.getBranch_in()
								&& runningline.equals(lineto.getLines_in().get(lineto.getBranch_in()))) {
							d = false;
							runningline = lineto;
							reloadRunRoute();
							teleport(railwaymanager, -position);
							invert = !invert;
							speed = -speed;
							if (d) {
								railwaymanager.trainDerailment(this);
							}
						}
					} else {
						railwaymanager.trainDerailment(this);
					}
				} else if (!loop && position < length / 2 - b) {
					teleport(railwaymanager, length / 2 - b);
					railwaymanager.trainDerailment(this);
				} else {
					this.position = position;
				}
			} else if (runningline.getLength() < position + length / 2) {
				double b = 0;
				int c = 0;
				for (; c < runroute.size(); c++) {
					if (runroute.get(c).equals(runningline)) {
						for (int d = c + 1; d < runroute.size(); d++) {
							b += runroute.get(d).getLength();
						}
						break;
					}
				}
				if (runningline.getLength() < position) {
					if (c + 1 < runroute.size()) {
						boolean d = true;
						Line oldline = runningline;
						Line lineto = runroute.get(c + 1);
						if (0 <= lineto.getBranch_in()
								&& runningline.equals(lineto.getLines_in().get(lineto.getBranch_in()))) {
							d = false;
							runningline = lineto;
							teleport(railwaymanager, position - oldline.getLength());
						}
						if (d && 0 <= lineto.getBranch_out()
								&& runningline.equals(lineto.getLines_out().get(lineto.getBranch_out()))) {
							d = false;
							runningline = lineto;
							teleport(railwaymanager, lineto.getLength() + oldline.getLength() - position);
							invert = !invert;
							speed = -speed;
							if (d) {
								railwaymanager.trainDerailment(this);
							}
						}
					} else {
						railwaymanager.trainDerailment(this);
					}
				} else if (!loop && runningline.getLength() - length / 2 + b < position) {
					teleport(railwaymanager, runningline.getLength() - length / 2 + b);
					railwaymanager.trainDerailment(this);
				} else {
					this.position = position;
				}
			} else {
				this.position = position;
			}
			if (!broken) {
				reloadRunRoute();
				int gauge = trainset.getGauge();
				if (gauge != TrainSet.FREE_GAUGE_TRAIN) {
					for (int a = 0; a < runninglines.size(); a++) {
						if (runninglines.get(a).getGauge() != gauge) {
							railwaymanager.trainDerailment(this);
							break;
						}
					}
				}
			}
		}
	}

	public final void place(Line runningline, double position, boolean invert) {
		if (runningline != null) {
			this.runningline = runningline;
			this.position = position;
			this.invert = invert;
			reloadRunRoute();
		}
	}

	public final void remove() {
		this.runningline = null;
	}

	/**
	 * setspeedまで加速するために必要な距離を返します。
	 *
	 * @param setspeed
	 *            (正数)
	 * @return setspeedまで加速するために必要な距離(正数)
	 */
	public double getAccelDistance(double setspeed) {
		double a = 0;
		for (double b = Math.max(speed, -speed); b < setspeed; a += b * 1000 / 60 / 60
				/ RailwayManager.MAXSPEED_AUTO_UPDATE) {
			b += trainset.getAcceleration() / RailwayManager.MAXSPEED_AUTO_UPDATE;
		}
		return a;
	}

	/**
	 * setspeedまで減速するために必要な距離を返します。
	 *
	 * @param setspeed
	 *            (正数)
	 * @return setspeedまで減速するために必要な距離(正数)
	 */
	public double getBrakeDistance(double setspeed) {
		double a = 0;
		for (double b = Math.max(speed, -speed); setspeed < b; a += b * 1000 / 60 / 60
				/ RailwayManager.MAXSPEED_AUTO_UPDATE) {
			b -= trainset.getDeceleration() / RailwayManager.MAXSPEED_AUTO_UPDATE;
			if (b < 0) {
				b = 0;
			}
		}
		return a;
	}

	public void stopStation() {
		if (driver != null) {
			driver.stopStation();
		}
	}

	public void reloadRunRoute() {
		loop = false;
		List<Line> lines = new ArrayList<Line>();
		lines.add(runningline);
		aaa(lines, runningline, false, invert);
		aaa(lines, runningline, true, invert);
		runroute = lines;
		reloadRunningLines();
	}

	private void aaa(List<Line> lines, Line line, boolean right, boolean invert) {
		if (right) {
			if (invert) {
				int branch_in = line.getBranch_in();
				if (0 <= branch_in) {
					Line line_in = line.getLines_in().get(branch_in);
					for (int a = 0; a < lines.size(); a++) {
						if (lines.get(a).equals(line_in)) {
							loop = true;
							return;
						}
					}
					lines.add(line_in);
					int line_in_branch_out = line_in.getBranch_out();
					if (0 <= line_in_branch_out) {
						Line line_in_out = line_in.getLines_out().get(line_in_branch_out);
						if (line.equals(line_in_out)) {
							aaa(lines, line_in, true, true);
						}
					} else {
						int line_in_branch_in = line_in.getBranch_in();
						if (0 <= line_in_branch_in) {
							Line line_in_in = line_in.getLines_in().get(line_in_branch_in);
							if (line.equals(line_in_in)) {
								aaa(lines, line_in, true, false);
							}
						}
					}
				}
			} else {
				int branch_out = line.getBranch_out();
				if (0 <= branch_out) {
					Line line_out = line.getLines_out().get(branch_out);
					for (int a = 0; a < lines.size(); a++) {
						if (lines.get(a).equals(line_out)) {
							loop = true;
							return;
						}
					}
					lines.add(line_out);
					int line_out_branch_in = line_out.getBranch_in();
					if (0 <= line_out_branch_in) {
						Line line_out_in = line_out.getLines_in().get(line_out_branch_in);
						if (line.equals(line_out_in)) {
							aaa(lines, line_out, true, false);
						}
					} else {
						int line_out_branch_out = line_out.getBranch_out();
						if (0 <= line_out_branch_out) {
							Line line_out_out = line_out.getLines_out().get(line_out_branch_out);
							if (line.equals(line_out_out)) {
								aaa(lines, line_out, true, true);
							}
						}
					}
				}
			}
		} else {
			if (invert) {
				int branch_out = line.getBranch_out();
				if (0 <= branch_out) {
					Line line_out = line.getLines_out().get(branch_out);
					for (int a = 0; a < lines.size(); a++) {
						if (lines.get(a).equals(line_out)) {
							loop = true;
							return;
						}
					}
					lines.add(0, line_out);
					int line_out_branch_out = line_out.getBranch_out();
					if (0 <= line_out_branch_out) {
						Line line_out_out = line_out.getLines_out().get(line_out_branch_out);
						if (line.equals(line_out_out)) {
							aaa(lines, line_out, false, true);
						}
					} else {
						int line_out_branch_in = line_out.getBranch_in();
						if (0 <= line_out_branch_in) {
							Line line_out_in = line_out.getLines_in().get(line_out_branch_in);
							if (line.equals(line_out_in)) {
								aaa(lines, line_out, false, false);
							}
						}
					}
				}
			} else {
				int branch_in = line.getBranch_in();
				if (0 <= branch_in) {
					Line line_in = line.getLines_in().get(branch_in);
					for (int a = 0; a < lines.size(); a++) {
						if (lines.get(a).equals(line_in)) {
							loop = true;
							return;
						}
					}
					lines.add(0, line_in);
					int line_in_branch_in = line_in.getBranch_in();
					if (0 <= line_in_branch_in) {
						Line line_in_in = line_in.getLines_in().get(line_in_branch_in);
						if (line.equals(line_in_in)) {
							aaa(lines, line_in, false, false);
						}
					} else {
						int line_in_branch_out = line_in.getBranch_out();
						if (0 <= line_in_branch_out) {
							Line line_in_out = line_in.getLines_out().get(line_in_branch_out);
							if (line.equals(line_in_out)) {
								aaa(lines, line_in, false, true);
							}
						}
					}
				}
			}
		}
	}

	private void reloadRunningLines() {
		List<Line> lines = new ArrayList<Line>();
		lines.add(runningline);
		double a = position;
		boolean b = false;
		double length = trainset.getLength();
		for (int c = runroute.size() - 1; 0 <= c; c--) {
			Line line = runroute.get(c);
			if (b) {
				if (a < length / 2) {
					lines.add(0, line);
				}
				a += line.getLength();
			} else if (line.equals(runningline)) {
				b = true;
			}
		}
		a = runningline.getLength() - position;
		b = false;
		for (int c = 0; c < runroute.size(); c++) {
			Line line = runroute.get(c);
			if (b) {
				if (a < length / 2) {
					lines.add(line);
				}
				a += line.getLength();
			} else if (line.equals(runningline)) {
				b = true;
			}
		}
		runninglines = lines;
	}

	/**
	 * trainからstationまでの最短ルートを計算し、距離を返します。 距離がlimitdistanceを超えることはありません。
	 *
	 * @param limitdistance
	 *            距離の限界
	 * @param train
	 *            列車
	 * @param station
	 *            駅(目的地)
	 * @return stationまでの距離（ルートが存在しないはnull）
	 */
	public Double getDistance(double limitdistance, Station station) {
		Line runningline = getRunningLine();
		double a = runningline.getRailStartPosition(station);
		if (a == -1) {// 現在の線路に次の停車駅がないか
			Double b = aab(0, limitdistance, station, new ArrayList<Line>(), getRunningLine(), true, invert);
			if (b == null) {
				if ((b = aab(0, limitdistance, station, new ArrayList<Line>(), getRunningLine(), false,
						invert)) != null) {
					return -b - getPosition() - station.getLength() / 2;
				}
			} else {
				return b + getPosition() + station.getLength() / 2;
			}
		} else {
			// あった場合は距離を返す
			return a - getPosition() + station.getLength() / 2;
		}
		return null;
	}

	private Double aab(double distance, double limitdistance, Station station, List<Line> lines, Line line,
			boolean right, boolean invert) {
		// 接続する線路から駅までの距離を求める(ポイントが接続していなくてもポイントを切り替えれば行けるような場所でも良い)
		if (limitdistance <= distance) {
			return limitdistance;
		}
		if (right) {
			if (invert) {
				List<Line> lines_in = line.getLines_in();
				for (int a = 0; a < lines_in.size(); a++) {
					Line line_in = lines_in.get(a);
					for (int b = 0; b < lines.size(); b++) {
						if (lines.get(b).equals(line_in)) {
							return null;
						}
					}
					lines.add(line_in);
					int line_in_branch_out = line_in.getBranch_out();
					if (0 <= line_in_branch_out) {
						Line line_in_out = line_in.getLines_out().get(line_in_branch_out);
						if (line.equals(line_in_out)) {
							return aab(distance, limitdistance, station, lines, line_in, true, true);
						}
					} else {
						int line_in_branch_in = line_in.getBranch_in();
						if (0 <= line_in_branch_in) {
							Line line_in_in = line_in.getLines_in().get(line_in_branch_in);
							if (line.equals(line_in_in)) {
								return aab(distance, limitdistance, station, lines, line_in, true, false);
							}
						}
					}
				}
			} else {
				List<Line> lines_out = line.getLines_out();
				for (int a = 0; a < lines_out.size(); a++) {
					Line line_out = lines_out.get(a);
					for (int b = 0; b < lines.size(); b++) {
						if (lines.get(b).equals(line_out)) {
							return null;
						}
					}
					lines.add(line_out);
					int line_out_branch_in = line_out.getBranch_in();
					if (0 <= line_out_branch_in) {
						Line line_out_in = line_out.getLines_in().get(line_out_branch_in);
						if (line.equals(line_out_in)) {
							return aab(distance, limitdistance, station, lines, line_out, true, false);
						}
					} else {
						int line_out_branch_out = line_out.getBranch_out();
						if (0 <= line_out_branch_out) {
							Line line_out_out = line_out.getLines_out().get(line_out_branch_out);
							if (line.equals(line_out_out)) {
								return aab(distance, limitdistance, station, lines, line_out, true, true);
							}
						}
					}
				}
			}
		} else {

			if (invert) {
				int branch_out = line.getBranch_out();
				if (0 <= branch_out) {
					Line line_out = line.getLines_out().get(branch_out);
					for (int a = 0; a < lines.size(); a++) {
						if (lines.get(a).equals(line_out)) {
							return null;
						}
					}
					lines.add(0, line_out);
					int line_out_branch_out = line_out.getBranch_out();
					if (0 <= line_out_branch_out) {
						Line line_out_out = line_out.getLines_out().get(line_out_branch_out);
						if (line.equals(line_out_out)) {
							return aab(distance, limitdistance, station, lines, line_out, false, true);
						}
					} else {
						int line_out_branch_in = line_out.getBranch_in();
						if (0 <= line_out_branch_in) {
							Line line_out_in = line_out.getLines_in().get(line_out_branch_in);
							if (line.equals(line_out_in)) {
								return aab(distance, limitdistance, station, lines, line_out, false, false);
							}
						}
					}
				}
			} else {
				int branch_in = line.getBranch_in();
				if (0 <= branch_in) {
					Line line_in = line.getLines_in().get(branch_in);
					for (int a = 0; a < lines.size(); a++) {
						if (lines.get(a).equals(line_in)) {
							return null;
						}
					}
					lines.add(0, line_in);
					int line_in_branch_in = line_in.getBranch_in();
					if (0 <= line_in_branch_in) {
						Line line_in_in = line_in.getLines_in().get(line_in_branch_in);
						if (line.equals(line_in_in)) {
							return aab(distance, limitdistance, station, lines, line_in, false, false);
						}
					} else {
						int line_in_branch_out = line_in.getBranch_out();
						if (0 <= line_in_branch_out) {
							Line line_in_out = line_in.getLines_out().get(line_in_branch_out);
							if (line.equals(line_in_out)) {
								return aab(distance, limitdistance, station, lines, line_in, false, true);
							}
						}
					}
				}
			}

			double a = line.getRailStartPosition(station);
			if (a != -1) {
				return Math.min(distance + line.getLength() - a, limitdistance);
			}
			// if (0 <= line.getBranch_in()) {
			/*
			Line line_in = line.getLines_in().get(line.getBranch_in()); if (3
			<= lines.size()) { for (int b = 0; b < lines.size(); b++) { if
			(lines.get(b).equals(line_in)) { return null; } } } lines.add(0,
			line_in);
			*/
			// return aab(distance, limitdistance, station, lines, line_in,
			// false, false);
			// }
		}
		return null;
	}
}
