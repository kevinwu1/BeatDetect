package soundTest;

import javax.swing.JFrame;

public class Vis extends JFrame {
	static final int WIDTH = 600;
	static final int HEIGHT = 900;

	public Vis() {
		super("Test");
		setSize(WIDTH, HEIGHT);
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}
