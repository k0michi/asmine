package com.koyomiji.asmine.stencil.insn;

import com.koyomiji.asmine.stencil.AbstractParameter;
import com.koyomiji.asmine.stencil.AbstractStencil;
import com.koyomiji.asmine.stencil.IParameterRegistry;
import com.koyomiji.asmine.stencil.ResolutionExeption;
import org.objectweb.asm.tree.AbstractInsnNode;

public abstract class AbstractInsnStencil extends AbstractStencil<AbstractInsnNode> {
  public AbstractParameter<Integer> opcode;

  public AbstractInsnStencil(AbstractParameter<Integer> opcode) {
    this.opcode = opcode;
  }

  public boolean match(IParameterRegistry registry, AbstractInsnNode insn) {
    return insn != null && opcode.match(registry, insn.getOpcode());
  }

  public abstract AbstractInsnNode instantiate(IParameterRegistry registry) throws ResolutionExeption;

  public abstract boolean isReal();

  public boolean isPseudo() {
    return !isReal();
  }
}
