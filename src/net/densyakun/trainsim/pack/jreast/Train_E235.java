package net.densyakun.trainsim.pack.jreast;
import net.densyakun.trainsim.Car;
import net.densyakun.trainsim.LineColor;
import net.densyakun.trainsim.OneAxisMC;
import net.densyakun.trainsim.TrainSet;
//車両データ: E235系
@SuppressWarnings("serial")
public final class Train_E235 extends TrainSet {
	public Train_E235() {
		super("JR東日本E235系",
				new Car[]{new Car("クハE234-0", 20),
						new Car("モハE234-0", 20),
						new Car("モハE235-0", 20),
						new Car("サハE235-0", 20),
						new Car("モハE234-0", 20),
						new Car("モハE235-0", 20),
						new Car("サハE234-0", 20),
						new Car("モハE234-0", 20),
						new Car("モハE235-0", 20),
						new Car("サハE235-4600", 20),
						new Car("クハE235-0", 20)},
				1067, 120, 3.0, 4.2, new OneAxisMC(5, 8));
		setLineColor(new LineColor(51, 153, 238));
	}
}
