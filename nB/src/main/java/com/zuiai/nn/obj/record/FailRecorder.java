package com.zuiai.nn.obj.record;

public class FailRecorder {
    private final FailType type;

    private final Throwable cause;

    public FailRecorder(FailType type, Throwable cause) {
        this.type = type;
        this.cause = cause;
    }

    public FailType getType() {
        return type;
    }

    public Throwable getCause() {
        return cause;
    }

    public enum FailType {
        /** 没有权限 */
        NO_PERMISSION,
        /** 位置异常 */
        UNKNOWN
    }
}
