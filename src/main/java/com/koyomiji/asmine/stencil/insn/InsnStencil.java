package com.koyomiji.asmine.stencil.insn;

import com.koyomiji.asmine.stencil.AbstractParameter;
import com.koyomiji.asmine.stencil.IParameterRegistry;
import com.koyomiji.asmine.stencil.ResolutionExeption;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.InsnNode;

public class InsnStencil extends AbstractInsnStencil {
  public InsnStencil(AbstractParameter<Integer> opcode) {
    super(opcode);
  }

  @Override
  public boolean match(IParameterRegistry registry, AbstractInsnNode insn) {
    return super.match(registry, insn) && insn instanceof InsnNode;
  }

  @Override
  public AbstractInsnNode instantiate(IParameterRegistry registry) throws ResolutionExeption {
    return new InsnNode(
        this.opcode.instantiate(registry)
    );
  }

  @Override
  public boolean isReal() {
    return true;
  }
}
