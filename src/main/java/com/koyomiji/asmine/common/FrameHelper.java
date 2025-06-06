package com.koyomiji.asmine.common;

import org.objectweb.asm.Opcodes;

public class FrameHelper {
  public static int getSize(Object verificationType) {
    return verificationType == Opcodes.LONG || verificationType == Opcodes.DOUBLE ? 2 : 1;
  }
}
