package soundTest;

public class StdDevBeatAnalyzer extends VisualizableBeatAnalyze {

	private final VolumeQueue vq;
	private double volume;
	private double chainStart = 0;
	private int chainLen = 0;
	private final int chain;
	private final double dif;
	private double pv;

	StdDevBeatAnalyzer() {
		this(2, 40, 1);
	}

	StdDevBeatAnalyzer(int chain, double dif, int avgN) {
		this.chain = chain;
		this.dif = dif;
		vq = new VolumeQueue(0, 110) {
			@Override
			double get() {
				int avN = avgN;
				double ret = 0;
				int s2 = s;
				for (int i = 0; i < avN; i++) {
					ret += data[s2];
					s2 = p(s2);
				}
				ret /= avN;
				s = n(s);
				return ret;
			}
		};
		v = new Visualizer[0];
	}

	@Override
	public void analyze(byte[] data) {
		calcVolume(data);
		double nv = vq.get();
		boolean beat = false;
		if (nv >= pv)
			chainLen++;
		else {
			double diff = pv - chainStart;
			if (chainLen >= chain && diff > dif) {
				double newV = nv;
				beat = true;
				if (newV == volume)
					volume = newV + 1;
				else
					volume = newV;
			}
			chainStart = nv;
			chainLen = 0;
		}
		for (Visualizer vi : v)
			vi.add((int) scale(nv) / 5, beat);
		pv = nv;
	}

	private void calcVolume(byte[] d) {
		long t = 0;
		int num0 = 0;
		for (int i = 0; i < d.length; i++) {
			t += d[i];
			if (d[i] == 0)
				num0++;
		}
		if (num0 > 1500) {
			vq.add(0);
			return;
		}
		t /= d.length;
		double r = 0;
		for (int i = 0; i < d.length; i++) {
			r += (d[i] - t) * (d[i] - t);
		}
		r /= d.length;
		r = Math.sqrt(r);
		vq.add(r);
	}

	@Override
	public double getVolume() {
		return scale(volume);
	}

	private double scale(double v) {
		return (v - 40) * 30;
	}
}
