package com.koyomiji.asmine.stencil.insn;

import com.koyomiji.asmine.stencil.*;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.FrameNode;

import java.util.List;

public class FrameStencil extends AbstractInsnStencil {
  public IStencil<FrameNode> frame;

  public FrameStencil(IStencil<FrameNode> frame) {
    super(new ConstStencil<>(-1));
    this.frame = frame;
  }

  @Override
  public boolean match(IStencilRegistry registry, AbstractInsnNode insn) {
    return super.match(registry, insn)
            && insn instanceof FrameNode
            && frame.match(registry, ((FrameNode) insn));
  }

  @Override
  public AbstractInsnNode evaluate(IStencilRegistry registry) throws StencilEvaluationException {
    return this.frame.evaluate(registry);
  }

  @Override
  public boolean isReal() {
    return false;
  }
}
