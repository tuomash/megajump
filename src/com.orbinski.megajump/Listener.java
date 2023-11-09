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
    Animations.load();
    Audio.load();

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
    final float frameTime = Gdx.graphics.getDeltaTime();
    float frameTimeForPhysicsStep = frameTime;
    controller.update();

    while (frameTimeForPhysicsStep > 0.0f)
    {
      final float delta = Math.min(frameTimeForPhysicsStep, TIME_STEP_SECONDS);
      game.updatePhysics(delta);
      frameTimeForPhysicsStep = frameTimeForPhysicsStep - delta;
      updates++;

      if (updates >= MAX_UPDATES)
      {
        break;
      }
    }

    updates = 0;
    game.update(frameTime);
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
