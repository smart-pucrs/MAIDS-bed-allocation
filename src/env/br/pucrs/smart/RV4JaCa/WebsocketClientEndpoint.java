package br.pucrs.smart.RV4JaCa;


import java.net.URI;
import java.util.Map;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.handshake.ServerHandshake;

import com.google.gson.Gson;

public class WebsocketClientEndpoint extends WebSocketClient {
	private Gson gson = new Gson();
	static IArtifact rv4jaca = null;
    
	public static void setListener(IArtifact a) {
		rv4jaca = a;
	}

  public WebsocketClientEndpoint(URI serverUri, Draft draft) {
    super(serverUri, draft);
  }

  public WebsocketClientEndpoint(URI serverURI) {
    super(serverURI);
  }

  public WebsocketClientEndpoint(URI serverUri, Map<String, String> httpHeaders) {
    super(serverUri, httpHeaders);
  }

  @Override
  public void onOpen(ServerHandshake handshakedata) {
    System.out.println("[RV4JaCa] - Opened connection");
  }

  @Override
  public void onMessage(String message) {
    System.out.println("[RV4JaCa] received: " + message);
    MsgSent msg = gson.fromJson(message, MsgSent.class);
    if (msg.getVerdict().equals("false")) {
    	System.out.println("[RV4JaCa] Call the monitor agent");
    	this.rv4jaca.informViolation(msg);
    }
    
  }

  @Override
  public void onClose(int code, String reason, boolean remote) {
    System.out.println(
        "Connection closed by " + (remote ? "remote peer" : "us") + " Code: " + code + " Reason: "
            + reason);
  }

  @Override
  public void onError(Exception ex) {
    ex.printStackTrace();
    // if the error is fatal then onClose will be called additionally
  }
}