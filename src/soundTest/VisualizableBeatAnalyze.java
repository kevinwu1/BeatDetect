package soundTest;

public abstract class VisualizableBeatAnalyze implements BeatAnalyze {
	protected Visualizer[] v;

	public void setVis(Visualizer[] v) {
		this.v = v;
	}

	public Visualizer[] getVisualizers() {
		return v;
	}
}
