package com.koyomiji.asmine.stencil.insn;

import com.koyomiji.asmine.stencil.AbstractParameter;
import com.koyomiji.asmine.stencil.ConstParameter;
import com.koyomiji.asmine.stencil.IParameterRegistry;
import com.koyomiji.asmine.stencil.ResolutionExeption;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.LineNumberNode;

public class LineNumberStencil extends AbstractInsnStencil {
  public AbstractParameter<Integer> line;
  public AbstractParameter<LabelNode> start;

  public LineNumberStencil(AbstractParameter<Integer> line, AbstractParameter<LabelNode> start) {
    super(new ConstParameter<>(-1));
    this.line = line;
    this.start = start;
  }

  @Override
  public boolean match(IParameterRegistry registry, AbstractInsnNode insn) {
    return super.match(registry, insn)
        && insn instanceof LineNumberNode
        && line.match(registry, ((LineNumberNode) insn).line)
        && start.match(registry, ((LineNumberNode) insn).start);
  }

  @Override
  public AbstractInsnNode instantiate(IParameterRegistry registry) throws ResolutionExeption {
    return new LineNumberNode(
        this.line.instantiate(registry),
        this.start.instantiate(registry)
    );
  }

  @Override
  public boolean isReal() {
    return false;
  }
}
