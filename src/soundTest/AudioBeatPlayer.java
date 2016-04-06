package soundTest;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;

public class AudioBeatPlayer implements Runnable {

	AudioBeatPlayer(String path) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
		this(new File(path));
	}

	AudioBeatPlayer(File f) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
		ais = AudioSystem.getAudioInputStream(f);
		format = ais.getFormat();
		System.out.println("Sample Rate: " + format.getSampleRate());
		BUFFER_SIZE = (int) (format.getSampleRate() / BEATS_PER_SEC);
		DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);
		sourceLine = (SourceDataLine) AudioSystem.getLine(info);
	}

	private final int BUFFER_SIZE;
	private final int BEATS_PER_SEC = 10;
	private final AudioFormat format;
	private final SourceDataLine sourceLine;
	private final AudioInputStream ais;
	private final BeatAnalyzer VA = new BeatAnalyzer();

	private void play() throws IOException, LineUnavailableException, InterruptedException {
		sourceLine.open();
		sourceLine.start();
		int nBytesRead = 0;
		byte[] abData = new byte[BUFFER_SIZE];
		@SuppressWarnings("unused")
		int what = 1;
		while (nBytesRead != -1) {
			nBytesRead = ais.read(abData, 0, BUFFER_SIZE);
			if (nBytesRead >= 0)
				sourceLine.write(abData, 0, nBytesRead);
			VA.analyze(abData);
			Thread.sleep(1);
			what++;
		}
		ais.close();
		sourceLine.close();
	}

	double getVolume() {
		return VA.getVolume();
	}

	@Override
	public void run() {
		try {
			play();
		}
		catch (IOException | LineUnavailableException | InterruptedException e) {
			e.printStackTrace();
		}
	}
}
