package com.vietphuongdo.shopapp.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.vietphuongdo.shopapp.entities.Category;
import lombok.*;

import java.util.List;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryResponse {
    @JsonProperty("message")
    private String message;

    @JsonProperty("errors")
    private List<String> errors;

    @JsonProperty("category")
    private Category category;
}

