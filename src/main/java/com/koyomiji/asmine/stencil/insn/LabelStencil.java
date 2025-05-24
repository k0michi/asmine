package com.koyomiji.asmine.stencil.insn;

import com.koyomiji.asmine.stencil.IStencil;
import com.koyomiji.asmine.stencil.ConstStencil;
import com.koyomiji.asmine.stencil.IStencilRegistry;
import com.koyomiji.asmine.stencil.StencilEvaluationException;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.LabelNode;

public class LabelStencil extends AbstractInsnStencil {
  public IStencil<LabelNode> label;

  public LabelStencil(IStencil<LabelNode> label) {
    super(new ConstStencil<>(-1));
    this.label = label;
  }

  @Override
  public boolean match(IStencilRegistry registry, AbstractInsnNode insn) {
    return super.match(registry, insn)
            && insn instanceof LabelNode
            && label.match(registry, (LabelNode) insn);
  }

  @Override
  public AbstractInsnNode evaluate(IStencilRegistry registry) throws StencilEvaluationException {
    return this.label.evaluate(registry);
  }

  @Override
  public boolean isReal() {
    return false;
  }
}
