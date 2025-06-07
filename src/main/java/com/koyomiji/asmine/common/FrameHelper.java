package com.koyomiji.asmine.common;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

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

  public static List<Object> fromType(Type type) {
    if (Objects.equals(type, Type.VOID_TYPE)) {
      return Arrays.asList();
    } else if (Objects.equals(type, Type.BOOLEAN_TYPE) ||
            Objects.equals(type, Type.CHAR_TYPE) ||
            Objects.equals(type, Type.BYTE_TYPE) ||
            Objects.equals(type, Type.SHORT_TYPE) ||
            Objects.equals(type, Type.INT_TYPE)) {
      return Arrays.asList(Opcodes.INTEGER);
    } else if (Objects.equals(type, Type.FLOAT_TYPE)) {
      return Arrays.asList(Opcodes.FLOAT);
    } else if (Objects.equals(type, Type.LONG_TYPE)) {
      return Arrays.asList(Opcodes.LONG, Opcodes.TOP);
    } else if (Objects.equals(type, Type.DOUBLE_TYPE)) {
      return Arrays.asList(Opcodes.DOUBLE, Opcodes.TOP);
    } else if (type.getSort() == Type.OBJECT || type.getSort() == Type.ARRAY) {
      return Arrays.asList(type.getInternalName());
    } else {
      throw new IllegalArgumentException("Unsupported type: " + type);
    }
  }
}
