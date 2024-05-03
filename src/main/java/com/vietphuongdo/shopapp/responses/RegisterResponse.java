package com.vietphuongdo.shopapp.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.vietphuongdo.shopapp.entities.User;
import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterResponse {

        @JsonProperty("message")
        private String message;

        @JsonProperty("user")
        private User user;

}
