package com.orbinski.megajump;

class SpawnWrapper extends EntityWrapper
{
  int version = 1;

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
