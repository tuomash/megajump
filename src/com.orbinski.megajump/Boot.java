package com.orbinski.megajump;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;

class Boot
{
  public static void main(final String[] args)
  {
    final Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
    config.setMaximized(true);
    config.setTitle("Orndorf's Megajump");
    config.setForegroundFPS(60);
    config.setIdleFPS(30);

    for (int i = 0; i < args.length; i++)
    {
      if (args[i].equalsIgnoreCase("debug"))
      {
        Globals.debug = true;
      }
    }

    try
    {
      new Lwjgl3Application(new Listener(), config)
      {
        @Override
        public void exit()
        {
          super.exit();
        }
      };
    }
    catch (final Exception e)
    {
      e.printStackTrace();
    }
  }
}
