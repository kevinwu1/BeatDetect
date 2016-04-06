package soundTest;

public class BeatAnalyzer {

	private final VolumeQueue vq = new VolumeQueue(0, 110) {
		@Override
		int get() {
			int avN = 10;
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
	private double volume;
	private int chainStart = 0;
	private int chainLen = 0;
	private final int chain, dif;
	private int pv;

	BeatAnalyzer() {
		this(3, 5);
	}

	BeatAnalyzer(int chain, int dif) {
		this.chain = chain;
		this.dif = dif;
	}

	public void analyze(byte[] data) {
		calcVolume(data);
		int nv = vq.get();
		if (nv >= pv)
			chainLen++;
		else {
			int diff = nv - chainStart;
			if (chainLen > chain && diff > dif)
				volume = nv;
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

	public double getVolume() {
		return volume * 6;
	}
}
