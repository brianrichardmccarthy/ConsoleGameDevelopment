/**
 *
 * @file        Assets
 * @author      Brian McCarthy, 20063914
 * @assignment  Warbirds
 * @brief       Loads and stores the various assets.
 * @notes       DESCRIPTION OF CODE, BUGS, FEATURES, ISSUES, ETC.
 *
 */
package wit.cgd.warbirds.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetErrorListener;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;

import wit.cgd.warbirds.game.util.Constants;

public class Assets implements Disposable, AssetErrorListener {

    public static final String TAG = Assets.class.getName();
    public static final Assets instance = new Assets();

    private AssetManager assetManager;

    public AssetFonts fonts;

    public AssetSounds sounds;
    public AssetMusic music;

    public AssetAirplane player;
    public AssetAirplane enemy[];
    public Asset bullet;
    public Asset doubleBullet;
    public Asset enemyBullet;
    public AssetLevelDecoration levelDecoration;
    public Asset extraLive;
    public Asset health;
    
    private Assets() {
    }

    /**
     * Loads the various assets.
     * Such as images, fonts, I tried sounds and music still have to debug it (figure out why libgdx dosen't want to load it)
     * @param assetManager
     */
    public void init(AssetManager assetManager) {

        this.assetManager = assetManager;
        assetManager.setErrorListener(this);

        // load texture for game sprites
        assetManager.load(Constants.TEXTURE_ATLAS_GAME, TextureAtlas.class);
        assetManager.finishLoading();
        
        Gdx.app.debug(TAG, "# of assets loaded: " + assetManager.getAssetNames().size);
        for (String a: assetManager.getAssetNames())
            Gdx.app.debug(TAG, "asset: " + a);

        // create atlas for game sprites
        TextureAtlas atlas = assetManager.get(Constants.TEXTURE_ATLAS_GAME);
        for (Texture t: atlas.getTextures())
            t.setFilter(TextureFilter.Linear, TextureFilter.Linear);

        fonts = new AssetFonts();

        // create game resource objects
        player = new AssetAirplane(atlas, "player");
        enemy = new AssetAirplane[]{
                new AssetAirplane(atlas, "enemy_plane_green"),
                new AssetAirplane(atlas, "enemy_plane_white"),
                new AssetAirplane(atlas, "enemy_plane_yellow")
        };
        levelDecoration = new AssetLevelDecoration(atlas);
        bullet = new Asset(atlas, "bullet");
        doubleBullet = new Asset(atlas, "bullet_double");

        enemyBullet = new Asset(atlas, "emeny_bullet_2");
        extraLive = new Asset(atlas, "player");
        health = new Asset(atlas, "Health");
        
        // load sounds
        /* assetManager.load("sounds/enemy_shoot.wav", Sound.class);
        assetManager.load("sounds/explosion.wav", Sound.class);
        assetManager.load("sounds/explosion_large.wav", Sound.class);
        assetManager.load("sounds/player_bullet_01.wav", Sound.class);
        assetManager.load("sounds/player_hit.wav", Sound.class);
        assetManager.load("sounds/warning.wav", Sound.class);
        assetManager.load("sounds/click_01.wav", Sound.class);
        assetManager.load("sounds/click_02.wav", Sound.class);
        assetManager.finishLoading();
        
        // load music
        assetManager.load("music/AcesHigh.mp3", Music.class);
        assetManager.finishLoading();
        
        // create sound and music resource objects
        sounds = new AssetSounds(assetManager);
        music = new AssetMusic(assetManager);
    */
    }

    /**
     * Dispose of the fonts and assets manager.
     */
    @Override
    public void dispose() {
        assetManager.dispose();
        fonts.defaultSmall.dispose();
        fonts.defaultNormal.dispose();
        fonts.defaultBig.dispose();
    }
    
    /**
     * When assets manager decides it wants to load one sound file from sounds/
     * and not the rest of the sounds this method is called. Or when asset manager can't load an asset.
     */
    @Override
    public void error(@SuppressWarnings("rawtypes") AssetDescriptor asset, Throwable throwable) {

        Gdx.app.error(TAG, "Couldn't load asset '" + asset + "'", (Exception) throwable);
    }

    /**
     * Basic asset class.
     */
    public class Asset {

        public final AtlasRegion region;

        public Asset(TextureAtlas atlas, String imageName) {
            region = atlas.findRegion(imageName);
            Gdx.app.log(TAG, "Loaded asset '" + imageName + "'");
        }
    }

    /**
     * Asset class for airplanes.
     * Contains animation for explosion, and flying
     */
    @SuppressWarnings("rawtypes")
    public class AssetAirplane {

        public final AtlasRegion region;
        public final Animation animationNormal;
        public final Animation animationExplosionBig;
        public final String regionName;

        @SuppressWarnings("unchecked")
        public AssetAirplane(TextureAtlas atlas, String region) {
            this.region = atlas.findRegion(region);
            regionName = region;
            Array<AtlasRegion> regions = atlas.findRegions(region);
            animationNormal = new Animation(1.0f / 15.0f, regions, Animation.PlayMode.LOOP);
            regions = atlas.findRegions("explosion_big");
            animationExplosionBig = new Animation(1.0f / 15.0f, regions, Animation.PlayMode.LOOP);
        }
    }

    /**
     * Asset class for level decoration (islands and water)
     */
    public class AssetLevelDecoration {

        public final AtlasRegion islandBig;
        public final AtlasRegion islandSmall;
        public final AtlasRegion islandTiny;
        public final AtlasRegion water;

        public AssetLevelDecoration(TextureAtlas atlas) {
            water = atlas.findRegion("water");
            islandBig = atlas.findRegion("island_big");
            islandSmall = atlas.findRegion("island_small");
            islandTiny = atlas.findRegion("island_tiny");
        }
    }

    /**
     * Asset class for fonts
     */
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

    
    /**
     * Asset class for sounds.
     */
    public class AssetSounds {

        public final Sound enemyShoot;
        public final Sound explosion;
        public final Sound explosionLarge;
        public final Sound playerBullet;
        public final Sound playerHit;
        public final Sound warning;
        public final Sound click01;
        public final Sound click02;

        public AssetSounds(AssetManager am) {
            enemyShoot = am.get("sounds/enemy_shoot.wav", Sound.class);
            explosion = am.get("sounds/explosion.wav", Sound.class);
            explosionLarge = am.get("sounds/explosion_large.wav", Sound.class);
            playerBullet = am.get("sounds/player_bullet_01.wav", Sound.class);
            playerHit = am.get("sounds/player_hit.wav", Sound.class);
            warning = am.get("sounds/warning.wav", Sound.class);
            click01 = am.get("sounds/click_01.wav", Sound.class);
            click02 = am.get("sounds/click_02.wav", Sound.class);
        }
    }

    /**
     * Asset class for background music.
     */
    public class AssetMusic {
        public final Music song01;

        public AssetMusic(AssetManager am) {
            song01 = am.get("music/AcesHigh.mp3", Music.class);
        }
    }

}
