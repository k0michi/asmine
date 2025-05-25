package com.koyomiji.asmine.stencil.insn;

import com.koyomiji.asmine.stencil.AbstractParameter;
import com.koyomiji.asmine.stencil.IParameterRegistry;
import com.koyomiji.asmine.stencil.ResolutionExeption;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.VarInsnNode;

public class VarInsnStencil extends AbstractInsnStencil {
  public AbstractParameter<Integer> var;

  public VarInsnStencil(AbstractParameter<Integer> opcode, AbstractParameter<Integer> var) {
    super(opcode);
    this.var = var;
  }

  @Override
  public boolean match(IParameterRegistry registry, AbstractInsnNode insn) {
    return super.match(registry, insn)
            && insn instanceof VarInsnNode
            && var.match(registry, ((VarInsnNode) insn).var);
  }

  @Override
  public AbstractInsnNode instantiate(IParameterRegistry registry) throws ResolutionExeption {
    return new VarInsnNode(
            this.opcode.instantiate(registry),
            this.var.instantiate(registry)
    );
  }

  @Override
  public boolean isReal() {
    return true;
  }
}
