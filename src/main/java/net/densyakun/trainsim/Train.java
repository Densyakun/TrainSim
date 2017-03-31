package net.densyakun.trainsim;
import java.util.ArrayList;
import java.util.List;
//列車データ。車両をまとめた編成データでもある。
public class Train {
	public static final int FREE_GAUGE_TRAIN = -1;//フリーゲージトレイン用の軌間指定値
	private String name;//列車名。列車番号などを入れると良い(例:トウ520編成)
	private Car[] cars;//列車編成。配列の0の方が1号車となり、路線のin側が1号車となる。
	private double length = Double.MIN_NORMAL;//列車の長さ(列車の編成が設定されると自動的に更新される)
	private int gauge;//軌間(ゲージ)。フリーゲージトレインにすることもできる。
	private Line runningline;//列車の座標がある路線
	private double position;//走行位置(in側を基準とする)
	private int maxspeed;//設計最高速度
	private double speed = 0;//速度(in側に進むと負の数になる)
	private double acceleration;//起動加速度
	private double deceleration;//減速度
	private double accel = 0;//加速(0~1)
	private double brake = 0;//減速(0~1)
	private TrainDriver driver;//運転士 //TODO 前後の指定がないため、これから作る予定
	private LineColor color;//列車の帯色
	private Reverser reverser = Reverser.off;//逆転器 //TODO 前後の運転台ごとに分ける予定
	private MasterController mc;//マスコン //TODO 前後の運転台ごとに分ける予定
	private boolean broken = false;//車両が壊れているか
	private boolean derailment = false;//脱線しているか
	private boolean crush = false;//衝突しているか
	private boolean moved = false;//車両を動かしたことがあるか

	//列車が走行するルートの全区間。
	//分岐器や列車の場所によって可変する。
	//隣の路線がある場合は必ず入れるため、同じ路線がループしている場合は3つ存在する。
	//事故が起きると事故時のルートのままになる。
	//理由は、ポイントを走行中に分岐器が作動したことにより脱線した場合、
	//分岐器が作動したあとのルートに変わってしまい、列車が別の線路に移動してしまうため。

	//TODO 前後の運転台・乗務員室の操作を分ける
	private List<Line> runroute;

	private List<Line> runninglines;//休止中 列車が走行中（使用中）の路線
	private boolean invert = false;
	private boolean loop = false;//走行中の路線が環状線かどうか
	public Train(String name, Car[] cars, int gauge, Line runningline, double position, int maxspeed, double acceleration, double deceleration, MasterController mc, boolean invert) {
		this.name = name;
		this.cars = cars;
		this.gauge = gauge;
		this.runningline = runningline;
		this.position = position;
		this.maxspeed = maxspeed < 0 ? 0 : maxspeed;
		this.acceleration = acceleration <= 0 ? Double.MIN_NORMAL : acceleration;
		this.deceleration = deceleration <= 0 ? Double.MIN_NORMAL : deceleration;
		this.mc = mc;
		this.invert = invert;
		reloadLength();
		reloadRunRoute();
	}
	public String getName() {
		return name;
	}
	public final Car[] getCars() {
		return cars;
	}
	public final double getLength() {
		return length;
	}
	public final void reloadLength() {
		length = 0;
		for (int a = 0; a < cars.length; a++) {
			length += cars[a].getLength();
		}
	}
	public final int getGauge() {
		return gauge;
	}
	private final void setRunningLine(Line runningline) {
		if ((this.runningline = runningline) != null) {
			reloadRunRoute();
		}
	}
	public final Line getRunningLine() {
		return runningline;
	}
	public final double getPosition() {
		return position;
	}
	public final int getMaxSpeed() {
		return maxspeed;
	}
	public final double getSpeed() {
		return speed;
	}
	public final void setSpeed(double speed) {
		this.speed = speed;
	}
	public final double getAcceleration() {
		return acceleration;
	}
	public final double getDeceleration() {
		return deceleration;
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
	public final LineColor getLineColor() {
		return color;
	}
	public final void setLineColor(LineColor linecolor) {
		color = linecolor;
	}
	public final Reverser getReverser() {
		return reverser;
	}
	public final void setReverser(Reverser reverser) {
		this.reverser = reverser;
	}
	public final MasterController getMasterController() {
		return mc;
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
	//衝突の判定はRailwayManagerが行う
	public void updatetick(RailwayManager railwaymanager) {
		if (driver != null) {
			driver.updatetick(railwaymanager, this);
		}
		if (!isBroken()) {
			if (accel != 0) {
				if (invert) {
					if ((reverser == Reverser.back) && (speed > -5)) {
						speed += (acceleration * accel * RailwayManager.MAXSPEED_AUTO_UPDATE / 1000) * ((500 - speed) / 500);
					} else if ((reverser == Reverser.forward) && (speed < 5)) {
						speed -= (acceleration * accel * RailwayManager.MAXSPEED_AUTO_UPDATE / 1000) * ((500 - speed) / 500);
					}
				} else {
					if ((reverser == Reverser.back) && (speed < 5)) {
						speed -= (acceleration * accel * RailwayManager.MAXSPEED_AUTO_UPDATE / 1000) * ((500 - speed) / 500);
					} else if ((reverser == Reverser.forward) && (speed > -5)) {
						speed += (acceleration * accel * RailwayManager.MAXSPEED_AUTO_UPDATE / 1000) * ((500 - speed) / 500);
					}
				}
			}
		}
		if (speed < 0) {
			speed += deceleration * brake * RailwayManager.MAXSPEED_AUTO_UPDATE / 1000;
			if (accel == 0) {
				speed += 0.1 * RailwayManager.MAXSPEED_AUTO_UPDATE / 1000;//TODO 空気抵抗・摩擦については後々正確に作る予定
			}
			if (0 < speed) {
				speed = 0;
			}
		} else if (speed > 0) {
			speed -= deceleration * brake * RailwayManager.MAXSPEED_AUTO_UPDATE / 1000;
			if (accel == 0) {
				speed -= 0.1 * RailwayManager.MAXSPEED_AUTO_UPDATE / 1000;//TODO 〃
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
	public final void teleport(RailwayManager railwaymanager, double position) {
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
					if (0 <= lineto.getBranch_out() && runningline.equals(lineto.getLines_out().get(lineto.getBranch_out()))) {
						d = false;
						setRunningLine(lineto);
						teleport(railwaymanager, runningline.getLength() + position);
					}
					if (d && 0 <= lineto.getBranch_in() && runningline.equals(lineto.getLines_in().get(lineto.getBranch_in()))) {
						d = false;
						setRunningLine(lineto);
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
					if (0 <= lineto.getBranch_in() && runningline.equals(lineto.getLines_in().get(lineto.getBranch_in()))) {
						d = false;
						setRunningLine(lineto);
						teleport(railwaymanager, position - oldline.getLength());
					}
					if (d && 0 <= lineto.getBranch_out() && runningline.equals(lineto.getLines_out().get(lineto.getBranch_out()))) {
						d = false;
						setRunningLine(lineto);
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
			if (gauge != FREE_GAUGE_TRAIN) {
				for (int a = 0; a < runninglines.size(); a++) {
					if (runninglines.get(a).getGauge() != gauge) {
						railwaymanager.trainDerailment(this);
						break;
					}
				}
			}
		}
	}
	/**
	 * setspeedまで加速するために必要な距離を返します。
	 * @param setspeed (正数)
	 * @return setspeedまで加速するために必要な距離(正数)
	 */
	public double getAccelDistance(double setspeed) {
		double a = 0;
		for (double b = Math.max(speed, -speed); b < setspeed; a += b * 1000 / 60 / 60 / RailwayManager.MAXSPEED_AUTO_UPDATE) {
			b += acceleration / RailwayManager.MAXSPEED_AUTO_UPDATE;
		}
		return a;
	}
	/**
	 * setspeedまで減速するために必要な距離を返します。
	 * @param setspeed (正数)
	 * @return setspeedまで減速するために必要な距離(正数)
	 */
	public double getBrakeDistance(double setspeed) {
		double a = 0;
		for (double b = Math.max(speed, -speed); setspeed < b; a += b * 1000 / 60 / 60 / RailwayManager.MAXSPEED_AUTO_UPDATE) {
			b -= deceleration / RailwayManager.MAXSPEED_AUTO_UPDATE;
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
					//if (6 <= lines.size()) {
						for (int a = 0; a < lines.size(); a++) {
							if (lines.get(a).equals(line_in)) {
								loop = true;
								return;
							}
						}
					//}
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
					//if (6 <= lines.size()) {
						for (int a = 0; a < lines.size(); a++) {
							if (lines.get(a).equals(line_out)) {
								loop = true;
								return;
							}
						}
					//}
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
					//if (6 <= lines.size()) {
						for (int a = 0; a < lines.size(); a++) {
							if (lines.get(a).equals(line_out)) {
								loop = true;
								return;
							}
						}
					//}
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
					//if (6 <= lines.size()) {
						for (int a = 0; a < lines.size(); a++) {
							if (lines.get(a).equals(line_in)) {
								loop = true;
								return;
							}
						}
					//}
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
	 * trainからstationまでの距離を返します。
	 * 距離がlimitdistanceを超えることはありません。
	 * (注意: 列車の向き関係なく計算していることに注意)
	 * @param limitdistance 距離の限界
	 * @param train 列車
	 * @param station 駅(目的地)
	 * @return stationまでの距離
	 */
	public double getDistance(double limitdistance, Station station) {
		Line runningline = getRunningLine();
		double a = runningline.getRailStartPosition(station);
		if (a == -1) {//現在の線路に次の停車駅がないか
			if ((a = aaa(0, limitdistance, station, new ArrayList<Line>(), getRunningLine(), false)) == -1) {
				if ((a = aaa(0, limitdistance, station, new ArrayList<Line>(), getRunningLine(), true)) != -1) {
					return a + getPosition() + station.getLength() / 2;
				}
			} else {
				return -a - getPosition() - station.getLength() / 2;
			}
		} else {
			//あった場合は距離を返す
			return a - getPosition() + station.getLength() / 2;
		}
		return 0;
	}
	private double aaa(double distance, double limitdistance, Station station, List<Line> lines, Line line, boolean right) {
		//接続する線路から駅までの距離を求める(ポイントが接続していなくてもポイントを切り替えれば行けるような場所でも良い)
		if (limitdistance <= distance) {
			return limitdistance;
		}
		if (right) {
			if (0 <= line.getBranch_out()) {
				Line line_out = line.getLines_out().get(line.getBranch_out());
				if (3 <= lines.size()) {
					for (int a = 0; a < lines.size(); a++) {
						if (lines.get(a).equals(line_out)) {
							return -1;
						}
					}
				}
				lines.add(line_out);
				return aaa(distance, limitdistance, station, lines, line_out, true);
			}
		} else {
			if (0 <= line.getBranch_in()) {
				Line line_in = line.getLines_in().get(line.getBranch_in());
				if (3 <= lines.size()) {
					for (int a = 0; a < lines.size(); a++) {
						if (lines.get(a).equals(line_in)) {
							return -1;
						}
					}
				}
				lines.add(0, line_in);
				return aaa(distance, limitdistance, station, lines, line_in, false);
			}
		}
		return -1;
	}
}
