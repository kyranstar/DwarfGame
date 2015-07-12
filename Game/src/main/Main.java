package main;

import graphics.GameDisplay;

import java.awt.HeadlessException;
import java.lang.reflect.InvocationTargetException;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class Main {

    public static void main(final String[] args) throws HeadlessException, InvocationTargetException, InterruptedException {

	final JFrame frame = new JFrame();
	final GameDisplay display = new GameDisplay(78, 44);

	// Swing stuff should be done on the swing thread
	SwingUtilities.invokeAndWait(() -> {
	    frame.add(display);
	    frame.pack();
	    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    frame.setVisible(true);
	});
	new Game(display).run();
    }

}
