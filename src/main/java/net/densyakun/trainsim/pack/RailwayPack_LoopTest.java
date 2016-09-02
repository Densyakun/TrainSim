package net.densyakun.trainsim.pack;
import net.densyakun.trainsim.Diagram;
import net.densyakun.trainsim.Line;
import net.densyakun.trainsim.LineColor;
import net.densyakun.trainsim.Rail;
import net.densyakun.trainsim.Station;
import net.densyakun.trainsim.Train;
import net.densyakun.trainsim.TrainDriver;
import net.densyakun.trainsim.pack.jreast.Train_E235;
public final class RailwayPack_LoopTest extends RailwayPack {
	public RailwayPack_LoopTest() {
		Line line = new Line("test" + getLines().size(), 1067, 320);
		Rail rail = new Station(210, "A駅");
		line.addRails(rail);
		line.addRails(new Rail(1000));
		line.addRails(new Station(210, "B駅"));
		line.addRails(new Rail(1000));
		line.setLoop(true);
		addLine(line);
		Train train = new Train_E235("(ﾟ∀ﾟ)ｱﾋｬ", line, 105, false);
		train.setLineColor(new LineColor(51, 153, 238));
		TrainDriver driver = new TrainDriver("電車君");
		driver.setDiagram(new Diagram("test"));
		train.setDriver(driver);
		addTrain(train);
	}
}
