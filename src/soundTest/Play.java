package soundTest;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Play {
	public static void main(String[] args) {
		ExecutorService executor = Executors.newFixedThreadPool(3);
		AudioBeatPlayer hi;
		try {
			BeatAnalyze s = new StdDevBeatAnalyzer(5, 0.7, 5);
			BeatAnalyze m = new MaxBeatAnalyzer(5, 3, 10);

			// AudioFormat format = new AudioFormat(8000.0f, 16, 1, true, true);
			// TargetDataLine microphone = AudioSystem.getTargetDataLine(format);
			// DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
			// microphone = (TargetDataLine) AudioSystem.getLine(info);
			// microphone.open(format);
			// microphone.start();
			// hi = new AudioBeatPlayer(new AudioInputStream(microphone), new BeatAnalyze[] { s, m });
			// AudioSystem.System.out.println();

			hi = new AudioBeatPlayer(new File("mus/VVV.mp3"), new BeatAnalyze[] { s, m });
			// executor.execute(new VisWrap(s, 0));
			executor.execute(new VisWrap(m, Vis.WIDTH));
			executor.execute(hi);
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
		// e.printStackTrace();
		// }
	}
}
