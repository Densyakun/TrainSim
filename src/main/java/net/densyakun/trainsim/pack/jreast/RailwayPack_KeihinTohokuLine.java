package net.densyakun.trainsim.pack.jreast;
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
import net.densyakun.trainsim.pack.RailwayPack;
public final class RailwayPack_KeihinTohokuLine extends RailwayPack {
	public RailwayPack_KeihinTohokuLine() {
		Line line = new Line("東北本線上り(京浜東北線南行)" + getLines().size(), TrainSimMath.getSpeedLevel(90));
		Rail rail = new Station(210, "大宮駅");
		line.addRails(rail);
		line.addRails(new Rail(350));
		line.addRails(rail = new Rail(400));
		rail.setLimitSpeed(TrainSimMath.getSpeedLevel(75));
		line.addRails(new Rail(600));
		line.addRails(new Station(210, "さいたま新都心駅"));
		line.addRails(rail = new Rail(200));
		rail.setLimitSpeed(TrainSimMath.getSpeedLevel(65));
		line.addRails(new Rail(600));
		line.addRails(new Station(210, "与野駅"));
		line.addRails(new Rail(600));
		line.addRails(new Station(210, "北浦和駅"));
		line.addRails(new Rail(600));
		line.addRails(new Station(210, "浦和駅"));
		line.addRails(new Rail(600));
		line.addRails(new Station(210, "南浦和駅"));
		line.addRails(new Rail(600));
		line.addRails(new Station(210, "さいたま車両センター"));
		line.addRails(new Rail(600));
		line.addRails(new Station(210, "蕨駅"));
		line.addRails(new Rail(600));
		line.addRails(new Station(210, "西川口駅"));
		line.addRails(new Rail(600));
		line.addRails(new Station(210, "川口駅"));
		line.addRails(new Rail(600));
		line.addRails(new Station(210, "赤羽駅"));
		addLine(line);
		double b = 0;
		LineColor color = new LineColor(51, 153, 238);
		for (int a = 0; a < line.getRails().size() / 2; a++) {
			if (line.getRails().get(a) instanceof Station) {
				Train train = new Train_E233_1000("ウラ" + (100 + a + 1) + "編成", line, b + line.getRails().get(a).getLength() / 2);
				train.setLineColor(color);
				TrainDriver driver = new TrainDriver("電車君" + a + "号");
				List<Station> stopstationlist = new ArrayList<Station>();
				for (int c = a; c < line.getRails().size(); c++) {
					if (line.getRails().get(c) instanceof Station) {
						stopstationlist.add((Station) line.getRails().get(c));
					}
				}
				driver.setDiagram(new Diagram("" + a, stopstationlist));
				train.setDriver(driver);
				//train.setSpeed(95.0 / 4 + (new Random().nextDouble() * 95 - 95.0 / 2));
				addTrain(train);
			}
			b += line.getRails().get(a).getLength();
		}
	}
}
