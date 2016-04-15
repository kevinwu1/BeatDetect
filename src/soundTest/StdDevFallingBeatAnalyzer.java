package soundTest;

public class StdDevFallingBeatAnalyzer implements BeatAnalyze {

	private final VolumeQueue vq;
	private double volume;
	private double chainStart = 0;
	private int chainLen = 0;
	private final int chain;
	private final double dif;
	private double pv;
	private int lastPeak;
	private double lastPeakV;
	private int time = 0;

	StdDevFallingBeatAnalyzer() {
		this(2, 40, 1);
	}

	StdDevFallingBeatAnalyzer(int chain, double dif, int avgN) {
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
	}

	@Override
	public void analyze(byte[] data) {
		calcVolume(data);
		double nv = vq.get();
		// System.out.println(nv);
		if (nv >= pv)
			chainLen++;
		else {
			double diff = pv - chainStart;
			if (chainLen >= chain && diff > dif) {
				lastPeak = time;
				lastPeakV = nv;

			}
			chainStart = nv;
			chainLen = 0;
		}
		if (time == lastPeak + 1) {
			if (lastPeakV == volume)
				volume = lastPeakV + 1;
			else
				volume = lastPeakV;
		}
		pv = nv;
		time++;
	}

	private void calcVolume(byte[] d) {
		long t = 0;
		int num0 = 0;
		for (int i = 0; i < d.length; i++) {
			t += d[i];
			if (d[i] == 0)
				num0++;
		}
		if (num0 > 1000) {
			vq.add(0);
			return;
		}
		t /= d.length;
		t /= 2;
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
		return (volume - 40) * 30;
	}
}
