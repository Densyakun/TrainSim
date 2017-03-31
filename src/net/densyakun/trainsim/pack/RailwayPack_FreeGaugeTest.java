package net.densyakun.trainsim.pack;
import java.util.ArrayList;
import java.util.List;

import net.densyakun.trainsim.Car;
import net.densyakun.trainsim.Diagram;
import net.densyakun.trainsim.Line;
import net.densyakun.trainsim.Rail;
import net.densyakun.trainsim.Station;
import net.densyakun.trainsim.Train;
import net.densyakun.trainsim.TrainDriver;
import net.densyakun.trainsim.TrainSet;
import net.densyakun.trainsim.TwoAxesMC;
//試験用の鉄道網データ。アップデートにより随時更新
@SuppressWarnings("serial")
public class RailwayPack_FreeGaugeTest extends RailwayPack {
	public RailwayPack_FreeGaugeTest() {
		super("フリーゲージトレインテストデータ");
		Line line = new Line("test_" + getLines().size(), 1067, 120);
		Station sta = new Station(210, "A駅");
		line.addRails(sta);
		line.addRails(new Rail(500));
		Line gaugechangeline = new Line("軌間変換装置", TrainSet.FREE_GAUGE_TRAIN, 45);
		gaugechangeline.addRails(new Rail(10));
		Line line2 = new Line("test2_" + getLines().size(), 1435, 120);
		line2.addRails(new Rail(1500));
		Station sta2 = new Station(210, "B駅");
		line2.addRails(sta2);
		List<Line> a = new ArrayList<Line>();
		a.add(line);
		gaugechangeline.setLines_in(a);
		a = new ArrayList<Line>();
		a.add(gaugechangeline);
		line.setLines_out(a);
		line2.setLines_in(a);
		a = new ArrayList<Line>();
		a.add(line2);
		gaugechangeline.setLines_out(a);
		//line.setLines_in(a);
		addLine(line);
		addLine(gaugechangeline);
		addLine(line2);
		Train train = new Train("(ﾟ∀ﾟ)ｱﾋｬ", new TrainSet("test", new Car[]{new Car("test_car0", 20)}, TrainSet.FREE_GAUGE_TRAIN, 240, 10.0, 10.0, new TwoAxesMC(3, 3)));
		train.place(line, 105, false);
		TrainDriver driver = new TrainDriver("電車君");
		List<Station> stopstationlist = new ArrayList<Station>();
		stopstationlist.add(sta);
		stopstationlist.add(sta2);
		driver.setDiagram(new Diagram("☆", stopstationlist));
		train.setDriver(driver);
		addTrain(train);
	}
}
