package graphics;

import graphics.asciiPanel.AsciiPanel;
import graphics.console.Console;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JLayeredPane;
import javax.swing.JPanel;

public class GameDisplay extends JLayeredPane {
	private static final long serialVersionUID = 1L;
	private final AsciiPanel asciiPanel;
	private final DisplayHighlighter displayHighlighter;
	private final Console console;

	public GameDisplay(final int widthInChars, final int heightInChars) {
		asciiPanel = new AsciiPanel(widthInChars, heightInChars);
		displayHighlighter = new DisplayHighlighter(widthInChars, heightInChars);
		displayHighlighter.setPreferredSize(asciiPanel.getPreferredSize());
		displayHighlighter.setBounds(0, 0,
				displayHighlighter.getPreferredSize().width,
				displayHighlighter.getPreferredSize().height);
		console = new Console(widthInChars / 4, heightInChars);

		console.write("Hello World\n");
		console.write("Hello World\n");
		console.write("Hello World\n");
		for (int i = 0; i < 45; i++) {
			console.write(i + "\n");
		}

		final JPanel backgroundPanel = new JPanel();
		backgroundPanel.setLayout(new BorderLayout());
		backgroundPanel.add(asciiPanel, BorderLayout.WEST);
		backgroundPanel.add(console.scrollPane, BorderLayout.EAST);
		backgroundPanel.setBounds(0, 0,
				backgroundPanel.getPreferredSize().width,
				backgroundPanel.getPreferredSize().height);

		this.add(backgroundPanel, new Integer(0));
		this.add(displayHighlighter, new Integer(1));

		setPreferredSize(new Dimension(
				backgroundPanel.getPreferredSize().width,
				backgroundPanel.getPreferredSize().height));
	}

	public AsciiPanel getAsciiPanel() {
		return asciiPanel;
	}

	public DisplayHighlighter getDisplayHighlighter() {
		return displayHighlighter;
	}

	public Console getConsole() {
		return console;
	}
}
