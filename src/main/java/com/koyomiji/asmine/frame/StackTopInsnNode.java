package com.koyomiji.asmine.frame;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.LabelNode;

import java.util.Map;

public class StackTopInsnNode extends FrameManipInsnNode {
  public Object verificationType;

  public StackTopInsnNode(Object verificationType) {
    this.verificationType = verificationType;
  }

  @Override
  public int getType() {
    return 17;
  }

  @Override
  public void accept(MethodVisitor methodVisitor) {
  }

  @Override
  public AbstractInsnNode clone(Map<LabelNode, LabelNode> clonedLabels) {
    return new StackTopInsnNode(verificationType).cloneAnnotations(this);
  }

  @Override
  public void apply(Frame frame) {
    frame.setStackTop(verificationType);
  }
}
