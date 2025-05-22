package com.koyomiji.asmine.sexpr.map;

public class ReferenceKindMap {
  private static final String[] REFERENCE_KINDS = {
          "",
          "REF_getField",
          "REF_getStatic",
          "REF_putField",
          "REF_putStatic",
          "REF_invokeVirtual",
          "REF_invokeStatic",
          "REF_invokeSpecial",
          "REF_newInvokeSpecial",
          "REF_invokeInterface",
  };

  public static String toString(int kind) {
    if (kind < 1 || kind >= REFERENCE_KINDS.length) {
      throw new IllegalArgumentException("Unknown ReferenceKind");
    }

    return REFERENCE_KINDS[kind];
  }

  public static int toInt(String kind) {
    for (int i = 1; i < REFERENCE_KINDS.length; i++) {
      if (REFERENCE_KINDS[i].equals(kind)) {
        return i;
      }
    }

    throw new IllegalArgumentException("Unknown ReferenceKind: " + kind);
  }
}
