package test;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.Arrays;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class GraphicsTest extends JFrame implements Runnable {

	public static void main(String[] args) throws InterruptedException {
		GraphicsTest t = new GraphicsTest();
		t.add(30);
		t.p.repaint();
		// Thread.sleep(1000);
		t.add(60);
		t.add(30);
		t.add(60);
		t.add(30);
		t.add(60);
		t.add(30);
		t.add(60);
		t.add(30);
		t.add(60);
		t.add(30);
		System.out.println("p2");
		Thread.sleep(1000);
		t.repaint();
		// t.repaint();
	}

	JPanel p;

	PQueue pq = new PQueue();

	void add(int x) {
		pq.add(x);
	}
	int xp, t = -1, yp;

	GraphicsTest() {
		setLocation(0, 0);
		setSize(1080, 500);
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
				Graphics graphToBack = back.createGraphics();
				((Graphics2D) graphToBack).setRenderingHint(RenderingHints.KEY_ANTIALIASING,
						RenderingHints.VALUE_ANTIALIAS_ON);
				// graphToBack.clearRect(0, 0, getWidth(), getHeight());
				Tup xT;
				do {
					xT = pq.x(t);
					if (xT != null) {
						t = xT.t;
						int y = xT.v;
						xp += 5;
						System.out.println("get: " + xp + ", " + y);
						graphToBack.setColor(Color.RED);
						graphToBack.fillRect(xp, y, 3, 3);
						graphToBack.drawLine(xp - 5 + 1, yp + 1, xp + 1, y + 1);
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

	@Override
	public void run() {
		p.repaint();
	}

	static class PQueue {
		int N = 50;
		int[] q = new int[N];
		boolean[] get = new boolean[N];
		int a = -1;

		int n(int i) {
			return (i + 1) % N;
		}

		int p(int i) {
			return (i - 1 + N) % N;
		}

		Tup x(int t) {
			System.out.println("gq: " + Arrays.toString(q) + "| " + t);
			if (!get[n(t)])
				return null;
			t = n(t);
			get[t] = false;
			return new Tup(q[t], t);
		}

		void add(int x) {
			System.out.println("aq: " + Arrays.toString(q) + " |: " + a);
			q[a = n(a)] = x;
			get[a] = true;
		}
	}

	static class Tup {
		int v, t;

		Tup(int v, int t) {
			this.v = v;
			this.t = t;
		}

	}

}
