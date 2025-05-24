package com.koyomiji.asmine.stencil.insn;

import com.koyomiji.asmine.stencil.IStencil;
import com.koyomiji.asmine.stencil.ConstParameter;
import com.koyomiji.asmine.stencil.IParameterRegistry;
import com.koyomiji.asmine.stencil.ResolutionExeption;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.LdcInsnNode;

public class LdcInsnStencil extends AbstractInsnStencil {
  public IStencil<Object> cst;

  public LdcInsnStencil(IStencil<Object> cst) {
    super(new ConstParameter<>(Opcodes.LDC));
    this.cst = cst;
  }

  @Override
  public boolean match(IParameterRegistry registry, AbstractInsnNode insn) {
    return super.match(registry, insn)
        && insn instanceof LdcInsnNode
        && cst.match(registry, ((LdcInsnNode) insn).cst);
  }

  @Override
  public AbstractInsnNode instantiate(IParameterRegistry registry) throws ResolutionExeption {
    return new LdcInsnNode(
        this.cst.instantiate(registry)
    );
  }

  @Override
  public boolean isReal() {
    return true;
  }
}
