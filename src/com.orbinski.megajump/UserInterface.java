package com.orbinski.megajump;

class UserInterface
{
  final static Text levelNameText = new Text();

  static void load()
  {
    levelNameText.setX(20);
    levelNameText.setY(50);
    levelNameText.font = UIRenderer.font24White;
  }

  static void updateLevelName(final String levelName)
  {
    levelNameText.text = "Level: " + levelName;
  }
}
