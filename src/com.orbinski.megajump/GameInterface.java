package com.orbinski.megajump;

import com.badlogic.gdx.math.Vector2;

import java.util.List;

public interface GameInterface
{
  // Loop specific functions

  void updatePhysics(float delta);

  void update(float delta);

  void handleMultiplayer();

  // Game specific functions

  boolean isHelp();

  void toggleHelp();

  boolean isPaused();

  void togglePaused();

  CameraState getCameraState();

  Player getPlayer();

  List<Player> getPlayers();

  Level getLevel();

  boolean isLevelEditor();

  void toggleLevelEditor();

  LevelEditor getLevelEditor();

  boolean isMultiplayer();

  void resetPlayerToStart();

  void selectPreviousLevel();

  void selectNextLevel();

  boolean isTargeting();

  void setTargeting(boolean targeting);

  void updateAssistantPosition(Vector2 mouse);

  void jump();

  void moveUp();

  void moveLeft();

  void moveRight();

  void moveDown();

  void createNewLevel();

  // Multiplayer specific functions

  void connectToServer();

  void disconnectFromServer();
}