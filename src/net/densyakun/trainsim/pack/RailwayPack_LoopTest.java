package net.densyakun.trainsim.pack;
import net.densyakun.trainsim.Diagram;
import net.densyakun.trainsim.Line;
import net.densyakun.trainsim.Rail;
import net.densyakun.trainsim.Station;
import net.densyakun.trainsim.Train;
import net.densyakun.trainsim.TrainDriver;
import net.densyakun.trainsim.pack.jreast.Train_E235;
//環状線が正常に動作するかテストする鉄道網データ。
@SuppressWarnings("serial")
public class RailwayPack_LoopTest extends RailwayPack {
	public RailwayPack_LoopTest() {
		super("環状線テスト");
		Line line = new Line("test", 1067, 320, new Station(200, "A駅"), new Rail(1000));
		line.setLoop(true);
		Train train = new Train("(ﾟ∀ﾟ)ｱﾋｬ", new Train_E235());
		train.place(line, 10, false);
		TrainDriver driver = new TrainDriver("電車君");
		driver.setDiagram(new Diagram("test"));
		train.setDriver(driver);
		addTrain(train);
	}
}
