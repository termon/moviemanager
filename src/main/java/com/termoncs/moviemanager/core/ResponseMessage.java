package com.termoncs.moviemanager.core;

import lombok.*;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@With
public class ResponseMessage<T> {
    private boolean success = true;
    private String message = "";
    private Map<String, String> errors;
    private T response;

}
