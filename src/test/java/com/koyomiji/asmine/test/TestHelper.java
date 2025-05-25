package com.koyomiji.asmine.test;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.MethodNode;

import java.io.IOException;
import java.io.InputStream;

public class TestHelper {
  public static ClassNode readClass(String className) {
    InputStream is = TestHelper.class.getClassLoader().getResourceAsStream(className.replaceAll("\\.", "/") + ".class");
    ClassNode node = new ClassNode();

    try {
      ClassReader reader = new ClassReader(is.readAllBytes());
      reader.accept(node, 0);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    return node;
  }

  public static FieldNode getField(ClassNode classNode, String name) {
    return classNode.fields.stream().filter(m -> m.name.equals(name)).findFirst().get();
  }

  public static MethodNode getMethod(ClassNode classNode, String name) {
    return classNode.methods.stream().filter(m -> m.name.equals(name)).findFirst().get();
  }
}
