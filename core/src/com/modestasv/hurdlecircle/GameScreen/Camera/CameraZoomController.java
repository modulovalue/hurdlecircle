package com.mv.desktop.hurdlecircle.GameScreen.Camera;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Interpolation;
import com.modestasv.hurdlecircle.MVInterpolate;

/**
 * Created by Modestas Valauskas on 21.03.2015.
 */
public class CameraZoomController {

    public final float OUTSPEED = 2f;
    public final float INSPEED = 1f;

    public MVInterpolate zoomInterpolator = new MVInterpolate(new Interpolation.Exp(4,3), INSPEED, 1f, 1.7f);

    public CameraZoomController() {
    }

    public CameraZoomController updateInterpolatorZoomOut() {
        zoomInterpolator.setSpeed(OUTSPEED);
        zoomInterpolator.update();
        return this;
    }

    public CameraZoomController updateInterpolatorZoomIn() {
        zoomInterpolator.setSpeed(INSPEED);
        zoomInterpolator.updateBW();
        return this;
    }

    public void update(OrthographicCamera camera) {
        camera.zoom = zoomInterpolator.getValue();
        camera.up.set(0, 1, 0);
        camera.direction.set(0, 0, 1);
    }
}
