package com.koyomiji.asmine.stencil.insn;

import com.koyomiji.asmine.stencil.AbstractParameter;
import com.koyomiji.asmine.stencil.IParameterRegistry;
import com.koyomiji.asmine.stencil.ConstParameter;
import com.koyomiji.asmine.stencil.ResolutionExeption;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.IincInsnNode;

public class IincInsnStencil extends AbstractInsnStencil {
  public AbstractParameter<Integer> var;
  public AbstractParameter<Integer> incr;

  public IincInsnStencil(AbstractParameter<Integer> var, AbstractParameter<Integer> incr) {
    super(new ConstParameter<>(Opcodes.IINC));
    this.var = var;
    this.incr = incr;
  }

  @Override
  public boolean match(IParameterRegistry registry, AbstractInsnNode insn) {
    return super.match(registry, insn)
        && insn instanceof IincInsnNode
        && var.match(registry, ((IincInsnNode) insn).var)
        && incr.match(registry, ((IincInsnNode) insn).incr);
  }

  @Override
  public AbstractInsnNode instantiate(IParameterRegistry registry) throws ResolutionExeption {
    return new IincInsnNode(
        this.var.instantiate(registry),
        this.incr.instantiate(registry)
    );
  }

  @Override
  public boolean isReal() {
    return true;
  }
}
