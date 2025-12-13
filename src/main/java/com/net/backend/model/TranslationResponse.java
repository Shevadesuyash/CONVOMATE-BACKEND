package com.net.backend.model;

import lombok.Data;

@Data
public class TranslationResponse {
    private String translatedText;
    private String pronunciation;
    private String fromLanguage;
    private String toLanguage;

}