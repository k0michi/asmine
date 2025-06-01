package com.koyomiji.asmine.frame;

import org.objectweb.asm.tree.AbstractInsnNode;

public abstract class FrameManipInsnNode extends AbstractInsnNode {
  public FrameManipInsnNode() {
    super(-1);
  }

  public abstract void apply(Frame frame);
}
