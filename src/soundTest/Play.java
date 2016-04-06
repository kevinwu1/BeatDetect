package soundTest;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class Play {
	public static void main(String[] args) {
		ExecutorService executor = Executors.newFixedThreadPool(2);
		AudioBeatPlayer hi;
		try {
			hi = new AudioBeatPlayer("mus/a.wav");
			executor.execute(new VisWrap(hi));
			executor.execute(hi);
		}
		catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
			e.printStackTrace();
		}
	}

}
