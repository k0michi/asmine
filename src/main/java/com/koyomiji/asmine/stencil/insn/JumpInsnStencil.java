package com.koyomiji.asmine.stencil.insn;

import com.koyomiji.asmine.stencil.IStencil;
import com.koyomiji.asmine.stencil.IStencilRegistry;
import com.koyomiji.asmine.stencil.ResolutionExeption;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;

public class JumpInsnStencil extends AbstractInsnStencil {
  public IStencil<LabelNode> label;

  public JumpInsnStencil(IStencil<Integer> opcode, IStencil<LabelNode> label) {
    super(opcode);
    this.label = label;
  }

  @Override
  public boolean match(IStencilRegistry registry, AbstractInsnNode insn) {
    return super.match(registry, insn)
        && insn instanceof JumpInsnNode
        && label.match(registry, ((JumpInsnNode) insn).label);
  }

  @Override
  public AbstractInsnNode instantiate(IStencilRegistry registry) throws ResolutionExeption {
    return new JumpInsnNode(
        this.opcode.instantiate(registry),
        this.label.instantiate(registry)
    );
  }

  @Override
  public boolean isReal() {
    return true;
  }
}
