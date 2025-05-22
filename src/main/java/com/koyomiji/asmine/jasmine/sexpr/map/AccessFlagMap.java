package com.koyomiji.asmine.sexpr.map;

import com.koyomiji.asmine.common.AccessFlagTarget;
import com.koyomiji.asmine.compat.OpcodesCompat;
import com.koyomiji.asmine.tuple.Pair;
import org.objectweb.asm.Opcodes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class AccessFlagMap {
  private static final List<Pair<Integer, String>> CLASS_ACCESS_FLAGS = Arrays.asList(
          Pair.of(Opcodes.ACC_PUBLIC, "ACC_PUBLIC"),
          Pair.of(Opcodes.ACC_FINAL, "ACC_FINAL"),
          Pair.of(Opcodes.ACC_SUPER, "ACC_SUPER"),
          Pair.of(Opcodes.ACC_INTERFACE, "ACC_INTERFACE"),
          Pair.of(Opcodes.ACC_ABSTRACT, "ACC_ABSTRACT"),
          Pair.of(Opcodes.ACC_SYNTHETIC, "ACC_SYNTHETIC"),
          Pair.of(Opcodes.ACC_ANNOTATION, "ACC_ANNOTATION"),
          Pair.of(Opcodes.ACC_ENUM, "ACC_ENUM"),
          Pair.of(OpcodesCompat.ACC_MODULE, "ACC_MODULE")
  );

  private static final List<Pair<Integer, String>> FIELD_ACCESS_FLAGS = Arrays.asList(
          Pair.of(Opcodes.ACC_PUBLIC, "ACC_PUBLIC"),
          Pair.of(Opcodes.ACC_PRIVATE, "ACC_PRIVATE"),
          Pair.of(Opcodes.ACC_PROTECTED, "ACC_PROTECTED"),
          Pair.of(Opcodes.ACC_STATIC, "ACC_STATIC"),
          Pair.of(Opcodes.ACC_FINAL, "ACC_FINAL"),
          Pair.of(Opcodes.ACC_VOLATILE, "ACC_VOLATILE"),
          Pair.of(Opcodes.ACC_TRANSIENT, "ACC_TRANSIENT"),
          Pair.of(Opcodes.ACC_SYNTHETIC, "ACC_SYNTHETIC"),
          Pair.of(Opcodes.ACC_ENUM, "ACC_ENUM")
  );

  private static final List<Pair<Integer, String>> METHOD_ACCESS_FLAGS = Arrays.asList(
          Pair.of(Opcodes.ACC_PUBLIC, "ACC_PUBLIC"),
          Pair.of(Opcodes.ACC_PRIVATE, "ACC_PRIVATE"),
          Pair.of(Opcodes.ACC_PROTECTED, "ACC_PROTECTED"),
          Pair.of(Opcodes.ACC_STATIC, "ACC_STATIC"),
          Pair.of(Opcodes.ACC_FINAL, "ACC_FINAL"),
          Pair.of(Opcodes.ACC_SYNCHRONIZED, "ACC_SYNCHRONIZED"),
          Pair.of(Opcodes.ACC_BRIDGE, "ACC_BRIDGE"),
          Pair.of(Opcodes.ACC_VARARGS, "ACC_VARARGS"),
          Pair.of(Opcodes.ACC_NATIVE, "ACC_NATIVE"),
          Pair.of(Opcodes.ACC_ABSTRACT, "ACC_ABSTRACT"),
          Pair.of(Opcodes.ACC_STRICT, "ACC_STRICT"),
          Pair.of(Opcodes.ACC_SYNTHETIC, "ACC_SYNTHETIC")
  );

  private static final List<Pair<Integer, String>> INNER_CLASS_ACCESS_FLAGS = Arrays.asList(
          Pair.of(Opcodes.ACC_PUBLIC, "ACC_PUBLIC"),
          Pair.of(Opcodes.ACC_PRIVATE, "ACC_PRIVATE"),
          Pair.of(Opcodes.ACC_PROTECTED, "ACC_PROTECTED"),
          Pair.of(Opcodes.ACC_STATIC, "ACC_STATIC"),
          Pair.of(Opcodes.ACC_FINAL, "ACC_FINAL"),
          Pair.of(Opcodes.ACC_INTERFACE, "ACC_INTERFACE"),
          Pair.of(Opcodes.ACC_ABSTRACT, "ACC_ABSTRACT"),
          Pair.of(Opcodes.ACC_SYNTHETIC, "ACC_SYNTHETIC"),
          Pair.of(Opcodes.ACC_ANNOTATION, "ACC_ANNOTATION"),
          Pair.of(Opcodes.ACC_ENUM, "ACC_ENUM")
  );

  private static final List<Pair<Integer, String>> MODULE_ACCESS_FLAGS = Arrays.asList(
          Pair.of(OpcodesCompat.ACC_OPEN, "ACC_OPEN"),
          Pair.of(Opcodes.ACC_SYNTHETIC, "ACC_SYNTHETIC"),
          Pair.of(Opcodes.ACC_MANDATED, "ACC_MANDATED")
  );

  private static final List<Pair<Integer, String>> MODULE_REQUIRES_ACCESS_FLAGS = Arrays.asList(
          Pair.of(OpcodesCompat.ACC_TRANSITIVE, "ACC_TRANSITIVE"),
          Pair.of(OpcodesCompat.ACC_STATIC_PHASE, "ACC_STATIC_PHASE"),
          Pair.of(Opcodes.ACC_SYNTHETIC, "ACC_SYNTHETIC"),
          Pair.of(Opcodes.ACC_MANDATED, "ACC_MANDATED")
  );

  private static final List<Pair<Integer, String>> METHOD_PARAMETER_ACCESS_FLAGS = Arrays.asList(
          Pair.of(Opcodes.ACC_FINAL, "ACC_FINAL"),
          Pair.of(Opcodes.ACC_SYNTHETIC, "ACC_SYNTHETIC"),
          Pair.of(Opcodes.ACC_MANDATED, "ACC_MANDATED")
  );

  public static Optional<Integer> toInt(String name, AccessFlagTarget target) {
    List<Pair<Integer, String>> flags;

    switch (target) {
      case CLASS:
        flags = CLASS_ACCESS_FLAGS;
        break;
      case FIELD:
        flags = FIELD_ACCESS_FLAGS;
        break;
      case METHOD:
        flags = METHOD_ACCESS_FLAGS;
        break;
      case INNER_CLASS:
        flags = INNER_CLASS_ACCESS_FLAGS;
        break;
      case METHOD_PARAMETER:
        flags = METHOD_PARAMETER_ACCESS_FLAGS;
        break;
      case MODULE:
      case MODULE_EXPORTS:
      case MODULE_OPENS:
        flags = MODULE_ACCESS_FLAGS;
        break;
      case MODULE_REQUIRES:
        flags = MODULE_REQUIRES_ACCESS_FLAGS;
        break;
      default:
        throw new IllegalArgumentException("Unknown target " + target);
    }

    for (Pair<Integer, String> flag : flags) {
      if (flag.second.equals(name)) {
        return Optional.of(flag.first);
      }
    }

    return Optional.empty();
  }

  public static List<String> toNames(int access, AccessFlagTarget target) {
    List<Pair<Integer, String>> flags;

    switch (target) {
      case CLASS:
        flags = CLASS_ACCESS_FLAGS;
        break;
      case FIELD:
        flags = FIELD_ACCESS_FLAGS;
        break;
      case METHOD:
        flags = METHOD_ACCESS_FLAGS;
        break;
      case INNER_CLASS:
        flags = INNER_CLASS_ACCESS_FLAGS;
        break;
      case METHOD_PARAMETER:
        flags = METHOD_PARAMETER_ACCESS_FLAGS;
        break;
      case MODULE:
      case MODULE_EXPORTS:
      case MODULE_OPENS:
        flags = MODULE_ACCESS_FLAGS;
        break;
      case MODULE_REQUIRES:
        flags = MODULE_REQUIRES_ACCESS_FLAGS;
        break;
      default:
        throw new IllegalArgumentException("Unknown target " + target);
    }

    List<String> names = new ArrayList<>();

    for (Pair<Integer, String> flag : flags) {
      if ((access & flag.first) != 0) {
        names.add(flag.second);
      }
    }

    return names;
  }
}
