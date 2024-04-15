package com.example.digital_library.payload.response.author;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateAuthorResponse {
    private String message;
    private Integer authorId;
    private String authorName;
    private String authorAddress;
}
