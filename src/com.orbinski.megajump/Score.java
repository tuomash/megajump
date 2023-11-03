package com.orbinski.megajump;

import java.io.Serializable;

class Score implements Serializable
{
  private static final long serialVersionUID = 1L;

  String levelTag;
  String trophyStr;
  int bestTimeMilliseconds;

  void save(final Level level)
  {
    levelTag = level.getTag();
    trophyStr = level.trophy.toString().toUpperCase();
    bestTimeMilliseconds = level.bestTimeMillisecondsElapsed;
  }

  void load(final Level level)
  {
    level.cleared = true;
    level.trophy = Level.Trophy.valueOf(trophyStr);
    level.bestTimeMillisecondsElapsed = bestTimeMilliseconds;
  }
}
