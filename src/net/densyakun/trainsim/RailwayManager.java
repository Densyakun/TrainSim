package net.densyakun.trainsim;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import net.densyakun.trainsim.pack.RailwayPack;
//TrainSimを扱う上で必ず使う基本クラス
public final class RailwayManager implements Runnable {
	public static final String VERSION = "0.0.2a";//TrainSimのバージョン

	/*時間の処理-START-

	時間を再生するにはplay()を実行する。
	時間を停止するにはstop()を実行する。
	時間を更新するにはupdate()を実行する。
	更新すると現実の経過時間が反映される。

	setTimeScale(timescale)でupdate()が実行された際の現実時間の経過時間を倍速にする設定もできる。

	自動でupdateを実行するリアルタイム機能がある。
	setUpdateinterval(updateinterval)で設定できる。
	updateintervalには更新間隔(ミリ秒)を代入する。
	自動で時間を進めない場合はupdateintervalにNOT_AUTO_UPDATEを入れる。*/

	public static final int NOT_AUTO_UPDATE = 0;//自動で時間を進めない
	public static final int MAXSPEED_AUTO_UPDATE = 50;//update()を行うときの最小経過時間
	public static final int REAL_TIMESCALE = 1;//倍速設定の初期設定

	private boolean playing = false;//再生されているか
	private int timescale = REAL_TIMESCALE;//倍速設定
	private Thread thread;//リアルタイム機能のスレッド
	private int updateinterval = NOT_AUTO_UPDATE;//リアルタイム機能の更新間隔(ミリ秒)
	private Date update;//更新後の時間
	private Date time;//現在の時間

	//時間の処理-END-

	public Logger logger = Logger.getLogger(RailwayManager.class.getName());//ロガー
	private List<RailwayListener> listeners = new ArrayList<RailwayListener>();//リスナー
	public List<TrainSet> trainsets = new ArrayList<TrainSet>();//編成データベース
	private List<Line> lines = new ArrayList<Line>();//路線情報
	private List<Train> trains = new ArrayList<Train>();//列車情報
	//private List<Station> stations = new ArrayList<Station>();//駅情報
	public RailwayManager() {
		this(new Date());
	}
	public RailwayManager(Date time) {
		update = this.time = time;
		logger.info("RailwayManagerが起動しました" + update);
	}
	@Override
	public void run() {
		while (updateinterval != NOT_AUTO_UPDATE) {
			if (playing) {
				update();
				try {
					Thread.sleep(updateinterval);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		thread = null;
	}
	public List<Line> getLines() {
		return lines;
	}
	public boolean addLine(Line line) {
		for (int a = 0; a < lines.size(); a++) {
			if (lines.get(a).getName().equals(line.getName())) {
				return false;
			}
		}
		lines.add(line);
		for (int a = 0; a < listeners.size(); a++) {
			listeners.get(a).addLine(this, line);
		}
		return true;
	}
	public List<Train> getTrains() {
		return trains;
	}
	public void addTrain(Train train) {
		trains.add(train);
		for (int a = 0; a < listeners.size(); a++) {
			listeners.get(a).addTrain(this, train);
		}
		update();
	}
	/**
	 * 鉄道網データを読み込む。
	 * すでに他の鉄道網データを読み込んでいる状態に追加される。
	 * @param pack 鉄道網データ
	 */
	public void loadRailwayPack(RailwayPack pack) {
		logger.info("鉄道網データを読み込みました: " + pack.getName());
		List<Line> lines = pack.getLines();
		for (int a = 0; a < lines.size(); a++) {
			addLine(lines.get(a));
		}
		List<Train> trains = pack.getTrains();
		for (int a = 0; a < trains.size(); a++) {
			addTrain(trains.get(a));
		}
	}
	/**
	 * 読み込んでいる路線・列車データを初期化する。
	 */
	public void clearRailway() {
		lines.clear();
		trains.clear();
		for (int a = 0; a < listeners.size(); a++) {
			listeners.get(a).clearRailway(this);
		}
	}
	public boolean isPlaying() {
		return playing;
	}
	public Date getUpdateTime() {
		return update;
	}
	public void play() {
		if (!playing) {
			update = new Date();
			playing = true;
			for (int a = 0; a < listeners.size(); a++) {
				listeners.get(a).played(this);
			}
		}
	}
	public void stop() {
		if (playing) {
			update();
			playing = false;
			for (int a = 0; a < listeners.size(); a++) {
				listeners.get(a).stopped(this);
			}
		}
	}
	public int getTimeScale() {
		return timescale;
	}
	public void setTimeScale(int timescale) {
		this.timescale = 0 <= timescale ? timescale : 0;
	}
	public Date getTime() {
		return time;
	}
	public void setTime(Date time) {
		this.time = time;
	}
	public void update() {
		if (playing) {
			Date old = update;
			Date update = new Date();
			if (MAXSPEED_AUTO_UPDATE <= update.getTime() - old.getTime()) {
				this.update = update;
				long ms = (update.getTime() - old.getTime()) * timescale;
				for (int a = 0; a < ms / MAXSPEED_AUTO_UPDATE; a++) {
					time.setTime(time.getTime() + ms / MAXSPEED_AUTO_UPDATE);
					//列車の情報を更新
					for (int b = 0; b < trains.size(); b++) {
						trains.get(b).updatetick(this);
					}

					//--- 衝突の判定 -START- ---
					//TODO 負担をかけやすい処理なため今後軽量化予定
					for (int b = 0; b < trains.size(); b++) {
						for (int c = b + 1; c < trains.size(); c++) {
							Train train_a = trains.get(b);
							Train train_b = trains.get(c);
							Line train_a_runningline = train_a.getRunningLine();
							Line train_b_runningline = train_b.getRunningLine();
							double train_a_position = train_a.getPosition();
							double train_b_position = train_b.getPosition();
							double train_a_length = train_a.getTrainset().getLength();
							double train_b_length = train_b.getTrainset().getLength();
							if (train_a_runningline.equals(train_b_runningline)) {
								boolean d = train_a_position + train_a_length / 2 >= train_b_position + train_b_length / 2 && train_a_position - train_a_length / 2 < train_b_position + train_b_length / 2;
								boolean e = train_a_position - train_a_length / 2 <= train_b_position - train_b_length / 2 && train_a_position + train_a_length / 2 > train_b_position - train_b_length / 2;
								if (d) {
									trainCrush(train_a, train_b);
									//TODO 衝突地点が速度と時間によりズレが生じるため、交点を求めることで問題を修正予定
									double collision_point = (train_a_position - train_a_length / 2 + train_b_position + train_b_length / 2) / 2;
									train_a.teleport(this, collision_point - train_a_length / 2);
									train_b.teleport(this, collision_point + train_b_length / 2);
								} else if (e) {
									trainCrush(train_a, train_b);
									//TODO 衝突地点が速度と時間によりズレが生じるため、交点を求めることで問題を修正予定
									double collision_point = (train_a_position + train_a_length / 2 + train_b_position - train_b_length / 2) / 2;
									train_a.teleport(this, collision_point + train_a_length / 2);
									train_b.teleport(this, collision_point - train_b_length / 2);
								}
								if (d || e) {
									double train_a_speed = train_a.getSpeed();
									train_a.setSpeed((train_a_speed + train_b.getSpeed()) / 2);
									train_b.setSpeed(train_a.getSpeed());
								}
							} else {
								List<Line> train_a_runroute = train_a.getRunRoute();
								List<Line> train_b_runroute = train_b.getRunRoute();
								double distance_a = train_a_position;
								if (distance_a < train_a_length / 2) {
									boolean d = false;
									boolean invert_a = false;
									for (int e = train_a_runroute.size() - 1; 0 <= e; e--) {
										Line line_a = train_a_runroute.get(e);
										if (d) {
											if (line_a.equals(train_a_runningline)) {
												break;
											}
											Line line_back_a = train_a_runroute.get(e + 1);
											if (invert_a) {
												List<Line> lines_out_a = line_a.getLines_out();
												for (int f = 0; f < lines_out_a.size(); f++) {
													if (lines_out_a.get(f).equals(line_back_a)) {
														invert_a = !invert_a;
														break;
													}
												}
											} else {
												List<Line> lines_in_a = line_a.getLines_in();
												for (int f = 0; f < lines_in_a.size(); f++) {
													if (lines_in_a.get(f).equals(line_back_a)) {
														invert_a = !invert_a;
														break;
													}
												}
											}
											double distance_b = train_b_position;
											if (distance_b < train_b_length / 2) {
												boolean f = false;
												boolean g = false;
												boolean invert_b = false;
												for (int h = train_b_runroute.size() - 1; 0 <= h; h--) {
													Line line_b = train_b_runroute.get(h);
													if (f) {
														if (line_b.equals(train_b_runningline)) {
															break;
														}
														Line line_back_b = train_b_runroute.get(h + 1);
														if (invert_b) {
															List<Line> lines_out_b = line_b.getLines_out();
															for (int i = 0; i < lines_out_b.size(); i++) {
																if (lines_out_b.get(i).equals(line_back_b)) {
																	invert_b = !invert_b;
																	break;
																}
															}
														} else {
															List<Line> lines_in_b = line_b.getLines_in();
															for (int i = 0; i < lines_in_b.size(); i++) {
																if (lines_in_b.get(i).equals(line_back_b)) {
																	invert_b = !invert_b;
																	break;
																}
															}
														}
														if (line_a.equals(line_b)) {
															double linelength = line_a.getLength();
															double train_a_left = 0;
															double train_a_right = 0;
															double train_b_left = 0;
															double train_b_right = 0;
															if (invert_a) {
																train_a_left = -distance_a - train_a_length / 2;
																train_a_right = -distance_a + train_a_length / 2;
															} else {
																train_a_left = linelength + distance_a - train_a_length / 2;
																train_a_right = linelength + distance_a + train_a_length / 2;
															}
															if (invert_b) {
																train_b_left = -distance_b - train_b_length / 2;
																train_b_right = -distance_b + train_b_length / 2;
															} else {
																train_b_left = linelength + distance_b - train_b_length / 2;
																train_b_right = linelength + distance_b + train_b_length / 2;
															}
															boolean i = train_a_right >= train_b_right && train_a_left < train_b_right;
															boolean j = train_a_left <= train_b_left && train_a_right > train_b_left;
															if (i) {
																trainCrush(train_a, train_b);
																if (line_back_a.equals(line_back_b)) {
																	//TODO 衝突地点が速度と時間によりズレが生じるため、交点を求めることで問題を修正予定
																	double collision_point = (train_a_left + train_b_right) / 2;
																	train_a.teleport(this, collision_point - train_a_length / 2);
																	train_b.teleport(this, collision_point + train_b_length / 2);
																	double train_a_speed = train_a.getSpeed();
																	train_a.setSpeed((train_a_speed + train_b.getSpeed()) / 2);
																	train_b.setSpeed(train_a.getSpeed());
																} else {
																	train_a.setSpeed(0);
																	train_b.setSpeed(0);
																}
															} else 	if (j) {
																trainCrush(train_a, train_b);
																if (line_back_a.equals(line_back_b)) {
																	//TODO 衝突地点が速度と時間によりズレが生じるため、交点を求めることで問題を修正予定
																	double collision_point = (train_a_right + train_b_left) / 2;
																	train_a.teleport(this, collision_point + train_a_length / 2);
																	train_b.teleport(this, collision_point - train_b_length / 2);
																	double train_a_speed = train_a.getSpeed();
																	train_a.setSpeed((train_a_speed + train_b.getSpeed()) / 2);
																	train_b.setSpeed(train_a.getSpeed());
																} else {
																	train_a.setSpeed(0);
																	train_b.setSpeed(0);
																}
															}
															g = true;
															break;
														}
														if ((distance_b += line_b.getLength()) >= train_b_length) {
															break;
														}
													} else if (line_b.equals(train_b_runningline)) {
														f = true;
													}
												}
												if (g) {
													break;
												}
											} else if ((distance_b = train_b_runningline.getLength() - distance_b) < train_b_length) {
												boolean f = false;
												boolean g = false;
												boolean invert_b = false;
												for (int h = 0; h < train_b_runroute.size(); h++) {
													Line line_b = train_b_runroute.get(h);
													if (f) {
														if (line_b.equals(train_b_runningline)) {
															break;
														}
														Line line_back_b = train_b_runroute.get(h - 1);
														if (invert_b) {
															List<Line> lines_in_b = line_b.getLines_in();
															for (int i = 0; i < lines_in_b.size(); i++) {
																if (lines_in_b.get(i).equals(line_back_b)) {
																	invert_b = !invert_b;
																	break;
																}
															}
														} else {
															List<Line> lines_out_b = line_b.getLines_out();
															for (int i = 0; i < lines_out_b.size(); i++) {
																if (lines_out_b.get(i).equals(line_back_b)) {
																	invert_b = !invert_b;
																	break;
																}
															}
														}
														if (line_a.equals(line_b)) {
															double linelength = line_a.getLength();
															double train_a_left = 0;
															double train_a_right = 0;
															double train_b_left = 0;
															double train_b_right = 0;
															if (invert_a) {
																train_a_left = -distance_a - train_a_length / 2;
																train_a_right = -distance_a + train_a_length / 2;
															} else {
																train_a_left = linelength + distance_a - train_a_length / 2;
																train_a_right = linelength + distance_a + train_a_length / 2;
															}
															if (invert_b) {
																train_b_left = linelength + distance_b - train_b_length / 2;
																train_b_right = linelength + distance_b + train_b_length / 2;
															} else {
																train_b_left = -distance_b - train_b_length / 2;
																train_b_right = -distance_b + train_b_length / 2;
															}
															boolean i = train_a_right >= train_b_right && train_a_left < train_b_right;
															boolean j = train_a_left <= train_b_left && train_a_right > train_b_left;
															if (i) {
																trainCrush(train_a, train_b);
																if (line_back_a.equals(line_back_b)) {
																	//TODO 衝突地点が速度と時間によりズレが生じるため、交点を求めることで問題を修正予定
																	double collision_point = (train_a_left + train_b_right) / 2;
																	train_a.teleport(this, collision_point - train_a_length / 2);
																	train_b.teleport(this, collision_point + train_b_length / 2);
																	double train_a_speed = train_a.getSpeed();
																	train_a.setSpeed((train_a_speed + train_b.getSpeed()) / 2);
																	train_b.setSpeed(train_a.getSpeed());
																} else {
																	train_a.setSpeed(0);
																	train_b.setSpeed(0);
																}
															} else 	if (j) {
																trainCrush(train_a, train_b);
																if (line_back_a.equals(line_back_b)) {
																	//TODO 衝突地点が速度と時間によりズレが生じるため、交点を求めることで問題を修正予定
																	double collision_point = (train_a_right + train_b_left) / 2;
																	train_a.teleport(this, collision_point + train_a_length / 2);
																	train_b.teleport(this, collision_point - train_b_length / 2);
																	double train_a_speed = train_a.getSpeed();
																	train_a.setSpeed((train_a_speed + train_b.getSpeed()) / 2);
																	train_b.setSpeed(train_a.getSpeed());
																} else {
																	train_a.setSpeed(0);
																	train_b.setSpeed(0);
																}
															}
															g = true;
															break;
														}
														if ((distance_b += line_b.getLength()) >= train_b_length) {
															break;
														}
													} else if (line_b.equals(train_b_runningline)) {
														f = true;
													}
												}
												if (g) {
													break;
												}
											}
											if ((distance_a += line_a.getLength()) >= train_a_length) {
												break;
											}
										} else if (line_a.equals(train_a_runningline)) {
											d = true;
										}
									}
								} else if ((distance_a = train_a_runningline.getLength() - distance_a) < train_a_length) {
									boolean d = false;
									boolean invert_a = false;
									for (int e = 0; e < train_a_runroute.size(); e++) {
										Line line_a = train_a_runroute.get(e);
										if (d) {
											if (line_a.equals(train_a_runningline)) {
												break;
											}
											Line line_back_a = train_a_runroute.get(e - 1);
											if (invert_a) {
												List<Line> lines_in_a = line_a.getLines_in();
												for (int f = 0; f < lines_in_a.size(); f++) {
													if (lines_in_a.get(f).equals(line_back_a)) {
														invert_a = !invert_a;
														break;
													}
												}
											} else {
												List<Line> lines_out_a = line_a.getLines_out();
												for (int f = 0; f < lines_out_a.size(); f++) {
													if (lines_out_a.get(f).equals(line_back_a)) {
														invert_a = !invert_a;
														break;
													}
												}
											}
											double distance_b = train_b_position;
											if (distance_b < train_b_length / 2) {
												boolean f = false;
												boolean g = false;
												boolean invert_b = false;
												for (int h = train_b_runroute.size() - 1; 0 <= h; h--) {
													Line line_b = train_b_runroute.get(h);
													if (f) {
														if (line_b.equals(train_b_runningline)) {
															break;
														}
														Line line_back_b = train_b_runroute.get(h + 1);
														if (invert_b) {
															List<Line> lines_out_b = line_b.getLines_out();
															for (int i = 0; i < lines_out_b.size(); i++) {
																if (lines_out_b.get(i).equals(line_back_b)) {
																	invert_b = !invert_b;
																	break;
																}
															}
														} else {
															List<Line> lines_in_b = line_b.getLines_in();
															for (int i = 0; i < lines_in_b.size(); i++) {
																if (lines_in_b.get(i).equals(line_back_b)) {
																	invert_b = !invert_b;
																	break;
																}
															}
														}
														if (line_a.equals(line_b)) {
															double linelength = line_a.getLength();
															double train_a_left = 0;
															double train_a_right = 0;
															double train_b_left = 0;
															double train_b_right = 0;
															if (invert_a) {
																train_a_left = linelength + distance_a - train_a_length / 2;
																train_a_right = linelength + distance_a + train_a_length / 2;
															} else {
																train_a_left = -distance_a - train_a_length / 2;
																train_a_right = -distance_a + train_a_length / 2;
															}
															if (invert_b) {
																train_b_left = -distance_b - train_b_length / 2;
																train_b_right = -distance_b + train_b_length / 2;
															} else {
																train_b_left = linelength + distance_b - train_b_length / 2;
																train_b_right = linelength + distance_b + train_b_length / 2;
															}
															boolean i = train_a_right >= train_b_right && train_a_left < train_b_right;
															boolean j = train_a_left <= train_b_left && train_a_right > train_b_left;
															if (i) {
																trainCrush(train_a, train_b);
																if (line_back_a.equals(line_back_b)) {
																	//TODO 衝突地点が速度と時間によりズレが生じるため、交点を求めることで問題を修正予定
																	double collision_point = (train_a_left + train_b_right) / 2;
																	train_a.teleport(this, collision_point - train_a_length / 2);
																	train_b.teleport(this, collision_point + train_b_length / 2);
																	double train_a_speed = train_a.getSpeed();
																	train_a.setSpeed((train_a_speed + train_b.getSpeed()) / 2);
																	train_b.setSpeed(train_a.getSpeed());
																} else {
																	train_a.setSpeed(0);
																	train_b.setSpeed(0);
																}
															} else 	if (j) {
																trainCrush(train_a, train_b);
																if (line_back_a.equals(line_back_b)) {
																	//TODO 衝突地点が速度と時間によりズレが生じるため、交点を求めることで問題を修正予定
																	double collision_point = (train_a_right + train_b_left) / 2;
																	train_a.teleport(this, collision_point + train_a_length / 2);
																	train_b.teleport(this, collision_point - train_b_length / 2);
																	double train_a_speed = train_a.getSpeed();
																	train_a.setSpeed((train_a_speed + train_b.getSpeed()) / 2);
																	train_b.setSpeed(train_a.getSpeed());
																} else {
																	train_a.setSpeed(0);
																	train_b.setSpeed(0);
																}
															}
															g = true;
															break;
														}
														if ((distance_b += line_b.getLength()) >= train_b_length) {
															break;
														}
													} else if (line_b.equals(train_b_runningline)) {
														f = true;
													}
												}
												if (g) {
													break;
												}
											} else if ((distance_b = train_b_runningline.getLength() - distance_b) < train_b_length) {
												boolean f = false;
												boolean g = false;
												boolean invert_b = false;
												for (int h = 0; h < train_b_runroute.size(); h++) {
													Line line_b = train_b_runroute.get(h);
													if (f) {
														if (line_b.equals(train_b_runningline)) {
															break;
														}
														Line line_back_b = train_b_runroute.get(h - 1);
														if (invert_b) {
															List<Line> lines_in_b = line_b.getLines_in();
															for (int i = 0; i < lines_in_b.size(); i++) {
																if (lines_in_b.get(i).equals(line_back_b)) {
																	invert_b = !invert_b;
																	break;
																}
															}
														} else {
															List<Line> lines_out_b = line_b.getLines_out();
															for (int i = 0; i < lines_out_b.size(); i++) {
																if (lines_out_b.get(i).equals(line_back_b)) {
																	invert_b = !invert_b;
																	break;
																}
															}
														}
														if (line_a.equals(line_b)) {
															double linelength = line_a.getLength();
															double train_a_left = 0;
															double train_a_right = 0;
															double train_b_left = 0;
															double train_b_right = 0;
															if (invert_a) {
																train_a_left = linelength + distance_a - train_a_length / 2;
																train_a_right = linelength + distance_a + train_a_length / 2;
															} else {
																train_a_left = -distance_a - train_a_length / 2;
																train_a_right = -distance_a + train_a_length / 2;
															}
															if (invert_b) {
																train_b_left = linelength + distance_b - train_b_length / 2;
																train_b_right = linelength + distance_b + train_b_length / 2;
															} else {
																train_b_left = -distance_b - train_b_length / 2;
																train_b_right = -distance_b + train_b_length / 2;
															}
															boolean i = train_a_right >= train_b_right && train_a_left < train_b_right;
															boolean j = train_a_left <= train_b_left && train_a_right > train_b_left;
															if (i) {
																trainCrush(train_a, train_b);
																if (line_back_a.equals(line_back_b)) {
																	//TODO 衝突地点が速度と時間によりズレが生じるため、交点を求めることで問題を修正予定
																	double collision_point = (train_a_left + train_b_right) / 2;
																	train_a.teleport(this, collision_point - train_a_length / 2);
																	train_b.teleport(this, collision_point + train_b_length / 2);
																	double train_a_speed = train_a.getSpeed();
																	train_a.setSpeed((train_a_speed + train_b.getSpeed()) / 2);
																	train_b.setSpeed(train_a.getSpeed());
																} else {
																	train_a.setSpeed(0);
																	train_b.setSpeed(0);
																}
															} else 	if (j) {
																trainCrush(train_a, train_b);
																if (line_back_a.equals(line_back_b)) {
																	//TODO 衝突地点が速度と時間によりズレが生じるため、交点を求めることで問題を修正予定
																	double collision_point = (train_a_right + train_b_left) / 2;
																	train_a.teleport(this, collision_point + train_a_length / 2);
																	train_b.teleport(this, collision_point - train_b_length / 2);
																	double train_a_speed = train_a.getSpeed();
																	train_a.setSpeed((train_a_speed + train_b.getSpeed()) / 2);
																	train_b.setSpeed(train_a.getSpeed());
																} else {
																	train_a.setSpeed(0);
																	train_b.setSpeed(0);
																}
															}
															g = true;
															break;
														}
														if ((distance_b += line_b.getLength()) >= train_b_length) {
															break;
														}
													} else if (line_b.equals(train_b_runningline)) {
														f = true;
													}
												}
												if (g) {
													break;
												}
											}
											if ((distance_a += line_a.getLength()) >= train_a_length) {
												break;
											}
										} else if (line_a.equals(train_a_runningline)) {
											d = true;
										}
									}
								}
							}
						}
					}
					//--- 衝突の判定  -END-  ---
				}
				for (int a = 0; a < listeners.size(); a++) {
					listeners.get(a).updated(this);
				}
			}
		}
	}
	public void addListener(RailwayListener listener) {
		listeners.add(listener);
	}

	public void setUpdateinterval(int updateinterval) {
		if ((this.updateinterval = (updateinterval < MAXSPEED_AUTO_UPDATE ? MAXSPEED_AUTO_UPDATE : updateinterval)) != NOT_AUTO_UPDATE) {
			if (thread == null) {
				(thread = new Thread(this)).start();
			}
		}
	}

	public void trainDerailment(Train train) {
		if (!train.isDerailment()) {
			double speed = train.getSpeed();
			train.derailment();
			for (int a = 0; a < listeners.size(); a++) {
				listeners.get(a).trainDerailment(this, train, speed);
			}
		}
	}

	public void trainCrush(Train train_a, Train train_b) {
		train_a.crush();
		train_b.crush();
		for (int a = 0; a < listeners.size(); a++) {
			listeners.get(a).trainCrush(this, train_a, train_b);
		}
	}

	public void trainStopped(Train train, Station station) {
		train.stopStation();
		for (int a = 0; a < listeners.size(); a++) {
			listeners.get(a).trainStopped(this, train, station);
		}
	}
}
