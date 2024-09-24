package com.boredream.springbootdemo.service;

import com.boredream.springbootdemo.entity.Case;

public interface IAiService {
    void parseAIContent(Case talkCase);
}