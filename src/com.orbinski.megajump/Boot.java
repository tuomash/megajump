package com.orbinski.megajump;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;

public class Boot
{
  public static void main(final String[] args)
  {
    final Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
    config.setMaximized(true);
    config.setTitle("Orndorf's Megajump");
    config.setForegroundFPS(60);
    config.setIdleFPS(30);

    new Lwjgl3Application(new Game(), config)
    {
      @Override
      public void exit()
      {
        super.exit();
      }
    };
  }
}
