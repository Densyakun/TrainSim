package net.densyakun.trainsim;
public class BranchBlock {
	public static final int state_notconnect = -1;
	public static final int state_main = 0;
	public static final int state_branch = 1;
	private double distance_main;
	private double distance_branch;
	private int state = state_notconnect;
	public BranchBlock(double distance_main, double distance_branch) {
		this.distance_main = distance_main <= 0 ? Double.MIN_NORMAL : distance_main;
		this.distance_branch = distance_branch <= 0 ? Double.MIN_NORMAL : distance_branch;
	}
}
