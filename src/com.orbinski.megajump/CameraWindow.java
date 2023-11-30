package com.orbinski.megajump;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class CameraWindow
{
  private final float width = 140.0f;
  private final float widthOffset = width / 2.0f;

  public Vector2 positionLeft = new Vector2();
  public Vector2 positionRight = new Vector2();
  public boolean render = true;

  public void setToLeftSide(final Player player, final OrthographicCamera camera)
  {
    positionLeft.x = player.getPosition().x;
    positionLeft.y = player.getPosition().y;

    positionRight.x = player.getPosition().x + width;
    positionRight.y = player.getPosition().y;

    camera.position.x = player.getPosition().x + widthOffset;
    camera.position.y = player.getPosition().y;
  }

  public void center(final Player player, final OrthographicCamera camera)
  {
    camera.position.x = player.getPosition().x;
    camera.position.y = player.getPosition().y;

    positionLeft.x = player.getPosition().x - widthOffset;
    positionLeft.y = player.getPosition().y;

    positionRight.x = player.getPosition().x + widthOffset;
    positionRight.y = player.getPosition().y;
  }

  public void moveX(final Player player, final OrthographicCamera camera, final float delta)
  {
    final float leftSideCameraTarget = player.getPosition().x - widthOffset;
    positionLeft.x = leftSideCameraTarget;
    final float rightSideCameraTarget = player.getPosition().x + widthOffset;
    positionRight.x = rightSideCameraTarget;
    final Vector3 newPosition = new Vector3(camera.position);

    if (player.velocityX > 0.0f)
    {
      newPosition.x = rightSideCameraTarget;
    }
    else if (player.velocityX < 0.0f)
    {
      newPosition.x = leftSideCameraTarget;
    }

    camera.position.lerp(newPosition, 0.015f);
  }

  public void moveY(final Player player, final OrthographicCamera camera, final float delta)
  {
    final float distance = delta * player.velocityY;
    camera.position.y = camera.position.y + distance;
    positionLeft.y = positionLeft.y + distance;
    positionRight.y = positionRight.y + distance;
  }
}
