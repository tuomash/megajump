package com.orbinski.megajump.multiplayer;

public abstract class Response
{
  public enum Type
  {
    TCP,
    UDP
  }

  transient Response.Type type = Response.Type.UDP;
  transient boolean dirty;

  public void reset()
  {
    dirty = false;
  }
}
