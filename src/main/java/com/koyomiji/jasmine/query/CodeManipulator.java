package com.koyomiji.jasmine.query;

import com.koyomiji.jasmine.common.*;
import com.koyomiji.jasmine.tuple.Pair;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.MethodNode;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/*
 *   -1  0   1   ...  n-1  n
 * |   | _ | _ | ... | _ |   |
 * -1  0   1   2     n-1 n   n+1
 */
public class CodeManipulator {
  protected MethodNode methodNode;
  protected List<Set<Object>> indexSymbols = new ArrayList<>();

  public CodeManipulator(MethodNode methodNode) {
    this.methodNode = methodNode;

    indexSymbols.add(HashSetHelper.of(new Object()));

    for (AbstractInsnNode insn : new InsnListIterableAdapter(methodNode.instructions)) {
      indexSymbols.add(HashSetHelper.of(new Object()));
    }

    indexSymbols.add(HashSetHelper.of(new Object()));
    indexSymbols.add(HashSetHelper.of(new Object()));
  }

  public Object getIndexSymbol(int index) {
    return indexSymbols.get(index + 1).iterator().next();
  }

  public int getIndexForSymbol(Object symbol) {
    for (int i = 0; i < indexSymbols.size(); i++) {
      if (indexSymbols.get(i).contains(symbol)) {
        return i - 1;
      }
    }

    return -2;
  }

  public int getLastIndexForSymbol(Object symbol) {
    for (int i = indexSymbols.size() - 1; i >= 0; i--) {
      if (indexSymbols.get(i).contains(symbol)) {
        return i - 1;
      }
    }

    return -2;
  }

  public Pair<Integer, Integer> getIndicesForSymbols(Pair<Object, Object> symbols) {
    int begin = getIndexForSymbol(symbols.first);
    int end = getLastIndexForSymbol(symbols.second);

    if (begin == -2 || end == -2) {
      return null;
    }

    return Pair.of(begin, end);
  }

  public void removeInsn(int index) {
    removeInsns(index, index + 1);
  }

  public void removeInsns(int begin, int end) {
    replaceInsns(begin, end, new AbstractInsnNode[0]);
  }

  public void insertInsnsAfter(int index, AbstractInsnNode... insns) {
    insertInsnsAfter(index, ArrayHelper.toList(insns));
  }

  public void insertInsnsAfter(int index, InsnList insns) {
    insertInsnsAfter(index, new InsnListListAdapter(insns));
  }

  public void insertInsnsAfter(int index, List<AbstractInsnNode> insns) {
    for (int i = 0; i < insns.size(); i++) {
      indexSymbols.add(index + 1 + 1, HashSetHelper.of(new Object()));
    }

    InsnListHelper.insert(methodNode.instructions, InsnListHelper.getHeaded(methodNode.instructions, index), insns);
  }

  public void insertInsnsBefore(int index, AbstractInsnNode... insns) {
    insertInsnsBefore(index, ArrayHelper.toList(insns));
  }

  public void insertInsnsBefore(int index, InsnList insns) {
    insertInsnsBefore(index, new InsnListListAdapter(insns));
  }

  public void insertInsnsBefore(int index, List<AbstractInsnNode> insns) {
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
    replaceInsns(begin, end, ArrayHelper.toList(insns));
  }

  public void replaceInsns(int begin, int end, InsnList insns) {
    replaceInsns(begin, end, new InsnListListAdapter(insns));
  }

  public void replaceInsns(int begin, int end, List<AbstractInsnNode> insns) {
    Set<Object> endSymbols = indexSymbols.get(end + 1);
    ListHelper.removeRange(indexSymbols, begin + 1 + 1, end + 1 + 1);
    InsnListHelper.removeRange(methodNode.instructions, InsnListHelper.getTailed(methodNode.instructions, begin), InsnListHelper.getTailed(methodNode.instructions, end));

    if (insns.size() == 0) {
      indexSymbols.get(begin + 1).addAll(endSymbols);
    } else {
      indexSymbols.add(begin + 1 + 1, endSymbols);
    }

    for (int i = 0; i < insns.size() - 1; i++) {
      indexSymbols.add(begin + 1 + 1, HashSetHelper.of(new Object()));
    }

    InsnListHelper.insert(methodNode.instructions, InsnListHelper.getHeaded(methodNode.instructions, begin - 1), insns);
  }

  public MethodNode getMethodNode() {
    return methodNode;
  }
}
