package com.koyomiji.asmine.stencil.insn;

import com.koyomiji.asmine.stencil.AbstractParameter;
import com.koyomiji.asmine.stencil.ConstParameter;
import com.koyomiji.asmine.stencil.IParameterRegistry;
import com.koyomiji.asmine.stencil.ResolutionExeption;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.TableSwitchInsnNode;

import java.util.List;

public class TableSwitchInsnStencil extends AbstractInsnStencil {
  public AbstractParameter<Integer> min;
  public AbstractParameter<Integer> max;
  public AbstractParameter<LabelNode> dflt;
  public AbstractParameter<List<LabelNode>> labels;

  public TableSwitchInsnStencil(AbstractParameter<Integer> min, AbstractParameter<Integer> max, AbstractParameter<LabelNode> dflt, AbstractParameter<List<LabelNode>> labels) {
    super(new ConstParameter<>(Opcodes.TABLESWITCH));
    this.min = min;
    this.max = max;
    this.dflt = dflt;
    this.labels = labels;
  }

  @Override
  public boolean match(IParameterRegistry registry, AbstractInsnNode insn) {
    return super.match(registry, insn)
        && insn instanceof TableSwitchInsnNode
        && min.match(registry, ((TableSwitchInsnNode) insn).min)
        && max.match(registry, ((TableSwitchInsnNode) insn).max)
        && dflt.match(registry, ((TableSwitchInsnNode) insn).dflt)
        && labels.match(registry, ((TableSwitchInsnNode) insn).labels);
  }

  @Override
  public AbstractInsnNode instantiate(IParameterRegistry registry) throws ResolutionExeption {
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
