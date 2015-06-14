package main;

import graphics.GameDisplay;

import javax.swing.JFrame;

public class Main {

    public static void main(final String[] args) {
	final JFrame frame = new JFrame();
	final GameDisplay display = new GameDisplay(78, 44);
	frame.add(display);
	frame.pack();
	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	frame.setVisible(true);

	new Game(display).run();
    }

}
