package ie.wit.cgd.bunnyhop.game;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;
import java.util.logging.FileHandler;

import javax.imageio.ImageIO;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.PixmapIO;
import com.badlogic.gdx.math.MathUtils;

public class ProceduralGeneratedLevel {

	private static final String TAG = ProceduralGeneratedLevel.class.getName();
	Pixmap image;
	public static ProceduralGeneratedLevel proceduralGeneratedLevel;
	private Random rand;

	public ProceduralGeneratedLevel() {
		rand = new Random();
	}

	public void init() {

		proceduralGeneratedLevel = new ProceduralGeneratedLevel();
	}

	public void generateLevel() {

		image = new Pixmap(128, 32, Format.RGBA8888);

		/*
		 * for (int setupY = 0; setupY < 32; setupY++) { for (int setupX = 0;
		 * setupX < 128; setupX++) { image.setRGB(setupX, setupY, );
		 * 
		 * } }
		 */
		image.setColor(generateColor(0, 0, 0));
		PixmapIO.writePNG(
				new FileHandle(
						"C:\\Projects\\Java\\ConsoleGameDevelopment1\\BunnyHop\\android\\assets\\levels\\NewLevel2.png"),
				image);

		for (int pixelY = 0; pixelY < image.getHeight(); pixelY++) {
			for (int pixelX = 0; pixelX < image.getWidth(); pixelX++) {
				if (image.getPixel(pixelX, pixelY) == 0) {
					Gdx.app.debug(TAG, "Image Pixel <" + image.getPixel(pixelX, pixelY) + ">  Generated Color <" + generateColor(0, 0, 0) + ">");
				}
			}
		}

		/*
		 * int x = 0, y = (int) (image.getHeight() * 0.75); int lengthOfGap = 0;
		 * boolean bunnyIsSpawned = false, goalIsSpawned = false; int
		 * xOfLastRock = 0, yOfLastRock = 0; for (x = 0; x < image.getWidth();
		 * x++) {
		 * 
		 * int next = rand.nextInt();
		 * 
		 * if (next % 2 == 1 || lengthOfGap == 1) {
		 * 
		 * int[] rockLength = new int[(x + 10 < image.getWidth()) ?
		 * (rand.nextInt(10) + 1) : (rand.nextInt(image.getWidth() - x) + 1)];
		 * 
		 * for (int rLength = 0; rLength < rockLength.length; rLength++) {
		 * rockLength[rLength] = generateColor(0, 255, 0); }
		 * 
		 * image.setRGB(x, y, rockLength.length, (y + 1 >= image.getHeight()) ?
		 * 0 : 1, rockLength, 0, 0);
		 * 
		 * xOfLastRock = x + rockLength.length; yOfLastRock = y;
		 * 
		 * float spawn = rand.nextFloat();
		 * 
		 * if (!bunnyIsSpawned) {
		 * 
		 * image.setRGB(x, y - 1, generateColor(255, 255, 255)); bunnyIsSpawned
		 * = true; continue; }
		 * 
		 * if (spawn >= 0.25f) { for (int rLength = 0; rLength <
		 * rockLength.length; rLength++) { if (spawn >= 0.40f)
		 * rockLength[rLength] = generateColor(255, 255, 0); else
		 * rockLength[rLength] = generateColor(0, 0, 0); spawn =
		 * rand.nextFloat(); } image.setRGB(x, y - 1, (rockLength.length + x >=
		 * image.getWidth()) ? (image.getWidth() - (x + rockLength.length)) :
		 * rockLength.length, 1, rockLength, 0, 0);
		 * 
		 * for (int rLength = 0; rLength < rockLength.length; rLength++) { if
		 * (spawn >= 0.60f) rockLength[rLength] = generateColor(255, 255, 0);
		 * else rockLength[rLength] = generateColor(0, 0, 0); spawn =
		 * rand.nextFloat(); } image.setRGB(x, y - 2, (rockLength.length + x >=
		 * image.getWidth()) ? (image.getWidth() - (x + rockLength.length)) :
		 * rockLength.length, 1, rockLength, 0, 0);
		 * 
		 * } if (spawn >= 0.75f) { if (rand.nextBoolean()) image.setRGB(x, y -
		 * 1, generateColor(128, 128, 128)); else image.setRGB(x, y - 1,
		 * generateColor(255, 0, 255)); }
		 * 
		 * x += rockLength.length; lengthOfGap = 0; y = MathUtils.clamp(y +
		 * MathUtils.clamp((rand.nextInt(4) - 2), -1, 1), (int)
		 * (image.getHeight() * 0.70), image.getHeight()); } else {
		 * lengthOfGap++; }
		 * 
		 * }
		 * 
		 * try { image.setRGB(xOfLastRock - 1, yOfLastRock - 1,
		 * generateColor(22, 22, 229)); } catch (ArrayIndexOutOfBoundsException
		 * e) { Gdx.app.error(TAG, "Error xOfLastRock <" + xOfLastRock + ">"); }
		 * 
		 */
	}

	private int generateColor(int r, int g, int b) {

		return r << 24 | g << 16 | b << 8 | 0xff;

	}

}
