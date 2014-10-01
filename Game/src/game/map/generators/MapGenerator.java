package game.map.generators;

public interface MapGenerator<T> {
	public T[][] generate(int width, int height);
}
