package com.koyomiji.asmine.stencil.insn;

import com.koyomiji.asmine.stencil.IStencil;
import com.koyomiji.asmine.stencil.IStencilRegistry;
import com.koyomiji.asmine.stencil.ConstStencil;
import com.koyomiji.asmine.stencil.StencilEvaluationException;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.MultiANewArrayInsnNode;

public class MultiANewArrayInsnStencil extends AbstractInsnStencil {
  public IStencil<String> desc;
  public IStencil<Integer> dims;

  public MultiANewArrayInsnStencil(IStencil<String> desc, IStencil<Integer> dims) {
    super(new ConstStencil<>(Opcodes.MULTIANEWARRAY));
    this.desc = desc;
    this.dims = dims;
  }

  @Override
  public boolean match(IStencilRegistry registry, AbstractInsnNode insn) {
    return super.match(registry, insn)
            && insn instanceof MultiANewArrayInsnNode
            && desc.match(registry, ((MultiANewArrayInsnNode) insn).desc)
            && dims.match(registry, ((MultiANewArrayInsnNode) insn).dims);
  }

  @Override
  public AbstractInsnNode evaluate(IStencilRegistry registry) throws StencilEvaluationException {
    return new MultiANewArrayInsnNode(
            this.desc.evaluate(registry),
            this.dims.evaluate(registry)
    );
  }

  @Override
  public boolean isReal() {
    return true;
  }
}
