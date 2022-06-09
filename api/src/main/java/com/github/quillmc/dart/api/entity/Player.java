package com.github.quillmc.dart.api.entity;

public interface Player {
    String getUsername();

    boolean isSneaking();

    void sendMessage(String message);
}
