package com.orbinski.megajump;

import com.badlogic.gdx.Gdx;

import static com.orbinski.megajump.Globals.*;

class SemiFixedTimestepLoop extends Loop
{
  SemiFixedTimestepLoop(final Game game,
                        final Controller controller,
                        final Renderer renderer,
                        final UIRenderer uiRenderer)
  {
    super(game, controller, renderer, uiRenderer);
  }

  @Override
  void update()
  {
    float frameTime = Gdx.graphics.getDeltaTime();

    if (frameTime > MAX_FRAME_TIME_SECONDS)
    {
      frameTime = MAX_FRAME_TIME_SECONDS;
    }

    // System.out.println("frame time: " + frameTime);
    float frameTimeForPhysics = frameTime;
    controller.update();

    while (frameTimeForPhysics > 0.0f)
    {
      final float delta = Math.min(frameTimeForPhysics, TIME_STEP_SECONDS);
      game.updatePhysics(delta);
      frameTimeForPhysics = frameTimeForPhysics - delta;
    }

    game.lastFrameTime = frameTime;
    game.update(frameTime);
    renderer.render();
    uiRenderer.render();
    game.handleMultiplayer();
  }
}
