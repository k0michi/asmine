package com.koyomiji.asmine.stencil.insn;

import com.koyomiji.asmine.stencil.IStencil;
import com.koyomiji.asmine.stencil.ConstStencil;
import com.koyomiji.asmine.stencil.IStencilRegistry;
import com.koyomiji.asmine.stencil.StencilEvaluationException;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.LdcInsnNode;

public class LdcInsnStencil extends AbstractInsnStencil {
  public IStencil<Object> cst;

  public LdcInsnStencil(IStencil<Object> cst) {
    super(new ConstStencil<>(Opcodes.LDC));
    this.cst = cst;
  }

  @Override
  public boolean match(IStencilRegistry registry, AbstractInsnNode insn) {
    return super.match(registry, insn)
        && insn instanceof LdcInsnNode
        && cst.match(registry, ((LdcInsnNode) insn).cst);
  }

  @Override
  public AbstractInsnNode evaluate(IStencilRegistry registry) throws StencilEvaluationException {
    return new LdcInsnNode(
        this.cst.evaluate(registry)
    );
  }

  @Override
  public boolean isReal() {
    return true;
  }
}
