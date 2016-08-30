package net.densyakun.trainsim.pack;
import java.util.ArrayList;
import java.util.List;

import net.densyakun.trainsim.Diagram;
import net.densyakun.trainsim.Line;
import net.densyakun.trainsim.LineColor;
import net.densyakun.trainsim.Rail;
import net.densyakun.trainsim.Station;
import net.densyakun.trainsim.Train;
import net.densyakun.trainsim.TrainDriver;
import net.densyakun.trainsim.TrainSimMath;
import net.densyakun.trainsim.pack.jreast.Train_E233_1000;
public final class RailwayPack_Test extends RailwayPack {
	public RailwayPack_Test() {
		Line line = new Line("test" + getLines().size(), TrainSimMath.getSpeedLevel(320));
		Rail rail = new Station(210, "宇都宮駅");
		line.addRails(rail);
		//line.addRails(new Rail(387480));
		line.addRails(new Rail(1000));
		line.addRails(new Station(210, "盛岡駅"));
		addLine(line);
		Train train = new Train_E233_1000("(ﾟ∀ﾟ)ｱﾋｬ", line, 105);
		train.setLineColor(new LineColor(51, 153, 238));
		TrainDriver driver = new TrainDriver("電車君");
		List<Station> stopstationlist = new ArrayList<Station>();
		for (int c = 0; c < line.getRails().size(); c++) {
			if (line.getRails().get(c) instanceof Station) {
				stopstationlist.add((Station) line.getRails().get(c));
			}
		}
		driver.setDiagram(new Diagram("☆", stopstationlist));
		train.setDriver(driver);
		addTrain(train);
		addTrain(new Train_E233_1000("＼(^o^)／", line, 700));
	}
}
