package com.koyomiji.asmine.stencil.insn;

import com.koyomiji.asmine.stencil.AbstractParameter;
import com.koyomiji.asmine.stencil.IParameterRegistry;
import com.koyomiji.asmine.stencil.ResolutionExeption;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.FieldInsnNode;

public class FieldInsnStencil extends AbstractInsnStencil {
  public AbstractParameter<String> owner;
  public AbstractParameter<String> name;
  public AbstractParameter<String> desc;

  public FieldInsnStencil(AbstractParameter<Integer> opcode, AbstractParameter<String> owner, AbstractParameter<String> name, AbstractParameter<String> desc) {
    super(opcode);
    this.owner = owner;
    this.name = name;
    this.desc = desc;
  }

  @Override
  public boolean match(IParameterRegistry registry, AbstractInsnNode insn) {
    return super.match(registry, insn) && insn instanceof FieldInsnNode
        && owner.match(registry, ((FieldInsnNode) insn).owner)
        && name.match(registry, ((FieldInsnNode) insn).name)
        && desc.match(registry, ((FieldInsnNode) insn).desc);
  }

  @Override
  public AbstractInsnNode instantiate(IParameterRegistry registry) throws ResolutionExeption {
    return new FieldInsnNode(
        this.opcode.instantiate(registry),
        this.owner.instantiate(registry),
        this.name.instantiate(registry),
        this.desc.instantiate(registry)
    );
  }

  @Override
  public boolean isReal() {
    return true;
  }
}
