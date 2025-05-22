package com.koyomiji.jasmine.stencil.insn;

import com.koyomiji.jasmine.stencil.AbstractParameter;
import com.koyomiji.jasmine.stencil.IParameterRegistry;
import com.koyomiji.jasmine.stencil.ResolutionExeption;
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
