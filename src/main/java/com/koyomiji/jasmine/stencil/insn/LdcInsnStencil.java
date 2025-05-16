package com.koyomiji.jasmine.stencil.insn;

import com.koyomiji.jasmine.stencil.AbstractParameter;
import com.koyomiji.jasmine.stencil.ConstParameter;
import com.koyomiji.jasmine.stencil.IParameterRegistry;
import com.koyomiji.jasmine.stencil.ResolutionExeption;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.LdcInsnNode;

public class LdcInsnStencil extends AbstractInsnStencil {
  public AbstractParameter<Object> cst;

  public LdcInsnStencil(AbstractParameter<Object> cst) {
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
