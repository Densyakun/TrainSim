package net.densyakun.trainsim.pack.jreast;
import net.densyakun.trainsim.Car;
import net.densyakun.trainsim.Line;
import net.densyakun.trainsim.OneAxisMC;
import net.densyakun.trainsim.Train;
import net.densyakun.trainsim.TrainSimMath;
public final class Train_E235 extends Train {
	public Train_E235(String name, Line runningline, double position) {
		super(name, new Car[]{new Car(20), new Car(20), new Car(20), new Car(20), new Car(20), new Car(20), new Car(20), new Car(20), new Car(20), new Car(20), new Car(20)},
			runningline, position, TrainSimMath.getSpeedLevel(120), 3.0, 4.2, new OneAxisMC(5, 8));
	}
}
