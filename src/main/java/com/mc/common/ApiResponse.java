package com.mc.common;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mc.entity.Replies;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ApiResponse implements Serializable {

    private int code;

    private String message;

    private ApiResponseData data;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ApiResponseData implements Serializable {

        private List<Replies> replies;

    }
}
