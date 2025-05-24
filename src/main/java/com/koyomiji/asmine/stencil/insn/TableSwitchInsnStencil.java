package com.koyomiji.asmine.stencil.insn;

import com.koyomiji.asmine.stencil.IStencil;
import com.koyomiji.asmine.stencil.ConstStencil;
import com.koyomiji.asmine.stencil.IStencilRegistry;
import com.koyomiji.asmine.stencil.ResolutionExeption;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.TableSwitchInsnNode;

import java.util.List;

public class TableSwitchInsnStencil extends AbstractInsnStencil {
  public IStencil<Integer> min;
  public IStencil<Integer> max;
  public IStencil<LabelNode> dflt;
  public IStencil<List<LabelNode>> labels;

  public TableSwitchInsnStencil(IStencil<Integer> min, IStencil<Integer> max, IStencil<LabelNode> dflt, IStencil<List<LabelNode>> labels) {
    super(new ConstStencil<>(Opcodes.TABLESWITCH));
    this.min = min;
    this.max = max;
    this.dflt = dflt;
    this.labels = labels;
  }

  @Override
  public boolean match(IStencilRegistry registry, AbstractInsnNode insn) {
    return super.match(registry, insn)
        && insn instanceof TableSwitchInsnNode
        && min.match(registry, ((TableSwitchInsnNode) insn).min)
        && max.match(registry, ((TableSwitchInsnNode) insn).max)
        && dflt.match(registry, ((TableSwitchInsnNode) insn).dflt)
        && labels.match(registry, ((TableSwitchInsnNode) insn).labels);
  }

  @Override
  public AbstractInsnNode instantiate(IStencilRegistry registry) throws ResolutionExeption {
    return new TableSwitchInsnNode(
        this.min.instantiate(registry),
        this.max.instantiate(registry),
        this.dflt.instantiate(registry),
        labels.instantiate(registry).toArray(new LabelNode[0])
    );
  }

  @Override
  public boolean isReal() {
    return true;
  }
}
