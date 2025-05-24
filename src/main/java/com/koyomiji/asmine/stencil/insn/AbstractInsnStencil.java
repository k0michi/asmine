package com.koyomiji.asmine.stencil.insn;

import com.koyomiji.asmine.stencil.IStencil;
import com.koyomiji.asmine.stencil.IStencil;
import com.koyomiji.asmine.stencil.IParameterRegistry;
import com.koyomiji.asmine.stencil.ResolutionExeption;
import org.objectweb.asm.tree.AbstractInsnNode;

public abstract class AbstractInsnStencil implements IStencil<AbstractInsnNode> {
  public IStencil<Integer> opcode;

  public AbstractInsnStencil(IStencil<Integer> opcode) {
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
