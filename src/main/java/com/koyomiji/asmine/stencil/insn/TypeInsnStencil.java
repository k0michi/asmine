package com.koyomiji.asmine.stencil.insn;

import com.koyomiji.asmine.stencil.AbstractParameter;
import com.koyomiji.asmine.stencil.IParameterRegistry;
import com.koyomiji.asmine.stencil.ResolutionExeption;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.TypeInsnNode;

public class TypeInsnStencil extends AbstractInsnStencil {
  public AbstractParameter<String> desc;

  public TypeInsnStencil(AbstractParameter<Integer> opcode, AbstractParameter<String> desc) {
    super(opcode);
    this.desc = desc;
  }

  @Override
  public boolean match(IParameterRegistry registry, AbstractInsnNode insn) {
    return super.match(registry, insn)
        && insn instanceof TypeInsnNode
        && desc.match(registry, ((TypeInsnNode) insn).desc);
  }

  @Override
  public AbstractInsnNode instantiate(IParameterRegistry registry) throws ResolutionExeption {
    return new TypeInsnNode(
        this.opcode.instantiate(registry),
        this.desc.instantiate(registry)
    );
  }

  @Override
  public boolean isReal() {
    return true;
  }
}
