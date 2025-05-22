package com.koyomiji.jasmine.regex.compiler.code;

import com.koyomiji.jasmine.stencil.insn.AbstractInsnStencil;
import org.objectweb.asm.tree.AbstractInsnNode;

public class CodeRegexes {
  public static InsnLiteralNode literal(AbstractInsnNode node) {
    return new InsnLiteralNode(node);
  }

  public static InsnStencilNode stencil(AbstractInsnStencil stencil) {
    return new InsnStencilNode(stencil);
  }
}
