package com.orbinski.megajump;

public abstract class Loop
{
  final Game gameObj;
  final Controller controller;
  final Renderer renderer;
  final UIRenderer uiRenderer;

  GameInterface game;

  public Loop(final Game game,
              final Controller controller,
              final Renderer renderer,
              final UIRenderer uiRenderer)
  {
    this.gameObj = game;
    this.controller = controller;
    this.renderer = renderer;
    this.uiRenderer = uiRenderer;
  }

  public abstract void update();
}
