package ie.wit.cgd.bunnyhop.game;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;

public class ProceduralGeneratedLevel {

    private static final String TAG = ProceduralGeneratedLevel.class.getName();
    BufferedImage image;
    public static ProceduralGeneratedLevel proceduralGeneratedLevel;
    private Random rand;

    public ProceduralGeneratedLevel() {
        rand = new Random();
    }

    public void init() {

        proceduralGeneratedLevel = new ProceduralGeneratedLevel();
    }

    public void generateLevel(String fileName) {

        try {
            image = ImageIO.read(new File("C:/Projects/Java/ConsoleGameDevelopment1/BunnyHop/android/assets/levels/template.png"));
            int x = 0, y = (int) (image.getHeight() * 0.75);
            int lengthOfGap = 0;
            for (x = 0; x < image.getWidth(); x++) {

                /*
                 * // red color channel
                 * int r = 0xff & (image.getRGB(x, y) >>> 16);
                 * // green color channel
                 * int g = 0xff & (image.getRGB(x, y) >>> 8);
                 * // blue color channel
                 * int b = 0xff & image.getRGB(x, y);
                 * if (r != 0 && g != 0 && b != 0) continue;
                 * int z = rand.nextInt(image.getHeight() - image.getHeight() / 2) + image.getHeight();
                 */
                int next = rand.nextInt();

                if (next % 2 == 1 || lengthOfGap == 3) {

                    int[] rockLength = new int[(((rand.nextInt(image.getWidth() - x) + 1) % 10) + 1)];

                    for (int rLength = 0; rLength < rockLength.length; rLength++) {
                        rockLength[rLength] = generateColor(0, 255, 0);
                    }

                    image.setRGB(x, y, (rockLength.length + x >= image.getWidth()) ? (image.getWidth() - (x + rockLength.length)) : rockLength.length, (y + 1 >= image.getHeight()) ? 0 : 1, rockLength, 0, 0);

                    x += rockLength.length;
                    lengthOfGap = 0;
                    if (rand.nextBoolean()) y += 1;
                    else y -= 1;
                } else {
                    lengthOfGap++;
                }

                /*
                 * if (y > image.getHeight() / 2) {
                 * int next = rand.nextInt();
                 * if (y > image.getHeight() / 2 && next % 2 == 1) {
                 * int[] tempColor = new int[rand.nextInt(10 - 2) + 2];
                 * for (int z = 0; z < tempColor.length; z++) {
                 * tempColor[z] = generateColor(0, 255, 0);
                 * }
                 * try {
                 * image.setRGB(x, y, tempColor.length, y + 1, tempColor, 0, 0);
                 * } catch (ArrayIndexOutOfBoundsException e) {
                 * Gdx.app.debug(TAG, "Image width <" + image.getWidth() + "> Image Height <" + image.getHeight() + "> x <" + x + "> y <" + y + "> array length <" + tempColor.length + "> y+1 <" + (y + 1) + ">");
                 * }
                 * }
                 * }
                 */

            }
            write(fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void write(String fileName) {

        try {
            ImageIO.write(image, "png", new File(fileName + ".png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private int generateColor(int r, int g, int b) {

        return r << 16 | g << 8 | b;
    }

}
