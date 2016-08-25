package net.densyakun.trainsim;
public final class TrainDriver {
	private String name;
	private Diagram diagram;
	public TrainDriver(String name) {
		this.name = name;
	}
	public String getName() {
		return name;
	}
	public Diagram getDiagram() {
		return diagram;
	}
	public void setDiagram(Diagram diagram) {
		this.diagram = diagram;
	}
	@Override
	public boolean equals(Object o) {
		return o == null ? false : o instanceof TrainDriver ? name.equals(((TrainDriver) o).getName()) : false;
	}
}
