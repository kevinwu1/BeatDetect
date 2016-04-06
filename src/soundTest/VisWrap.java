package soundTest;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

public class VisWrap implements Runnable {
	Canvas can;

	private final int VOLUME_FADE_SPEED = 4;
	private final int COLOR_FADE_SPEED = 1;

	private double pVol = 0;
	private double volSub = 0;
	private double pSpi = 0;
	private double spiSub = 0;

	VisWrap(AudioBeatPlayer hi) {
		Vis v = new Vis();
		v.add(can = new Canvas() {
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
				double vol = hi.getVolume();
				int spi = 0;
				if (vol == pVol) {
					volSub += VOLUME_FADE_SPEED;
					spi = 30;
				}
				else {
					volSub = 0;
					pVol = vol;
				}
				if (spi == pSpi)
					spiSub += COLOR_FADE_SPEED;
				else {
					spiSub = 0;
					pSpi = spi;
				}
				graphToBack.setColor(spi - spiSub > 0 ? Color.GREEN : Color.BLACK);
				graphToBack.fillRect(30, 30, 200, Math.max(0, (int) (vol - volSub)) + 10);
				twoDGraph.drawImage(back, null, 0, 0);
			}
		});
	}

	@Override
	public void run() {
		try {
			while (true) {
				can.repaint();
				Thread.sleep(5);
			}
		}
		catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
