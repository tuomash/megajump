package com.orbinski.megajump;

import com.badlogic.gdx.Gdx;

import static com.orbinski.megajump.Globals.MAX_FRAME_TIME_SECONDS;
import static com.orbinski.megajump.Globals.TIME_STEP_SECONDS;

class SemiFixedTimestepLoop extends Loop
{
  public SemiFixedTimestepLoop(final Game game,
                               final Controller controller,
                               final Renderer renderer,
                               final UIRenderer uiRenderer)
  {
    super(game, controller, renderer, uiRenderer);
  }

  @Override
  public void update()
  {
    float frameTime = Gdx.graphics.getDeltaTime();

    if (frameTime > MAX_FRAME_TIME_SECONDS)
    {
      frameTime = MAX_FRAME_TIME_SECONDS;
    }

    // System.out.println("frame time: " + frameTime);
    game = gameObj.getGame();
    float frameTimeForPhysics = frameTime;
    controller.update(frameTime);

    while (frameTimeForPhysics > 0.0f)
    {
      final float delta = Math.min(frameTimeForPhysics, TIME_STEP_SECONDS);
      game.updatePhysics(delta);
      frameTimeForPhysics = frameTimeForPhysics - delta;
    }

    game.update(frameTime);
    renderer.render();
    uiRenderer.render();
    game.handleMultiplayer();
  }
}
