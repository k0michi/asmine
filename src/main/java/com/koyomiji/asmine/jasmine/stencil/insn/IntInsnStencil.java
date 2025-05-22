package com.koyomiji.asmine.stencil.insn;

import com.koyomiji.asmine.stencil.AbstractParameter;
import com.koyomiji.asmine.stencil.IParameterRegistry;
import com.koyomiji.asmine.stencil.ResolutionExeption;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.IntInsnNode;

public class IntInsnStencil extends AbstractInsnStencil {
  public AbstractParameter<Integer> operand;

  public IntInsnStencil(AbstractParameter<Integer> opcode, AbstractParameter<Integer> operand) {
    super(opcode);
    this.operand = operand;
  }

  @Override
  public boolean match(IParameterRegistry registry, AbstractInsnNode insn) {
    return super.match(registry, insn)
        && insn instanceof IntInsnNode
        && operand.match(registry, ((IntInsnNode) insn).operand);
  }

  @Override
  public AbstractInsnNode instantiate(IParameterRegistry registry) throws ResolutionExeption {
    return new IntInsnNode(
        this.opcode.instantiate(registry),
        this.operand.instantiate(registry)
    );
  }

  @Override
  public boolean isReal() {
    return true;
  }
}
