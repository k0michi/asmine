package com.koyomiji.asmine.stencil.insn;

import com.koyomiji.asmine.stencil.IStencil;
import com.koyomiji.asmine.stencil.IStencilRegistry;
import com.koyomiji.asmine.stencil.StencilEvaluationException;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.VarInsnNode;

public class VarInsnStencil extends AbstractInsnStencil {
  public IStencil<Integer> var;

  public VarInsnStencil(IStencil<Integer> opcode, IStencil<Integer> var) {
    super(opcode);
    this.var = var;
  }

  @Override
  public boolean match(IStencilRegistry registry, AbstractInsnNode insn) {
    return super.match(registry, insn)
            && insn instanceof VarInsnNode
            && var.match(registry, ((VarInsnNode) insn).var);
  }

  @Override
  public AbstractInsnNode evaluate(IStencilRegistry registry) throws StencilEvaluationException {
    return new VarInsnNode(
            this.opcode.evaluate(registry),
            this.var.evaluate(registry)
    );
  }

  @Override
  public boolean isReal() {
    return true;
  }
}
