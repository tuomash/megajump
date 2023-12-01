package com.orbinski.megajump;

public class SpawnWrapper extends EntityWrapper
{
  private static final long serialVersionUID = 1L;

  public SpawnWrapper()
  {
    // Empty constructor is needed for deserialization
  }

  public SpawnWrapper(final Spawn spawn)
  {
    super(spawn);
  }

  public Spawn unwrap()
  {
    return new Spawn(this);
  }
}
