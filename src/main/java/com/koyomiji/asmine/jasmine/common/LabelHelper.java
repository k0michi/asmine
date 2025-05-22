package com.koyomiji.asmine.common;

import org.objectweb.asm.Label;
import org.objectweb.asm.tree.LabelNode;

import java.util.List;

public class LabelHelper {
  /*
   * Copied from MethodNode.getLabelNode
   */
  @Deprecated
  public static LabelNode getLabelNode(Label label) {
      if (!(label.info instanceof LabelNode)) {
        label.info = new LabelNode();
      }

      return (LabelNode) label.info;
    }

  @Deprecated
    public static List<LabelNode> toLabelNodes(List<Label> labels) {
      List<LabelNode> labelNodes = new java.util.ArrayList<>();

      for (Label label : labels) {
        labelNodes.add(getLabelNode(label));
      }

      return labelNodes;
    }

  @Deprecated
    public static LabelNode[] toLabelNodes(Label[] labels) {
      LabelNode[] labelNodes = new LabelNode[labels.length];

      for (int i = 0; i < labels.length; i++) {
        labelNodes[i] = getLabelNode(labels[i]);
      }

      return labelNodes;
    }
}
