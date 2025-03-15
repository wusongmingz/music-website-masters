package com.example.yin.service;

import com.alibaba.dashscope.exception.ApiException;
import com.alibaba.dashscope.exception.InputRequiredException;
import com.alibaba.dashscope.exception.NoApiKeyException;

public interface AliTongYiQianWenService {
    /**
     * 调用通义千问进行简单多模态对话
     * @param question 用户的问题
     * @return 通义千问的回答
     * @throws ApiException API调用异常
     * @throws NoApiKeyException 缺少API Key异常
     * @throws InputRequiredException 缺少必要输入异常
     */
    String simpleMultiModalConversationCall(String question) throws ApiException, NoApiKeyException, InputRequiredException;
}