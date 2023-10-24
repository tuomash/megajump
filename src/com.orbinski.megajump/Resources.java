package com.orbinski.megajump;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

class Resources
{
  static MTexture dwarfLeft;
  static MTexture dwarfRight;
  static MTexture door;
  static MTexture block;
  static MTexture candle;
  static MTexture playerJumpRightAscend;
  static MTexture playerJumpRightMax;
  static MTexture playerJumpRightDescend;

  static List<MTexture> playerIdleRight;

  static BitmapFont font24White;
  static BitmapFont font18White;
  static BitmapFont font14White;

  static void load()
  {
    dwarfLeft = loadTexture("dwarf-left.png");
    dwarfRight = loadTexture("dwarf-right.png");
    door = loadTexture("door.png");
    block = loadTexture("block.png");
    candle = loadTexture("candle.png");

    {
      playerIdleRight = new ArrayList<>();

      final MTexture root = loadTexture("player-idle-right.png");

      for (int i = 0; i < 10; i++)
      {
        final MTexture texture = root.copy();
        texture.srcX = i * 48;
        texture.srcWidth = 48;
        texture.srcHeight = 48;
        playerIdleRight.add(texture);
      }
    }

    {
      final List<MTexture> textures = new ArrayList<>();
      final MTexture root = loadTexture("player-jump-right.png");

      for (int i = 0; i < 3; i++)
      {
        final MTexture texture = root.copy();
        texture.srcX = i * 48;
        texture.srcWidth = 48;
        texture.srcHeight = 48;
        textures.add(texture);
      }

      playerJumpRightAscend = textures.get(0);
      playerJumpRightMax = textures.get(1);
      playerJumpRightDescend = textures.get(2);
    }

    font24White = generateFont("lunchds.ttf", 24, Color.WHITE);
    font18White = generateFont("lunchds.ttf", 18, Color.WHITE);
    font14White = generateFont("lunchds.ttf", 14, Color.WHITE);
  }

  private static MTexture loadTexture(final String fileName)
  {
    final File file = new File(System.getProperty("user.dir") + File.separator + "graphics" + File.separator + fileName);
    final Texture texture = new Texture(Gdx.files.absolute(file.getAbsolutePath()));
    final MTexture mTexture = new MTexture();
    mTexture.texture = texture;
    mTexture.srcWidth = texture.getWidth();
    mTexture.srcHeight = texture.getHeight();
    mTexture.srcX = 0;
    mTexture.srcY = 0;
    return mTexture;
  }

  private static BitmapFont generateFont(final String fontFileName, final int size, final Color color)
  {
    final File file = new File(System.getProperty("user.dir")
                        + File.separator
                        + "graphics"
                        + File.separator
                        + fontFileName);
    final FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal(file.getAbsolutePath()));
    final FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
    parameter.size = size;
    parameter.color = color;
    return generator.generateFont(parameter);
  }
}
