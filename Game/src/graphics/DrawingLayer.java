package graphics;

public enum DrawingLayer {
	BACKGROUND(0), SECONDARY(1), PRIMARY(2);

	public final int layer;

	private DrawingLayer(final int layer) {
		this.layer = layer;
	}
}
