package com.net.backend.model;
import com.fasterxml.jackson.annotation.JsonProperty;


public class TranslationRequest {
    @JsonProperty("from_language")
    private String fromLanguage;

    @JsonProperty("to_language")
    private String toLanguage;

    @JsonProperty("text_to_translate")
    private String textToTranslate;

    // Getters and Setters
    public String getFromLanguage() {
        return fromLanguage;
    }

    public void setFromLanguage(String fromLanguage) {
        this.fromLanguage = fromLanguage;
    }

    public String getToLanguage() {
        return toLanguage;
    }

    public void setToLanguage(String toLanguage) {
        this.toLanguage = toLanguage;
    }

    public String getTextToTranslate() {
        return textToTranslate;
    }

    public void setTextToTranslate(String textToTranslate) {
        this.textToTranslate = textToTranslate;
    }

    @Override
    public String toString() {
        return "TranslationRequest{" +
                "fromLanguage='" + fromLanguage + '\'' +
                ", toLanguage='" + toLanguage + '\'' +
                ", textToTranslate='" + textToTranslate + '\'' +
                '}';
    }
}