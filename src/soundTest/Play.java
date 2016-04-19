package soundTest;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Play {
	public static void main(String[] args) {
		ExecutorService executor = Executors.newFixedThreadPool(5);
		AudioBeatPlayer hi;
		try {
			StdDevBeatAnalyzer s = new StdDevBeatAnalyzer(5, 0.7, 5);
			MaxBeatAnalyzer m = new MaxBeatAnalyzer(2, 10, 3);
			VisWrap vw = new VisWrap(m, Vis.WIDTH);
			Visualizer v = new Visualizer(vw.getRight(), 0, 1200, 300);
			Visualizer v2 = new Visualizer(vw.getRight(), 302, 1200, 300);
			m.setVis(new Visualizer[] { v });
			s.setVis(new Visualizer[] { v2 });
			// AudioFormat format = new AudioFormat(8000.0f, 16, 1, true, true);
			// TargetDataLine microphone = AudioSystem.getTargetDataLine(format);
			// DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
			// microphone = (TargetDataLine) AudioSystem.getLine(info);
			// microphone.open(format);
			// microphone.start();
			// hi = new AudioBeatPlayer(new AudioInputStream(microphone), new BeatAnalyze[] { s, m });
			// AudioSystem.System.out.println();

			hi = new AudioBeatPlayer(new File("mus/A.wav"), new BeatAnalyze[] { s, m });
			executor.execute(new VisWrap(s, 0));
			executor.execute(vw);
			executor.execute(hi);
			executor.execute(v);
			executor.execute(v2);
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
