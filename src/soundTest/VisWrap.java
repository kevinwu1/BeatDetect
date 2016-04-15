package soundTest;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

public class VisWrap implements Runnable {
	Vis v;

	private final double VOLUME_FADE_SPEED = 8;
	private final double COLOR_FADE_SPEED = 1;
	private final double SPI_START = 30;

	private double pVol = 0;
	private double pUniqueVol = 0;
	private double dispVol;
	private double dispCol;
	int what = 0;

	VisWrap(BeatAnalyze ba, int x) {
		v = new Vis(new JPanel() {
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
				graphToBack.clearRect(0, 0, getWidth(), getHeight());
				double vol = ba.getVolume();
				// System.out.println(vol);
				if (vol != pVol) {
					dispCol = SPI_START;
					// System.out.println(what++);
				}
				if (vol != pUniqueVol) {
					if (vol > dispVol) {
						dispVol = vol;
					}
					pUniqueVol = vol;
				}
				dispCol -= COLOR_FADE_SPEED;
				dispVol -= VOLUME_FADE_SPEED;
				pVol = vol;
				graphToBack.setColor(dispCol > 0 ? Color.GREEN : Color.WHITE);
				graphToBack.fillRect(30, 30, 200, Math.max(0, (int) dispVol) + 10);
				twoDGraph.drawImage(back, null, 0, 0);
			}
		}, x);
		// v.setUndecorated(true);
	}

	@Override
	public void run() {
		try {
			while (true) {
				v.canvas().repaint();
				Thread.sleep(5);
			}
		}
		catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
