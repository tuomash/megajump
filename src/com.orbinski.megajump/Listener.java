package com.orbinski.megajump;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;

import static com.orbinski.megajump.Globals.*;

class Listener implements ApplicationListener
{
  Game game;
  Controller controller;
  Renderer renderer;
  UIRenderer uiRenderer;
  int updates;

  @Override
  public void create()
  {
    Resources.load();
    UserInterface.create();

    game = new Game();
    controller = new Controller(game);
    renderer = new Renderer(game);
    uiRenderer = new UIRenderer(game);
  }

  @Override
  public void resize(final int width, final int height)
  {
    renderer.viewport.update(width, height);
    uiRenderer.viewport.update(width, height, true);
    screenWidth = width;
    screenHeight = height;
    UserInterface.layout(width, height);
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
    uiRenderer.render();
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
