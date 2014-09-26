package graphics.console;

import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;

public class Console {

	public JTextArea textArea;
	public JScrollPane scrollPane;

	public Console(final int rows, final int columns) {
		textArea = new JTextArea(rows, columns);
		scrollPane = new JScrollPane(textArea);
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		textArea.setEditable(false);
		scrollPane
		.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
	}

	public void write(final String s) {
		textArea.append(s);
		textArea.setCaretPosition(textArea.getDocument().getLength());
	}
}
