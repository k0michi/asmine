package com.koyomiji.asmine.frame;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.IntInsnNode;
import org.objectweb.asm.tree.LabelNode;

import java.util.Map;

public class LocalEndInsnNode extends FrameManipInsnNode {
  public int operand;

  public LocalEndInsnNode(int operand) {
    this.operand = operand;
  }

  @Override
  public int getType() {
    return 16;
  }

  @Override
  public void accept(MethodVisitor methodVisitor) {
  }

  @Override
  public AbstractInsnNode clone(Map<LabelNode, LabelNode> clonedLabels) {
    return new LocalEndInsnNode(opcode).cloneAnnotations(this);
  }

  @Override
  public void apply(Frame frame) {
    frame.setLocal(operand, Opcodes.TOP);
  }
}
