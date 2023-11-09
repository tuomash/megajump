package com.orbinski.megajump;

abstract class Loop
{
  final Game game;
  final Controller controller;
  final Renderer renderer;
  final UIRenderer uiRenderer;

  Loop(final Game game,
              final Controller controller,
              final Renderer renderer,
              final UIRenderer uiRenderer)
  {
    this.game = game;
    this.controller = controller;
    this.renderer = renderer;
    this.uiRenderer = uiRenderer;
  }

  abstract void update();
}
