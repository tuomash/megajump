package com.orbinski.megajump.multiplayer;

import com.esotericsoftware.kryonet.Client;
import org.apache.commons.collections4.queue.CircularFifoQueue;

public class MClient
{
  public final Client client;
  public final ClientListener listener;
  public final CircularFifoQueue<Request> requests = new CircularFifoQueue<>(100);

  public MClient(final Client client, final MultiplayerGame game)
  {
    this.client = client;
    listener = new ClientListener(game);
    client.addListener(listener);
  }

  public void sendRequests()
  {
    for (int i = 0; i < requests.size(); i++)
    {
      send(requests.get(i));
    }

    requests.clear();
  }

  private void send(final Request request)
  {
    if (request.type == Request.Type.TCP)
    {
      sendTCP(request);
    }
    else
    {
      sendUDP(request);
    }

    request.reset();
  }

  private void sendTCP(final Object request)
  {
    if (isConnected())
    {
      client.sendTCP(request);
    }
  }

  private void sendUDP(final Object request)
  {
    if (isConnected())
    {
      client.sendUDP(request);
    }
  }

  public boolean isConnected()
  {
    return client.isConnected();
  }

  public void shutdown()
  {
    if (isConnected())
    {
      client.stop();
    }

    client.close();
  }
}
