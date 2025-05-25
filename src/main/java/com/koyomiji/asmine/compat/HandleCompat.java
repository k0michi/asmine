package com.koyomiji.asmine.compat;

import org.objectweb.asm.Handle;
import org.objectweb.asm.Opcodes;

public class HandleCompat {
  public static boolean isInterface(Handle handle) {
    return handle.getTag() == Opcodes.H_INVOKEINTERFACE;
  }
}
