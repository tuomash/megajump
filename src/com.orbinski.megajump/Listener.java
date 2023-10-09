package com.orbinski.megajump;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;

import static com.orbinski.megajump.Globals.*;

class Listener implements ApplicationListener
{
  Game game;
  Controller controller;
  Renderer renderer;
  int updates;

  @Override
  public void create()
  {
    game = new Game();
    controller = new Controller(game);
    renderer = new Renderer(game);
  }

  @Override
  public void resize(final int width, final int height)
  {
    renderer.viewport.update(width, height);
  }

  @Override
  public void render()
  {
    float frameTime = Gdx.graphics.getDeltaTime();
    controller.update();

    while (frameTime > 0.0f)
    {
      final float delta = Math.min(frameTime, TIME_STEP_SECONDS);
      game.update(delta);
      frameTime = frameTime - delta;
      updates++;

      if (updates >= MAX_UPDATES)
      {
        break;
      }
    }

    updates = 0;
    renderer.render();
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
    renderer.dispose();
  }
}
