package net.densyakun.trainsim;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import net.densyakun.trainsim.pack.RailwayPack;
import net.densyakun.trainsim.pack.RailwayPack_Test;
//デバッグ用クラス
public class Main implements RailwayListener {
	RailwayManager railwaymanager = new RailwayManager();
	public Main() {
		/*railwaymanager.addListener(this);//リスナーを登録
		railwaymanager.loadRailwayPack(new RailwayPack_Test());//鉄道網データを読み込む
		railwaymanager.setTimeScale(2);//時間の倍速を変更
		railwaymanager.setUpdateinterval(250);//自動でデータを更新する間隔を指定(ミリ秒)
		railwaymanager.play();//シミュレーターを再生*/
		// シリアライズ
		try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("person.txt"))) {
			RailwayPack railwaypack = new RailwayPack_Test();
			oos.writeObject(railwaypack);
		} catch (IOException e) {
			e.printStackTrace();
		}

		// デシリアライズ
		try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("person.txt"))) {
			RailwayPack railwaypack = (RailwayPack) ois.readObject();
			if (railwaypack != null) {

			}
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
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
		System.out.println("現在位置: " + railwaymanager.getTrains().get(0).getPosition() + ": " + railwaymanager.getTrains().get(0).getSpeed() + ": " + railwaymanager.getTrains().get(0).getReverser() + ": " + railwaymanager.getTrains().get(0).getAccel() + ": " + railwaymanager.getTrains().get(0).getBrake() + ": " + railwaymanager.getTrains().get(0).getRunningLine() + ": " + railwaymanager.getTrains().get(0).getRunningLines() + ": " + railwaymanager.getTrains().get(0).getRunRoute());
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
		System.out.println("衝突事故: " + train_a + ": " + train_a.getPosition() + ": " + train_b + ": " + train_b.getPosition());
	}
	@Override
	public void trainStopped(RailwayManager railwaymanager, Train train, Station station) {
		System.out.println("停車: " + train + ": " + station + ": " + train.getDriver().getNextStopStation());
	}
}
