package com.boredream.springbootdemo.websocket;

import com.alibaba.dashscope.audio.asr.translation.TranslationRecognizerParam;
import com.alibaba.dashscope.audio.asr.translation.TranslationRecognizerRealtime;
import com.alibaba.fastjson.JSON;
import com.boredream.springbootdemo.service.SpeechService;
import com.boredream.springbootdemo.websocket.constant.WsAction;
import com.boredream.springbootdemo.websocket.dto.WsMessage;
import io.reactivex.disposables.Disposable;
import io.reactivex.processors.PublishProcessor;
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
    private boolean isTranscribing = false;
    private TranslationRecognizerRealtime translator;
    private PublishProcessor<ByteBuffer> audioProcessor;
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

        if (audioProcessor == null) {
            log.error("音频处理器未初始化");
            sendError("音频处理器未初始化");
            return;
        }

        log.info("收到音频数据: {} bytes", message.length);
        try {
            // 将音频数据发送到处理器
            ByteBuffer buffer = ByteBuffer.wrap(message);
            audioProcessor.onNext(buffer);
            log.debug("音频数据已发送到处理器，数据大小: {} bytes, position: {}, limit: {}", 
                    buffer.capacity(), buffer.position(), buffer.limit());
        } catch (Exception e) {
            log.error("处理音频数据失败: {}, 堆栈信息: ", e.getMessage(), e);
            sendError("处理音频数据失败: " + e.getMessage());
            stopTranscription();
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
            log.info("开始初始化语音识别...");
            // 初始化音频处理器
            audioProcessor = PublishProcessor.create();
            log.info("音频处理器初始化成功");
            
            // 创建Recognizer
            translator = new TranslationRecognizerRealtime();
            log.info("TranslationRecognizerRealtime 创建成功");
            
            // 创建识别参数
            String apiKey = getDashScopeApiKey();
            log.info("获取到API Key: {}", apiKey.substring(0, 4) + "****");
            
            TranslationRecognizerParam param = TranslationRecognizerParam.builder()
                    .model("gummy-realtime-v1")
                    .format("pcm")
                    .sampleRate(16000)
                    .apiKey(apiKey)
                    .transcriptionEnabled(true)
                    .translationEnabled(true)
                    .translationLanguages(new String[]{TARGET_LANGUAGE})
                    .build();
            log.info("语音识别参数配置完成: model={}, format={}, sampleRate={}", 
                    param.getModel(), param.getFormat(), param.getSampleRate());

            // 开始流式识别
            subscription = translator.streamCall(param, audioProcessor.onBackpressureBuffer())
                    .doOnSubscribe(disposable -> {
                        log.info("音频流处理器订阅成功，开始处理音频数据");
                    })
                    .doOnNext(result -> {
                        log.info("接收到识别结果，开始处理...");
                    })
                    .doOnError(error -> {
                        log.error("音频流处理错误: {}, 错误类型: {}", error.getMessage(), error.getClass().getName(), error);
                    })
                    .doOnComplete(() -> {
                        log.info("音频流处理完成，关闭处理器");
                    })
                    .subscribe(
                            result -> {
                                try {
                                    log.info("收到阿里云识别结果: {}", JSON.toJSONString(result));
                                    if (result.getTranscriptionResult() != null) {
                                        String text = result.isSentenceEnd()
                                                ? result.getTranscriptionResult().getText()
                                                : result.getTranscriptionResult().getText() + "(临时结果)";
                                        log.info("转写结果: {}", text);
                                        WsMessage response = WsMessage.success(WsAction.TRANSCRIPT, text);
                                        sendMessage(response);
                                    } else {
                                        log.warn("收到的识别结果中没有转写内容");
                                    }
                                } catch (Exception e) {
                                    log.error("处理识别结果时发生错误: ", e);
                                }
                            },
                            error -> {
                                log.error("处理音频流错误: {}, 堆栈信息: ", error.getMessage(), error);
                                sendError("处理音频流错误: " + error.getMessage());
                                stopTranscription();
                            },
                            () -> {
                                log.info("语音识别流程正常完成");
                                stopTranscription();
                            });

            log.info("语音识别连接建立成功，等待音频数据...");
            isTranscribing = true;
            WsMessage response = WsMessage.success(WsAction.START_RESPONSE);
            sendMessage(response);

        } catch (Exception e) {
            log.error("启动转写失败: {}, 堆栈信息: ", e.getMessage(), e);
            sendError("启动转写失败: " + e.getMessage());
            stopTranscription();
        }
    }

    private String getDashScopeApiKey() {
        return "sk-75b80f758bf1455b9e57b9a56b7c4cdd";  // 使用您提供的key
    }

    private void handleStop() {
        stopTranscription();
        WsMessage response = WsMessage.success(WsAction.STOP);
        sendMessage(response);
    }

    private void stopTranscription() {
        log.info("开始停止转写...");
        if (subscription != null && !subscription.isDisposed()) {
            try {
                subscription.dispose();
                log.info("已停止订阅");
            } catch (Exception e) {
                log.error("停止订阅时发生错误: ", e);
            }
            subscription = null;
        }
        if (audioProcessor != null) {
            try {
                audioProcessor.onComplete();
                log.info("已完成音频处理器");
            } catch (Exception e) {
                log.error("停止音频处理器时发生错误: ", e);
            }
            audioProcessor = null;
        }
        isTranscribing = false;
        log.info("转写已完全停止");
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