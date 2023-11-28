package com.orbinski.megajump;

import java.util.ArrayList;
import java.util.List;

public class Help
{
  public final List<Text> gameTexts = new ArrayList<>();
  public final List<Text> editorTexts = new ArrayList<>();

  public void create()
  {
    // Game

    addGame("Your objective is to get to the exit as fast as possible.");
    addGame("");
    addGame("Press R to restart the level");
    addGame("Press N for next level and P for previous level");
    addGame("Use WASD or arrow keys for aerial controls");
    addGame("Press, drag and release mouse button 1 to jump");
    addGame("Green platforms enable you to jump again");

    // Editor

    addEditor("Press CTRL+E to toggle the level editor on/off");
    addEditor("Press CTRL+Z to toggle edge scrolling on/off");
    addEditor("Press CTRL+9 to zoom in and CTRL+0 to zoom out");
    addEditor("Press CTRL+O to run a custom command. Examples: set gold 10000, set silver 12000, set bronze 15000");
    addEditor("Press CTRL+T to toggle level camera movement X on/off. Press CTRL+G to toggle level camera movement Y on/off.");
    addEditor("Press CTRL+Y to raise and CTRL+H to lower death point");
    addEditor("Press CTRL+U to raise and CTRL+J to lower camera floor");
    addEditor("Press CTRL + right arrow to increase and CTRL + left arrow to decrease entity width");
    addEditor("Press CTRL + up arrow to increase and CTRL + down arrow to decrease entity height");
    addEditor("Press CTRL+X to delete an entity");
    addEditor("Press CTRL+S to save changes");
    addEditor("Press CTRL+R to rename a level");
    addEditor("Press CTRL+N to create a new level");
    addEditor("Press CTRL+1 to add a new platform");
    addEditor("Press CTRL+2 to add a new trampoline");
    addEditor("Press CTRL+3 to add a new decoration");
    addEditor("Press LEFT SHIFT and mouse 1 to move entities");
    addEditor("Press ALT + right arrow to increase and ALT + left arrow to decrease entity width FASTER");
    addEditor("Press ALT + up arrow to increase and ALT + down arrow to decrease entity height FASTER");
    addEditor("Press C to reset the camera to player spawn");
    addEditor("Use WASD or arrow keys to move the camera");
    addEditor("Press N for next level and P for previous level");
  }

  private void addGame(final String text)
  {
    final Text textObj = new Text();
    textObj.text = text;
    textObj.font = Resources.font24White;
    gameTexts.add(textObj);
  }

  private void addEditor(final String text)
  {
    final Text textObj = new Text();
    textObj.text = text;
    textObj.font = Resources.font24White;
    editorTexts.add(textObj);
  }
}
