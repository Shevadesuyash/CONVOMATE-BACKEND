package com.net.backend.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GrammarCheckResponse {
    @JsonProperty("original_text")
    private String originalText;

    @JsonProperty("grammar_corrected")
    private String correctedText;
}