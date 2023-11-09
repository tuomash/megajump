package com.orbinski.megajump;

import com.badlogic.gdx.Gdx;

import static com.orbinski.megajump.Globals.*;

class SemiFixedTimestepLoop extends Loop
{
  int updates;

  public SemiFixedTimestepLoop(final Game game,
                               final Controller controller,
                               final Renderer renderer,
                               final UIRenderer uiRenderer)
  {
    super(game, controller, renderer, uiRenderer);
  }

  @Override
  void update()
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
}
