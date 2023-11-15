package com.orbinski.megajump.multiplayer;

public abstract class Request
{
  public enum Type
  {
    TCP,
    UDP
  }

  Type type = Type.UDP;
  boolean dirty;

  public void reset()
  {
    dirty = false;
  }
}
