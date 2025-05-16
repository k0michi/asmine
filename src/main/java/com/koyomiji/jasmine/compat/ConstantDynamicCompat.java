package com.koyomiji.jasmine.compat;

import org.objectweb.asm.ConstantDynamic;
import org.objectweb.asm.Handle;

public class ConstantDynamicCompat {
  public static boolean instanceOf(Object object) {
    return object instanceof ConstantDynamic;
  }

  public static Handle getBootstrapMethod(Object object) {
    return object instanceof ConstantDynamic
        ? ((ConstantDynamic) object).getBootstrapMethod()
        : null;
  }

  public static int getBootstrapMethodArgumentCount(Object object) {
    return object instanceof ConstantDynamic
        ? ((ConstantDynamic) object).getBootstrapMethodArgumentCount()
        : 0;
  }

  public static Object getBootstrapMethodArgument(Object object, int index) {
    return object instanceof ConstantDynamic
        ? ((ConstantDynamic) object).getBootstrapMethodArgument(index)
        : null;
  }

  public static String getName(Object object) {
    return object instanceof ConstantDynamic
        ? ((ConstantDynamic) object).getName()
        : null;
  }

  public static String getDescriptor(Object object) {
    return object instanceof ConstantDynamic
        ? ((ConstantDynamic) object).getDescriptor()
        : null;
  }
}
