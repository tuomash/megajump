package com.orbinski.megajump.multiplayer;

public abstract class Response
{
  public enum Type
  {
    TCP,
    UDP
  }

  Response.Type type = Response.Type.UDP;
  boolean dirty;

  public void reset()
  {
    dirty = false;
  }
}
