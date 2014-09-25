package graphics.asciiPanel;

import java.awt.Color;

public class AsciiCharacterData {
	public AsciiCharacterData(char character2, Color foreground,
			Color background) {
		character = character2;
		foregroundColor = foreground;
		backgroundColor = background;
	}
	public AsciiCharacterData() {
		// TODO Auto-generated constructor stub
	}
	public char character;
	public Color foregroundColor;
	public Color backgroundColor;
}