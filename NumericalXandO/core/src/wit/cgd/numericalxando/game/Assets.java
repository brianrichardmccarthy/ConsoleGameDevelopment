/**
 * 
 * @file        Assets.java
 * @author      Brian McCarthy, 20063914
 * @assignment  Numerical X and O
 * @brief       Handles the game assets
 * @notes       No known BUGS or ISSUES.
 *
 */
package wit.cgd.numericalxando.game;

import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetErrorListener;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.utils.Disposable;

import wit.cgd.numericalxando.game.util.Constants;

public class Assets implements Disposable, AssetErrorListener {

    private static final String TAG = WorldRenderer.class.getName();

    public static final Assets instance = new Assets();
    private AssetManager assetManager;

    public AssetFonts fonts;

    public Asset board;

    public HashMap<Integer, Asset> numbers;

    public AssetSounds sounds;
    public AssetMusic music;

    public class Asset {

        public final AtlasRegion region;

        public Asset(TextureAtlas atlas, String imageName) {
            region = atlas.findRegion(imageName);
        }
    }

    public class AssetMusic {

        public final Music song01;

        public AssetMusic(AssetManager am) {
            song01 = am.get("music/keith303_-_brand_new_highscore.mp3", Music.class);
        }
    }

    public class AssetSounds {

        public final Sound first;
        public final Sound second;
        public final Sound win;
        public final Sound draw;

        public AssetSounds(AssetManager am) {
            first = am.get("sounds/first.wav", Sound.class);
            second = am.get("sounds/second.wav", Sound.class);
            win = am.get("sounds/win.wav", Sound.class);
            draw = am.get("sounds/draw.wav", Sound.class);
        }
    }

    public class AssetFonts {

        public final BitmapFont defaultSmall;
        public final BitmapFont defaultNormal;
        public final BitmapFont defaultBig;

        public AssetFonts() {
            // create three fonts using Libgdx's built-in 15px bitmap font
            defaultSmall = new BitmapFont(Gdx.files.internal("images/arial-15.fnt"), true);
            defaultNormal = new BitmapFont(Gdx.files.internal("images/arial-15.fnt"), true);
            defaultBig = new BitmapFont(Gdx.files.internal("images/arial-15.fnt"), true);

            // set font sizes
            defaultSmall.getData().setScale(0.75f);
            defaultNormal.getData().setScale(1.0f);
            defaultBig.getData().setScale(4.0f);

            // enable linear texture filtering for smooth fonts
            defaultSmall.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
            defaultNormal.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
            defaultBig.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
        }
    }

    private Assets() {
    }

    public void init(AssetManager assetManager) {

        if (numbers != null) numbers.clear();

        numbers = new HashMap<Integer, Asset>();

        this.assetManager = assetManager;

        // set asset manager error handler
        this.assetManager.setErrorListener(this);

        // load texture atlas
        this.assetManager.load(Constants.TEXTURE_ATLAS_OBJECTS, TextureAtlas.class);
        // start loading assets and wait until finished
        this.assetManager.finishLoading();

        Gdx.app.debug(TAG, "# of assets loaded: " + this.assetManager.getAssetNames().size);
        for (String a: this.assetManager.getAssetNames())
            Gdx.app.debug(TAG, "asset: " + a);

        TextureAtlas atlas = this.assetManager.get(Constants.TEXTURE_ATLAS_OBJECTS);

        // enable texture filtering for pixel smoothing
        for (Texture t: atlas.getTextures())
            t.setFilter(TextureFilter.Linear, TextureFilter.Linear);

        fonts = new AssetFonts();

        // build game resource objects
        board = new Asset(atlas, "board");

        // number images
        numbers.put(1, new Asset(atlas, "1"));
        numbers.put(2, new Asset(atlas, "2"));
        numbers.put(3, new Asset(atlas, "3"));
        numbers.put(4, new Asset(atlas, "4"));
        numbers.put(5, new Asset(atlas, "5"));
        numbers.put(6, new Asset(atlas, "6"));
        numbers.put(7, new Asset(atlas, "7"));
        numbers.put(8, new Asset(atlas, "8"));
        numbers.put(9, new Asset(atlas, "9"));

        // load sounds
        this.assetManager.load("sounds/first.wav", Sound.class);
        this.assetManager.load("sounds/second.wav", Sound.class);
        this.assetManager.load("sounds/win.wav", Sound.class);
        this.assetManager.load("sounds/draw.wav", Sound.class);
        this.assetManager.finishLoading();

        // load music
        this.assetManager.load("music/keith303_-_brand_new_highscore.mp3", Music.class);
        this.assetManager.finishLoading();

        sounds = new AssetSounds(this.assetManager);
        music = new AssetMusic(this.assetManager);

    }

    @Override
    public void dispose() {

        assetManager.dispose();
    }

    @SuppressWarnings("rawtypes")
    @Override
    public void error(AssetDescriptor asset, Throwable throwable) {

        Gdx.app.error(TAG, "Couldn't load asset '" + asset + "'", (Exception) throwable);
    }
}
