package com.orbinski.megajump;

import com.badlogic.gdx.Gdx;

import static com.orbinski.megajump.Globals.*;

class FullyFixedTimestepLoop extends Loop
{
  float accumulator = 0.0f;

  FullyFixedTimestepLoop(final Game game,
                         final Controller controller,
                         final Renderer renderer,
                         final UIRenderer uiRenderer)
  {
    super(game, controller, renderer, uiRenderer);

    renderer.interpolation = true;
  }

  @Override
  void update()
  {
    float frameTime = Gdx.graphics.getDeltaTime();

    if (frameTime > MAX_FRAME_TIME)
    {
      frameTime = MAX_FRAME_TIME;
    }

    accumulator = accumulator + frameTime;
    controller.update();

    while (accumulator >= TIME_STEP_SECONDS)
    {
      game.updatePhysics(TIME_STEP_SECONDS);
      accumulator = accumulator - TIME_STEP_SECONDS;
    }

    game.update(frameTime);
    renderer.interpolationAlpha = accumulator / TIME_STEP_SECONDS;
    renderer.render();
    uiRenderer.render();
  }
}
