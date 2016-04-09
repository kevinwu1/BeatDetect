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
