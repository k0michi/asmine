package com.koyomiji.asmine.common;

import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.InsnList;

import java.util.Iterator;

public class InsnListIterableAdapter implements Iterable<AbstractInsnNode> {
  private final InsnList insnList;

  public InsnListIterableAdapter(InsnList insnList) {
    this.insnList = insnList;
  }

  @Override
  public Iterator<AbstractInsnNode> iterator() {
    return insnList.iterator();
  }
}
