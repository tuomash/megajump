package com.orbinski.megajump;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;

class Boot
{
  public static void main(final String[] args)
  {
    final Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
    config.setMaximized(true);
    config.setTitle("Megajumpers");
    config.setForegroundFPS(60);
    config.setIdleFPS(30);

    for (int i = 0; i < args.length; i++)
    {
      if (args[i].equalsIgnoreCase("debug"))
      {
        Globals.debug = true;
      }
      else if (args[i].equalsIgnoreCase("watermark"))
      {
        Globals.watermark = true;
      }
      else if (args[i].equalsIgnoreCase("hide_ui"))
      {
        Globals.hideUI = true;
      }
      else if (args[i].equalsIgnoreCase("editor"))
      {
        Globals.editor = true;
      }
      else if (args[i].equalsIgnoreCase("mute"))
      {
        Audio.mute = true;
      }
      else if (args[i].contains("level"))
      {
        final String[] parts = args[i].split("=");

        if (parts.length == 2)
        {
          Globals.levelTag = parts[1];
        }
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
