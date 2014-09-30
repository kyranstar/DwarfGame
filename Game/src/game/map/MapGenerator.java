package game.map;

public interface MapGenerator<T> {
	public T[][] generate(int width, int height);
}
