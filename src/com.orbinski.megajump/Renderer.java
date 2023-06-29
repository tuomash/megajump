package com.orbinski.megajump;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import static com.orbinski.megajump.Globals.*;

class Renderer
{
  static Viewport staticViewport;

  final Game game;
  final Viewport viewport;
  final OrthographicCamera camera;
  final ShapeRenderer shapeRenderer;

  Renderer(Game game)
  {
    this.game = game;

    camera = new OrthographicCamera(WORLD_WIDTH, WORLD_HEIGHT);
    camera.update();

    viewport = new FitViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);
    staticViewport = viewport;

    shapeRenderer = new ShapeRenderer();
    shapeRenderer.setProjectionMatrix(camera.combined);
    shapeRenderer.setAutoShapeType(true);
  }

  void render()
  {
    ScreenUtils.clear(Color.BLACK);
    renderPlayer();
  }

  void renderPlayer()
  {
    if (game.player.targeting)
    {
      renderQuad(game.player.mouseWorld.x,
                 game.player.mouseWorld.y,
                 game.player.cursorWidth,
                 game.player.cursorHeight,
                 Color.WHITE);
      renderLine(game.player.mouseWorld.x,
                 game.player.mouseWorld.y,
                 game.player.x,
                 game.player.y,
                 Color.WHITE);
    }

    renderQuad(game.player.centerX,
               game.player.centerY,
               game.player.centerWidth,
               game.player.centerHeight,
               Color.WHITE);

    renderQuad(game.player.topLeftCornerX,
               game.player.topLeftCornerY,
               game.player.width,
               game.player.height,
               Color.RED);
  }

  void renderQuad(float x,
                  float y,
                  float width,
                  float height,
                  Color color)
  {
    renderQuad(x,
               y,
               width,
               height,
               color.r,
               color.g,
               color.b,
               color.a);
  }

  void renderQuad(float x,
                  float y,
                  float width,
                  float height,
                  float red,
                  float green,
                  float blue,
                  float alpha)
  {
    Gdx.graphics.getGL20().glEnable(GL20.GL_BLEND);
    Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

    shapeRenderer.setProjectionMatrix(camera.combined);
    shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
    shapeRenderer.setColor(red, green, blue, alpha);
    shapeRenderer.rect(x, y, width, height);
    shapeRenderer.end();

    Gdx.gl.glDisable(GL20.GL_BLEND);
  }

  void renderLine(float x1,
                  float y1,
                  float x2,
                  float y2,
                  Color color)
  {
    renderLine(x1,
               y1,
               x2,
               y2,
               color.r,
               color.g,
               color.b,
               color.a);
  }

  void renderLine(float x1,
                  float y1,
                  float x2,
                  float y2,
                  float red,
                  float green,
                  float blue,
                  float alpha)
  {
    Gdx.graphics.getGL20().glEnable(GL20.GL_BLEND);
    Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

    shapeRenderer.setProjectionMatrix(camera.combined);
    shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
    shapeRenderer.setColor(red, green, blue, alpha);
    shapeRenderer.line(x1, y1, x2, y2);
    shapeRenderer.end();

    Gdx.gl.glDisable(GL20.GL_BLEND);
  }

  void dispose()
  {
    if (shapeRenderer != null)
    {
      shapeRenderer.dispose();
    }
  }

  public static Vector3 unproject(final Vector3 screen)
  {
    if (staticViewport != null)
    {
      return staticViewport.unproject(screen);
    }

    return null;
  }
}
