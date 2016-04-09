package soundTest;

public class StdDevBeatAnalyzer implements BeatAnalyze {

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
	}

	@Override
	public void analyze(byte[] data) {
		calcVolume(data);
		double nv = vq.get();
		if (nv >= pv)
			chainLen++;
		else {
			double diff = pv - chainStart;
			if (chainLen >= chain && diff > dif) {
				double newV = nv;
				if (newV == volume)
					volume = newV + 1;
				else
					volume = newV;
			}
			chainStart = nv;
			chainLen = 0;
		}
		pv = nv;
	}

	private void calcVolume(byte[] d) {
		long t = 0;
		for (byte b : d)
			t += b;
		t /= d.length;
		double r = 0;
		for (byte b : d)
			r += (b - t) * (b - t);
		r /= d.length;
		r = Math.sqrt(r);
		vq.add(r);
	}

	@Override
	public double getVolume() {
		return (volume - 40) * 30;
	}
}
