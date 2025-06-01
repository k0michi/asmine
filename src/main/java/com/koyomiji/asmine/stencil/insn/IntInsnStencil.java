package com.koyomiji.asmine.stencil.insn;

import com.koyomiji.asmine.stencil.IStencil;
import com.koyomiji.asmine.stencil.IStencilRegistry;
import com.koyomiji.asmine.stencil.StencilEvaluationException;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.IntInsnNode;

public class IntInsnStencil extends AbstractInsnStencil {
  public IStencil<Integer> operand;

  public IntInsnStencil(IStencil<Integer> opcode, IStencil<Integer> operand) {
    super(opcode);
    this.operand = operand;
  }

  @Override
  public boolean match(IStencilRegistry registry, AbstractInsnNode insn) {
    return super.match(registry, insn)
        && insn instanceof IntInsnNode
        && operand.match(registry, ((IntInsnNode) insn).operand);
  }

  @Override
  public AbstractInsnNode evaluate(IStencilRegistry registry) throws StencilEvaluationException {
    return new IntInsnNode(
        this.opcode.evaluate(registry),
        this.operand.evaluate(registry)
    );
  }

  @Override
  public boolean isReal() {
    return true;
  }
}
