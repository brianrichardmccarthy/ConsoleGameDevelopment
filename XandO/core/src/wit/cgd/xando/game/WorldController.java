package wit.cgd.xando.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;

public class WorldController extends InputAdapter {

	private static final String TAG = WorldController.class.getName();
	public float viewportWidth;
	public int width, height;
	
	public WorldController() {
		init();
	}
	
	private void init() {
		Gdx.input.setInputProcessor(this);
	}

	public void update(float deltaTime) {
		
	}
	
	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		return false;
	}
	
}
