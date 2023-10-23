package com.orbinski.megajump;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

import java.io.File;

class Resources
{
  static Texture dwarfLeft;
  static Texture dwarfRight;
  static Texture door;
  static Texture block;
  static Texture candle;

  static BitmapFont font24White;
  static BitmapFont font18White;
  static BitmapFont font14White;

  static void load()
  {
    File file = new File(
        System.getProperty("user.dir") + File.separator + "graphics" + File.separator + "dwarf-left.png");
    dwarfLeft = new Texture(Gdx.files.absolute(file.getAbsolutePath()));

    file = new File(System.getProperty("user.dir") + File.separator + "graphics" + File.separator + "dwarf-right.png");
    dwarfRight = new Texture(Gdx.files.absolute(file.getAbsolutePath()));

    file = new File(System.getProperty("user.dir") + File.separator + "graphics" + File.separator + "door.png");
    door = new Texture(Gdx.files.absolute(file.getAbsolutePath()));

    file = new File(System.getProperty("user.dir") + File.separator + "graphics" + File.separator + "block.png");
    block = new Texture(Gdx.files.absolute(file.getAbsolutePath()));

    file = new File(System.getProperty("user.dir") + File.separator + "graphics" + File.separator + "candle.png");
    candle = new Texture(Gdx.files.absolute(file.getAbsolutePath()));

    font24White = generateFont("lunchds.ttf", 24, Color.WHITE);
    font18White = generateFont("lunchds.ttf", 18, Color.WHITE);
    font14White = generateFont("lunchds.ttf", 14, Color.WHITE);
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
