package soundTest;

import java.awt.Color;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Vis extends JFrame {
	static final int WIDTH = 300;
	static final int HEIGHT = 1080;

	JPanel c;

	public Vis(JPanel c, int x) {
		super("Test");
		setLocation(x, 0);
		setSize(WIDTH, HEIGHT);
		setUndecorated(true);
		setOpacity(0.5f);
		setBackground(new Color(0, 0, 0, 0));
		// getContentPane().setBackground(new Color(1.0f, 1.0f, 1.0f, 0.0f));
		c.setOpaque(false);
		add(this.c = c);
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public JPanel canvas() {
		return c;
	}
}
