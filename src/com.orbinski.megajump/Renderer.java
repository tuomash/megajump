package com.orbinski.megajump;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
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
  final MShapeRenderer shapeRenderer;

  final Entity[] entities = new Entity[1000];
  int entityIndex = -1;

  Renderer(final Game game)
  {
    this.game = game;

    viewport = new FitViewport(WORLD_WIDTH, WORLD_HEIGHT, game.camera);
    staticViewport = viewport;

    spriteBatch = new SpriteBatch();
    spriteBatch.setProjectionMatrix(viewport.getCamera().combined);

    shapeRenderer = new MShapeRenderer(viewport, 1000);
  }

  void render()
  {
    ScreenUtils.clear(Color.BLACK);

    entityIndex = -1;

    viewport.apply();
    spriteBatch.setProjectionMatrix(viewport.getCamera().combined);
    shapeRenderer.update();

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
    addTeleports();

    addSpawn();
    addLevelBorder();

    renderSprites();
    shapeRenderer.renderShapes();
  }

  void addDoor()
  {
    final Door door = game.level.door;
    addEntity(door);

    if (door.drawBorder || door.selected)
    {
      shapeRenderer.addQuad(door.getBottomLeftCornerX(),
                            door.getBottomLeftCornerY(),
                            door.getWidth(),
                            door.getHeight(),
                            Color.WHITE);
    }
  }

  void addBlocks()
  {
    for (int i = 0; i < game.level.blocks.size(); i++)
    {
      final Block block = game.level.blocks.get(i);
      addEntity(block);

      if (block.drawBorder || block.selected)
      {
        shapeRenderer.addQuad(block.getBottomLeftCornerX(),
                              block.getBottomLeftCornerY(),
                              block.getWidth(),
                              block.getHeight(),
                              Color.WHITE);
      }
    }
  }

  void addDecorations()
  {
    for (int i = 0; i < game.level.decorations.size(); i++)
    {
      final Decoration decoration = game.level.decorations.get(i);
      addEntity(game.level.decorations.get(i));

      if (decoration.drawBorder || decoration.selected)
      {
        shapeRenderer.addQuad(decoration.getBottomLeftCornerX(),
                              decoration.getBottomLeftCornerY(),
                              decoration.getWidth(),
                              decoration.getHeight(),
                              Color.WHITE);
      }
    }
  }

  void addTrampolines()
  {
    for (int i = 0; i < game.level.trampolines.size(); i++)
    {
      final Trampoline trampoline = game.level.trampolines.get(i);

      shapeRenderer.addFilledQuad(trampoline.getBottomLeftCornerX(),
                                  trampoline.getBottomLeftCornerY(),
                                  trampoline.getWidth(),
                                  trampoline.getHeight(),
                                  Color.BLUE);

      if (trampoline.drawBorder || trampoline.selected)
      {
        shapeRenderer.addQuad(trampoline.getBottomLeftCornerX(),
                              trampoline.getBottomLeftCornerY(),
                              trampoline.getWidth(),
                              trampoline.getHeight(),
                              Color.WHITE);
      }
    }
  }

  void addPlatforms()
  {
    for (int i = 0; i < game.level.platforms.size(); i++)
    {
      final Platform platform = game.level.platforms.get(i);

      shapeRenderer.addFilledQuad(platform.getBottomLeftCornerX(),
                                  platform.getBottomLeftCornerY(),
                                  platform.getWidth(),
                                  platform.getHeight(),
                                  UserInterface.DARK_GREEN);

      if (platform.drawBorder || platform.selected)
      {
        shapeRenderer.addQuad(platform.getBottomLeftCornerX(),
                              platform.getBottomLeftCornerY(),
                              platform.getWidth(),
                              platform.getHeight(),
                              Color.WHITE);
      }
    }
  }

  void addTeleports()
  {
    for (int i = 0; i < game.level.teleports.size(); i++)
    {
      final Teleport teleport = game.level.teleports.get(i);

      shapeRenderer.addFilledQuad(teleport.getBottomLeftCornerX(),
                                  teleport.getBottomLeftCornerY(),
                                  teleport.getWidth(),
                                  teleport.getHeight(),
                                  Color.MAGENTA);

      shapeRenderer.addFilledQuad(teleport.targetBottomLeftCorner.x,
                                  teleport.targetBottomLeftCorner.y,
                                  teleport.getWidth(),
                                  teleport.getHeight(),
                                  Color.MAGENTA);

      if (teleport.drawBorder || teleport.selected)
      {
        shapeRenderer.addQuad(teleport.getBottomLeftCornerX(),
                              teleport.getBottomLeftCornerY(),
                              teleport.getWidth(),
                              teleport.getHeight(),
                              Color.WHITE);
      }
    }
  }

  void addSpawn()
  {
    if (game.levelEditor.active)
    {
      final Spawn spawn = game.levelEditor.level.spawn;

      shapeRenderer.addFilledQuad(spawn.getBottomLeftCornerX(),
                                  spawn.getBottomLeftCornerY(),
                                  spawn.getWidth(),
                                  spawn.getHeight(),
                                  Color.YELLOW);

      if (spawn.drawBorder || spawn.selected)
      {
        shapeRenderer.addQuad(spawn.getBottomLeftCornerX(),
                              spawn.getBottomLeftCornerY(),
                              spawn.getWidth(),
                              spawn.getHeight(),
                              Color.WHITE);
      }
    }
  }

  void addLevelBorder()
  {
    if (game.levelEditor.active)
    {
      shapeRenderer.addQuad(0.0f - Level.MAX_DIMENSION_OFFSET,
                            0.0f - Level.MAX_DIMENSION_OFFSET,
                            Level.MAX_DIMENSION,
                            Level.MAX_DIMENSION,
                            Color.WHITE);
    }
  }

  void addPlayer()
  {
    if (game.levelEditor.active)
    {
      return;
    }

    final Player player = game.player;
    addEntity(player);

    if (player.drawBorder || player.selected)
    {
      shapeRenderer.addQuad(player.getBottomLeftCornerX(),
                            player.getBottomLeftCornerY(),
                            player.getWidth(),
                            player.getHeight(),
                            Color.WHITE);
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

      shapeRenderer.addQuad(player.topSide.x,
                            player.topSide.y,
                            player.topSide.width,
                            player.topSide.height,
                            Color.YELLOW);

      shapeRenderer.addQuad(player.leftSide.x,
                            player.leftSide.y,
                            player.leftSide.width,
                            player.leftSide.height,
                            Color.YELLOW);

      shapeRenderer.addQuad(player.rightSide.x,
                            player.rightSide.y,
                            player.rightSide.width,
                            player.rightSide.height,
                            Color.YELLOW);

      shapeRenderer.addQuad(player.bottomSide.x,
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
          shapeRenderer.addQuad(rectangle.x,
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

  void dispose()
  {
    if (spriteBatch != null)
    {
      spriteBatch.dispose();
    }

    shapeRenderer.dispose();
  }

  public static void unproject(final Vector3 screen)
  {
    if (staticViewport != null)
    {
      staticViewport.unproject(screen);
    }
  }
}
