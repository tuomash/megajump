package com.orbinski.megajump.multiplayer;

public abstract class Request
{
  public enum Type
  {
    TCP,
    UDP
  }

  transient Type type = Type.UDP;
  transient boolean dirty;

  public void reset()
  {
    dirty = false;
  }
}
