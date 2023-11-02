package com.orbinski.megajump;

import java.util.ArrayList;
import java.util.List;

class Help
{
  final List<Text> texts = new ArrayList<>();

  void create()
  {
    add("Your objective is to get to the exit as fast as possible.", 20, 500);

    add("Press R to restart the level", 20, 200);
    add("Press N for next level and P for previous level", 20, 160);
    add("Use WASD or arrow keys for aerial controls", 20, 120);
    add("Press, drag and release mouse button 1 to jump", 20, 80);
    add("Green platforms enable you to jump again", 20, 40);
  }

  private void add(final String text, final int x, final int y)
  {
    final Text textObj = new Text();
    textObj.text = text;
    textObj.font = Resources.font24White;
    textObj.setX(x);
    textObj.setY(y);
    texts.add(textObj);
  }
}
