package soundTest;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Visualizer extends JFrame implements Runnable {
	private final int W = 3;
	private final int O = W / 2;
	private final int XO = W + 2;
	private JPanel p;
	private int xp, t = -1, yp;
	private PQueue pq = new PQueue();
	private boolean start = false;

	// public static void main(String[] args) throws InterruptedException {
	// Visualizer v = new Visualizer(0, 0, 500, 500);
	// for (int i = 0; i < 200; i++) {
	//
	// v.add((int) (Math.random() * 200));
	// v.repaint();
	// Thread.sleep(100);
	// }
	// }

	Visualizer(int x, int y, int w, int h) {
		setLocation(x, y);
		setSize(w, h);
		setUndecorated(true);
		// setOpacity(0.5f);
		setBackground(new Color(0, 0, 0, 0));
		p = new JPanel() {
			BufferedImage back;

			@Override
			public void update(Graphics g) {
				paint(g);
			}

			@Override
			public void paint(Graphics g) {
				Graphics2D twoDGraph = (Graphics2D) g;
				if (back == null)
					back = (BufferedImage) createImage(getWidth(), getHeight());
				Graphics gtb = back.createGraphics();
				((Graphics2D) gtb).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				Tup xT;
				do {
					xT = pq.x(t);
					if (xT != null) {
						t = xT.t;
						int y = xT.v;
						// if (!start && y == 0)
						// break;
						xp += XO;
						if (xp > getWidth()) {
							gtb.clearRect(0, 0, getWidth(), getHeight());
							xp -= getWidth();
						}
						if (xT.b) {
							gtb.setColor(Color.GREEN);
						}
						else
							gtb.setColor(Color.RED);
						gtb.fillRect(xp, y, W, W);
						gtb.drawLine(xp - XO + O, yp + O, xp + O, y + O);
						yp = y;
					}
				} while (xT != null);
				twoDGraph.drawImage(back, null, 0, 0);
			}
		};
		p.setOpaque(false);
		add(p);
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	void add(int x, boolean b) {
		pq.add(x, b);
	}

	@Override
	public void run() {
		try {
			while (true) {
				p.repaint();
				Thread.sleep(1);
			}
		}
		catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	static class PQueue {
		int N = 500;
		int[] q = new int[N];
		boolean[] beat = new boolean[N];
		boolean[] get = new boolean[N];
		int a = -1;

		int n(int i) {
			return (i + 1) % N;
		}

		int p(int i) {
			return (i - 1 + N) % N;
		}

		Tup x(int t) {
			if (!get[n(t)])
				return null;
			t = n(t);
			get[t] = false;
			return new Tup(q[t], t, beat[t]);
		}

		void add(int x, boolean b) {
			q[a = n(a)] = x;
			beat[a] = b;
			get[a] = true;
		}

	}

	static class Tup {
		int v, t;
		boolean b;

		Tup(int v, int t, boolean b) {
			this.v = v;
			this.t = t;
			this.b = b;
		}
	}

}
