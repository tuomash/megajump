package com.orbinski.megajump;

abstract class Loop
{
  final Game gameObj;
  final Controller controller;
  final Renderer renderer;
  final UIRenderer uiRenderer;

  GameInterface game;

  Loop(final Game game,
       final Controller controller,
       final Renderer renderer,
       final UIRenderer uiRenderer)
  {
    this.gameObj = game;
    this.controller = controller;
    this.renderer = renderer;
    this.uiRenderer = uiRenderer;
  }

  abstract void update();
}
