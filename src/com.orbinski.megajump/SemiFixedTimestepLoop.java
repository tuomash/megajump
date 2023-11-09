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

    if (frameTime > MAX_FRAME_TIME)
    {
      frameTime = MAX_FRAME_TIME;
    }

    float frameTimeForPhysicsStep = frameTime;
    controller.update();

    while (frameTimeForPhysicsStep > 0.0f)
    {
      final float delta = Math.min(frameTimeForPhysicsStep, TIME_STEP_SECONDS);
      game.updatePhysics(delta);
      frameTimeForPhysicsStep = frameTimeForPhysicsStep - delta;
    }

    game.update(frameTime);
    renderer.render();
    uiRenderer.render();
  }
}
