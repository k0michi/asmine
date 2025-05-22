package com.koyomiji.asmine.tree;

import org.objectweb.asm.tree.FrameNode;
import org.objectweb.asm.tree.LabelNode;

public class ExternalizedFrameNode extends FrameNode {
  public LabelNode start;

  public ExternalizedFrameNode(
          final int type,
          final int numLocal,
          final Object[] local,
          final int numStack,
          final Object[] stack, LabelNode start) {
    super(type, numLocal, local, numStack, stack);
    this.start = start;
  }

  public static ExternalizedFrameNode fromFrameNode(FrameNode frameNode, LabelNode start) {
    return new ExternalizedFrameNode(frameNode.type, frameNode.local.size(), frameNode.local.toArray(), frameNode.stack.size(), frameNode.stack.toArray(), start);
  }
}
