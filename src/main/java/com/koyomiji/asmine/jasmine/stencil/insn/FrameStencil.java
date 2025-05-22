package com.koyomiji.asmine.stencil.insn;

import com.koyomiji.asmine.stencil.AbstractParameter;
import com.koyomiji.asmine.stencil.IParameterRegistry;
import com.koyomiji.asmine.stencil.ResolutionExeption;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.FrameNode;

import java.util.List;

public class FrameStencil extends AbstractInsnStencil {
  public AbstractParameter<Integer> type;
  public AbstractParameter<Integer> numLocal;
  public AbstractParameter<List<Object>> local;
  public AbstractParameter<Integer> numStack;
  public AbstractParameter<List<Object>> stack;
  
  public FrameStencil(AbstractParameter<Integer> type, AbstractParameter<Integer> numLocal, AbstractParameter<List<Object>> local, AbstractParameter<Integer> numStack, AbstractParameter<List<Object>> stack) {
    super(type);
    this.type = type;
    this.numLocal = numLocal;
    this.local = local;
    this.numStack = numStack;
    this.stack = stack;
  }

  @Override
  public boolean match(IParameterRegistry registry, AbstractInsnNode insn) {
    return super.match(registry, insn)
        && insn instanceof FrameNode
        && type.match(registry, ((FrameNode) insn).type)
        && numLocal.match(registry, ((FrameNode) insn).local.size())
        && local.match(registry, ((FrameNode) insn).local)
        && numStack.match(registry, ((FrameNode) insn).stack.size())
        && stack.match(registry, ((FrameNode) insn).stack);
  }

  @Override
  public AbstractInsnNode instantiate(IParameterRegistry registry) throws ResolutionExeption {
    return new FrameNode(
        this.type.instantiate(registry),
        this.numLocal.instantiate(registry),
        this.local.instantiate(registry).toArray(),
        this.numStack.instantiate(registry),
        this.stack.instantiate(registry).toArray()
    );
  }

  @Override
  public boolean isReal() {
    return false;
  }
}
