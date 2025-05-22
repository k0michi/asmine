package com.koyomiji.asmine.stencil;

public class ResolutionExeption extends Exception {
  public ResolutionExeption() {
  }

  public ResolutionExeption(String message) {
    super(message);
  }

  public ResolutionExeption(String message, Throwable cause) {
    super(message, cause);
  }

  public ResolutionExeption(Throwable cause) {
    super(cause);
  }
}
