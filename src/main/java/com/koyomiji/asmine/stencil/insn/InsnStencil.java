package com.koyomiji.asmine.stencil.insn;

import com.koyomiji.asmine.stencil.IStencil;
import com.koyomiji.asmine.stencil.IStencilRegistry;
import com.koyomiji.asmine.stencil.ResolutionException;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.InsnNode;

public class InsnStencil extends AbstractInsnStencil {
  public InsnStencil(IStencil<Integer> opcode) {
    super(opcode);
  }

  @Override
  public boolean match(IStencilRegistry registry, AbstractInsnNode insn) {
    return super.match(registry, insn) && insn instanceof InsnNode;
  }

  @Override
  public AbstractInsnNode instantiate(IStencilRegistry registry) throws ResolutionException {
    return new InsnNode(
        this.opcode.instantiate(registry)
    );
  }

  @Override
  public boolean isReal() {
    return true;
  }
}
