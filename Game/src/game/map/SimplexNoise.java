package game.map;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;

public class SimplexNoise {

	SimplexNoise_octave[] octaves;
	double[] frequencys;
	double[] amplitudes;

	int largestFeature;
	double persistence;
	long seed;

	public SimplexNoise(final int largestFeature, final double persistence) {
		this(largestFeature, persistence, System.currentTimeMillis());
	}

	public SimplexNoise(final int largestFeature, final double persistence,
			final long seed) {
		this.largestFeature = largestFeature;
		this.persistence = persistence;
		this.seed = seed;

		// recieves a number (eg 128) and calculates what power of 2 it is (eg
		// 2^7)
		final int numberOfOctaves = (int) Math.ceil(Math.log10(largestFeature)
				/ Math.log10(2));

		octaves = new SimplexNoise_octave[numberOfOctaves];
		frequencys = new double[numberOfOctaves];
		amplitudes = new double[numberOfOctaves];

		final Random rnd = new Random(seed);

		for (int i = 0; i < numberOfOctaves; i++) {
			octaves[i] = new SimplexNoise_octave(rnd.nextInt());

			frequencys[i] = Math.pow(2, i);
			amplitudes[i] = Math.pow(persistence, octaves.length - i);
		}

	}

	public double getNoise(final int x, final int y) {

		double result = 0;

		for (int i = 0; i < octaves.length; i++) {
			// double frequency = Math.pow(2,i);
			// double amplitude = Math.pow(persistence,octaves.length-i);

			result = result
					+ octaves[i].noise(x / frequencys[i], y / frequencys[i])
					* amplitudes[i];
		}

		return result;

	}

	public double getNoise(final int x, final int y, final int z) {

		double result = 0;

		for (int i = 0; i < octaves.length; i++) {
			final double frequency = Math.pow(2, i);
			final double amplitude = Math.pow(persistence, octaves.length - i);

			result = result
					+ octaves[i].noise(x / frequency, y / frequency, z
							/ frequency) * amplitude;
		}

		return result;

	}

	public void writeImage(final int width, final int height,
			final String filename) {
		final double[][] data = new double[width][height];

		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				data[i][j] = 0.5 * (1 + getNoise(i, j));
			}
		}

		// this takes and array of doubles between 0 and 1 and generates a grey
		// scale image from them

		final BufferedImage image = new BufferedImage(data.length,
				data[0].length, BufferedImage.TYPE_INT_RGB);

		for (int y = 0; y < data[0].length; y++) {
			for (int x = 0; x < data.length; x++) {
				if (data[x][y] > 1) {
					data[x][y] = 1;
				}
				if (data[x][y] < 0) {
					data[x][y] = 0;
				}
				final Color col = new Color((float) data[x][y],
						(float) data[x][y], (float) data[x][y]);
				image.setRGB(x, y, col.getRGB());
			}
		}

		try {
			// retrieve image
			final File outputfile = new File(filename + ".png");
			outputfile.createNewFile();

			ImageIO.write(image, "png", outputfile);
		} catch (final IOException e) {
			// o no! Blank catches are bad
			throw new RuntimeException("I didn't handle this very well");
		}
	}
}