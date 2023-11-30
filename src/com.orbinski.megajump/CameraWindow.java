package com.orbinski.megajump;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;

public class CameraWindow
{
  private final float width = 140.0f;
  private final float widthOffset = width / 2.0f;
  private final float velocity = 10.0f;

  public Vector2 positionLeft = new Vector2();
  public Vector2 positionRight = new Vector2();
  public boolean render = true;

  public void setToLeft(final Player player, final OrthographicCamera camera)
  {
    positionLeft.x = player.getPosition().x - widthOffset;
    positionLeft.y = player.getPosition().y;

    positionRight.x = player.getPosition().x + widthOffset;
    positionRight.y = player.getPosition().y;

    player.setPosition(positionLeft);

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
    final float cameraDistance = delta * (player.velocityX * 1.5f);
    positionLeft.x = positionLeft.x + cameraDistance;
    positionRight.x = positionRight.x + cameraDistance;

    if (player.getPosition().x < positionLeft.x)
    {
      final float distance = delta * player.velocityX;
      camera.position.x = camera.position.x + distance;
      positionRight.x = positionRight.x + distance;
    }
    else if (player.getPosition().x > positionRight.x)
    {
      final float distance = delta * player.velocityX;
      camera.position.x = camera.position.x + distance;
      positionLeft.x = positionLeft.x + distance;
      positionRight.x = positionRight.x + distance;
    }
  }

  public void moveY(final Player player, final OrthographicCamera camera, final float delta)
  {
    final float distance = delta * player.velocityY;
    camera.position.y = camera.position.y + distance;
    positionLeft.y = positionLeft.y + distance;
    positionRight.y = positionRight.y + distance;
  }
}
