package net.densyakun.trainsim.pack;
import net.densyakun.trainsim.Diagram;
import net.densyakun.trainsim.Line;
import net.densyakun.trainsim.LineColor;
import net.densyakun.trainsim.Rail;
import net.densyakun.trainsim.Station;
import net.densyakun.trainsim.Train;
import net.densyakun.trainsim.TrainDriver;
import net.densyakun.trainsim.pack.jreast.Train_E235;
//環状線が正常に動作するかテストする鉄道網データ。
public final class RailwayPack_LoopTest extends RailwayPack {
	public RailwayPack_LoopTest() {
		Line line = new Line("test", 1067, 320, new Station(200, "A駅"), new Rail(1000));
		line.setLoop(true);
		Train train = new Train_E235("(ﾟ∀ﾟ)ｱﾋｬ", line, 10, false);
		train.setLineColor(new LineColor(51, 153, 238));
		TrainDriver driver = new TrainDriver("電車君");
		driver.setDiagram(new Diagram("test"));
		train.setDriver(driver);
		addTrain(train);
	}
}
