package com.koyomiji.asmine.stencil.insn;

import com.koyomiji.asmine.stencil.IStencil;
import com.koyomiji.asmine.stencil.ConstStencil;
import com.koyomiji.asmine.stencil.IStencilRegistry;
import com.koyomiji.asmine.stencil.ResolutionException;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.LineNumberNode;

public class LineNumberStencil extends AbstractInsnStencil {
  public IStencil<Integer> line;
  public IStencil<LabelNode> start;

  public LineNumberStencil(IStencil<Integer> line, IStencil<LabelNode> start) {
    super(new ConstStencil<>(-1));
    this.line = line;
    this.start = start;
  }

  @Override
  public boolean match(IStencilRegistry registry, AbstractInsnNode insn) {
    return super.match(registry, insn)
        && insn instanceof LineNumberNode
        && line.match(registry, ((LineNumberNode) insn).line)
        && start.match(registry, ((LineNumberNode) insn).start);
  }

  @Override
  public AbstractInsnNode instantiate(IStencilRegistry registry) throws ResolutionException {
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
