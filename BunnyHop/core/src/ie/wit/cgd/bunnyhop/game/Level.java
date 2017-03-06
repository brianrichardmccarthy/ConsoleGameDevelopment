/**
 * @file        Level.java
 * @author      Brian McCarthy, 20063914
 * @assignment  BunnyHop
 * @brief       Creates a level out of a Pixmap
 * @notes       Creates a level from either the level one or randomly generates a level. 
 *              Also keeps track off all rocks, gold coins, feathers, extra lives, goal, bunny head objects and updates them.
 *
 */
package ie.wit.cgd.bunnyhop.game;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;

import ie.wit.cgd.bunnyhop.game.objects.AbstractGameObject;
import ie.wit.cgd.bunnyhop.game.objects.BunnyHead;
import ie.wit.cgd.bunnyhop.game.objects.Clouds;
import ie.wit.cgd.bunnyhop.game.objects.ExtraLives;
import ie.wit.cgd.bunnyhop.game.objects.Feather;
import ie.wit.cgd.bunnyhop.game.objects.Goal;
import ie.wit.cgd.bunnyhop.game.objects.GoldCoin;
import ie.wit.cgd.bunnyhop.game.objects.Mountains;
import ie.wit.cgd.bunnyhop.game.objects.Rock;
import ie.wit.cgd.bunnyhop.game.objects.WaterOverlay;
import ie.wit.cgd.bunnyhop.util.Constants;

public class Level {

    public static final String TAG = Level.class.getName();

    public enum BLOCK_TYPE {

                            // black
                            EMPTY(0, 0, 0),

                            // green
                            ROCK(0, 255, 0),

                            // white
                            PLAYER_SPAWNPOINT(255, 255, 255),

                            // purple
                            ITEM_FEATHER(255, 0, 255),

                            // blue
                            ITEM_GOAL(22, 22, 229),

                            // gray or grey (can't remember which is correct and which is American)
                            EXTRA_LIVES(128, 128, 128),

                            // yellow
                            ITEM_GOLD_COIN(255, 255, 0);

        private int color;

        private BLOCK_TYPE(int r, int g, int b) {
            color = generateColor(r, g, b);
        }

        public boolean sameColor(int color) {

            return this.color == color;
        }

        public static int generateColor(int r, int g, int b) {

            return r << 24 | g << 16 | b << 8 | 0xff;

        }

        public int getColor() {

            return color;
        }
    }

    // objects
    public BunnyHead bunnyHead;
    public Goal goal;
    public Array<Rock> rocks;
    public Array<GoldCoin> goldCoins;
    public Array<Feather> feathers;
    public Array<ExtraLives> extraLives;

    // decoration
    public Clouds clouds;
    public Mountains mountains;
    public WaterOverlay waterOverlay;

    public Level(String filename) {
        if (filename.equals(Constants.LEVEL_01)) init(filename);
        else if (filename.equals(Constants.LEVEL_02)) init(generateLevel());
    }

    private void init(String filename) {

        // load image file that represents the level data
        init(new Pixmap(Gdx.files.internal(filename)));

    }

    private void init(Pixmap pixmap) {

        // player character
        bunnyHead = null;

        // objects
        rocks = new Array<Rock>();
        goldCoins = new Array<GoldCoin>();
        feathers = new Array<Feather>();
        extraLives = new Array<ExtraLives>();

        // scan pixels from top-left to bottom-right
        int lastPixel = -1;
        for (int pixelY = 0; pixelY < pixmap.getHeight(); pixelY++) {
            for (int pixelX = 0; pixelX < pixmap.getWidth(); pixelX++) {
                AbstractGameObject obj = null;
                float offsetHeight = 0;
                // height grows from bottom to top
                float baseHeight = pixmap.getHeight() - pixelY;
                // get color of current pixel as 32-bit RGBA value
                int currentPixel = pixmap.getPixel(pixelX, pixelY);
                // find matching color value to identify block type at (x,y)
                // point and create the corresponding game object if there is
                // a match
                if (BLOCK_TYPE.EMPTY.sameColor(currentPixel)) {
                    // empty space, do nothing
                } else if (BLOCK_TYPE.ROCK.sameColor(currentPixel)) {
                    // rock
                    if (lastPixel != currentPixel) {
                        obj = new Rock();
                        float heightIncreaseFactor = 0.25f;
                        offsetHeight = -2.5f;
                        obj.position.set(pixelX, baseHeight * obj.dimension.y * heightIncreaseFactor + offsetHeight);
                        rocks.add((Rock) obj);
                    } else {
                        rocks.get(rocks.size - 1).increaseLength(1);
                    }

                } else if (BLOCK_TYPE.PLAYER_SPAWNPOINT.sameColor(currentPixel)) {
                    // player spawn point
                    obj = new BunnyHead();
                    offsetHeight = -3.0f;
                    obj.position.set(pixelX, baseHeight * obj.dimension.y + offsetHeight);
                    bunnyHead = (BunnyHead) obj;
                } else if (BLOCK_TYPE.ITEM_FEATHER.sameColor(currentPixel)) {
                    // feather
                    obj = new Feather();
                    offsetHeight = -1.5f;
                    obj.position.set(pixelX, baseHeight * obj.dimension.y + offsetHeight);
                    feathers.add((Feather) obj);
                } else if (BLOCK_TYPE.ITEM_GOLD_COIN.sameColor(currentPixel)) {
                    // gold coin
                    obj = new GoldCoin();
                    offsetHeight = -1.5f;
                    obj.position.set(pixelX, baseHeight * obj.dimension.y + offsetHeight);
                    goldCoins.add((GoldCoin) obj);
                } else if (BLOCK_TYPE.ITEM_GOAL.sameColor(currentPixel)) {
                    // goal
                    obj = new Goal();
                    offsetHeight = -1.5f;
                    obj.position.set(pixelX, baseHeight * obj.dimension.y + offsetHeight);
                    goal = (Goal) obj;
                } else if (BLOCK_TYPE.EXTRA_LIVES.sameColor(currentPixel)) {
                    obj = new ExtraLives();
                    offsetHeight = -1.5f;
                    obj.position.set(pixelX, baseHeight * obj.dimension.y + offsetHeight);
                    extraLives.add((ExtraLives) obj);
                } else {
                    // unknown object/pixel color

                    // red color channel
                    int r = 0xff & (currentPixel >>> 24);

                    // green color channel
                    int g = 0xff & (currentPixel >>> 16);

                    // blue color channel
                    int b = 0xff & (currentPixel >>> 8);

                    // alpha channel
                    int a = 0xff & currentPixel;
                    Gdx.app.error(TAG,
                        "Unknown object at x<" + pixelX + "> y<" + pixelY + ">: r<" + r + "> g<" + g + "> b<" + b + "> a<" + a + ">");

                }
                lastPixel = currentPixel;
            }
        }

        // decoration
        clouds = new Clouds(pixmap.getWidth());
        clouds.position.set(0, 2);
        mountains = new Mountains(pixmap.getWidth());
        mountains.position.set(-1, -1);
        waterOverlay = new WaterOverlay(pixmap.getWidth());
        waterOverlay.position.set(0, -3.75f);

        // free memory
        pixmap.dispose();
    }

    public void render(SpriteBatch batch) {

        // Draw Mountains
        mountains.render(batch);

        // Draw Rocks
        for (Rock rock: rocks)
            rock.render(batch);

        // Draw Gold Coins
        for (GoldCoin goldCoin: goldCoins)
            goldCoin.render(batch);

        // Draw Feathers
        for (Feather feather: feathers)
            feather.render(batch);

        // draw extra lives
        for (ExtraLives extraLives: extraLives)
            extraLives.render(batch);

        // Draw Player Character
        bunnyHead.render(batch);

        // draw goal object
        goal.render(batch);

        // Draw Water Overlay
        waterOverlay.render(batch);

        // Draw Clouds
        clouds.render(batch);
    }

    public void update(float deltaTime) {

        bunnyHead.update(deltaTime);
        goal.update(deltaTime);
        for (Rock rock: rocks)
            rock.update(deltaTime);
        for (GoldCoin goldCoin: goldCoins)
            goldCoin.update(deltaTime);
        for (Feather feather: feathers)
            feather.update(deltaTime);
        for (ExtraLives extraLives: extraLives)
            extraLives.update(deltaTime);
        clouds.update(deltaTime);
    }

    private Pixmap generateLevel() {

        // Pixmap object
        Pixmap image = new Pixmap(128, 32, Format.RGBA8888);

        // used for rocks, gold coins feathers, and extra lives
        Random rand = new Random();

        // set the colour to black
        image.setColor(Color.BLACK);

        // fill the entire image to current colour
        image.fill();

        /*
         * Y: used for next rock height,
         * xOfLastRock: used for spawning goal on last rock
         * yOfLastRock: used for spawning goal on last rock
         * lengthOfGap: used to make sure gaps aren't to wide
         * bunnyIsSpawned: used to make sure player spawn point is set
         * */
        int y = (int) (image.getHeight() * 0.90), xOfLastRock = 0, yOfLastRock = 0, lengthOfGap = 0;
        boolean bunnyIsSpawned = false;

        // loop across the width of image
        for (int pixelX = 0; pixelX < image.getWidth(); pixelX++) {

            // should a rock be spawned?
            if (rand.nextBoolean() || lengthOfGap == 1) {

                // get a random number between 0 < = > 1
                float spawn = rand.nextFloat();

                /*
                 * Rock length will be between 1 < = > 7
                 * or between a number the pixmap width minus pixelX plus one
                 * */
                int rockLength = (pixelX + 7 < image.getWidth()) ? (rand.nextInt(7) + 1) : (rand.nextInt(
                    image.getWidth() - pixelX) + 1);

                // set current colour to the rock colour
                image.setColor(Color.rgba8888(new Color(BLOCK_TYPE.generateColor(0, 255, 0))));

                // draw rectangle from pixelX, y with a width of rockLength and height of one
                image.drawRectangle(pixelX, y, rockLength, 1);

                // track the last pixel of the rock
                xOfLastRock = pixelX + rockLength;
                yOfLastRock = y;

                /*
                 * If the player spawn point has not been set
                 * 		Set the current colour to player spawn colour
                 * 		draw the pixel above the start of the current rock
                 * 		set the player spawned to true
                 * 		continue the loop to stop spawn point being overwritten
                 * */
                if (!bunnyIsSpawned) {
                    image.setColor(Color.rgba8888(new Color(BLOCK_TYPE.generateColor(255, 255, 255))));
                    image.drawPixel(pixelX, y - 1);
                    bunnyIsSpawned = true;
                    continue;
                }

                // set the current colour to gold coin
                image.setColor(Color.rgba8888(new Color(BLOCK_TYPE.generateColor(255, 255, 0))));

                /*
                 * for the length of the rock
                 *      50% chance of spawning one gold coin y - 1
                 *      25%  chance of spawning one gold coin y - 2
                 *      reset spawn
                 * */
                for (int goldSpawn = 0; goldSpawn < rockLength; goldSpawn++) {
                    if (spawn >= 0.50f) image.drawPixel(goldSpawn + pixelX, y - 1);
                    if (spawn >= 0.75f) image.drawPixel(goldSpawn + pixelX, y - 2);
                    spawn = rand.nextFloat();
                }

                /*
                 * 25% chance of spawning either a feather or an extra life
                 *      10% chance of spawning a extra life
                 *      90% chance of spawning a feather
                 * */
                if (spawn >= 0.75f) {
                    if (spawn >= 0.90f) image.setColor(Color.rgba8888(new Color(BLOCK_TYPE.generateColor(128, 128,
                        128))));
                    else image.setColor(Color.rgba8888(new Color(BLOCK_TYPE.generateColor(255, 0, 255))));

                    image.drawPixel(pixelX, y - 1);
                }

                // move loop to the current x + length of rock
                pixelX += rockLength;
                
                // reset gap
                lengthOfGap = 0;
                
                // increase or decrease height by -1 an 1
                y = MathUtils.clamp(y + MathUtils.clamp( (rand.nextInt(4) - 2), -1, 1),
                    (int) (image.getHeight() * 0.90), image.getHeight() - 1);

            } else {
                lengthOfGap++;
            }

        }

        // set the colour to goal colour
        image.setColor(Color.rgba8888(new Color(BLOCK_TYPE.generateColor(22, 22, 229))));

        // draw single pixel
        image.drawPixel(xOfLastRock - 1, yOfLastRock - 1);

        return image;

    }

}
