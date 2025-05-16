package com.koyomiji.jasmine.query;

import com.koyomiji.jasmine.common.ArrayHelper;
import com.koyomiji.jasmine.common.HashSetHelper;
import com.koyomiji.jasmine.common.InsnListHelper;
import com.koyomiji.jasmine.common.ListHelper;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.MethodNode;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class MethodManipulator {
  protected MethodNode methodNode;
  protected List<Set<Object>> indexSymbols = new ArrayList<>();

  public MethodManipulator(MethodNode methodNode) {
    this.methodNode = methodNode;

    for (AbstractInsnNode insn : methodNode.instructions) {
      if (insn instanceof LabelNode) {
        indexSymbols.add(HashSetHelper.of(new Object()));
      }
    }

    indexSymbols.add(HashSetHelper.of(new Object()));
  }

  public Object getIndexSymbol(int index) {
    return indexSymbols.get(index).iterator().next();
  }

  public int getIndexForSymbol(Object symbol) {
    for (int i = 0; i < indexSymbols.size(); i++) {
      if (indexSymbols.get(i).contains(symbol)) {
        return i;
      }
    }

    return -1;
  }

  public void removeInsn(int index) {
    removeInsns(index, index + 1);
  }

  public void removeInsns(int begin, int end) {
    indexSymbols.get(begin).addAll(indexSymbols.get(end));
    ListHelper.removeRange(indexSymbols, begin + 1, end + 1);
    InsnListHelper.removeRange(methodNode.instructions, InsnListHelper.getTailed(methodNode.instructions, begin), InsnListHelper.getTailed(methodNode.instructions, end));
  }

  public void insertInsnsAfter(int index, AbstractInsnNode... insns) {
    for (int i = 0; i < insns.length; i++) {
      indexSymbols.add(index + 1, HashSetHelper.of(new Object()));
    }

    InsnListHelper.insert(methodNode.instructions, InsnListHelper.getHeaded(methodNode.instructions, index), ArrayHelper.toList(insns));
  }

  public void insertInsnsAfter(int index, InsnList insns) {
    for (int i = 0; i < insns.size(); i++) {
      indexSymbols.add(index + 1, HashSetHelper.of(new Object()));
    }

    InsnListHelper.insert(methodNode.instructions, InsnListHelper.getHeaded(methodNode.instructions, index), insns);
  }

  public void insertInsnsBefore(int index, AbstractInsnNode... insns) {
    for (int i = 0; i < insns.length; i++) {
      indexSymbols.add(index + 1, HashSetHelper.of(new Object()));
    }

    InsnListHelper.insertBefore(methodNode.instructions, InsnListHelper.getTailed(methodNode.instructions, index), ArrayHelper.toList(insns));
  }

  public void insertInsnsBefore(int index, InsnList insns) {
    for (int i = 0; i < insns.size(); i++) {
      indexSymbols.add(index + 1, HashSetHelper.of(new Object()));
    }

    InsnListHelper.insertBefore(methodNode.instructions, InsnListHelper.getTailed(methodNode.instructions, index), insns);
  }

  public void addInsns(AbstractInsnNode... insns) {
    insertInsnsBefore(methodNode.instructions.size(), insns);
  }

  public void addInsnsFirst(AbstractInsnNode... insns) {
    insertInsnsAfter(-1, insns);
  }

  public void addInsnsLast(AbstractInsnNode... insns) {
    addInsns(insns);
  }

  public void replaceInsn(int index, AbstractInsnNode... insns) {
    replaceInsns(index, index + 1, insns);
  }

  public void replaceInsns(int begin, int end, AbstractInsnNode... insns) {
    insertInsnsBefore(begin, insns);
    removeInsns(begin + insns.length, end + insns.length);
  }

  public void replaceInsns(int begin, int end, InsnList insns) {
    int length = insns.size();
    insertInsnsBefore(begin, insns);
    removeInsns(begin + length, end + length);
  }

  public MethodNode getMethodNode() {
    return methodNode;
  }
}
