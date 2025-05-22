package com.koyomiji.asmine.stencil.insn;

import com.koyomiji.asmine.common.ArrayHelper;
import com.koyomiji.asmine.stencil.AbstractParameter;
import com.koyomiji.asmine.stencil.ConstParameter;
import com.koyomiji.asmine.stencil.IParameterRegistry;
import com.koyomiji.asmine.stencil.ResolutionExeption;
import org.objectweb.asm.Handle;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.InvokeDynamicInsnNode;

import java.util.List;

public class InvokeDynamicInsnStencil extends AbstractInsnStencil {
  public AbstractParameter<String> name;
  public AbstractParameter<String> desc;
  public AbstractParameter<Handle> bsm;
  public AbstractParameter<List<Object>> bsmArgs;

  public InvokeDynamicInsnStencil(AbstractParameter<String> name, AbstractParameter<String> desc, AbstractParameter<Handle> bsm, AbstractParameter<List<Object>> bsmArgs) {
    super(new ConstParameter<>(Opcodes.INVOKEDYNAMIC));
    this.name = name;
    this.desc = desc;
    this.bsm = bsm;
    this.bsmArgs = bsmArgs;
  }

  @Override
  public boolean match(IParameterRegistry registry, AbstractInsnNode insn) {
    return super.match(registry, insn)
        && insn instanceof InvokeDynamicInsnNode
        && name.match(registry, ((InvokeDynamicInsnNode) insn).name)
        && desc.match(registry, ((InvokeDynamicInsnNode) insn).desc)
        && bsm.match(registry, ((InvokeDynamicInsnNode) insn).bsm)
        && bsmArgs.match(registry, ArrayHelper.toList(((InvokeDynamicInsnNode) insn).bsmArgs));
  }

  @Override
  public AbstractInsnNode instantiate(IParameterRegistry registry) throws ResolutionExeption {
    return new InvokeDynamicInsnNode(
        this.name.instantiate(registry),
        this.desc.instantiate(registry),
        this.bsm.instantiate(registry),
        this.bsmArgs.instantiate(registry)
    );
  }

  @Override
  public boolean isReal() {
    return true;
  }
}
