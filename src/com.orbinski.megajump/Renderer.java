package com.orbinski.megajump;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import static com.orbinski.megajump.Globals.*;

class Renderer
{
  private static Viewport staticViewport;

  final Game game;
  final Viewport viewport;
  final SpriteBatch spriteBatch;
  final ShapeRenderer shapeRenderer;

  final Entity[] entities = new Entity[1000];
  int entityIndex = -1;

  final Shape[] shapes = new Shape[1000];
  int shapeIndex = -1;

  Renderer(final Game game)
  {
    this.game = game;

    viewport = new FitViewport(WORLD_WIDTH, WORLD_HEIGHT, game.camera);
    staticViewport = viewport;

    spriteBatch = new SpriteBatch();
    spriteBatch.setProjectionMatrix(viewport.getCamera().combined);

    shapeRenderer = new ShapeRenderer();
    shapeRenderer.setProjectionMatrix(viewport.getCamera().combined);
    shapeRenderer.setAutoShapeType(true);

    for (int i = 0; i < shapes.length; i++)
    {
      shapes[i] = new Shape();
    }
  }

  void render()
  {
    ScreenUtils.clear(Color.BLACK);

    entityIndex = -1;
    shapeIndex = -1;

    viewport.apply();
    spriteBatch.setProjectionMatrix(viewport.getCamera().combined);
    shapeRenderer.setProjectionMatrix(viewport.getCamera().combined);

    if (game.help)
    {
      return;
    }

    addDoor();
    addBlocks();
    addDecorations();
    addTrampolines();
    addPlatforms();
    addPlayer();

    renderSprites();
    renderShapes();
  }

  void addDoor()
  {
    addEntity(game.level.door);
  }

  void addBlocks()
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

  void addDecorations()
  {
    for (int i = 0; i < game.level.decorations.size(); i++)
    {
      addEntity(game.level.decorations.get(i));
    }
  }

  void addTrampolines()
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

  void addPlatforms()
  {
    for (int i = 0; i < game.level.platforms.size(); i++)
    {
      final Platform platform = game.level.platforms.get(i);
      addFilledQuad(platform.getBottomLeftCornerX(),
                    platform.getBottomLeftCornerY(),
                    platform.getWidth(),
                    platform.getHeight(),
                    UserInterface.DARK_GREEN);
    }
  }

  void addPlayer()
  {
    final Player player = game.player;
    addEntity(player);

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
      /*
      addQuad(player.collisionBox.x,
              player.collisionBox.y,
              player.collisionBox.width,
              player.collisionBox.height,
              Color.YELLOW);
       */

      addQuad(player.topSide.x,
              player.topSide.y,
              player.topSide.width,
              player.topSide.height,
              Color.YELLOW);

      addQuad(player.leftSide.x,
              player.leftSide.y,
              player.leftSide.width,
              player.leftSide.height,
              Color.YELLOW);

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

    final JumpAssistant assistant = player.assistant;

    if (assistant.targeting)
    {
      /*
      addQuad(assistant.cursorX,
              assistant.cursorY,
              assistant.cursorWidth,
              assistant.cursorHeight,
              Color.WHITE);

      addLine(assistant.cursorX,
              assistant.cursorY,
              player.getX(),
              player.getY(),
              Color.WHITE);
       */

      for (int i = 1; i < 120; i++)
      {
        if (i % 3 == 0)
        {
          final Rectangle rectangle = assistant.jumpCurve[i];
          addQuad(rectangle.x,
                  rectangle.y,
                  0.5f,
                  0.5f,
                  Color.WHITE);
        }
      }
    }
  }

  void addEntity(final Entity entity)
  {
    if (entity != null && entity.texture != null)
    {
      entityIndex++;

      if (entityIndex >= entities.length)
      {
        entityIndex = entities.length - 1;
      }

      entities[entityIndex] = entity;
    }
  }

  void renderSprites()
  {
    if (entityIndex == -1)
    {
      return;
    }

    spriteBatch.begin();

    for (int i = 0; i <= entityIndex; i++)
    {
      final Entity entity = entities[i];

      if (entity != null && entity.visible && entity.texture != null)
      {
        spriteBatch.draw(entity.texture.texture,
                         entity.getBottomLeftCornerX(),
                         entity.getBottomLeftCornerY(),
                         entity.getWidth(),
                         entity.getHeight(),
                         entity.texture.srcX,
                         entity.texture.srcY,
                         entity.texture.srcWidth,
                         entity.texture.srcHeight,
                         false,
                         false);
      }
    }

    spriteBatch.end();
  }

  void addQuad(final float x,
               final float y,
               final float width,
               final float height,
               final Color color)
  {
    shapeIndex++;

    if (shapeIndex >= shapes.length)
    {
      shapeIndex = shapes.length - 1;
    }

    final Shape shape = shapes[shapeIndex];
    shape.type = Shape.Type.QUAD;
    shape.shapeType = ShapeRenderer.ShapeType.Line;
    shape.color = color;
    shape.x = x;
    shape.y = y;
    shape.width = width;
    shape.height = height;
  }

  void addFilledQuad(final float x,
                     final float y,
                     final float width,
                     final float height,
                     final Color color)
  {
    shapeIndex++;

    if (shapeIndex >= shapes.length)
    {
      shapeIndex = shapes.length - 1;
    }

    final Shape shape = shapes[shapeIndex];
    shape.type = Shape.Type.FILLED_QUAD;
    shape.shapeType = ShapeRenderer.ShapeType.Filled;
    shape.color = color;
    shape.x = x;
    shape.y = y;
    shape.width = width;
    shape.height = height;
  }

  void addLine(final float x1,
               final float y1,
               final float x2,
               final float y2,
               final Color color)
  {
    shapeIndex++;

    if (shapeIndex >= shapes.length)
    {
      shapeIndex = shapes.length - 1;
    }

    final Shape shape = shapes[shapeIndex];
    shape.type = Shape.Type.LINE;
    shape.shapeType = ShapeRenderer.ShapeType.Line;
    shape.color = color;
    shape.x1 = x1;
    shape.y1 = y1;
    shape.x2 = x2;
    shape.y2 = y2;
  }

  void renderShapes()
  {
    if (shapeIndex == -1)
    {
      return;
    }

    shapeRenderer.begin();

    for (int i = 0; i <= shapeIndex; i++)
    {
      final Shape shape = shapes[i];

      if (shape != null && shape.color != null)
      {
        shapeRenderer.setColor(shape.color.r, shape.color.g, shape.color.b, shape.color.a);
        shapeRenderer.set(shape.shapeType);

        if (shape.type == Shape.Type.QUAD || shape.type == Shape.Type.FILLED_QUAD)
        {
          shapeRenderer.rect(shape.x, shape.y, shape.width, shape.height);
        }
        else if (shape.type == Shape.Type.LINE)
        {
          shapeRenderer.line(shape.x1, shape.y1, shape.x2, shape.y2);
        }
      }
    }

    shapeRenderer.end();
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
