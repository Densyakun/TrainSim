package net.densyakun.trainsim.pack;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.densyakun.trainsim.Line;
import net.densyakun.trainsim.Train;
//鉄道網データ。RailwayManagerで読み込むことができる。
//シリアライズ・デシリアライズ可能。
public abstract class RailwayPack implements Serializable {
	/**
	 * 0.0.2aで使用可能
	 */
	private static final long serialVersionUID = 1L;

	private List<Line> lines = new ArrayList<Line>();
	private List<Train> trains = new ArrayList<Train>();
	private String author;
	private Date created;
	private String comment;
	public List<Line> getLines() {
		return lines;
	}
	public void setLines(List<Line> lines) {
		this.lines = lines;
	}
	public void addLine(Line line) {
		lines.add(line);
	}
	public List<Train> getTrains() {
		return trains;
	}
	public void setTrains(List<Train> trains) {
		this.trains = trains;
	}
	public void addTrain(Train train) {
		trains.add(train);
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public Date getCreated() {
		return created;
	}
	public void setCreated(Date created) {
		this.created = created;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
}
