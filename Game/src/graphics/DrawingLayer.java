package graphics;

public enum DrawingLayer {
    BACKGROUND(0), // tiles
    SECONDARY(1), // secondary entities: trees, plants
    PRIMARY(2), // primary entities: dwarves, enemies
    CLOUDS(3);

    public final int layer;

    private DrawingLayer(final int layer) {
	this.layer = layer;
    }

    public static DrawingLayer highestLayer() {
	int max = Integer.MIN_VALUE;
	DrawingLayer highest = null;
	for (final DrawingLayer layer : values()) {
	    if (layer.layer > max) {
		highest = layer;
		max = layer.layer;
	    }
	}
	return highest;
    }

    public static DrawingLayer get(final int z) {
	for (final DrawingLayer layer : values()) {
	    if (layer.layer == z)
		return layer;
	}
	return null;
    }
}
