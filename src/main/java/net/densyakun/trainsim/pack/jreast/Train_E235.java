package net.densyakun.trainsim.pack.jreast;
import net.densyakun.trainsim.Car;
import net.densyakun.trainsim.Line;
import net.densyakun.trainsim.OneAxisMC;
import net.densyakun.trainsim.Train;
//車両データ: E235系
public final class Train_E235 extends Train {
	public Train_E235(String name, Line runningline, double position, boolean invert) {
		super(name,
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
				1067, runningline, position, 120, 3.0, 4.2, new OneAxisMC(5, 8), invert);
	}
}
