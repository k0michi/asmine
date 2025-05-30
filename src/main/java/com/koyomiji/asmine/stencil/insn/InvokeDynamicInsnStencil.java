package com.koyomiji.asmine.stencil.insn;

import com.koyomiji.asmine.common.ArrayHelper;
import com.koyomiji.asmine.stencil.IStencil;
import com.koyomiji.asmine.stencil.ConstStencil;
import com.koyomiji.asmine.stencil.IStencilRegistry;
import com.koyomiji.asmine.stencil.StencilEvaluationException;
import org.objectweb.asm.Handle;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.InvokeDynamicInsnNode;

import java.util.List;

public class InvokeDynamicInsnStencil extends AbstractInsnStencil {
  public IStencil<String> name;
  public IStencil<String> desc;
  public IStencil<Handle> bsm;
  public IStencil<List<Object>> bsmArgs;

  public InvokeDynamicInsnStencil(IStencil<String> name, IStencil<String> desc, IStencil<Handle> bsm, IStencil<List<Object>> bsmArgs) {
    super(new ConstStencil<>(Opcodes.INVOKEDYNAMIC));
    this.name = name;
    this.desc = desc;
    this.bsm = bsm;
    this.bsmArgs = bsmArgs;
  }

  @Override
  public boolean match(IStencilRegistry registry, AbstractInsnNode insn) {
    return super.match(registry, insn)
        && insn instanceof InvokeDynamicInsnNode
        && name.match(registry, ((InvokeDynamicInsnNode) insn).name)
        && desc.match(registry, ((InvokeDynamicInsnNode) insn).desc)
        && bsm.match(registry, ((InvokeDynamicInsnNode) insn).bsm)
        && bsmArgs.match(registry, ArrayHelper.toList(((InvokeDynamicInsnNode) insn).bsmArgs));
  }

  @Override
  public AbstractInsnNode evaluate(IStencilRegistry registry) throws StencilEvaluationException {
    return new InvokeDynamicInsnNode(
        this.name.evaluate(registry),
        this.desc.evaluate(registry),
        this.bsm.evaluate(registry),
        this.bsmArgs.evaluate(registry)
    );
  }

  @Override
  public boolean isReal() {
    return true;
  }
}
