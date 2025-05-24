package com.koyomiji.asmine.stencil.insn;

import com.koyomiji.asmine.stencil.IStencil;
import com.koyomiji.asmine.stencil.IStencilRegistry;
import com.koyomiji.asmine.stencil.EvaluationException;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.FrameNode;

import java.util.List;

public class FrameStencil extends AbstractInsnStencil {
  public IStencil<Integer> type;
  public IStencil<Integer> numLocal;
  public IStencil<List<Object>> local;
  public IStencil<Integer> numStack;
  public IStencil<List<Object>> stack;
  
  public FrameStencil(IStencil<Integer> type, IStencil<Integer> numLocal, IStencil<List<Object>> local, IStencil<Integer> numStack, IStencil<List<Object>> stack) {
    super(type);
    this.type = type;
    this.numLocal = numLocal;
    this.local = local;
    this.numStack = numStack;
    this.stack = stack;
  }

  @Override
  public boolean match(IStencilRegistry registry, AbstractInsnNode insn) {
    return super.match(registry, insn)
        && insn instanceof FrameNode
        && type.match(registry, ((FrameNode) insn).type)
        && numLocal.match(registry, ((FrameNode) insn).local.size())
        && local.match(registry, ((FrameNode) insn).local)
        && numStack.match(registry, ((FrameNode) insn).stack.size())
        && stack.match(registry, ((FrameNode) insn).stack);
  }

  @Override
  public AbstractInsnNode evaluate(IStencilRegistry registry) throws EvaluationException {
    return new FrameNode(
        this.type.evaluate(registry),
        this.numLocal.evaluate(registry),
        this.local.evaluate(registry).toArray(),
        this.numStack.evaluate(registry),
        this.stack.evaluate(registry).toArray()
    );
  }

  @Override
  public boolean isReal() {
    return false;
  }
}
