package net.densyakun.trainsim.pack.jreast;
import net.densyakun.trainsim.Car;
import net.densyakun.trainsim.Line;
import net.densyakun.trainsim.OneAxisMC;
import net.densyakun.trainsim.Train;
import net.densyakun.trainsim.TrainSimMath;
public final class Train_E233_1000 extends Train {
	public Train_E233_1000(String name, Line runningline, double position) {
		super(name, new Car[]{new Car(20), new Car(20), new Car(20), new Car(20), new Car(20), new Car(20), new Car(20), new Car(20), new Car(20), new Car(20)},
			runningline, position, TrainSimMath.getSpeedLevel(120), 2.5, 5.0, new OneAxisMC(5, 8));
	}
}
