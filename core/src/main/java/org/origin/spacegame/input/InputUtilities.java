package org.origin.spacegame.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;

public class InputUtilities
{
    public static void detectCameraMovement(OrthographicCamera camera)
    {
        if(Gdx.input.isKeyPressed(Input.Keys.DPAD_UP))
            camera.position.y++;
        if(Gdx.input.isKeyPressed(Input.Keys.DPAD_DOWN))
            camera.position.y--;
        if(Gdx.input.isKeyPressed(Input.Keys.DPAD_LEFT))
            camera.position.x--;
        if(Gdx.input.isKeyPressed(Input.Keys.DPAD_RIGHT))
            camera.position.x++;
        if(Gdx.input.isKeyPressed(Input.Keys.ENTER) || Gdx.input.isKeyPressed(Input.Keys.PLUS))
            camera.zoom += 0.01f;
        if(Gdx.input.isKeyPressed(Input.Keys.BACKSPACE) || Gdx.input.isKeyPressed(Input.Keys.MINUS))
            camera.zoom -= 0.01f;
        if(camera.zoom <= 0.01f)
            camera.zoom = 0.01f;
    }
}
