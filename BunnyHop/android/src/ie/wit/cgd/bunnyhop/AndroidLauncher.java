/**
 *
 * @file        AndroidLauncher.java
 * @author      Brian McCarthy, 20063914
 * @assignment  BunnyHop
 * @brief       Android launcher
 * @notes       Launches the app on android, No known BUGS or ISSUES
 *
 */
package ie.wit.cgd.bunnyhop;

import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import ie.wit.cgd.bunnyhop.BunnyHopMain;

public class AndroidLauncher extends AndroidApplication {
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		initialize(new BunnyHopMain(), config);
	}
}
