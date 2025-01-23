package com.easybbs.entity.po;

public class AiResultPromptTokensDetails {
    private Integer text_tokens;
    private Integer audio_tokens;
    private Integer image_tokens;
    private Integer cached_tokens;

    public Integer getText_tokens() {
        return text_tokens;
    }

    public void setText_tokens(Integer text_tokens) {
        this.text_tokens = text_tokens;
    }

    public Integer getAudio_tokens() {
        return audio_tokens;
    }

    public void setAudio_tokens(Integer audio_tokens) {
        this.audio_tokens = audio_tokens;
    }

    public Integer getImage_tokens() {
        return image_tokens;
    }

    public void setImage_tokens(Integer image_tokens) {
        this.image_tokens = image_tokens;
    }

    public Integer getCached_tokens() {
        return cached_tokens;
    }

    public void setCached_tokens(Integer cached_tokens) {
        this.cached_tokens = cached_tokens;
    }
}
