package com.rsh.probe.chat.websocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class ChatWebSocketHandler  extends TextWebSocketHandler {
	private static final Logger log = LoggerFactory.getLogger(ChatWebSocketHandler.class);
	List<WebSocketSession> sessions = Collections.synchronizedList(new ArrayList<WebSocketSession>());

	public ChatWebSocketHandler() {
		super();
		log.debug("constructor ChatWebSocketHandler");
	}
 	@Override
 	public void handleTextMessage(WebSocketSession session, TextMessage message)
 			throws InterruptedException, IOException {

		var value = message.getPayload();
		log.debug("received " + value);
		// at this point we ignore receiving messages over web socketx
 	}

	@Override
	protected void handlePongMessage(WebSocketSession session, PongMessage message) throws Exception {
		super.handlePongMessage(session, message);
	}

	@Override
	public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
		super.handleTransportError(session, exception);
	}

	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
		super.afterConnectionClosed(session, status);
		sessions.remove(session);
		log.debug("one session closed, total sessions remaining: " + sessions.size());
	}

	@Override
	public boolean supportsPartialMessages() {
		//return super.supportsPartialMessages();
		return false;
	}

	@Override
 	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
 		// remember all sessions for broadcasting
		log.debug("new web socket connection established.");
		synchronized (sessions) {
			sessions.add(session);
			log.debug("total sessions established: " + sessions.size());
		}
 	}

	public void broadcast(String message) throws IOException {
		 synchronized (sessions) {
			 log.debug("broadcasting '" + message + "' to " + sessions.size() + " sessions.");
			 for(WebSocketSession session : sessions) {
				 session.sendMessage(new TextMessage(message));
			 }
		 }
	}
}

