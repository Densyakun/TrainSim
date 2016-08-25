package net.densyakun.trainsim.pack;
import java.util.ArrayList;
import java.util.List;

import net.densyakun.trainsim.Line;
import net.densyakun.trainsim.Train;
public abstract class RailwayPack {
	private List<Line> lines = new ArrayList<Line>();
	private List<Train> trains = new ArrayList<Train>();
	public final List<Line> getLines() {
		return lines;
	}
	public final void addLine(Line line) {
		lines.add(line);
	}
	public final List<Train> getTrains() {
		return trains;
	}
	public final void addTrain(Train train) {
		trains.add(train);
	}
}
