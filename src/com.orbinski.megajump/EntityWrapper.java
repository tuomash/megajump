package com.orbinski.megajump;

import java.awt.geom.Point2D;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

class EntityWrapper implements Serializable
{
  private static final long serialVersionUID = 1L;

  float x;
  float y;
  float width;
  float height;
  String movement;
  List<Point2D.Float> waypoints;

  EntityWrapper()
  {
    // Empty constructor is needed for deserialization
  }

  EntityWrapper(final Entity entity)
  {
    x = entity.getPosition().x;
    y = entity.getPosition().y;
    width = entity.getWidth();
    height = entity.getHeight();
    movement = entity.movement.toString().toUpperCase();

    if (entity.getWaypoints() != null)
    {
      waypoints = new ArrayList<>();

      for (int i = 0; i < entity.getWaypoints().size(); i++)
      {
        final Point2D.Float waypoint = entity.getWaypoints().get(i);
        final Point2D.Float copy = new Point2D.Float();
        copy.setLocation(waypoint.x, waypoint.y);
        waypoints.add(copy);
      }
    }
  }
}
