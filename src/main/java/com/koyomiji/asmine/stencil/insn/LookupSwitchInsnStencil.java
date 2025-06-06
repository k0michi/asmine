package com.koyomiji.asmine.stencil.insn;

import com.koyomiji.asmine.common.ListHelper;
import com.koyomiji.asmine.stencil.IStencil;
import com.koyomiji.asmine.stencil.IStencilRegistry;
import com.koyomiji.asmine.stencil.ConstStencil;
import com.koyomiji.asmine.stencil.StencilEvaluationException;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.LookupSwitchInsnNode;

import java.util.List;

public class LookupSwitchInsnStencil extends AbstractInsnStencil {
  public IStencil<LabelNode> dflt;
  public IStencil<List<Integer>> keys;
  public IStencil<List<LabelNode>> labels;

  public LookupSwitchInsnStencil(IStencil<LabelNode> dflt, IStencil<List<Integer>> keys, IStencil<List<LabelNode>> labels) {
    super(new ConstStencil<>(Opcodes.LOOKUPSWITCH));
    this.dflt = dflt;
    this.keys = keys;
    this.labels = labels;
  }

  @Override
  public boolean match(IStencilRegistry registry, AbstractInsnNode insn) {
    return super.match(registry, insn)
        && insn instanceof LookupSwitchInsnNode
        && dflt.match(registry, ((LookupSwitchInsnNode) insn).dflt)
        && keys.match(registry, ((LookupSwitchInsnNode) insn).keys)
        && labels.match(registry, ((LookupSwitchInsnNode) insn).labels);
  }

  @Override
  public AbstractInsnNode evaluate(IStencilRegistry registry) throws StencilEvaluationException {
    return new LookupSwitchInsnNode(
        this.dflt.evaluate(registry),
            ListHelper.toIntArray(this.keys.evaluate(registry)),
            labels.evaluate(registry).toArray(new LabelNode[0])
    );
  }

  @Override
  public boolean isReal() {
    return true;
  }
}
