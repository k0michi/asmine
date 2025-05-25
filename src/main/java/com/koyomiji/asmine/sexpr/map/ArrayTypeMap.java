package com.koyomiji.asmine.sexpr.map;

import org.objectweb.asm.util.Printer;

public class ArrayTypeMap {
  public static int toInt(String type) {
    for (int i = 4; i < Printer.TYPES.length; i++) {
      if (Printer.TYPES[i].equals(type)) {
        return i;
      }
    }

    throw new IllegalArgumentException("Invalid type: " + type);
  }

  public static String toString(int type) {
    if (type < 4 || type >= Printer.TYPES.length) {
      throw new IllegalArgumentException("Invalid type index: " + type);
    }

    return Printer.TYPES[type];
  }
}
