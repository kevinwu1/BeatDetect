package soundTest;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class Play {
	public static void main(String[] args) {
		ExecutorService executor = Executors.newFixedThreadPool(3);
		AudioBeatPlayer hi;
		try {
			BeatAnalyze s = new StdDevBeatAnalyzer(5, 0.7, 5);
			BeatAnalyze m = new MaxBeatAnalyzer();

			hi = new AudioBeatPlayer(new File("mus/v.wav"), new BeatAnalyze[] { s, m });
			executor.execute(new VisWrap(s, 0));
			executor.execute(new VisWrap(m, Vis.WIDTH));
			executor.execute(hi);
		}
		catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
			e.printStackTrace();
		}
	}
}
