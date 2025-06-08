package com.koyomiji.asmine.stencil.insn;

import com.koyomiji.asmine.stencil.IStencil;
import com.koyomiji.asmine.stencil.IStencilRegistry;
import com.koyomiji.asmine.stencil.StencilEvaluationException;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;

public class MethodInsnStencil extends AbstractInsnStencil {
  public IStencil<String> owner;
  public IStencil<String> name;
  public IStencil<String> desc;
  public IStencil<Boolean> itf;

  public MethodInsnStencil(IStencil<Integer> opcode, IStencil<String> owner, IStencil<String> name, IStencil<String> desc, IStencil<Boolean> itf) {
    super(opcode);
    this.owner = owner;
    this.name = name;
    this.desc = desc;
    this.itf = itf;
  }

  @Override
  public boolean match(IStencilRegistry registry, AbstractInsnNode insn) {
    return super.match(registry, insn) && insn instanceof MethodInsnNode
        && owner.match(registry, ((MethodInsnNode) insn).owner)
        && name.match(registry, ((MethodInsnNode) insn).name)
        && desc.match(registry, ((MethodInsnNode) insn).desc)
        && itf.match(registry, ((MethodInsnNode) insn).itf);
  }

  @Override
  public AbstractInsnNode evaluate(IStencilRegistry registry) throws StencilEvaluationException {
    return new MethodInsnNode(
        this.opcode.evaluate(registry),
        this.owner.evaluate(registry),
        this.name.evaluate(registry),
        this.desc.evaluate(registry),
        this.itf.evaluate(registry)
    );
  }

  @Override
  public boolean isReal() {
    return true;
  }
}
