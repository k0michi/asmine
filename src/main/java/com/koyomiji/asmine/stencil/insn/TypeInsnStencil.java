package com.koyomiji.asmine.stencil.insn;

import com.koyomiji.asmine.stencil.IStencil;
import com.koyomiji.asmine.stencil.IStencilRegistry;
import com.koyomiji.asmine.stencil.ResolutionException;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.TypeInsnNode;

public class TypeInsnStencil extends AbstractInsnStencil {
  public IStencil<String> desc;

  public TypeInsnStencil(IStencil<Integer> opcode, IStencil<String> desc) {
    super(opcode);
    this.desc = desc;
  }

  @Override
  public boolean match(IStencilRegistry registry, AbstractInsnNode insn) {
    return super.match(registry, insn)
        && insn instanceof TypeInsnNode
        && desc.match(registry, ((TypeInsnNode) insn).desc);
  }

  @Override
  public AbstractInsnNode instantiate(IStencilRegistry registry) throws ResolutionException {
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
