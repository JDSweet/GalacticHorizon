package org.origin.spacegame.utilities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import org.origin.spacegame.Constants;

public class CameraManager
{
    public enum RenderView
    {
        SYSTEM_VIEW,
        GALACTIC_VIEW
    }

    private OrthographicCamera currentCamera;
    private final OrthographicCamera galacticCamera;
    private final OrthographicCamera systemViewCamera;
    private RenderView renderView;

    public CameraManager()
    {
        this.currentCamera = null;
        this.galacticCamera = new OrthographicCamera(Constants.GALACTIC_MAP_CAMERA_VIEWPORT_WIDTH, Constants.GALACTIC_MAP_CAMERA_VIEWPORT_HEIGHT);;
        this.systemViewCamera = new OrthographicCamera(Constants.StarSystemConstants.STAR_SYSTEM_VIEWPORT_WIDTH, Constants.StarSystemConstants.STAR_SYSTEM_VIEWPORT_HEIGHT);
        setRenderView(RenderView.GALACTIC_VIEW);
    }

    public void update()
    {
        this.currentCamera.update();
    }

    private boolean cameraChange = false;

    public void toggleChangeCameraFlag()
    {
        cameraChange = !cameraChange;
    }

    public boolean hasRecentlyChangedCamera()
    {
        return cameraChange;
    }

    public void setRenderView(RenderView renderView)
    {
        this.renderView = renderView;
        if(renderView == RenderView.GALACTIC_VIEW)
            this.currentCamera = galacticCamera;
        else
            this.currentCamera = systemViewCamera;
        toggleChangeCameraFlag();
    }

    public RenderView getRenderView()
    {
        return renderView;
    }

    public OrthographicCamera getCurrentCamera()
    {
        if(currentCamera == null)
            Gdx.app.log("CameraManagerDebug", "currentCamera variable is null. This will likely cause an exception.");
        return this.currentCamera;
    }
}
