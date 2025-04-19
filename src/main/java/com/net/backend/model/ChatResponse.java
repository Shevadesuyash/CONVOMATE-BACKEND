package com.net.backend.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ChatResponse {
    @JsonProperty("compliment")
    private String compliment;

    @JsonProperty("corrected_text")
    private String correctedText;

    @JsonProperty("end_conversation")
    private boolean endConversation;

    @JsonProperty("is_corrected")
    private boolean isCorrected;

    @JsonProperty("next_question")
    private String nextQuestion;

    @JsonProperty("original_text")
    private String originalText;
}
