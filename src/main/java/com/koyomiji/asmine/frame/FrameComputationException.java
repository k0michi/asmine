package com.koyomiji.asmine.frame;

public class FrameComputationException extends RuntimeException {
  public FrameComputationException() {
  }

  public FrameComputationException(String message) {
    super(message);
  }

  public FrameComputationException(String message, Throwable cause) {
    super(message, cause);
  }

  public FrameComputationException(Throwable cause) {
    super(cause);
  }
}
