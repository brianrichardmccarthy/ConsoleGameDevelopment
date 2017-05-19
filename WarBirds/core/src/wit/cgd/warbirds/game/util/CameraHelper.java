/**
 *
 * @file CameraHelper
 * @author Brian McCarthy, 20063914
 * @assignment Warbirds
 * @brief Camera Class, handles moving & position, and zoom in and out, tracking of a target.
 * @notes DESCRIPTION OF CODE, BUGS, FEATURES, ISSUES, ETC.
 *
 */
package wit.cgd.warbirds.game.util;

import wit.cgd.warbirds.game.objects.AbstractGameObject;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class CameraHelper {

    @SuppressWarnings("unused")
    private static final String TAG = CameraHelper.class.getName();

    private final float MAX_ZOOM_IN = 0.25f;
    private final float MAX_ZOOM_OUT = 10.0f;

    private Vector2 position;
    private float zoom;

    private AbstractGameObject target;

    /**
     * Constructor
     */
    public CameraHelper() {
        position = new Vector2();
        zoom = 1.0f;
    }

    /**
     * if the camera has a target
     *      Updates the position relative to the target
     * @param deltaTime
     */
    public void update(float deltaTime) {

        if (!hasTarget()) return;

        position.x = target.position.x + target.origin.x;
        position.y = target.position.y + target.origin.y;
    }

    /**
     * Resets the zoom and position of the camera
     */
    public void reset() {

        setPosition(0, 0);
        setZoom(1);
    }

    /**
     * Sets the position to the given parameters
     * @param x
     * @param y
     */
    public void setPosition(float x, float y) {

        this.position.set(x, y);
    }

    /**
     * Gets the position of the camera
     * @return
     */
    public Vector2 getPosition() {

        return position;
    }

    /**
     * Adds parameter to current value of zoom
     * @param amount
     */
    public void addZoom(float amount) {

        setZoom(zoom + amount);
    }

    /**
     * Sets the zoom to the given value if it's greater than the max zoom in and less than max zoom out
     * @param zoom
     */
    public void setZoom(float zoom) {

        this.zoom = MathUtils.clamp(zoom, MAX_ZOOM_IN, MAX_ZOOM_OUT);
    }

    /*
     * Gets the current zoom
     */
    public float getZoom() {

        return zoom;
    }

    /**
     * Sets the cameras' target
     * @param target
     */
    public void setTarget(AbstractGameObject target) {

        this.target = target;
    }
    
    /*
     * Gets the Cameras' target
     */
    public AbstractGameObject getTarget() {

        return target;
    }

    /**
     * Boolean, has camera a target
     * @return
     */
    public boolean hasTarget() {

        return target != null;
    }

    /**
     * Is the cameras' target the given parameter
     * @param target
     * @return
     */
    public boolean hasTarget(AbstractGameObject target) {

        return hasTarget() && this.target.equals(target);
    }

    /**
     * Copy the position and zoom of "this" camera to parameter camera
     * @param camera
     */
    public void applyTo(OrthographicCamera camera) {

        camera.position.x = position.x;
        camera.position.y = position.y;
        camera.zoom = zoom;
        camera.update();
    }

}
