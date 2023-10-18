package com.orbinski.megajump;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.Arrays;

import static com.orbinski.megajump.Globals.WORLD_HEIGHT;
import static com.orbinski.megajump.Globals.WORLD_WIDTH;

class Renderer
{
  static Viewport staticViewport;

  final Game game;
  final Viewport viewport;
  final OrthographicCamera camera;
  final SpriteBatch spriteBatch;
  final ShapeRenderer shapeRenderer;
  final Color background;

  final Entity[] entities = new Entity[1000];
  int entityIndex = 0;

  final Shape[] shapes = new Shape[1000];
  int shapeIndex = 0;

  Renderer(final Game game)
  {
    this.game = game;

    camera = new OrthographicCamera(WORLD_WIDTH, WORLD_HEIGHT);
    camera.update();

    viewport = new FitViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);
    staticViewport = viewport;

    spriteBatch = new SpriteBatch();
    spriteBatch.setProjectionMatrix(camera.combined);

    shapeRenderer = new ShapeRenderer();
    shapeRenderer.setProjectionMatrix(camera.combined);
    shapeRenderer.setAutoShapeType(true);

    background = new Color(173.0f / 255.0f, 216.0f / 255.0f, 230.0f / 255.0f, 1.0f);

    for (int i = 0; i < shapes.length; i++)
    {
      shapes[i] = new Shape();
    }
  }

  void render()
  {
    ScreenUtils.clear(Color.BLACK);

    clearEntities();
    clearShapes();

    viewport.apply();
    shapeRenderer.setProjectionMatrix(camera.combined);
    spriteBatch.setProjectionMatrix(camera.combined);

    if (game.help)
    {
      return;
    }

    renderDoor();
    renderBlocks();
    renderDecorations();
    renderTrampolines();
    renderPlayer();

    renderEntities();
    renderShapes();
  }

  void renderDoor()
  {
    addEntity(game.level.door);
  }

  void renderBlocks()
  {
    for (int i = 0; i < game.level.blocks.size(); i++)
    {
      final Block block = game.level.blocks.get(i);
      addEntity(block);

      if (block.drawBorder)
      {
        addQuad(block.getBottomLeftCornerX(),
                block.getBottomLeftCornerY(),
                block.getWidth(),
                block.getHeight(),
                Color.RED);
      }
    }
  }

  void renderDecorations()
  {
    for (int i = 0; i < game.level.decorations.size(); i++)
    {
      addEntity(game.level.decorations.get(i));
    }
  }

  void renderTrampolines()
  {
    if (!game.level.trampolines.isEmpty())
    {
      for (int i = 0; i < game.level.trampolines.size(); i++)
      {
        final Trampoline trampoline = game.level.trampolines.get(i);
        addFilledQuad(trampoline.getBottomLeftCornerX(),
                      trampoline.getBottomLeftCornerY(),
                      trampoline.getWidth(),
                      trampoline.getHeight(),
                      Color.BLUE);
      }
    }
  }

  void renderPlayer()
  {
    final Player player = game.player;

    if (player.texture != null)
    {
      addEntity(player);
    }

    if (player.drawBorder)
    {
      addQuad(player.getBottomLeftCornerX(),
              player.getBottomLeftCornerY(),
              player.getWidth(),
              player.getHeight(),
              Color.RED);
    }

    if (player.drawCollisions)
    {
      addQuad(player.rightSide.x,
              player.rightSide.y,
              player.rightSide.width,
              player.rightSide.height,
              Color.YELLOW);

      addQuad(player.bottomSide.x,
              player.bottomSide.y,
              player.bottomSide.width,
              player.bottomSide.height,
              Color.YELLOW);
    }

    if (player.targeting)
    {
      addQuad(player.cursorX,
              player.cursorY,
              player.cursorWidth,
              player.cursorHeight,
              Color.WHITE);

      addLine(player.cursorX,
              player.cursorY,
              player.getX(),
              player.getY(),
              Color.WHITE);
    }
  }

  void addEntity(final Entity entity)
  {
    if (entity != null && entity.texture != null)
    {
      entities[entityIndex] = entity;
      entityIndex++;
    }
  }

  void renderEntities()
  {
    if (entityIndex == 0)
    {
      return;
    }

    spriteBatch.begin();

    for (int i = 0; i < entities.length; i++)
    {
      final Entity entity = entities[i];

      // Reached end
      if (entity == null)
      {
        break;
      }

      spriteBatch.draw(entity.texture,
                       entity.getBottomLeftCornerX(),
                       entity.getBottomLeftCornerY(),
                       entity.getWidth(),
                       entity.getHeight());
    }

    spriteBatch.end();
  }

  void clearEntities()
  {
    Arrays.fill(entities, null);
    entityIndex = 0;
  }

  void addQuad(final float x,
               final float y,
               final float width,
               final float height,
               final Color color)
  {
    final Shape shape = shapes[shapeIndex];
    shape.type = Shape.Type.QUAD;
    shape.color = color;
    shape.x = x;
    shape.y = y;
    shape.width = width;
    shape.height = height;
    shape.render = true;

    shapeIndex++;
  }

  void addFilledQuad(final float x,
                     final float y,
                     final float width,
                     final float height,
                     final Color color)
  {
    final Shape shape = shapes[shapeIndex];
    shape.type = Shape.Type.FILLED_QUAD;
    shape.color = color;
    shape.x = x;
    shape.y = y;
    shape.width = width;
    shape.height = height;
    shape.render = true;

    shapeIndex++;
  }

  void addLine(final float x1,
               final float y1,
               final float x2,
               final float y2,
               final Color color)
  {
    final Shape shape = shapes[shapeIndex];
    shape.type = Shape.Type.LINE;
    shape.color = color;
    shape.x1 = x1;
    shape.y1 = y1;
    shape.x2 = x2;
    shape.y2 = y2;
    shape.render = true;

    shapeIndex++;
  }

  void renderShapes()
  {
    if (shapeIndex == 0)
    {
      return;
    }

    shapeRenderer.begin();

    for (int i = 0; i < shapes.length; i++)
    {
      final Shape shape = shapes[i];

      if (!shape.render)
      {
        break;
      }

      shapeRenderer.setColor(shape.color.r, shape.color.g, shape.color.b, shape.color.a);

      if (shape.type == Shape.Type.QUAD || shape.type == Shape.Type.FILLED_QUAD)
      {
        shapeRenderer.rect(shape.x, shape.y, shape.width, shape.height);
      }
      else if (shape.type == Shape.Type.LINE)
      {
        shapeRenderer.line(shape.x1, shape.y1, shape.x2, shape.y2);
      }
    }

    shapeRenderer.end();
  }

  void clearShapes()
  {
    for (int i = 0; i < shapes.length; i++)
    {
      shapes[i].render = false;
    }

    shapeIndex = 0;
  }

  void dispose()
  {
    if (spriteBatch != null)
    {
      spriteBatch.dispose();
    }

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
