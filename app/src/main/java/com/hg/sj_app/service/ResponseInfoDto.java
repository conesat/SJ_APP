package com.hg.sj_app.service;

public class ResponseInfoDto {
    private int code;
    private String msg;
    private String detail;

    public ResponseInfoDto() {
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }
    public ResponseInfoDto(int code, String msg, String detail) {
        this.code = code;
        this.msg = msg;
        this.detail = detail;
    }

    @Override
    public String toString() {
        return "ResponseInfoDto{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", detail='" + detail + '\'' +
                '}';
    }
}
