package graphics.console;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;

import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;

public class Console {
	
	private static final String DEFAULT_FONT = "Lucida Console";
	private static final Color DEFAULT_BACKGROUND = Color.BLACK;
	private static final Color DEFAULT_FOREGROUND = Color.WHITE;
	public JTextPane textPane;
	public JScrollPane scrollPane;
	
	public Console(final Dimension prefferedSize) {
		textPane = new JTextPane();
		scrollPane = new JScrollPane(textPane);
		textPane.setMargin(new Insets(5, 5, 5, 5));
		
		textPane.setPreferredSize(prefferedSize);
		
		textPane.setBackground(DEFAULT_BACKGROUND);
		textPane.setForeground(DEFAULT_FOREGROUND);
		textPane.setEditable(false);
		scrollPane.setBorder(new EmptyBorder(0, 0, 0, 0));
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
		textPane.setFont(new Font("Courier New", Font.PLAIN, 12));
		
		new SmartScroller(scrollPane, SmartScroller.VERTICAL, SmartScroller.END);
	}
	
	public void write(final String msg, final Color foreground, final Color background) {
		final StyleContext sc = StyleContext.getDefaultStyleContext();
		AttributeSet aset = sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, foreground);
		aset = sc.addAttribute(aset, StyleConstants.Background, background);
		aset = sc.addAttribute(aset, StyleConstants.FontFamily, DEFAULT_FONT);
		aset = sc.addAttribute(aset, StyleConstants.Alignment, StyleConstants.ALIGN_JUSTIFIED);
		
		final int len = textPane.getDocument().getLength();
		textPane.setCaretPosition(len);
		textPane.setCharacterAttributes(aset, false);
		try {
			textPane.getDocument().insertString(len, msg, aset);
		} catch (final BadLocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void write(final String msg) {
		write(msg, DEFAULT_FOREGROUND, DEFAULT_BACKGROUND);
	}
	
	public void write(final String msg, final Color foreground) {
		write(msg, foreground, DEFAULT_BACKGROUND);
	}
}
