package com.boredream.springbootdemo.websocket;

import com.alibaba.fastjson.JSON;
import com.boredream.springbootdemo.service.SpeechService;
import com.boredream.springbootdemo.websocket.constant.WsAction;
import com.boredream.springbootdemo.websocket.dto.WsMessage;
import io.reactivex.Flowable;
import io.reactivex.disposables.Disposable;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.alibaba.dashscope.audio.asr.translation.TranslationRecognizerParam;
import com.alibaba.dashscope.audio.asr.translation.TranslationRecognizerRealtime;
import com.alibaba.dashscope.utils.ApiKey;

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
    private boolean isTranscribing = false;
    private TranslationRecognizerRealtime translator;
    private static final String TARGET_LANGUAGE = "en";

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
    }

    @OnClose
    public void onClose() {
        CLIENTS.remove(session.getId());
        stopTranscription();
        log.info("WebSocket connection closed: {}", session.getId());
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        log.info("收到WebSocket文本消息: {}", message);
        try {
            WsMessage wsMessage = JSON.parseObject(message, WsMessage.class);
            handleMessage(wsMessage);
        } catch (Exception e) {
            log.error("处理消息失败", e);
            sendError("消息格式错误");
        }
    }

    @OnMessage
    public void onMessage(byte[] message, Session session) {
        if (!isTranscribing) {
            sendError("请先调用 start 接口开始转写");
            return;
        }

        log.info("收到音频数据: {} bytes", message.length);
        try {
            // 处理语音识别
            Flowable<ByteBuffer> audioSource = Flowable.just(ByteBuffer.wrap(message));
            TranslationRecognizerParam param = TranslationRecognizerParam.builder()
//            .model("paraformer-realtime-v2")
                    .model("gummy-realtime-v1")
                    .format("pcm") // 'pcm'、'wav'、'opus'、'speex'、'aac'、'amr', you
                    // can check the supported formats in the document
                    .sampleRate(16000) // supported 8000、16000
                    .apiKey(getDashScopeApiKey())
                    .transcriptionEnabled(true)
                    .translationEnabled(true)
                    .translationLanguages(new String[]{"en"})
                    .build();
            subscription = translator.streamCall(param, audioSource)
                    .subscribe(result -> {
                                // 处理识别结果
                                if (result.getTranscriptionResult() != null) {
                                    String text = result.isSentenceEnd()
                                            ? result.getTranscriptionResult().getText()
                                            : result.getTranscriptionResult().getText() + "(临时结果)";
                                    WsMessage response = WsMessage.success(WsAction.TRANSCRIPT, text);
                                    sendMessage(response);
                                }
                            },
                            error -> {
                                log.error("处理音频流错误", error);
                                sendError("处理音频流错误: " + error.getMessage());
                            }
                    );
        } catch (Exception e) {
            log.error("处理音频数据失败", e);
            sendError("处理音频数据失败");
        }
    }

    private void handleMessage(WsMessage message) {
        switch (message.getAction()) {
            case WsAction.START:
                handleStart();
                break;
            case WsAction.STOP:
                handleStop();
                break;
            default:
                sendError("未知的消息类型: " + message.getAction());
        }
    }

    private void handleStart() {
        if (isTranscribing) {
            sendError("已经在转写中");
            return;
        }

        try {
            // 初始化阿里云语音识别SDK

            // 创建Recognizer
            translator = new TranslationRecognizerRealtime();

            isTranscribing = true;
            WsMessage response = WsMessage.success(WsAction.START_RESPONSE);
            sendMessage(response);
        } catch (Exception e) {
            log.error("启动转写失败", e);
            sendError("启动转写失败: " + e.getMessage());
        }
    }

    private String getDashScopeApiKey() {
        String dashScopeApiKey = null;
        try {
            ApiKey apiKey = new ApiKey();
            dashScopeApiKey = apiKey.getApiKey(null); // 从环境变量获取
        } catch (Exception e) {
            log.error("获取API key失败", e);
        }

        if (dashScopeApiKey == null) {
            // 如果环境变量中没有，可以在这里硬编码设置
            // dashScopeApiKey = "your-dashscope-api-key";
            throw new RuntimeException("未找到 DashScope API Key");
        }
        return dashScopeApiKey;
    }

    private void handleStop() {
        stopTranscription();
        WsMessage response = WsMessage.success(WsAction.STOP);
        sendMessage(response);
    }

    private void stopTranscription() {
        if (subscription != null && !subscription.isDisposed()) {
            subscription.dispose();
        }
        isTranscribing = false;
    }

    private void sendMessage(WsMessage message) {
        try {
            session.getBasicRemote().sendText(JSON.toJSONString(message));
        } catch (IOException e) {
            log.error("发送消息失败", e);
        }
    }

    private void sendError(String errorMessage) {
        WsMessage error = WsMessage.error(WsAction.ERROR, errorMessage);
        sendMessage(error);
    }

    @OnError
    public void onError(Session session, Throwable error) {
        log.error("WebSocket error for session {}: {}", session.getId(), error.getMessage(), error);
        CLIENTS.remove(session.getId());
        stopTranscription();
    }
} 