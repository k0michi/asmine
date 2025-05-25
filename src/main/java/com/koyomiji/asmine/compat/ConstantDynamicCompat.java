package com.koyomiji.asmine.compat;

import org.objectweb.asm.Handle;

public class ConstantDynamicCompat {
  public static boolean instanceOf(Object object) {
    return false;
  }

  public static Handle getBootstrapMethod(Object object) {
    return null;
  }

  public static int getBootstrapMethodArgumentCount(Object object) {
    return 0;
  }

  public static Object getBootstrapMethodArgument(Object object, int index) {
    return null;
  }

  public static String getName(Object object) {
    return null;
  }

  public static String getDescriptor(Object object) {
    return null;
  }
}
