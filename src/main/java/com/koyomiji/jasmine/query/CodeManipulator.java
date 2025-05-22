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

  /*
   * Indices
   */

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

  /*
   * Remove
   */

  public void remove(int index) {
    remove(index, index + 1);
  }

  public void remove(int begin, int end) {
    replace(begin, end, new AbstractInsnNode[0]);
  }

  /*
    * InsertAfter
   */

  public void insertAfter(int index, AbstractInsnNode... insns) {
    insertAfter(index, ArrayHelper.toList(insns));
  }

  public void insertAfter(int index, InsnList insns) {
    insertAfter(index, new InsnListListAdapter(insns));
  }

  public void insertAfter(int index, List<AbstractInsnNode> insns) {
    for (int i = 0; i < insns.size(); i++) {
      indexSymbols.add(index + 1 + 1, HashSetHelper.of(new Object()));
    }

    InsnListHelper.insert(methodNode.instructions, InsnListHelper.getHeaded(methodNode.instructions, index), insns);
  }

  /*
   * InsertBefore
   */

  public void insertBefore(int index, AbstractInsnNode... insns) {
    insertBefore(index, ArrayHelper.toList(insns));
  }

  public void insertBefore(int index, InsnList insns) {
    insertBefore(index, new InsnListListAdapter(insns));
  }

  public void insertBefore(int index, List<AbstractInsnNode> insns) {
    for (int i = 0; i < insns.size(); i++) {
      indexSymbols.add(index + 1, HashSetHelper.of(new Object()));
    }

    InsnListHelper.insertBefore(methodNode.instructions, InsnListHelper.getTailed(methodNode.instructions, index), insns);
  }

  /*
   * AddFirst
   */

  public void addFirst(AbstractInsnNode... insns) {
    insertAfter(-1, insns);
  }

  public void addFirst(InsnList insns) {
    insertAfter(-1, insns);
  }

  public void addFirst(List<AbstractInsnNode> insns) {
    insertAfter(-1, insns);
  }

  /*
   * AddLast
   */

  public void addLast(AbstractInsnNode... insns) {
    insertBefore(methodNode.instructions.size(), insns);
  }

  public void addLast(InsnList insns) {
    insertBefore(methodNode.instructions.size(), insns);
  }

  public void addLast(List<AbstractInsnNode> insns) {
    insertBefore(methodNode.instructions.size(), insns);
  }

  /*
   * Replace
   */

  public void replace(int index, AbstractInsnNode... insns) {
    replace(index, index + 1, insns);
  }

  public void replace(int index, InsnList insns) {
    replace(index, index + 1, insns);
  }

  public void replace(int index, List<AbstractInsnNode> insns) {
    replace(index, index + 1, insns);
  }

  public void replace(int begin, int end, AbstractInsnNode... insns) {
    replace(begin, end, ArrayHelper.toList(insns));
  }

  public void replace(int begin, int end, InsnList insns) {
    replace(begin, end, new InsnListListAdapter(insns));
  }

  public void replace(int begin, int end, List<AbstractInsnNode> insns) {
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

  /*
   * Misc
   */

  public MethodNode getMethodNode() {
    return methodNode;
  }
}
