package com.koyomiji.asmine.tree;

import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.MethodNode;

import java.util.Optional;

public class ClassNodeHelper {
  public static ClassNode clone(ClassNode classNode) {
    ClassNode cloned = new ClassNode();
    classNode.accept(cloned);
    return cloned;
  }

  public static Optional<FieldNode> getField(ClassNode classNode, String name) {
    Optional<FieldNode> result = Optional.empty();

    for (FieldNode f : classNode.fields) {
      if (f.name.equals(name)) {
        result = Optional.of(f);
        break;
      }
    }

    return result;
  }

  public static Optional<MethodNode> getMethod(ClassNode classNode, String name, String desc) {
    Optional<MethodNode> result = Optional.empty();

    for (MethodNode m : classNode.methods) {
      if (m.name.equals(name) && m.desc.equals(desc)) {
        result = Optional.of(m);
        break;
      }
    }

    return result;
  }
}
