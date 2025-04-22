package com.enterprise.platform.system.exception;

public class ClientNotFoundException extends RuntimeException {
    public ClientNotFoundException(String clientId) {
        super("客户端不存在: " + clientId);
    }
}
