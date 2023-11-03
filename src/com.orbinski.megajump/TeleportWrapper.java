package com.orbinski.megajump;

import java.awt.geom.Point2D;

class TeleportWrapper extends EntityWrapper
{
  private static final long serialVersionUID = 1L;

  Point2D.Float target;

  TeleportWrapper()
  {
    // Empty constructor is needed for deserialization
  }

  TeleportWrapper(final Teleport teleport)
  {
    super(teleport);

    target = new Point2D.Float();
    target.setLocation(teleport.target.x, teleport.target.y);
  }

  Teleport unwrap()
  {
    return new Teleport(this);
  }
}
