package com.koyomiji.asmine.stencil.insn;

import com.koyomiji.asmine.stencil.AbstractParameter;
import com.koyomiji.asmine.stencil.IParameterRegistry;
import com.koyomiji.asmine.stencil.ResolutionExeption;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;

public class JumpInsnStencil extends AbstractInsnStencil {
  public AbstractParameter<LabelNode> label;

  public JumpInsnStencil(AbstractParameter<Integer> opcode, AbstractParameter<LabelNode> label) {
    super(opcode);
    this.label = label;
  }

  @Override
  public boolean match(IParameterRegistry registry, AbstractInsnNode insn) {
    return super.match(registry, insn)
        && insn instanceof JumpInsnNode
        && label.match(registry, ((JumpInsnNode) insn).label);
  }

  @Override
  public AbstractInsnNode instantiate(IParameterRegistry registry) throws ResolutionExeption {
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
