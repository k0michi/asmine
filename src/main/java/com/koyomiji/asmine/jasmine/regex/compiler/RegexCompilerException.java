package com.koyomiji.asmine.regex.compiler;

public class RegexCompilerException extends RuntimeException {
  public RegexCompilerException() {
  }

  public RegexCompilerException(String message) {
    super(message);
  }

  public RegexCompilerException(String message, Throwable cause) {
    super(message, cause);
  }

  public RegexCompilerException(Throwable cause) {
    super(cause);
  }
}
