package net.densyakun.trainsim;
public final class TrainSimMath {
	public static int getSpeedLevel(double speed) {
		return speed < 20 ? 0 : (int) Math.floor(speed / 5) - 3;
	}
	public static int getSpeed(int speedlevel) {
		return (speedlevel < 0 ? 0 : speedlevel) * 5 + 15;
	}
}
