package soundTest;

import java.util.Arrays;

public class VolumeQueue {
	int preBuf;
	int N;
	double[] data;
	int s, e;

	VolumeQueue(int preBuf, int N) {
		this.preBuf = preBuf;
		this.N = N;
		if (preBuf > N - 2)
			preBuf = N - 2;
		s = 0;
		data = new double[N];
		e = preBuf + 1;
	}

	void add(double d) {
		data[e] = d;
		e = n(e);
	}

	double get() {
		double ret = data[s];
		s = n(s);
		return ret;
	}

	double getDev(int n) {
		double r = 0;
		double su = 0;
		int s2 = s;
		for (int i = 0; i < n; i++) {
			su += data[s2];
			s2 = p(s2);
		}
		su /= n;
		s2 = s;
		for (int i = 0; i < n; i++) {
			double d = data[s2] - su;
			r += d * d;
			s2 = p(s2);
		}
		return Math.sqrt(r);
	}

	int n(int i) {
		return (i + 1) % N;
	}

	int p(int i) {
		return (i - 1 + N) % N;
	}

	@Override
	public String toString() {
		return e + Arrays.toString(data);
	}
}
