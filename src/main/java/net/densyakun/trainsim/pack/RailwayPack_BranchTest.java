package net.densyakun.trainsim.pack;
import java.util.ArrayList;
import java.util.List;

import net.densyakun.trainsim.Diagram;
import net.densyakun.trainsim.Line;
import net.densyakun.trainsim.Rail;
import net.densyakun.trainsim.Train;
import net.densyakun.trainsim.TrainDriver;
import net.densyakun.trainsim.pack.jreast.Train_E235;
public final class RailwayPack_BranchTest extends RailwayPack {
	public RailwayPack_BranchTest() {
		Line line_0 = new Line("test" + getLines().size(), 1067, 320);
		line_0.addRails(new Rail(1000));
		addLine(line_0);
		Line line_1 = new Line("test" + getLines().size(), 1067, 320);
		line_1.addRails(new Rail(1000));
		addLine(line_1);
		List<Line> array = new ArrayList<Line>();
		array.add(line_1);
		line_0.setLines_out(array);
		(array = new ArrayList<Line>()).add(line_0);
		line_1.setLines_out(array);
		Train train = new Train_E235("(ﾟ∀ﾟ)ｱﾋｬ", line_0, 115, false);
		TrainDriver driver = new TrainDriver("股尾前科");
		driver.setDiagram(new Diagram("☆"));
		train.setDriver(driver);
		addTrain(train);
	}
}
