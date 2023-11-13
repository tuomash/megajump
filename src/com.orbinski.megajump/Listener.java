package com.orbinski.megajump;

import com.badlogic.gdx.ApplicationListener;

import static com.orbinski.megajump.Globals.*;

class Listener implements ApplicationListener
{
  Game game;
  Controller controller;
  Renderer renderer;
  UIRenderer uiRenderer;
  Loop loop;

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
    loop = new SemiFixedTimestepLoop(game, controller, renderer, uiRenderer);
    // loop = new FullyFixedTimestepLoop(game, controller, renderer, uiRenderer);
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
    loop.update();
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
