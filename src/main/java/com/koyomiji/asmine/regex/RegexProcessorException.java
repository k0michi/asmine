package com.koyomiji.asmine.regex;

public class RegexProcessorException extends RuntimeException {
  public RegexProcessorException() {
  }

  public RegexProcessorException(String message) {
    super(message);
  }

  public RegexProcessorException(String message, Throwable cause) {
    super(message, cause);
  }

  public RegexProcessorException(Throwable cause) {
    super(cause);
  }
}
