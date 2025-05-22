package com.koyomiji.asmine.stencil.insn;

import com.koyomiji.asmine.stencil.AbstractParameter;
import com.koyomiji.asmine.stencil.IParameterRegistry;
import com.koyomiji.asmine.stencil.ResolutionExeption;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;

public class MethodInsnStencil extends AbstractInsnStencil {
  public AbstractParameter<String> owner;
  public AbstractParameter<String> name;
  public AbstractParameter<String> desc;
  public AbstractParameter<Boolean> itf;

  public MethodInsnStencil(AbstractParameter<Integer> opcode, AbstractParameter<String> owner, AbstractParameter<String> name, AbstractParameter<String> desc, AbstractParameter<Boolean> itf) {
    super(opcode);
    this.owner = owner;
    this.name = name;
    this.desc = desc;
    this.itf = itf;
  }

  @Override
  public boolean match(IParameterRegistry registry, AbstractInsnNode insn) {
    return super.match(registry, insn) && insn instanceof MethodInsnNode
        && owner.match(registry, ((MethodInsnNode) insn).owner)
        && name.match(registry, ((MethodInsnNode) insn).name)
        && desc.match(registry, ((MethodInsnNode) insn).desc)
        && itf.match(registry, ((MethodInsnNode) insn).itf);
  }

  @Override
  public AbstractInsnNode instantiate(IParameterRegistry registry) throws ResolutionExeption {
    return new MethodInsnNode(
        this.opcode.instantiate(registry),
        this.owner.instantiate(registry),
        this.name.instantiate(registry),
        this.desc.instantiate(registry),
        this.itf.instantiate(registry)
    );
  }

  @Override
  public boolean isReal() {
    return true;
  }
}
