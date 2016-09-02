package net.densyakun.trainsim.pack.jreast;
import net.densyakun.trainsim.Car;
import net.densyakun.trainsim.Line;
import net.densyakun.trainsim.OneAxisMC;
import net.densyakun.trainsim.Train;
public final class Train_E233_1000 extends Train {
	public Train_E233_1000(String name, Line runningline, double position, boolean invert) {
		super(name, 
				new Car[]{new Car(20), new Car(20), new Car(20), new Car(20), new Car(20), new Car(20), new Car(20), new Car(20), new Car(20), new Car(20)},
				1067, runningline, position, 120, 2.5, 5.0, new OneAxisMC(5, 8), invert);
	}
}
