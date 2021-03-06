package net.densyakun.trainsim.pack.jreast;
import net.densyakun.trainsim.Car;
import net.densyakun.trainsim.LineColor;
import net.densyakun.trainsim.OneAxisMC;
import net.densyakun.trainsim.TrainSet;
//車両データ: E233系1000番台(京浜東北線)
@SuppressWarnings("serial")
public final class Train_E233_1000 extends TrainSet {
	public Train_E233_1000() {
		super("JR東日本E233系1000番台",
				new Car[]{new Car("クハE232-1000", 20),
						new Car("モハE232-1200", 20),
						new Car("モハE233-1200", 20),
						new Car("モハE232-1000", 20),
						new Car("モハE233-1000", 20),
						new Car("サハE233-1000", 20),
						new Car("モハE232-1400", 20),
						new Car("モハE233-1400", 20),
						new Car("サハE233-1200", 20),
						new Car("クハE233-1000", 20)},
				1067, 120, 2.5, 5.0, new OneAxisMC(5, 8));
		setLineColor(new LineColor(51, 153, 238));
	}
}
