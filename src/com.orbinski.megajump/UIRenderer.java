package com.orbinski.megajump;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

class UIRenderer
{
  final Game game;
  final OrthographicCamera camera;
  final Viewport viewport;
  final SpriteBatch spriteBatch;
  final MShapeRenderer shapeRenderer;

  final Text[] texts = new Text[50];
  int textIndex = -1;

  UIRenderer(final Game game)
  {
    this.game = game;

    camera = new OrthographicCamera();
    camera.update();

    viewport = new ScreenViewport(camera);

    spriteBatch = new SpriteBatch();
    spriteBatch.setProjectionMatrix(viewport.getCamera().combined);

    shapeRenderer = new MShapeRenderer(viewport, 50);
  }

  void render()
  {
    viewport.apply();
    spriteBatch.setProjectionMatrix(viewport.getCamera().combined);
    shapeRenderer.update();

    textIndex = -1;

    if (game.help)
    {
      addGameHelpTexts();
    }
    else if (game.levelEditor.help)
    {
      addEditorHelpTexts();
    }
    else if (!Globals.hideUI)
    {
      if (game.levelEditor.active)
      {
        addText(UserInterface.levelNameText);
        addText(UserInterface.unsavedChangesText);
        addText(UserInterface.newLevelNameText);
        addText(UserInterface.commandText);
        addText(UserInterface.moveCameraXText);
        addText(UserInterface.moveCameraYText);
      }
      else if (game.isMultiplayer())
      {
        addText(UserInterface.levelNameText);
        addBar(UserInterface.jumpBar);
      }
      else
      {
        if (Globals.watermark)
        {
          addText(UserInterface.waterMarkText);
        }

        if (game.paused)
        {
          addText(UserInterface.pausedText);
        }

        addText(UserInterface.levelNameText);
        addText(UserInterface.elapsedTimeText);
        addText(UserInterface.bestTimeText);
        addText(UserInterface.speedText);

        addText(UserInterface.retryText);
        addText(UserInterface.nextLevelText);
        addText(UserInterface.trophyLevelText);

        addText(UserInterface.goldRequirementText);
        addText(UserInterface.silverRequirementText);
        addText(UserInterface.bronzeRequirementText);

        addBar(UserInterface.jumpBar);
      }
    }
    else if (Globals.watermark)
    {
      addText(UserInterface.waterMarkText);
    }

    // Draw a red border to signal that level editor is active
    if (game.levelEditor.active)
    {
      shapeRenderer.addQuad(0.0f + 1,
              0.0f + 1,
              Globals.screenWidth - 1,
              Globals.screenHeight - 1,
              Color.RED);
      shapeRenderer.addQuad(0.0f + 2,
              0.0f + 2,
              Globals.screenWidth - 2,
              Globals.screenHeight - 2,
              Color.RED);
      shapeRenderer.addQuad(0.0f + 3,
              0.0f + 3,
              Globals.screenWidth - 3,
              Globals.screenHeight - 3,
              Color.RED);
    }

    renderSprites();
    shapeRenderer.renderShapes();
  }

  void addGameHelpTexts()
  {
    for (int i = 0; i < UserInterface.help.gameTexts.size(); i++)
    {
      final Text text = UserInterface.help.gameTexts.get(i);
      addText(text);
    }
  }

  void addEditorHelpTexts()
  {
    for (int i = 0; i < UserInterface.help.editorTexts.size(); i++)
    {
      final Text text = UserInterface.help.editorTexts.get(i);
      addText(text);
    }
  }

  void addText(final Text text)
  {
    if (text != null && text.visible)
    {
      textIndex++;

      if (textIndex >= texts.length)
      {
        textIndex = texts.length - 1;
      }

      texts[textIndex] = text;
    }
  }

  void addBar(final Bar bar)
  {
    if (bar != null && bar.visible)
    {
      shapeRenderer.addFilledQuad(bar.getX(), bar.getY(), bar.barWidth, bar.getHeight(), bar.color);
    }
  }

  void renderSprites()
  {
    if (textIndex == -1)
    {
      return;
    }

    spriteBatch.begin();

    for (int i = 0; i <= textIndex; i++)
    {
      final Text text =  texts[i];

      if (text != null && text.visible && text.font != null && text.text != null && !text.text.isEmpty())
      {
        text.font.draw(spriteBatch,
                       text.text,
                       text.getX(),
                       text.getY());

      }
    }

    spriteBatch.end();
  }

  void renderUIElement(final UIElement element)
  {
    if (element != null)
    {
      if (element.backgroundColor != null)
      {
        /*
        renderHudFilledQuad(element.getX(),
                            element.getY(),
                            element.getWidth(),
                            element.getHeight(),
                            element.backgroundColor);
         */
      }

      if (element.texture != null)
      {
        spriteBatch.begin();
        spriteBatch.draw(element.texture, element.getX(), element.getY(), element.getWidth(), element.getHeight());
        spriteBatch.end();
      }

      if (element.getOverlay() != null)
      {
        // renderOverlay(element.getOverlay());
      }
    }
  }
}
