package net.densyakun.trainsim;
public enum AccidentCause {
	noaccident, derailment, crush, unknown;
	public boolean isBroken() {
		return this == derailment || this == crush;
	}
}
