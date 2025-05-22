package com.koyomiji.asmine.common;

import com.koyomiji.asmine.tree.AbstractInsnNodeHelper;
import com.koyomiji.asmine.tuple.Pair;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.MethodNode;

import java.util.List;

public class InsnListHelper {
  public static InsnList of(AbstractInsnNode... insns) {
    InsnList insnList = new InsnList();

    for (AbstractInsnNode insn : insns) {
      insnList.add(insn);
    }

    return insnList;
  }

  public static void insert(InsnList self, AbstractInsnNode previousInsn, AbstractInsnNode insnNode) {
    if (previousInsn == null) {
      self.insert(insnNode);
    } else {
      self.insert(previousInsn, insnNode);
    }
  }

  public static void insert(InsnList self, AbstractInsnNode previousInsn, InsnList insnList) {
    if (previousInsn == null) {
      self.insert(insnList);
    } else {
      self.insert(previousInsn, insnList);
    }
  }

  public static void insert(InsnList self, AbstractInsnNode previousInsn, List<AbstractInsnNode> insnList) {
    for (AbstractInsnNode insn : insnList) {
      insert(self, previousInsn, insn);
      previousInsn = insn;
    }
  }

  public static void insertBefore(InsnList self, AbstractInsnNode nextInsn, AbstractInsnNode insnNode) {
    if (nextInsn == null) {
      self.add(insnNode);
    } else {
      self.insertBefore(nextInsn, insnNode);
    }
  }

  public static void insertBefore(InsnList self, AbstractInsnNode nextInsn, InsnList insnList) {
    if (nextInsn == null) {
      self.add(insnList);
    } else {
      self.insertBefore(nextInsn, insnList);
    }
  }

  public static void insertBefore(InsnList self, AbstractInsnNode nextInsn, List<AbstractInsnNode> insnList) {
    for (AbstractInsnNode insn : insnList) {
      insertBefore(self, nextInsn, insn);
    }
  }

  public static int indexOf(InsnList self, AbstractInsnNode insn) {
    if (insn == null) {
      return self.size();
    }

    return self.indexOf(insn);
  }

  public static AbstractInsnNode getTailed(InsnList self, int index) {
    if (index == self.size()) {
      return null;
    }

    return self.get(index);
  }

  public static AbstractInsnNode getHeaded(InsnList self, int index) {
    if (index == -1) {
      return null;
    }

    return self.get(index);
  }

  public static AbstractInsnNode getPrevious(InsnList self, AbstractInsnNode insn) {
    if (insn == null) {
      return self.getLast();
    }

    return insn.getPrevious();
  }

  public static AbstractInsnNode getNext(InsnList self, AbstractInsnNode insn) {
    if (insn == null) {
      return self.getFirst();
    }

    return insn.getNext();
  }

  public static void removeRange(InsnList self, AbstractInsnNode begin, AbstractInsnNode end) {
    while (begin != end) {
      AbstractInsnNode next = begin.getNext();
      self.remove(begin);
      begin = next;
    }
  }

  public static void replaceRange(InsnList self, AbstractInsnNode begin, AbstractInsnNode end, AbstractInsnNode insn) {
    if (begin == end) {
      return;
    }

    removeRange(self, begin, end);
    insertBefore(self, end, insn);
  }

  public static Pair<AbstractInsnNode, AbstractInsnNode> replaceRange(InsnList self, AbstractInsnNode begin, AbstractInsnNode end, InsnList fragment) {
    removeRange(self, begin, end);
    insertBefore(self, end, fragment);
    return Pair.of(fragment.getFirst(), end);
  }

  public static Pair<AbstractInsnNode, AbstractInsnNode> replaceRange(InsnList self, AbstractInsnNode begin, AbstractInsnNode end, List<AbstractInsnNode> fragment) {
    removeRange(self, begin, end);
    insertBefore(self, end, fragment);
    return Pair.of(fragment.get(0), end);
  }

  public static InsnList clone(InsnList self) {
    MethodNode clonedTo = new MethodNode();
    self.accept(clonedTo);
    return clonedTo.instructions;
  }

  public static int compareIndex(InsnList self, AbstractInsnNode insn1, AbstractInsnNode insn2) {
    return indexOf(self, insn1) - indexOf(self, insn2);
  }

  public static AbstractInsnNode getBegin(InsnList self) {
    return self.getFirst();
  }

  public static AbstractInsnNode getEnd(InsnList self) {
    return self.getLast().getNext();
  }

  public static AbstractInsnNode getNextReal(InsnList self, AbstractInsnNode insn) {
    if (insn == null) {
      return self.getFirst();
    }

    AbstractInsnNode next = insn.getNext();

    while (next != null && !AbstractInsnNodeHelper.isReal(next)) {
      next = next.getNext();
    }

    return next;
  }

  public static AbstractInsnNode getPreviousReal(InsnList self, AbstractInsnNode insn) {
    if (insn == null) {
      return self.getLast();
    }

    AbstractInsnNode previous = insn.getPrevious();

    while (previous != null && !AbstractInsnNodeHelper.isReal(previous)) {
      previous = previous.getPrevious();
    }

    return previous;
  }
}
