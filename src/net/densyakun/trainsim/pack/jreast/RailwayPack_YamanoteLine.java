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
//山手線の鉄道網データ(開発中)
@SuppressWarnings("serial")
public class RailwayPack_YamanoteLine extends RailwayPack {
	public RailwayPack_YamanoteLine() {
		super("山手線パック");
		Line line = new Line("山手線外回り", 1067, 90);
		LineColor color = new LineColor(51, 153, 238);
		line.setLineColor(color);
		line.addRails(new Rail(985));
		Rail rail = new Station(230, "品川駅");
		line.addRails(rail);
		//line.addRails(new Rail(350));
		//line.addRails(rail = new Rail(400));
		//rail.setLimitSpeed(TrainSimMath.getSpeedLevel(75));
		line.addRails(new Rail(1770));
		line.addRails(new Station(230, "大崎駅"));
		line.addRails(new Rail(670));
		line.addRails(new Station(230, "五反田駅"));
		line.addRails(new Rail(970));
		line.addRails(new Station(230, "目黒駅"));
		line.addRails(new Rail(1270));
		line.addRails(new Station(230, "恵比寿駅"));
		line.addRails(new Rail(1370));
		line.addRails(new Station(230, "渋谷駅"));
		line.addRails(new Rail(970));
		line.addRails(new Station(230, "原宿駅"));
		line.addRails(new Rail(1270));
		line.addRails(new Station(230, "代々木駅"));
		line.addRails(new Rail(470));
		line.addRails(new Station(230, "新宿駅"));
		line.addRails(new Rail(1070));
		line.addRails(new Station(230, "新大久保駅"));
		line.addRails(new Rail(1170));
		line.addRails(new Station(230, "高田馬場駅"));
		line.addRails(new Rail(670));
		line.addRails(new Station(230, "目白駅"));
		line.addRails(new Rail(970));
		line.addRails(new Station(230, "池袋駅"));
		line.addRails(new Rail(1570));
		line.addRails(new Station(230, "大塚駅"));
		line.addRails(new Rail(870));
		line.addRails(new Station(230, "巣鴨駅"));
		line.addRails(new Rail(470));
		line.addRails(new Station(230, "駒込駅"));
		line.addRails(new Rail(1370));
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
		line.addRails(new Rail(1970));
		line.addRails(new Station(230, "品川駅"));
		line.addRails(new Rail(985));
		line.setLoop(true);
		addLine(line);
		double b = 0;
		for (int a = 0; a < line.getRails().size() / 2; a++) {
			if (line.getRails().get(a) instanceof Station) {
				Train train = new Train("トウ" + (100 + a + 1) + "編成", new Train_E231_500());
				train.place(line, b + line.getRails().get(a).getLength() / 2, true);
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
