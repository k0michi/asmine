package com.koyomiji.asmine.query;

public class QueryException extends RuntimeException {
  public QueryException() {
  }

  public QueryException(String message) {
    super(message);
  }

  public QueryException(String message, Throwable cause) {
    super(message, cause);
  }

  public QueryException(Throwable cause) {
    super(cause);
  }
}
