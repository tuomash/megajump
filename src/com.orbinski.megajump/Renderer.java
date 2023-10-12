package com.orbinski.megajump;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.io.File;

import static com.orbinski.megajump.Globals.WORLD_HEIGHT;
import static com.orbinski.megajump.Globals.WORLD_WIDTH;

class Renderer
{
  static Viewport staticViewport;
  static Texture block;
  static Texture candle;
  static Texture dwarfLeft;
  static Texture dwarfRight;

  final Game game;
  final Viewport viewport;
  final OrthographicCamera camera;
  final ShapeRenderer shapeRenderer;
  final SpriteBatch spriteBatch;
  final Texture door;
  final Color background;

  Renderer(final Game game)
  {
    this.game = game;

    camera = new OrthographicCamera(WORLD_WIDTH, WORLD_HEIGHT);
    camera.update();

    viewport = new FitViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);
    staticViewport = viewport;

    shapeRenderer = new ShapeRenderer();
    shapeRenderer.setProjectionMatrix(camera.combined);
    shapeRenderer.setAutoShapeType(true);

    spriteBatch = new SpriteBatch();
    spriteBatch.setProjectionMatrix(camera.combined);

    File file = new File(System.getProperty("user.dir") + File.separator + "dwarf-left.png");
    dwarfLeft = new Texture(Gdx.files.absolute(file.getAbsolutePath()));

    file = new File(System.getProperty("user.dir") + File.separator + "dwarf-right.png");
    dwarfRight = new Texture(Gdx.files.absolute(file.getAbsolutePath()));

    file = new File(System.getProperty("user.dir") + File.separator + "door.png");
    door = new Texture(Gdx.files.absolute(file.getAbsolutePath()));

    file = new File(System.getProperty("user.dir") + File.separator + "block.png");
    block = new Texture(Gdx.files.absolute(file.getAbsolutePath()));

    file = new File(System.getProperty("user.dir") + File.separator + "candle.png");
    candle = new Texture(Gdx.files.absolute(file.getAbsolutePath()));

    background = new Color(173.0f / 255.0f, 216.0f / 255.0f, 230.0f / 255.0f, 1.0f);
  }

  void render()
  {
    ScreenUtils.clear(Color.BLACK);

    viewport.apply();
    shapeRenderer.setProjectionMatrix(camera.combined);
    spriteBatch.setProjectionMatrix(camera.combined);

    renderDoor();
    renderBlocks();
    renderDecorations();
    renderPlayer();
  }

  void renderDoor()
  {
    spriteBatch.begin();
    spriteBatch.draw(door,
                     game.level.door.getBottomLeftCornerX(),
                     game.level.door.getBottomLeftCornerY(),
                     game.level.door.getWidth(),
                     game.level.door.getHeight());
    spriteBatch.end();
  }

  void renderBlocks()
  {
    if (!game.level.blocks.isEmpty())
    {
      spriteBatch.begin();

      for (int i = 0; i < game.level.blocks.size(); i++)
      {
        final Block block = game.level.blocks.get(i);
        spriteBatch.draw(Renderer.block,
                         block.getBottomLeftCornerX(),
                         block.getBottomLeftCornerY(),
                         block.getWidth(),
                         block.getHeight());
      }

      spriteBatch.end();

      for (int i = 0; i < game.level.blocks.size(); i++)
      {
        final Block block = game.level.blocks.get(i);

        if (block.drawBorder)
        {
          renderQuad(block.getBottomLeftCornerX(),
                     block.getBottomLeftCornerY(),
                     block.getWidth(),
                     block.getHeight(),
                     Color.RED);
        }
      }
    }
  }

  void renderDecorations()
  {
    if (!game.level.decorations.isEmpty())
    {
      spriteBatch.begin();

      for (int i = 0; i < game.level.decorations.size(); i++)
      {
        final Decoration decoration = game.level.decorations.get(i);
        spriteBatch.draw(candle,
                         decoration.getBottomLeftCornerX(),
                         decoration.getBottomLeftCornerY(),
                         decoration.getWidth(),
                         decoration.getHeight());
      }

      spriteBatch.end();
    }
  }

  void renderPlayer()
  {
    final Player player = game.player;

    if (player.texture != null)
    {
      spriteBatch.begin();
      spriteBatch.draw(player.texture,
                       player.getBottomLeftCornerX(),
                       player.getBottomLeftCornerY(),
                       player.getWidth(),
                       player.getHeight());
      spriteBatch.end();
    }

    if (player.drawBorder)
    {
      renderQuad(player.getBottomLeftCornerX(),
                 player.getBottomLeftCornerY(),
                 player.getWidth(),
                 player.getHeight(),
                 Color.RED);
    }

    if (player.drawCollisions)
    {
      renderQuad(player.rightSide.x,
                 player.rightSide.y,
                 player.rightSide.width,
                 player.rightSide.height,
                 Color.YELLOW);

      renderQuad(player.bottomSide.x,
                 player.bottomSide.y,
                 player.bottomSide.width,
                 player.bottomSide.height,
                 Color.YELLOW);
    }

    if (player.targeting)
    {
      renderQuad(player.cursorX,
                 player.cursorY,
                 player.cursorWidth,
                 player.cursorHeight,
                 Color.WHITE);


      renderLine(player.cursorX,
                 player.cursorY,
                 player.getX(),
                 player.getY(),
                 Color.WHITE);
    }
  }

  void renderQuad(final float x,
                  final float y,
                  final float width,
                  final float height,
                  final Color color)
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

  void renderQuad(final float x,
                  final float y,
                  final float width,
                  final float height,
                  final float red,
                  final float green,
                  final float blue,
                  final float alpha)
  {
    shapeRenderer.setProjectionMatrix(camera.combined);
    shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
    shapeRenderer.setColor(red, green, blue, alpha);
    shapeRenderer.rect(x, y, width, height);
    shapeRenderer.end();
  }

  void renderLine(final float x1,
                  final float y1,
                  final float x2,
                  final float y2,
                  final Color color)
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

  void renderLine(final float x1,
                  final float y1,
                  final float x2,
                  final float y2,
                  final float red,
                  final float green,
                  final float blue,
                  final float alpha)
  {
    shapeRenderer.setProjectionMatrix(camera.combined);
    shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
    shapeRenderer.setColor(red, green, blue, alpha);
    shapeRenderer.line(x1, y1, x2, y2);
    shapeRenderer.end();
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
