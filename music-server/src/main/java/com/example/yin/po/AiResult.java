package com.easybbs.entity.po;

import java.util.List;

public class AiResult {
    private String id;
    private String object;
    private Long created;
    private String model;
    private String system_fingerprint;
    private List<AiResultChoices> choices;
    private AiResultUsage usage;

    public AiResultUsage getUsage() {
        return usage;
    }

    public void setUsage(AiResultUsage usage) {
        this.usage = usage;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getObject() {
        return object;
    }

    public void setObject(String object) {
        this.object = object;
    }

    public Long getCreated() {
        return created;
    }

    public void setCreated(Long created) {
        this.created = created;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getSystem_fingerprint() {
        return system_fingerprint;
    }

    public void setSystem_fingerprint(String system_fingerprint) {
        this.system_fingerprint = system_fingerprint;
    }

    public List<AiResultChoices> getChoices() {
        return choices;
    }

    public void setChoices(List<AiResultChoices> choices) {
        this.choices = choices;
    }
}
