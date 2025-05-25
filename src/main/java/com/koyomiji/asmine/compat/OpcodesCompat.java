package com.koyomiji.asmine.compat;

import org.objectweb.asm.Opcodes;

public interface OpcodesCompat {
  int ASM_LATEST = Opcodes.ASM5;

  int ACC_OPEN = 0x0020;
  int ACC_TRANSITIVE = 0x0020;
  int ACC_STATIC_PHASE = 0x0040;
  int ACC_MODULE = 0x8000;
  int ACC_RECORD = 0x10000;
}
