package com.net.backend.model;

import lombok.Data;

@Data
public class SummarizeResponse {
    private String original_text;
    private String summarized_text;
}
