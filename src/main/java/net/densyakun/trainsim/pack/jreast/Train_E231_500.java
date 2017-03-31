package net.densyakun.trainsim.pack.jreast;
import net.densyakun.trainsim.Car;
import net.densyakun.trainsim.Line;
import net.densyakun.trainsim.OneAxisMC;
import net.densyakun.trainsim.Train;
//車両データ: E231系500番台(山手線)
public final class Train_E231_500 extends Train {
	public Train_E231_500(String name, Line runningline, double position, boolean invert) {
		super(name,
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
				1067, runningline, position, 120, 3.0, 4.2, new OneAxisMC(5, 8), invert);
	}
}
