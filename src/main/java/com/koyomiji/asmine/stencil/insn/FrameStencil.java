package com.koyomiji.asmine.stencil.insn;

import com.koyomiji.asmine.stencil.*;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.FrameNode;

import java.util.List;

public class FrameStencil extends AbstractInsnStencil {
  public IStencil<List<Object>> local;
  public IStencil<List<Object>> stack;

  public FrameStencil(IStencil<List<Object>> local, IStencil<List<Object>> stack) {
    super(new ConstStencil<>(-1));
    this.local = local;
    this.stack = stack;
  }

  @Override
  public boolean match(IStencilRegistry registry, AbstractInsnNode insn) {
    return super.match(registry, insn)
            && insn instanceof FrameNode
            && local.match(registry, ((FrameNode) insn).local)
            && stack.match(registry, ((FrameNode) insn).stack);
  }

  @Override
  public AbstractInsnNode evaluate(IStencilRegistry registry) throws StencilEvaluationException {
    List<Object> local = this.local.evaluate(registry);
    List<Object> stack = this.stack.evaluate(registry);

    return new FrameNode(
            Opcodes.F_NEW,
            local.size(),
            local.toArray(new Object[0]),
            stack.size(),
            stack.toArray(new Object[0]));
  }

  @Override
  public boolean isReal() {
    return false;
  }
}
