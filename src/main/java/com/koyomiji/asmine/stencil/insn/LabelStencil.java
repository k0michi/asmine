package com.koyomiji.asmine.stencil.insn;

import com.koyomiji.asmine.stencil.IStencil;
import com.koyomiji.asmine.stencil.ConstParameter;
import com.koyomiji.asmine.stencil.IParameterRegistry;
import com.koyomiji.asmine.stencil.ResolutionExeption;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.LabelNode;

public class LabelStencil extends AbstractInsnStencil {
  public IStencil<LabelNode> label;

  public LabelStencil(IStencil<LabelNode> label) {
    super(new ConstParameter<>(-1));
    this.label = label;
  }

  @Override
  public boolean match(IParameterRegistry registry, AbstractInsnNode insn) {
    return super.match(registry, insn)
            && insn instanceof LabelNode
            && label.match(registry, (LabelNode) insn);
  }

  @Override
  public AbstractInsnNode instantiate(IParameterRegistry registry) throws ResolutionExeption {
    return this.label.instantiate(registry);
  }

  @Override
  public boolean isReal() {
    return false;
  }
}
