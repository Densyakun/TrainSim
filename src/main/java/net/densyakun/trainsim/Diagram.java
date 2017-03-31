package net.densyakun.trainsim;
import java.util.ArrayList;
import java.util.List;
//列車のダイヤ
public final class Diagram {
	private String name;//ダイヤの名前(仮)
	private List<Station> stopstationlist;//停車駅のリスト
	public Diagram(String name) {
		this(name, new ArrayList<Station>());
	}
	public Diagram(String name, List<Station> stopstationlist) {
		this.name = name;
		this.stopstationlist = stopstationlist;
	}
	public String getName() {
		return name;
	}
	public List<Station> getStopStationList() {
		return stopstationlist;
	}
	public void setStopStationList(List<Station> stopstationlist) {
		this.stopstationlist = stopstationlist;
	}
	@Override
	public boolean equals(Object arg0) {
		return arg0 == null ? false : arg0 instanceof Diagram ? name.equals(((Diagram) arg0).getName()) : false;
	}
	@Override
	public String toString() {
		return name;
	}
}
