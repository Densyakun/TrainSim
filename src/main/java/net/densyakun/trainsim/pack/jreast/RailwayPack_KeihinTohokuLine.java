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
import net.densyakun.trainsim.pack.RailwayPack;
public final class RailwayPack_KeihinTohokuLine extends RailwayPack {
	public RailwayPack_KeihinTohokuLine() {
		Line line = new Line("東北本線上り(京浜東北線南行)" + getLines().size(), 1067, 90);
		LineColor color = new LineColor(51, 153, 238);
		line.setLineColor(color);
		Rail rail = new Station(210, "大宮駅");
		line.addRails(rail);
		//line.addRails(new Rail(350));
		//line.addRails(rail = new Rail(400));
		//rail.setLimitSpeed(TrainSimMath.getSpeedLevel(75));
		line.addRails(new Rail(1395));
		line.addRails(new Station(210, "さいたま新都心駅"));
		line.addRails(new Rail(890));
		line.addRails(new Station(210, "与野駅"));
		line.addRails(new Rail(1390));
		line.addRails(new Station(210, "北浦和駅"));
		line.addRails(new Rail(1590));
		line.addRails(new Station(210, "浦和駅"));
		line.addRails(new Rail(1490));
		line.addRails(new Station(210, "南浦和駅"));
		line.addRails(new Rail(1295));
		line.addRails(new Station(210, "さいたま車両センター"));
		line.addRails(new Rail(1295));
		line.addRails(new Station(210, "蕨駅"));
		line.addRails(new Rail(1690));
		line.addRails(new Station(210, "西川口駅"));
		line.addRails(new Rail(1790));
		line.addRails(new Station(210, "川口駅"));
		line.addRails(new Rail(2390));
		line.addRails(new Station(210, "赤羽駅"));
		line.addRails(new Rail(1590));
		line.addRails(new Station(210, "東十条駅"));
		line.addRails(new Rail(1290));
		line.addRails(new Station(210, "王子駅"));
		line.addRails(new Rail(890));
		line.addRails(new Station(210, "上中里駅"));
		line.addRails(new Rail(1480));
		line.addRails(new Station(230, "田端駅"));
		line.addRails(new Rail(570));
		line.addRails(new Station(230, "西日暮里駅"));
		line.addRails(new Rail(270));
		line.addRails(new Station(230, "日暮里駅"));
		line.addRails(new Rail(870));
		line.addRails(new Station(230, "鶯谷駅"));
		line.addRails(new Rail(870));
		line.addRails(new Station(230, "上野駅"));
		line.addRails(new Rail(370));
		line.addRails(new Station(230, "御徒町駅"));
		line.addRails(new Rail(770));
		line.addRails(new Station(230, "秋葉原駅"));
		line.addRails(new Rail(470));
		line.addRails(new Station(230, "神田駅"));
		line.addRails(new Rail(1070));
		line.addRails(new Station(230, "東京駅"));
		line.addRails(new Rail(570));
		line.addRails(new Station(230, "有楽町駅"));
		line.addRails(new Rail(870));
		line.addRails(new Station(230, "新橋駅"));
		line.addRails(new Rail(970));
		line.addRails(new Station(230, "浜松町駅"));
		line.addRails(new Rail(1270));
		line.addRails(new Station(230, "田町駅"));
		line.addRails(new Rail(1980));
		line.addRails(new Station(210, "品川駅"));
		line.addRails(new Rail(2190));
		line.addRails(new Station(210, "大井町駅"));
		line.addRails(new Rail(1990));
		line.addRails(new Station(210, "大森駅"));
		line.addRails(new Rail(2790));
		line.addRails(new Station(210, "蒲田駅"));
		line.addRails(new Rail(3590));
		line.addRails(new Station(210, "川崎駅"));
		line.addRails(new Rail(3290));
		line.addRails(new Station(210, "鶴見駅"));
		line.addRails(new Rail(2890));
		line.addRails(new Station(210, "新子安駅"));
		line.addRails(new Rail(1990));
		line.addRails(new Station(210, "東神奈川駅"));
		line.addRails(new Rail(1590));
		line.addRails(new Station(210, "横浜駅"));
		line.addRails(new Rail(1790));
		line.addRails(new Station(210, "桜木町駅"));
		line.addRails(new Rail(790));
		line.addRails(new Station(210, "関内駅"));
		line.addRails(new Rail(590));
		line.addRails(new Station(210, "石川町駅"));
		line.addRails(new Rail(990));
		line.addRails(new Station(210, "山手駅"));
		line.addRails(new Rail(1890));
		line.addRails(new Station(210, "根岸駅"));
		line.addRails(new Rail(2190));
		line.addRails(new Station(210, "磯子駅"));
		line.addRails(new Rail(1390));
		line.addRails(new Station(210, "新杉田駅"));
		line.addRails(new Rail(2790));
		line.addRails(new Station(210, "洋光台駅"));
		line.addRails(new Rail(1690));
		line.addRails(new Station(210, "港南台駅"));
		line.addRails(new Rail(2290));
		line.addRails(new Station(210, "本郷台駅"));
		line.addRails(new Rail(3395));
		line.addRails(new Station(210, "大船駅"));
		addLine(line);
		double b = 0;
		for (int a = 0; a < line.getRails().size() / 2; a++) {
			if (line.getRails().get(a) instanceof Station) {
				Train train = new Train_E233_1000("ウラ" + (100 + a + 1) + "編成", line, b + line.getRails().get(a).getLength() / 2, false);
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
