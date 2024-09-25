package com.boredream.springbootdemo.service;

import com.boredream.springbootdemo.entity.Case;

public interface IAiService {
    boolean isAvailable();
    void parseAIContent(Case talkCase);
}