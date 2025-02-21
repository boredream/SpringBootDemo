package com.boredream.springbootdemo.service;

import com.alibaba.dashscope.audio.asr.translation.TranslationRecognizerParam;
import com.alibaba.dashscope.audio.asr.translation.TranslationRecognizerRealtime;
import com.alibaba.dashscope.audio.asr.translation.results.Translation;
import com.alibaba.dashscope.exception.ApiException;
import com.alibaba.dashscope.exception.NoApiKeyException;
import com.alibaba.dashscope.utils.ApiKey;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.nio.ByteBuffer;

@Slf4j
@Service
public class SpeechService {
    private static final String TARGET_LANGUAGE = "en";

    public Flowable<String> processAudioStream(Flowable<ByteBuffer> audioSource) {
        return Flowable.create(emitter -> {
            try {
                TranslationRecognizerRealtime translator = new TranslationRecognizerRealtime();
                TranslationRecognizerParam param = TranslationRecognizerParam.builder()
                        .model("paraformer-realtime-v2")
                        .format("pcm")
                        .sampleRate(16000)
                        .apiKey(getDashScopeApiKey())
                        .transcriptionEnabled(true)
                        .translationEnabled(true)
                        .translationLanguages(new String[]{TARGET_LANGUAGE})
                        .build();

                translator.streamCall(param, audioSource)
                        .subscribe(result -> {
                            StringBuilder response = new StringBuilder();
                            if (result.getTranscriptionResult() != null) {
                                if (result.isSentenceEnd()) {
                                    response.append("原文: ").append(result.getTranscriptionResult().getText()).append("\n");
                                }
                            }
                            if (result.getTranslationResult() != null) {
                                Translation targetTranslation = result.getTranslationResult().getTranslation(TARGET_LANGUAGE);
                                if (targetTranslation != null && result.isSentenceEnd()) {
                                    response.append("翻译: ").append(targetTranslation.getText());
                                }
                            }
                            if (!response.toString().isEmpty()) {
                                emitter.onNext(response.toString());
                            }
                        }, throwable -> {
                            log.error("Speech processing error", throwable);
                            emitter.onError(throwable);
                        });
            } catch (Exception e) {
                log.error("Failed to initialize speech processor", e);
                emitter.onError(e);
            }
        }, BackpressureStrategy.BUFFER);
    }

    private String getDashScopeApiKey() throws NoApiKeyException {
        try {
            ApiKey apiKey = new ApiKey();
            return apiKey.getApiKey(null);
        } catch (NoApiKeyException e) {
            log.error("No API key found in environment", e);
            throw e;
        }
    }
} 