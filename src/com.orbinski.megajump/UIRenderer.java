package com.orbinski.megajump;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.io.File;

class UIRenderer
{
  static BitmapFont font24White;

  final Game game;
  final OrthographicCamera camera;
  final Viewport viewport;
  final ShapeRenderer shapeRenderer;
  final SpriteBatch spriteBatch;

  UIRenderer(final Game game)
  {
    this.game = game;

    camera = new OrthographicCamera();
    camera.update();

    viewport = new ScreenViewport(camera);

    spriteBatch = new SpriteBatch();
    spriteBatch.setProjectionMatrix(camera.combined);

    shapeRenderer = new ShapeRenderer();
    shapeRenderer.setProjectionMatrix(camera.combined);
    shapeRenderer.setAutoShapeType(true);

    font24White = new BitmapFont(true);
    font24White.setColor(Color.RED);

    final File file = new File(System.getProperty("user.dir")
                        + File.separator
                        + "lunchds.ttf");
    final FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal(file.getAbsolutePath()));
    final FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
    parameter.size = 24;
    parameter.color = com.badlogic.gdx.graphics.Color.WHITE;
    font24White = generator.generateFont(parameter);
  }

  void render()
  {
    viewport.apply();
    shapeRenderer.setProjectionMatrix(camera.combined);
    spriteBatch.setProjectionMatrix(camera.combined);

    renderText(UserInterface.levelNameText);
    renderText(UserInterface.elapsedTimeText);
    renderText(UserInterface.bestTimeText);

    renderText(UserInterface.retryText);
    renderText(UserInterface.nextLevelText);
  }

  void renderText(final Text text)
  {
    if (text != null && text.visible && text.font != null && text.text != null && !text.text.isEmpty())
    {
      renderUIElement(text);
      spriteBatch.begin();
      text.font.draw(spriteBatch,
                     text.text,
                     text.getX(),
                     text.getY());
      spriteBatch.end();
    }
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
