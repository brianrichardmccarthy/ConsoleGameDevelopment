package wit.cgd.warbirds.game.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;

import wit.cgd.warbirds.game.util.Constants;
import wit.cgd.warbirds.game.util.GamePreferences;

@SuppressWarnings("unused")
public class MenuScreen extends AbstractGameScreen {

    private static final String TAG = MenuScreen.class.getName();
    
    private Stage               stage;

     // MenuScreen widgets
     private Button              playButton;
     private Button              optionsButton;
     private Button              resetStatsButton;
     private Label               totalDeathsLabel;
     private Label               totalKillsLabel;
    
     // options window widgets
     private Table               optionsWindowLayer;
     private Window              optionsWindow;
    
     private Button              optionsSaveButton;
     private Button              optionsCancelButton;
    
     private CheckBox            firstPlayerHumanCheckBox;
     private Label               firstPlayerSkillLabel;
     private Slider              firstPlayerSkillSlider;
     private CheckBox            secondPlayerHumanCheckBox;
     private Label               secondPlayerSkillLabel;
     private Slider              secondPlayerSkillSlider;
    
     private CheckBox            soundCheckBox;
     private Slider              soundSlider;
     private CheckBox            musicCheckBox;
     private Slider              musicSlider;
    
     // debug
     private final float         DEBUG_REBUILD_INTERVAL  = 5.0f;
     private boolean             debugEnabled            = false;
     private float               debugRebuildStage;
    
    public MenuScreen(Game game) {
        super(game);
    }

    @Override
    public void render(float deltaTime) {
        Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        if (debugEnabled) {
            debugRebuildStage -= deltaTime;
            if (debugRebuildStage <= 0) {
                debugRebuildStage = DEBUG_REBUILD_INTERVAL;
                rebuildStage();
            }
        }
        stage.act(deltaTime);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update((int)Constants.VIEWPORT_GUI_WIDTH, (int)Constants.VIEWPORT_GUI_HEIGHT, false);
    }

    @Override
    public void hide() {
        stage.dispose();
    }

    @Override
    public void show() {
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);
        rebuildStage();
    }

    @Override
    public void pause() {

    }

    private void rebuildStage() {

        skin = new Skin(Gdx.files.internal(Constants.SKIN_UI), new TextureAtlas(Constants.TEXTURE_ATLAS_UI));

        // assemble stage for menu screen
        stage.clear();
        Stack stack = new Stack();
        stage.addActor(stack);
        stack.setSize(Constants.VIEWPORT_GUI_WIDTH, Constants.VIEWPORT_GUI_HEIGHT-20f);

        // build all layers
        stack.add(buildBackgroundLayer());
        stack.add(buildStatsLayer());
        stack.add(buildControlsLayer());

        optionsWindowLayer = buildOptionsWindowLayer();
        stage.addActor(optionsWindowLayer);
        stage.addActor(optionsWindowLayer);
    }
    
    private Table buildBackgroundLayer() {
        Table table = new Table();
        table.add(new Image(skin, "background"));
        return table;
    }

    private Table buildStatsLayer() {

        Table table = new Table();
        table.left().top();

        totalDeathsLabel = new Label("Total Deaths: * ", skin);
        table.add(totalDeathsLabel).left();
        table.row();
        totalKillsLabel = new Label("Total Kills: * ", skin);
        table.add(totalKillsLabel).left();

        table.row();
        resetStatsButton = new Button(skin, "reset");
        table.add(resetStatsButton).pad(Constants.BUTTON_PAD);
        resetStatsButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                onResetStatsClicked();
            }
        });

        if (debugEnabled) table.debug();
        return table;
    }

    private void onResetStatsClicked() { }
    
    private Table buildControlsLayer() {
        Table table = new Table();
        table.right().bottom();
        table.align(Align.bottom);

        playButton = new Button(skin, "play");
        table.add(playButton).pad(Constants.BUTTON_PAD);
        // table.add(playButton).padRight(Constants.BUTTON_PAD*3.5f);
        playButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                onPlayClicked();
            }
        });
        table.row();

        optionsButton = new Button(skin, "options");
        table.add(optionsButton).pad(Constants.BUTTON_PAD);
        optionsButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                onOptionsClicked();
            }
        });

        if (debugEnabled) table.debug();

        return table;
    }

    private void onPlayClicked() {
        game.setScreen(new GameScreen(game));
    }

    private Table buildOptionsWindowLayer() {

        // create instance of window
        optionsWindow = new Window("Options", defaultSkin);

        // cancel and save buttons 
        optionsCancelButton = new Button(skin, "cancel");
        optionsWindow.add(optionsCancelButton).pad(Constants.BUTTON_PAD);
        optionsCancelButton.addListener(new ChangeListener() {
            @Override
            public void changed (ChangeEvent event, Actor actor) {
              onCancelClicked();
            }
          });
        optionsSaveButton = new Button(skin, "save");
        optionsWindow.add(optionsSaveButton).pad(Constants.BUTTON_PAD);;
        optionsSaveButton.addListener(new ChangeListener() {
            @Override
            public void changed (ChangeEvent event, Actor actor) {
              onSaveClicked();
            }
          });
        
        // tidy up window = resize and center
        optionsWindow.setColor(1, 1, 1, 0.8f);
        optionsWindow.setVisible(false);
        if (debugEnabled) optionsWindow.debug();
        optionsWindow.pack();
        optionsWindow.setPosition((Constants.VIEWPORT_GUI_WIDTH - optionsWindow.getWidth())/2,
                        (Constants.VIEWPORT_GUI_HEIGHT - optionsWindow.getHeight())/2);
        
        // return constructed window
        return optionsWindow;
    }
    
    private void loadSettings() {
        GamePreferences prefs = GamePreferences.instance;
        prefs.load();

        // set each widget using values in prefs
    }
    
    private void saveSettings() {
        GamePreferences prefs = GamePreferences.instance;

        // save each widget value into prefs

        prefs.save();
    }
    
    private void onOptionsClicked() { 
        playButton.setVisible(false);
        optionsButton.setVisible(false);
        resetStatsButton.setVisible(false);
        optionsWindow.setVisible(true);
        loadSettings();
    }
    
    private void onSaveClicked() {
        saveSettings();
        onCancelClicked();
    }
    
    private void onCancelClicked() {

        playButton.setVisible(true);
        optionsButton.setVisible(true);
        resetStatsButton.setVisible(true);
        optionsWindow.setVisible(false);
        // AudioManager.instance.onSettingsUpdated();
    }
    
}
