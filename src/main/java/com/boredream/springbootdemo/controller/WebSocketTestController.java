package com.boredream.springbootdemo.controller;

import com.boredream.springbootdemo.websocket.SpeechWebSocket;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/websocket")
public class WebSocketTestController {

    @GetMapping("/status")
    public Map<String, Object> getWebSocketStatus() {
        Map<String, Object> status = new HashMap<>();
        status.put("isEnabled", true);
        status.put("activeConnections", SpeechWebSocket.getActiveConnections());
        status.put("websocketPath", "/ws/speech");
        return status;
    }
} 