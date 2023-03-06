package com.orbinski.megajump;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.Input;

public class Game extends ApplicationAdapter
{
  @Override
  public void create()
  {
    super.create();
  }

  @Override
  public void resize(final int width, final int height)
  {
  }

  @Override
  public void render()
  {
    if (Gdx.input.isKeyPressed(Input.Keys.ALT_LEFT) && Gdx.input.isKeyJustPressed(Input.Keys.ENTER))
    {
      final Graphics.DisplayMode currentMode = Gdx.graphics.getDisplayMode();

      if (Gdx.graphics.isFullscreen())
      {
        // System.out.println("w: " + currentMode.width + ", h:" + currentMode.height);
        Gdx.graphics.setWindowedMode(currentMode.width, currentMode.height);
      }
      else
      {
        // TODO: doesn't work properly when going from windowed (not maximized) to full screen
        Gdx.graphics.setFullscreenMode(currentMode);
      }
    }
  }

  @Override
  public void pause()
  {
  }

  @Override
  public void resume()
  {
  }

  @Override
  public void dispose()
  {
  }
}
