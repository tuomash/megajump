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
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }
}
