package com.github.quillmc.dart.api.exception;

public class InvalidPluginException extends Exception {
    public InvalidPluginException(String message) {
        super(message);
    }

    public InvalidPluginException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
