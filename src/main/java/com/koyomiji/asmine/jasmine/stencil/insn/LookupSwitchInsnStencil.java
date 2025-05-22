package com.koyomiji.asmine.stencil.insn;

import com.koyomiji.asmine.common.ListHelper;
import com.koyomiji.asmine.stencil.AbstractParameter;
import com.koyomiji.asmine.stencil.IParameterRegistry;
import com.koyomiji.asmine.stencil.ConstParameter;
import com.koyomiji.asmine.stencil.ResolutionExeption;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.LookupSwitchInsnNode;

import java.util.List;

public class LookupSwitchInsnStencil extends AbstractInsnStencil {
  public AbstractParameter<LabelNode> dflt;
  public AbstractParameter<List<Integer>> keys;
  public AbstractParameter<List<LabelNode>> labels;

  public LookupSwitchInsnStencil(AbstractParameter<LabelNode> dflt, AbstractParameter<List<Integer>> keys, AbstractParameter<List<LabelNode>> labels) {
    super(new ConstParameter<>(Opcodes.LOOKUPSWITCH));
    this.dflt = dflt;
    this.keys = keys;
    this.labels = labels;
  }

  @Override
  public boolean match(IParameterRegistry registry, AbstractInsnNode insn) {
    return super.match(registry, insn)
        && insn instanceof LookupSwitchInsnNode
        && dflt.match(registry, ((LookupSwitchInsnNode) insn).dflt)
        && keys.match(registry, ((LookupSwitchInsnNode) insn).keys)
        && labels.match(registry, ((LookupSwitchInsnNode) insn).labels);
  }

  @Override
  public AbstractInsnNode instantiate(IParameterRegistry registry) throws ResolutionExeption {
    return new LookupSwitchInsnNode(
        this.dflt.instantiate(registry),
            ListHelper.toIntArray(this.keys.instantiate(registry)),
            labels.instantiate(registry).toArray(new LabelNode[0])
    );
  }

  @Override
  public boolean isReal() {
    return true;
  }
}
