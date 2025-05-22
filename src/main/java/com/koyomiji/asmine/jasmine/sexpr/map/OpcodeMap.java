package com.koyomiji.asmine.sexpr.map;

import org.objectweb.asm.util.Printer;

public class OpcodeMap {
  public static String toString(int opcode) {
    if (opcode < 0 || opcode >= Printer.OPCODES.length) {
      return "Unknown Opcode: " + opcode;
    }

    return Printer.OPCODES[opcode];
  }

  public static int toInt(String opcode) {
    for (int i = 0; i < Printer.OPCODES.length; i++) {
      if (Printer.OPCODES[i].equals(opcode)) {
        return i;
      }
    }

    throw new IllegalArgumentException("Unknown Opcode: " + opcode);
  }
}
