package com.koyomiji.asmine.stencil.insn;

import com.koyomiji.asmine.stencil.IStencil;
import com.koyomiji.asmine.stencil.IStencilRegistry;
import com.koyomiji.asmine.stencil.StencilEvaluationException;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.FieldInsnNode;

public class FieldInsnStencil extends AbstractInsnStencil {
  public IStencil<String> owner;
  public IStencil<String> name;
  public IStencil<String> desc;

  public FieldInsnStencil(IStencil<Integer> opcode, IStencil<String> owner, IStencil<String> name, IStencil<String> desc) {
    super(opcode);
    this.owner = owner;
    this.name = name;
    this.desc = desc;
  }

  @Override
  public boolean match(IStencilRegistry registry, AbstractInsnNode insn) {
    return super.match(registry, insn) && insn instanceof FieldInsnNode
        && owner.match(registry, ((FieldInsnNode) insn).owner)
        && name.match(registry, ((FieldInsnNode) insn).name)
        && desc.match(registry, ((FieldInsnNode) insn).desc);
  }

  @Override
  public AbstractInsnNode evaluate(IStencilRegistry registry) throws StencilEvaluationException {
    return new FieldInsnNode(
        this.opcode.evaluate(registry),
        this.owner.evaluate(registry),
        this.name.evaluate(registry),
        this.desc.evaluate(registry)
    );
  }

  @Override
  public boolean isReal() {
    return true;
  }
}
