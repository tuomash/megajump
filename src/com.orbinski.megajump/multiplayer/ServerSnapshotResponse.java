package com.orbinski.megajump.multiplayer;

import java.util.Arrays;

import static com.orbinski.megajump.Globals.*;

public class ServerSnapshotResponse
{
  private int[] addedPlayers = new int[MAX_PLAYER_COUNT];
  transient int addedPlayersIndex = 0;

  private int[] removedPlayers = new int[MAX_PLAYER_COUNT];
  transient int removedPlayersIndex = 0;

  private PlayerData[] playerDataList = new PlayerData[MAX_PLAYER_COUNT];
  transient int playerDataListIndex = 0;

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
    }
  }

  public PlayerData[] getPlayerDataList()
  {
    return playerDataList;
  }

  public void addPlayerData(final PlayerData data)
  {
    if (playerDataListIndex < playerDataList.length)
    {
      playerDataList[playerDataListIndex] = data;
      playerDataListIndex++;
    }
  }

  public void reset()
  {
    Arrays.fill(addedPlayers, -1);
    addedPlayersIndex = 0;

    Arrays.fill(removedPlayers, -1);
    removedPlayersIndex = 0;

    Arrays.fill(playerDataList, null);
    playerDataListIndex = 0;
  }
}
