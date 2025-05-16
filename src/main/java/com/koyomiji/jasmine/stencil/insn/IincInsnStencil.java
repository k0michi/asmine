package com.koyomiji.jasmine.stencil.insn;

import com.koyomiji.jasmine.stencil.AbstractParameter;
import com.koyomiji.jasmine.stencil.IParameterRegistry;
import com.koyomiji.jasmine.stencil.ConstParameter;
import com.koyomiji.jasmine.stencil.ResolutionExeption;
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
