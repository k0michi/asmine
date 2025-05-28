package com.koyomiji.asmine.query;

import com.koyomiji.asmine.common.*;
import com.koyomiji.asmine.tree.AbstractInsnNodeHelper;
import com.koyomiji.asmine.tuple.Pair;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.MethodNode;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/*
 * n is the number of real instructions in methodNode
 * | is the boundary between real instructions
 *   -1  0   1   ...  n-1  n
 * |   | _ | _ | ... | _ |   |
 * -1  0   1   2     n-1 n   n+1
 */
public class CodeManipulator {
  protected MethodNode methodNode;
  protected List<Set<Cursor>> cursors = new ArrayList<>();

  public CodeManipulator(MethodNode methodNode) {
    this.methodNode = methodNode;

    cursors.add(HashSetHelper.of(new Cursor()));

    for (int i = 0; i < methodNode.instructions.size(); i++) {
      if (AbstractInsnNodeHelper.isReal(methodNode.instructions.get(i))) {
        cursors.add(HashSetHelper.of(new Cursor()));
      }
    }

    cursors.add(HashSetHelper.of(new Cursor()));
    cursors.add(HashSetHelper.of(new Cursor()));
    recomputeCursors();
  }

  /*
   * Indices
   */

  public Cursor getCursor(int index) {
    return cursors.get(index + 1).iterator().next();
  }

  public Pair<Integer, Integer> getIndicesForCursors(Pair<Cursor, Cursor> cursors) {
    int begin = cursors.first.getFirstIndex();
    int end = cursors.second.getLastIndex();

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
      if (AbstractInsnNodeHelper.isReal(insns.get(i))) {
        cursors.add(index + 1 + 1, HashSetHelper.of(new Cursor()));
      }
    }

    InsnListHelper.insert(methodNode.instructions, InsnListHelper.getHeaded(methodNode.instructions, index), insns);
    recomputeCursors();
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
      if (AbstractInsnNodeHelper.isReal(insns.get(i))) {
        cursors.add(index + 1, HashSetHelper.of(new Cursor()));
      }
    }

    InsnListHelper.insertBefore(methodNode.instructions, InsnListHelper.getTailed(methodNode.instructions, index), insns);
    recomputeCursors();
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

  private int countRealInsns(List<AbstractInsnNode> insns) {
    int count = 0;

    for (int i = 0; i < insns.size(); i++) {
      if (AbstractInsnNodeHelper.isReal(insns.get(i))) {
        count++;
      }
    }

    return count;
  }

  public void replace(int begin, int end, List<AbstractInsnNode> insns) {
    Set<Cursor> endCursors = cursors.get(end + 1);
    ListHelper.removeRange(cursors, begin + 1 + 1, end + 1 + 1);
    InsnListHelper.removeRange(methodNode.instructions, InsnListHelper.getTailed(methodNode.instructions, begin), InsnListHelper.getTailed(methodNode.instructions, end));

    if (countRealInsns(insns) == 0) {
      cursors.get(begin + 1).addAll(endCursors);
    } else {
      cursors.add(begin + 1 + 1, endCursors);
    }

    for (int i = 0; i < insns.size() - 1; i++) {
      if (AbstractInsnNodeHelper.isReal(insns.get(i))) {
        cursors.add(begin + 1 + 1, HashSetHelper.of(new Cursor()));
      }
    }

    InsnListHelper.insert(methodNode.instructions, InsnListHelper.getHeaded(methodNode.instructions, begin - 1), insns);
    recomputeCursors();
  }

  /*
   * Misc
   */

  public MethodNode getMethodNode() {
    return methodNode;
  }

  private void recomputeCursors() {
    for (Set<Cursor> cursorSet : cursors) {
      for (Cursor cursor : cursorSet) {
        cursor.reset();
      }
    }

    // Logical index
    int i = 0;
    for (Cursor cursor : cursors.get(i)) {
      cursor.setIndex(-1);
    }

    for (int j = 0; j < methodNode.instructions.size(); j++) {
      if (AbstractInsnNodeHelper.isPseudo(methodNode.instructions.get(j))) {
        continue;
      }

      i++;
      for (Cursor cursor : cursors.get(i)) {
        cursor.setIndex(j);
      }
    }

    i++;
    for (Cursor cursor : cursors.get(i)) {
      cursor.setIndex(methodNode.instructions.size());
    }

    i++;
    for (Cursor cursor : cursors.get(i)) {
      cursor.setIndex(methodNode.instructions.size() + 1);
    }
  }
}
