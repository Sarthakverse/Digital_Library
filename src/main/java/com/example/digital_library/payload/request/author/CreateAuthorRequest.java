package com.example.digital_library.payload.request.author;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateAuthorRequest implements Serializable {
    @NotBlank(message = "author name can't be blank")
    private String authorName;
    @NotBlank(message = "author address cant be blank")
    private String authorAddress;
}
