package com.orbinski.megajump;

import com.badlogic.gdx.Gdx;

import static com.orbinski.megajump.Globals.MAX_FRAME_TIME_SECONDS;
import static com.orbinski.megajump.Globals.TIME_STEP_SECONDS;

public class FullyFixedTimestepLoop extends Loop
{
  float accumulator = 0.0f;

  public FullyFixedTimestepLoop(final Game game,
                                final Controller controller,
                                final Renderer renderer,
                                final UIRenderer uiRenderer)
  {
    super(game, controller, renderer, uiRenderer);

    renderer.interpolation = true;
    game.player.interpolate = true;
  }

  @Override
  public void update()
  {
    float frameTime = Gdx.graphics.getDeltaTime();

    if (frameTime > MAX_FRAME_TIME_SECONDS)
    {
      frameTime = MAX_FRAME_TIME_SECONDS;
    }

    game = gameObj.getGame();
    accumulator = accumulator + frameTime;
    controller.update();

    // int updates = 0;

    while (accumulator >= TIME_STEP_SECONDS)
    {
      game.updatePhysics(TIME_STEP_SECONDS);
      accumulator = accumulator - TIME_STEP_SECONDS;
      // updates++;
    }

    // System.out.println("Did " + updates + " updates");
    game.update(frameTime);
    renderer.interpolationAlpha = accumulator / TIME_STEP_SECONDS;
    renderer.render();
    uiRenderer.render();
    game.handleMultiplayer();
  }
}
