package com.koyomiji.asmine.tree;

import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

public class MethodNodeHelper {
  public static MethodNode clone(MethodNode methodNode) {
    ClassNode clonedTo = new ClassNode();
    methodNode.accept(clonedTo);
    return clonedTo.methods.get(0);
  }
}
