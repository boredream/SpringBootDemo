package com.boredream.springbootdemo.websocket.dto;

import lombok.Data;

@Data
public class WsMessage {
    private String action;
    private Object data;
    private Integer code;
    private String msg;

    public static WsMessage success(String action) {
        WsMessage message = new WsMessage();
        message.setAction(action);
        message.setCode(200);
        return message;
    }

    public static WsMessage success(String action, Object data) {
        WsMessage message = success(action);
        message.setData(data);
        return message;
    }

    public static WsMessage error(String action, String msg) {
        WsMessage message = new WsMessage();
        message.setAction(action);
        message.setCode(500);
        message.setMsg(msg);
        return message;
    }
} 