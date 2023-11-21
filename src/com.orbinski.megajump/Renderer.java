package com.orbinski.megajump;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import static com.orbinski.megajump.Globals.WORLD_HEIGHT;
import static com.orbinski.megajump.Globals.WORLD_WIDTH;

public class Renderer
{
  private static Viewport staticViewport;

  final Game gameObj;
  final Viewport viewport;
  final SpriteBatch spriteBatch;
  final MShapeRenderer shapeRenderer;

  private GameInterface game;

  final Entity[] entities = new Entity[1000];
  int entityIndex = -1;

  boolean interpolation = false;
  float interpolationAlpha = 0.0f;

  Renderer(final Game game)
  {
    this.gameObj = game;

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

    game = gameObj.getGame();

    if (game.isHelp() || (game.getLevelEditor() != null && game.getLevelEditor().help))
    {
      return;
    }

    addExit();
    addBlocks();
    addDecorations();
    addTrampolines();
    addPlatforms();
    addPlayers();
    addTeleports();

    addSpawn();
    addLevelBorder();
    addLevelCameraFloor();
    addLevelDeathPoint();

    renderSprites();
    shapeRenderer.renderShapes();
  }

  void addExit()
  {
    addEntity(game.getLevel().exit);
  }

  void addBlocks()
  {
    for (int i = 0; i < game.getLevel().blocks.size(); i++)
    {
      addEntity(game.getLevel().blocks.get(i));
    }
  }

  void addDecorations()
  {
    for (int i = 0; i < game.getLevel().decorations.size(); i++)
    {
      addEntity(game.getLevel().decorations.get(i));
    }
  }

  void addTrampolines()
  {
    // TODO: use addEntity() when texture is available

    for (int i = 0; i < game.getLevel().trampolines.size(); i++)
    {
      final Trampoline trampoline = game.getLevel().trampolines.get(i);

      shapeRenderer.addFilledQuad(trampoline.getBottomLeftCornerPosition().x,
                                  trampoline.getBottomLeftCornerPosition().y,
                                  trampoline.getWidth(),
                                  trampoline.getHeight(),
                                  Color.BLUE);

      if (trampoline.drawBorder || trampoline.selected)
      {
        shapeRenderer.addQuad(trampoline.getBottomLeftCornerPosition().x,
                              trampoline.getBottomLeftCornerPosition().y,
                              trampoline.getWidth(),
                              trampoline.getHeight(),
                              Color.WHITE);
      }
    }
  }

  void addPlatforms()
  {
    // TODO: use addEntity() when texture is available

    for (int i = 0; i < game.getLevel().platforms.size(); i++)
    {
      final Platform platform = game.getLevel().platforms.get(i);

      shapeRenderer.addFilledQuad(platform.getBottomLeftCornerPosition().x,
                                  platform.getBottomLeftCornerPosition().y,
                                  platform.getWidth(),
                                  platform.getHeight(),
                                  UserInterface.DARK_GREEN);

      if (platform.drawBorder || platform.selected)
      {
        shapeRenderer.addQuad(platform.getBottomLeftCornerPosition().x,
                              platform.getBottomLeftCornerPosition().y,
                              platform.getWidth(),
                              platform.getHeight(),
                              Color.WHITE);
      }
    }
  }

  void addTeleports()
  {
    // TODO: use addEntity() when texture is available

    for (int i = 0; i < game.getLevel().teleports.size(); i++)
    {
      final Teleport teleport = game.getLevel().teleports.get(i);

      shapeRenderer.addFilledQuad(teleport.getBottomLeftCornerPosition().x,
                                  teleport.getBottomLeftCornerPosition().y,
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
        shapeRenderer.addQuad(teleport.getBottomLeftCornerPosition().x,
                              teleport.getBottomLeftCornerPosition().y,
                              teleport.getWidth(),
                              teleport.getHeight(),
                              Color.WHITE);
      }
    }
  }

  void addSpawn()
  {
    // TODO: use addEntity() when texture is available

    if (game.isLevelEditor())
    {
      final Spawn spawn = game.getLevelEditor().level.spawn;

      shapeRenderer.addFilledQuad(spawn.getBottomLeftCornerPosition().x,
                                  spawn.getBottomLeftCornerPosition().y,
                                  spawn.getWidth(),
                                  spawn.getHeight(),
                                  Color.YELLOW);

      if (spawn.drawBorder || spawn.selected)
      {
        shapeRenderer.addQuad(spawn.getBottomLeftCornerPosition().x,
                              spawn.getBottomLeftCornerPosition().y,
                              spawn.getWidth(),
                              spawn.getHeight(),
                              Color.WHITE);
      }
    }
  }

  void addLevelBorder()
  {
    if (game.isLevelEditor())
    {
      shapeRenderer.addQuad(0.0f - Level.MAX_DIMENSION_OFFSET,
                            0.0f - Level.MAX_DIMENSION_OFFSET,
                            Level.MAX_DIMENSION,
                            Level.MAX_DIMENSION,
                            Color.WHITE);
    }
  }

  void addLevelCameraFloor()
  {
    if (game.isLevelEditor())
    {
      final Level level = game.getLevelEditor().level;
      shapeRenderer.addLine(level.cameraFloor.x - Level.MAX_DIMENSION,
                            level.cameraFloor.y,
                            level.cameraFloor.x + Level.MAX_DIMENSION,
                            level.cameraFloor.y,
                            Color.YELLOW);
    }
  }

  void addLevelDeathPoint()
  {
    if (game.isLevelEditor())
    {
      final Level level = game.getLevelEditor().level;
      shapeRenderer.addLine(level.deathPoint.x - Level.MAX_DIMENSION,
                            level.deathPoint.y,
                            level.deathPoint.x + Level.MAX_DIMENSION,
                            level.deathPoint.y,
                            Color.MAGENTA);
    }
  }

  void addPlayers()
  {
    if (game.isLevelEditor())
    {
      return;
    }

    for (int i = 0; i < game.getPlayers().size(); i++)
    {
      final Player player = game.getPlayers().get(i);
      addPlayer(player);
    }
  }

  private void addPlayer(final Player player)
  {
    addEntity(player);

    if (player.drawCollisions)
    {
      shapeRenderer.addQuad(player.collisionBox.x,
                            player.collisionBox.y,
                            player.collisionBox.width,
                            player.collisionBox.height,
                            Color.YELLOW);

      /*
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
       */
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

      for (int z = 1; z < 120; z++)
      {
        if (z % 3 == 0)
        {
          final Rectangle rectangle = assistant.jumpCurve[z];
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

      if (entity.drawBorder || entity.selected)
      {
        Vector2 position = entity.getBottomLeftCornerPosition();

        if (entity.interpolate())
        {
          position = entity.getPreviousBottomLeftCornerPosition().lerp(entity.getBottomLeftCornerPosition(),
                                                                       interpolationAlpha);
        }

        shapeRenderer.addQuad(position.x,
                              position.y,
                              entity.getWidth(),
                              entity.getHeight(),
                              Color.WHITE);
      }
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
        Vector2 position = entity.getBottomLeftCornerPosition();

        if (entity.interpolate())
        {
          position = entity.getPreviousBottomLeftCornerPosition().lerp(entity.getBottomLeftCornerPosition(),
                                                                       interpolationAlpha);
        }

        spriteBatch.draw(entity.texture.texture,
                         position.x,
                         position.y,
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

  public static void project(final Vector2 world)
  {
    if (staticViewport != null)
    {
      staticViewport.project(world);
    }
  }

  public static void unproject(final Vector2 screen)
  {
    if (staticViewport != null)
    {
      staticViewport.unproject(screen);
    }
  }
}
