package com.koyomiji.asmine.stencil;

public class StencilEvaluationException extends Exception {
  public StencilEvaluationException() {
  }

  public StencilEvaluationException(String message) {
    super(message);
  }

  public StencilEvaluationException(String message, Throwable cause) {
    super(message, cause);
  }

  public StencilEvaluationException(Throwable cause) {
    super(cause);
  }
}
