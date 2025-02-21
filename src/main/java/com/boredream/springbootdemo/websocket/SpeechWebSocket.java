package com.boredream.springbootdemo.websocket;

import com.boredream.springbootdemo.service.SpeechService;
import io.reactivex.Flowable;
import io.reactivex.disposables.Disposable;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@ServerEndpoint("/ws/speech")
@Component
public class SpeechWebSocket {

    private static SpeechService speechService;
    private Session session;
    private Disposable subscription;
    private static final ConcurrentHashMap<String, SpeechWebSocket> CLIENTS = new ConcurrentHashMap<>();

    public static int getActiveConnections() {
        return CLIENTS.size();
    }

    @Autowired
    public void setSpeechService(SpeechService service) {
        speechService = service;
    }

    @OnOpen
    public void onOpen(Session session) {
        this.session = session;
        CLIENTS.put(session.getId(), this);
        log.info("New WebSocket connection established: {}, total connections: {}", session.getId(), CLIENTS.size());
        log.info("Connection parameters: {}", session.getRequestParameterMap());
        log.info("Connection path: {}", session.getRequestURI().getPath());
    }

    @OnClose
    public void onClose() {
        CLIENTS.remove(session.getId());
        if (subscription != null && !subscription.isDisposed()) {
            subscription.dispose();
        }
        log.info("WebSocket connection closed: {}", session.getId());
    }

    @OnMessage
    public void onMessage(byte[] message, Session session) {
        if (subscription != null && !subscription.isDisposed()) {
            subscription.dispose();
        }

        Flowable<ByteBuffer> audioSource = Flowable.just(ByteBuffer.wrap(message));
        subscription = speechService.processAudioStream(audioSource)
                .subscribe(
                        result -> {
                            try {
                                session.getBasicRemote().sendText(result);
                            } catch (IOException e) {
                                log.error("Failed to send message to client", e);
                            }
                        },
                        error -> {
                            log.error("Error processing audio stream", error);
                            try {
                                session.getBasicRemote().sendText("处理错误: " + error.getMessage());
                            } catch (IOException e) {
                                log.error("Failed to send error message to client", e);
                            }
                        }
                );
    }

    @OnError
    public void onError(Session session, Throwable error) {
        log.error("WebSocket error for session {}: {}", session.getId(), error.getMessage(), error);
        CLIENTS.remove(session.getId());
        if (subscription != null && !subscription.isDisposed()) {
            subscription.dispose();
        }
    }
} 