package pl.galas.app.exceptions;

import pl.galas.app.commons.Utils;

public class CustomRequestException extends Exception {
    private String message = "Custom request exception";

    public CustomRequestException(String message) {
        super();
        if (!Utils.isNullOrEmpty(message))
            this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
