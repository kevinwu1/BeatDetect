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
	AudioBeatPlayer(File f, BeatAnalyze ba) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
		this(f, new BeatAnalyze[] { ba });
	}

	AudioBeatPlayer(File f, BeatAnalyze[] ba) throws UnsupportedAudioFileException, IOException,
			LineUnavailableException {
		if (f.getName().lastIndexOf("mp3") == f.getName().length() - 3) {
			AudioInputStream in = AudioSystem.getAudioInputStream(f);
			baseFormat = in.getFormat();
			AudioFormat decf = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, baseFormat.getSampleRate(), 16,
					baseFormat.getChannels(), baseFormat.getChannels() * 2, baseFormat.getSampleRate(), false);
			System.out.println(decf);

			AudioInputStream deci = AudioSystem.getAudioInputStream(decf, in);
			ais = deci;
			int t = (int) (decf.getSampleRate() / BEATS_PER_SEC);
			BUFFER_SIZE = 4096;
			DataLine.Info info = new DataLine.Info(SourceDataLine.class, decf);
			sourceLine = (SourceDataLine) AudioSystem.getLine(info);
		}
		else {
			ais = AudioSystem.getAudioInputStream(f);
			baseFormat = ais.getFormat();
			BUFFER_SIZE = (int) (baseFormat.getSampleRate() / BEATS_PER_SEC);
			DataLine.Info info = new DataLine.Info(SourceDataLine.class, baseFormat);
			sourceLine = (SourceDataLine) AudioSystem.getLine(info);
		}

		this.ba = ba;

	}

	AudioBeatPlayer(AudioInputStream ais, BeatAnalyze[] ba) throws LineUnavailableException {
		this.ais = ais;
		baseFormat = ais.getFormat();
		// System.out.println("Sample Rate: " + format.getSampleRate());
		BUFFER_SIZE = (int) (baseFormat.getSampleRate() / BEATS_PER_SEC);
		DataLine.Info info = new DataLine.Info(SourceDataLine.class, baseFormat);
		sourceLine = (SourceDataLine) AudioSystem.getLine(info);
		this.ba = ba;
	}

	private final int BUFFER_SIZE;
	private final int BEATS_PER_SEC = 10;
	private final AudioFormat baseFormat;
	private final SourceDataLine sourceLine;
	private final AudioInputStream ais;
	private final BeatAnalyze[] ba;

	private void play() throws IOException, LineUnavailableException, InterruptedException {
		sourceLine.open();
		sourceLine.start();
		AudioInputStream din = ais;
		byte[] b = new byte[4096];
		int nBytesRead = 0;
		byte[] abData = new byte[BUFFER_SIZE];
		@SuppressWarnings("unused")
		int what = 1;
		while (nBytesRead != -1) {
			nBytesRead = ais.read(abData, 0, BUFFER_SIZE);
			if (nBytesRead >= 0)
				sourceLine.write(abData, 0, nBytesRead);
			for (BeatAnalyze B : ba)
				B.analyze(abData);
			Thread.sleep(1);
			what++;
		}
		System.out.println("Finished");
		ais.close();
		sourceLine.close();
	}

	double getVolume(int i) {
		return ba[i].getVolume();
	}

	double getVolume() {
		return ba[0].getVolume();
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
