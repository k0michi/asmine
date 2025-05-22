package com.koyomiji.asmine.tree;

import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;

public class FieldNodeHelper {
  public static FieldNode clone(FieldNode fieldNode) {
    ClassNode clonedTo = new ClassNode();
    fieldNode.accept(clonedTo);
    return clonedTo.fields.get(0);
  }
}
