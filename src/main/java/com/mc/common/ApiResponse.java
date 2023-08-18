package com.mc.common;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mc.entity.Replies;

import java.io.Serializable;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ApiResponse implements Serializable {
    private int code;
    private String message;
    private ApiResponseData data;

    // Getters and setters


    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ApiResponseData getData() {
        return data;
    }

    public void setData(ApiResponseData data) {
        this.data = data;
    }

    public static class ApiResponseData implements Serializable {
        private List<Replies> replies;

        // Getters and setters


        public List<Replies> getReplies() {
            return replies;
        }

        public void setReplies(List<Replies> replies) {
            this.replies = replies;
        }
    }
}
