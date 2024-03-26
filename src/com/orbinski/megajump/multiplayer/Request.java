package com.orbinski.megajump.multiplayer;

public abstract class Request
{
  public enum Type
  {
    TCP,
    UDP
  }

  transient Type type = Type.UDP;
}
