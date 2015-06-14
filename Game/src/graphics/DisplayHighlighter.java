package graphics;

import graphics.asciiPanel.AsciiPanel;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.swing.JPanel;

public class DisplayHighlighter extends JPanel {

    private static final long serialVersionUID = 1L;

    private final List<Highlight> highlighters = new ArrayList<>();
    private final Optional<Color>[][] tiles;

    private static final Color DEFAULT_COLOR = new Color(255, 255, 0, 100);

    private int viewportX, viewportY;

    @SuppressWarnings("unchecked")
    public DisplayHighlighter(final int widthInChars, final int heightInChars) {
	tiles = new Optional[widthInChars][heightInChars];
	setOpaque(false);
	for (int x = 0; x < widthInChars; x++) {
	    for (int y = 0; y < heightInChars; y++) {
		tiles[x][y] = Optional.empty();
	    }
	}
    }

    public void draw(final int viewportX, final int viewportY) {
	this.viewportX = viewportX;
	this.viewportY = viewportY;

	for (int i = highlighters.size() - 1; i >= 0; i--) {
	    final Highlight h = highlighters.get(i);
	    if (h.isBlinking) {
		if (h.currentTime <= 0) {
		    h.currentTime = h.blinkTime;
		    h.isOn = !h.isOn;
		} else {
		    h.currentTime--;
		}
	    }
	    if (h.isOn) {
		tiles[h.x][h.y] = Optional.of(h.c);
	    } else {
		tiles[h.x][h.y] = Optional.empty();
	    }
	}
    }

    @Override
    protected void paintComponent(final Graphics g) {
	super.paintComponent(g);
	for (int x = 0; x < tiles.length; x++) {
	    for (int y = 0; y < tiles[0].length; y++) {
		if (tiles[x][y].isPresent()) {
		    g.setColor(tiles[x][y].get());
		    g.fillRect((x - viewportX) * AsciiPanel.getCharWidth(), (y - viewportY) * AsciiPanel.getCharHeight(), AsciiPanel.getCharWidth(), AsciiPanel.getCharHeight());
		}
	    }
	}
    }

    public static Highlight createBlinker(final int x, final int y, final int blinkTime) {
	final Highlight h = new Highlight();
	h.x = x;
	h.y = y;
	h.isBlinking = true;
	h.blinkTime = blinkTime;
	return h;
    }

    public static Highlight createHighlight(final int x, final int y) {
	final Highlight h = new Highlight();
	h.x = x;
	h.y = y;
	h.isBlinking = false;
	h.isOn = true;
	return h;
    }

    public void highlightTile(final Highlight h) {
	highlighters.add(h);
    }

    public void unhighlightTile(final int x, final int y) {
	tiles[x][y] = Optional.empty();
	for (int i = highlighters.size(); i >= 0; i--) {
	    final Highlight h = highlighters.get(i);
	    if (h.x == x && h.y == y) {
		highlighters.remove(i);
		return;
	    }
	}
    }

    private static class Highlight {
	private Highlight() {
	}

	public Color c = DEFAULT_COLOR;
	public int x, y;
	public boolean isBlinking;
	public int blinkTime;
	public int currentTime;
	public boolean isOn;
    }
}
