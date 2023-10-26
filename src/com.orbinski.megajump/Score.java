package com.orbinski.megajump;

import java.io.Serializable;

class Score implements Serializable
{
  int version = 1;

  String levelTag;
  String trophyStr;
  int bestTimeMilliseconds;

  void save(final Level level)
  {
    levelTag = level.tag;
    trophyStr = level.trophy.toString().toUpperCase();
    bestTimeMilliseconds = level.bestTimeMillisecondsElapsed;
  }

  void load(final Level level)
  {
    if (version == 1)
    {
      level.trophy = Level.Trophy.valueOf(trophyStr);
      level.bestTimeMillisecondsElapsed = bestTimeMilliseconds;
    }
  }
}
