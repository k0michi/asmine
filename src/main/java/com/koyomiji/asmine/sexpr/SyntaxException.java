package com.koyomiji.asmine.sexpr;

import com.koyomiji.asmine.common.SourceLocation;

public class SyntaxException extends RuntimeException {
    private SourceLocation location;

    public SyntaxException(String message) {
        super(message);
    }

    public SyntaxException(String message, SourceLocation location) {
        super(message);
        this.location = location;
    }

    public SyntaxException(String message, Throwable cause) {
        super(message, cause);
    }

    public SyntaxException(Throwable cause) {
        super(cause);
    }

    public SourceLocation getSourceLocation() {
        return location;
    }

    @Override
    public String getMessage() {
        StringBuilder sb = new StringBuilder(super.getMessage());

        if (location != null) {
            sb.append(" at ").append(location);
        }

        return sb.toString();
    }
}
