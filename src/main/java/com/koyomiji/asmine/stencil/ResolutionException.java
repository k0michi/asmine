package com.koyomiji.asmine.stencil;

public class ResolutionException extends Exception {
  public ResolutionException() {
  }

  public ResolutionException(String message) {
    super(message);
  }

  public ResolutionException(String message, Throwable cause) {
    super(message, cause);
  }

  public ResolutionException(Throwable cause) {
    super(cause);
  }
}
