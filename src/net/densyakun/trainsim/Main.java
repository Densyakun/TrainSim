package net.densyakun.trainsim;

import net.densyakun.trainsim.pack.RailwayPack_Test;

//デバッグ用クラス
public class Main implements RailwayListener {
	RailwayManager railwaymanager = new RailwayManager();

	public Main() {
		railwaymanager.addListener(this);// リスナーを登録

		railwaymanager.loadRailwayPack(new RailwayPack_Test());

		/*
		// シリアライズ
		try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("test.txt"))) {
			oos.writeObject((RailwayPack) new RailwayPack_FreeGaugeTest());

			// デシリアライズ
			try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("test.txt"))) {
				RailwayPack railwaypack = (RailwayPack) ois.readObject();
				if (railwaypack != null) {
					System.out.println(railwaypack.getName());
					railwaymanager.loadRailwayPack(railwaypack);// 鉄道網データを読み込む
				}
			} catch (IOException | ClassNotFoundException e) {
				e.printStackTrace();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		*/

		railwaymanager.setTimeScale(2);// 時間の倍速を変更
		railwaymanager.setUpdateinterval(250);// 自動でデータを更新する間隔を指定(ミリ秒)
		railwaymanager.play();// シミュレーターを再生
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
		if (railwaymanager.getTrains().size() > 0)
			System.out.println("現在位置: " + railwaymanager.getTrains().get(0).getPosition() + ": "
					+ railwaymanager.getTrains().get(0).getSpeed() + ": "
					+ railwaymanager.getTrains().get(0).getReverser() + ": "
					+ railwaymanager.getTrains().get(0).getAccel() + ": " + railwaymanager.getTrains().get(0).getBrake()
					+ ": " + railwaymanager.getTrains().get(0).getRunningLine() + ": "
					+ railwaymanager.getTrains().get(0).getRunningLines() + ": "
					+ railwaymanager.getTrains().get(0).getRunRoute());
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
	public void trainDerailment(RailwayManager railwaymanager, Train train, double speed) {
		railwaymanager.setTimeScale(RailwayManager.REAL_TIMESCALE);
		System.out.println("脱線事故: " + train + ": " + train.getPosition() + ": " + speed);
	}

	@Override
	public void trainCrush(RailwayManager railwaymanager, Train train_a, Train train_b) {
		railwaymanager.setTimeScale(RailwayManager.REAL_TIMESCALE);
		System.out.println(
				"衝突事故: " + train_a + ": " + train_a.getPosition() + ": " + train_b + ": " + train_b.getPosition());
	}

	@Override
	public void trainStopped(RailwayManager railwaymanager, Train train, Station station) {
		System.out.println("停車: " + train + ": " + station + ": " + train.getDriver().getNextStopStation());
	}
}
