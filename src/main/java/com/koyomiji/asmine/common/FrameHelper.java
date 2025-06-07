package com.koyomiji.asmine.common;

import org.objectweb.asm.Opcodes;

import java.util.ArrayList;
import java.util.List;

public class FrameHelper {
  public static int getSize(Object verificationType) {
    return verificationType == Opcodes.LONG || verificationType == Opcodes.DOUBLE ? 2 : 1;
  }

  public static List<Object> toPhysicalForm(List<Object> verificationTypes) {
    List<Object> result = new ArrayList<>(verificationTypes.size());

    for (int i = 0; i < verificationTypes.size(); i++) {
      Object verificationType = verificationTypes.get(i);
      result.add(verificationType);

      if (getSize(verificationType) == 2) {
        result.add(Opcodes.TOP);
      }
    }

    return result;
  }

  public static List<Object> toLogicalForm(List<Object> physicalTypes) {
    List<Object> result = new ArrayList<>(physicalTypes.size());

    for (int i = 0; i < physicalTypes.size();) {
      Object physicalType = physicalTypes.get(i);
      result.add(physicalType);
      i += getSize(physicalType);
    }

    return result;
  }
}
