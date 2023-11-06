package com.orbinski.megajump;

import java.util.ArrayList;
import java.util.List;

class Help
{
  final List<Text> gameTexts = new ArrayList<>();
  final List<Text> editorTexts = new ArrayList<>();

  void create()
  {
    // Game

    addGame("Your objective is to get to the exit as fast as possible.", 20, 500);

    addGame("Press R to restart the level", 20, 200);
    addGame("Press N for next level and P for previous level", 20, 160);
    addGame("Use WASD or arrow keys for aerial controls", 20, 120);
    addGame("Press, drag and release mouse button 1 to jump", 20, 80);
    addGame("Green platforms enable you to jump again", 20, 40);

    // Editor

    addEditor("Press CTRL+E to toggle the level editor on/off", 20, 680);
    addEditor("Press CTRL+O to run a custom command. Examples: set gold 10000, set silver 12000, set bronze 15000", 20, 640);
    addEditor("Press CTRL+T to toggle move camera X on/off. Press CTRL+G to toggle move camera Y on/off.", 20, 600);
    addEditor("Press CTRL+Y to raise and CTRL+H to lower death point", 20, 560);
    addEditor("Press CTRL+U to raise and CTRL+J to lower camera floor", 20, 520);
    addEditor("Press CTRL+I to increase and CTRL+K to decrease entity size", 20, 480);
    addEditor("Press CTRL+X to delete an entity", 20, 440);
    addEditor("Press CTRL+S to save changes", 20, 400);
    addEditor("Press CTRL+R to rename a level", 20, 360);
    addEditor("Press CTRL+N to create a new level", 20, 320);
    addEditor("Press CTRL+1 to add a new platform", 20, 280);
    addEditor("Press CTRL+2 to add a new trampoline", 20, 240);
    addEditor("Press CTRL+3 to add a new decoration", 20, 200);
    addEditor("Press LEFT SHIFT and mouse 1 to move entities", 20, 160);
    addEditor("Press C to reset the level state", 20, 120);
    addEditor("Use WASD or arrow keys to move the camera", 20, 80);
    addEditor("Press N for next level and P for previous level", 20, 40);
  }

  private void addGame(final String text, final int x, final int y)
  {
    final Text textObj = new Text();
    textObj.text = text;
    textObj.font = Resources.font24White;
    textObj.setX(x);
    textObj.setY(y);
    gameTexts.add(textObj);
  }

  private void addEditor(final String text, final int x, final int y)
  {
    final Text textObj = new Text();
    textObj.text = text;
    textObj.font = Resources.font24White;
    textObj.setX(x);
    textObj.setY(y);
    editorTexts.add(textObj);
  }
}
