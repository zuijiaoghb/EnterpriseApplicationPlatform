package com.enterprise.platform.inventorymanagement.model.vo;

public class ResultVO<T> {
    private int code;
    private String message;
    private T data;

    public ResultVO() {}

    public ResultVO(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static <T> ResultVO<T> success(T data, String message) {
        return new ResultVO<>(200, message, data);
    }

    public static <T> ResultVO<T> fail(String message) {
        return new ResultVO<>(500, message, null);
    }

    // Getters and Setters
    public int getCode() { return code; }
    public void setCode(int code) { this.code = code; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public T getData() { return data; }
    public void setData(T data) { this.data = data; }
}