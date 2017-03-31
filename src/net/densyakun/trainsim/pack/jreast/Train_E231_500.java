package net.densyakun.trainsim.pack.jreast;
import net.densyakun.trainsim.Car;
import net.densyakun.trainsim.LineColor;
import net.densyakun.trainsim.OneAxisMC;
import net.densyakun.trainsim.TrainSet;
//車両データ: E231系500番台(山手線)
@SuppressWarnings("serial")
public final class Train_E231_500 extends TrainSet {
	public Train_E231_500() {
		super("JR東日本E231系500番台",
				new Car[]{new Car("クハE230-500", 20),
						new Car("モハE230-500", 20),
						new Car("モハE231-500", 20),
						new Car("サハE231-500", 20),
						new Car("モハE230-500", 20),
						new Car("モハE231-500", 20),
						new Car("サハE231-600", 20),
						new Car("モハE230-500", 20),
						new Car("モハE231-500", 20),
						new Car("サハE231-4600", 20),
						new Car("クハE231-500", 20)},
				1067, 120, 3.0, 4.2, new OneAxisMC(5, 8));
		setLineColor(new LineColor(51, 153, 238));
	}
}
