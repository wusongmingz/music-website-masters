package com.example.yin.controller;


import com.alibaba.dashscope.exception.ApiException;
import com.alibaba.dashscope.exception.InputRequiredException;
import com.alibaba.dashscope.exception.NoApiKeyException;
import com.example.yin.service.AliTongYiQianWenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ChatController {

    @Autowired
    private AliTongYiQianWenService aliTongYiQianWenService;

    @GetMapping("/chat")
    public String chat(@RequestParam String question) {
        try {
            return aliTongYiQianWenService.simpleMultiModalConversationCall(question);
        } catch (ApiException | NoApiKeyException | InputRequiredException e) {
            return "请求出错：" + e.getMessage();
        }
    }
}