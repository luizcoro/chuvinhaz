package com.example.luiz.chuvinhaz.network;

import android.app.Activity;
import android.util.Log;

import com.example.luiz.chuvinhaz.models.User;
import com.example.luiz.chuvinhaz.utils.MessageHandler;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;

/**
 * Created by luiz on 10/25/15.
 */
public class MyWebSocket extends WebSocketClient {

    public interface MyWebSocketListener {
        public void on_open();
        public void on_connection_confirmation_message(String uuid);
        public void on_user_add_message(String uuid);
        public void on_user_remove_message(String uuid);
        public void on_user_update_message(User user);
        public void on_close();
    }

    MyWebSocketListener _listener;

    public MyWebSocket(Activity activity, URI serverUri, Draft draft) {
        super(serverUri, draft);

        _listener = (MyWebSocketListener) activity;
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        _listener.on_open();
    }

    @Override
    public void onMessage(String message) {
        Log.e("websocket", message);
        try {
            JSONObject m = new JSONObject(message);
            if("confirmation".equals(m.getString("action"))) {
                _listener.on_connection_confirmation_message(m.getString("uuid"));
            } else if("add".equals(m.getString("action"))) {
                _listener.on_user_add_message(m.getString("uuid"));
            } else if("remove".equals(m.getString("action"))) {
                _listener.on_user_remove_message(m.getString("uuid"));
            } else if("update".equals(m.getString("action"))) {
                _listener.on_user_update_message(MessageHandler.messageToUser(m));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        _listener.on_close();
    }

    @Override
    public void onError(Exception ex) {

    }
}
