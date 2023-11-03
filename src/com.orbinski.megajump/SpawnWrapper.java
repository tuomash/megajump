package com.orbinski.megajump;

class SpawnWrapper extends EntityWrapper
{
  private static final long serialVersionUID = 1L;

  SpawnWrapper()
  {
    // Empty constructor is needed for deserialization
  }

  SpawnWrapper(final Spawn spawn)
  {
    super(spawn);
  }

  Spawn unwrap()
  {
    return new Spawn(this);
  }
}
