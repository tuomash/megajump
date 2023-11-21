package com.orbinski.megajump.multiplayer;

import java.util.Arrays;

import static com.orbinski.megajump.Globals.*;

public class ServerSnapshotResponse extends Response
{
  private PlayerMultiplayerState[] playerStateList = new PlayerMultiplayerState[SERVER_MAX_PLAYER_COUNT];
  transient int playerStateListIndex = 0;

  private Message[] messages = new Message[SERVER_MAX_MESSAGES];
  transient int messagesIndex = 0;

  private String levelTag;
  public int countdownState;
  public boolean levelStarted;

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
    }
  }

  public Message[] getMessages()
  {
    return messages;
  }

  public void addMessage(final Message message)
  {
    if (messagesIndex < messages.length)
    {
      messages[messagesIndex] = message;
      messagesIndex++;
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

  public void reset()
  {
    Arrays.fill(playerStateList, null);
    playerStateListIndex = 0;

    Arrays.fill(messages, null);
    messagesIndex = 0;
  }
}
