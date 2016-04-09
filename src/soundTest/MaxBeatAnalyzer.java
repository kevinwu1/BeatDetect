package soundTest;

public class MaxBeatAnalyzer implements BeatAnalyze {

	private final VolumeQueue vq;
	private double volume;
	private double chainStart = 0;
	private int chainLen = 0;
	private final int chain, dif;
	private double pv;

	MaxBeatAnalyzer() {
		this(3, 5, 10);
	}

	MaxBeatAnalyzer(int chain, int dif, int avgN) {
		this.chain = chain;
		this.dif = dif;
		vq = new VolumeQueue(0, 110) {
			@Override
			double get() {
				int avN = avgN;
				int ret = 0;
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
		int m = 0;
		for (int i = 1; i < d.length - 1; i++) {
			byte b = d[i];
			if (b * b > 25)
				if (Math.abs(b - d[i - 1]) < 32 && Math.abs(b - d[i + 1]) < 32)
					m = Math.max(m, b);

		}
		vq.add(m);
	}

	@Override
	public double getVolume() {
		return volume * 6;
	}
}
