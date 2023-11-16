package com.orbinski.megajump.multiplayer;

import java.util.Arrays;

import static com.orbinski.megajump.Globals.*;

public class ServerSnapshotResponse extends Response
{
  private int[] addedPlayers = new int[MAX_PLAYER_COUNT];
  transient int addedPlayersIndex = 0;

  private int[] removedPlayers = new int[MAX_PLAYER_COUNT];
  transient int removedPlayersIndex = 0;

  private PlayerMultiplayerState[] playerStateList = new PlayerMultiplayerState[MAX_PLAYER_COUNT];
  transient int playerStateListIndex = 0;

  private String levelTag;
  public int countdownState;

  public int[] getAddedPlayers()
  {
    return addedPlayers;
  }

  public void addPlayer(final int id)
  {
    if (addedPlayersIndex < addedPlayers.length)
    {
      addedPlayers[addedPlayersIndex] = id;
      addedPlayersIndex++;
      dirty = true;
    }
  }

  public int[] getRemovedPlayers()
  {
    return removedPlayers;
  }

  public void removePlayer(final int id)
  {
    if (removedPlayersIndex < removedPlayers.length)
    {
      removedPlayers[removedPlayersIndex] = id;
      removedPlayersIndex++;
      dirty = true;
    }
  }

  public PlayerMultiplayerState[] getPlayerStateList()
  {
    return playerStateList;
  }

  public void addPlayerState(final PlayerMultiplayerState state)
  {
    if (playerStateListIndex < playerStateList.length)
    {
      playerStateList[playerStateListIndex] = state;
      playerStateListIndex++;
      dirty = true;
    }
  }

  public String getLevelTag()
  {
    return levelTag;
  }

  public void setLevelTag(final String levelTag)
  {
    this.levelTag = levelTag;
  }

  @Override
  public void reset()
  {
    super.reset();

    Arrays.fill(addedPlayers, -1);
    addedPlayersIndex = 0;

    Arrays.fill(removedPlayers, -1);
    removedPlayersIndex = 0;

    Arrays.fill(playerStateList, null);
    playerStateListIndex = 0;
  }
}
