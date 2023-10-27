package com.orbinski.megajump;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

class UIRenderer
{
  final Game game;
  final OrthographicCamera camera;
  final Viewport viewport;
  final SpriteBatch spriteBatch;
  final ShapeRenderer shapeRenderer;

  final Text[] texts = new Text[50];
  int textIndex = -1;

  final Shape[] shapes = new Shape[50];
  int shapeIndex = -1;

  UIRenderer(final Game game)
  {
    this.game = game;

    camera = new OrthographicCamera();
    camera.update();

    viewport = new ScreenViewport(camera);

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
    viewport.apply();
    spriteBatch.setProjectionMatrix(viewport.getCamera().combined);
    shapeRenderer.setProjectionMatrix(viewport.getCamera().combined);

    textIndex = -1;
    shapeIndex = -1;

    if (!Globals.hideUI)
    {
      if (game.help)
      {
        addHelpTexts();
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

        addText(UserInterface.retryText);
        addText(UserInterface.nextLevelText);
        addText(UserInterface.trophyLevelText);

        addText(UserInterface.goldRequirementText);
        addText(UserInterface.silverRequirementText);
        addText(UserInterface.bronzeRequirementText);

        addBar(UserInterface.jumpBar);
      }
    }
    else
    {
      if (Globals.watermark)
      {
        addText(UserInterface.waterMarkText);
      }
    }

    renderSprites();
    renderShapes();
  }

  void addHelpTexts()
  {
    for (int i = 0; i < UserInterface.help.texts.size(); i++)
    {
      final Text text = UserInterface.help.texts.get(i);
      addText(text);
    }
  }

  void addText(final Text text)
  {
    textIndex++;

    if (textIndex >= texts.length)
    {
      textIndex = texts.length - 1;
    }

    texts[textIndex] = text;
  }

  void addBar(final Bar bar)
  {
    if (bar != null && bar.visible)
    {
      addFilledQuad(bar.getX(), bar.getY(), bar.barWidth, bar.getHeight(), bar.color);
    }
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
