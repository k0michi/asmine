package com.koyomiji.jasmine.stencil.insn;

import com.koyomiji.jasmine.stencil.AbstractParameter;
import com.koyomiji.jasmine.stencil.IParameterRegistry;
import com.koyomiji.jasmine.stencil.ConstParameter;
import com.koyomiji.jasmine.stencil.ResolutionExeption;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.MultiANewArrayInsnNode;

public class MultiANewArrayInsnStencil extends AbstractInsnStencil {
  public AbstractParameter<String> desc;
  public AbstractParameter<Integer> dims;

  public MultiANewArrayInsnStencil(AbstractParameter<String> desc, AbstractParameter<Integer> dims) {
    super(new ConstParameter<>(Opcodes.MULTIANEWARRAY));
    this.desc = desc;
    this.dims = dims;
  }

  @Override
  public boolean match(IParameterRegistry registry, AbstractInsnNode insn) {
    return super.match(registry, insn)
            && insn instanceof MultiANewArrayInsnNode
            && desc.match(registry, ((MultiANewArrayInsnNode) insn).desc)
            && dims.match(registry, ((MultiANewArrayInsnNode) insn).dims);
  }

  @Override
  public AbstractInsnNode instantiate(IParameterRegistry registry) throws ResolutionExeption {
    return new MultiANewArrayInsnNode(
            this.desc.instantiate(registry),
            this.dims.instantiate(registry)
    );
  }

  @Override
  public boolean isReal() {
    return true;
  }
}
