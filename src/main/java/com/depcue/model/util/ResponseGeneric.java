package com.depcue.model.util;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseGeneric {
    private boolean error;
    private String message;
    private Object data;

    public ResponseGeneric(boolean error, String message) {
        this.error = error;
        this.message = message;
    }

    public ResponseGeneric(boolean error, String message, Object data) {
        this.error = error;
        this.message = message;
        this.data = data;
    }
}
