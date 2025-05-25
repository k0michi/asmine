package com.koyomiji.asmine.common;

import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.InsnList;

import java.util.*;

public class InsnListListAdapter implements List<AbstractInsnNode> {
  private final InsnList insnList;

  public InsnListListAdapter(InsnList insnList) {
    this.insnList = insnList;
  }

  @Override
  public int size() {
    return insnList.size();
  }

  @Override
  public boolean isEmpty() {
    return insnList.size() == 0;
  }

  @Override
  public boolean contains(Object o) {
    if (o instanceof AbstractInsnNode) {
      return insnList.contains((AbstractInsnNode) o);
    }

    return false;
  }

  @Override
  public Iterator<AbstractInsnNode> iterator() {
    return insnList.iterator();
  }

  @Override
  public Object[] toArray() {
    return insnList.toArray();
  }

  @Override
  public <T> T[] toArray(T[] a) {
    if (a.length < size()) {
      return (T[]) toArray();
    }

    for (int i = 0; i < size(); i++) {
      a[i] = (T) insnList.get(i);
    }

    if (a.length > size()) {
      a[size()] = null;
    }

    return a;
  }

  @Override
  public boolean add(AbstractInsnNode abstractInsnNode) {
    insnList.add(abstractInsnNode);
    return true;
  }

  @Override
  public boolean remove(Object o) {
    if (o instanceof AbstractInsnNode) {
      insnList.remove((AbstractInsnNode) o);
      return true;
    }

    return false;
  }

  @Override
  public boolean containsAll(Collection<?> c) {
    for (Object o : c) {
      if (!contains(o)) {
        return false;
      }
    }

    return true;
  }

  @Override
  public boolean addAll(Collection<? extends AbstractInsnNode> c) {
    for (AbstractInsnNode insn : c) {
      add(insn);
    }

    return true;
  }

  @Override
  public boolean addAll(int index, Collection<? extends AbstractInsnNode> c) {
    for (AbstractInsnNode insn : c) {
      add(insn);
    }

    return true;
  }

  @Override
  public boolean removeAll(Collection<?> c) {
    boolean modified = false;

    for (Object o : c) {
      if (remove(o)) {
        modified = true;
      }
    }

    return modified;
  }

  @Override
  public boolean retainAll(Collection<?> c) {
    boolean modified = false;

    for (AbstractInsnNode insn : new InsnListIterableAdapter(insnList)) {
      if (!c.contains(insn)) {
        remove(insn);
        modified = true;
      }
    }

    return modified;
  }

  @Override
  public void clear() {
    insnList.clear();
  }

  @Override
  public AbstractInsnNode get(int index) {
    return insnList.get(index);
  }

  @Override
  public AbstractInsnNode set(int index, AbstractInsnNode element) {
    AbstractInsnNode oldInsn = insnList.get(index);
    insnList.set(insnList.get(index), element);
    return oldInsn;
  }

  @Override
  public void add(int index, AbstractInsnNode element) {
    insnList.insert(insnList.get(index), element);
  }

  @Override
  public AbstractInsnNode remove(int index) {
    AbstractInsnNode insn = insnList.get(index);
    insnList.remove(insn);
    return insn;
  }

  @Override
  public int indexOf(Object o) {
    if (o instanceof AbstractInsnNode) {
      return insnList.indexOf((AbstractInsnNode) o);
    }

    return -1;
  }

  @Override
  public int lastIndexOf(Object o) {
    if (o instanceof AbstractInsnNode) {
      return insnList.indexOf((AbstractInsnNode) o);
    }

    return -1;
  }

  @Override
  public ListIterator<AbstractInsnNode> listIterator() {
    return insnList.iterator();
  }

  @Override
  public ListIterator<AbstractInsnNode> listIterator(int index) {
    return insnList.iterator(index);
  }

  @Override
  public List<AbstractInsnNode> subList(int fromIndex, int toIndex) {
    ArrayList<AbstractInsnNode> subList = new ArrayList<>();

    for (int i = fromIndex; i < toIndex; i++) {
      subList.add(insnList.get(i));
    }

    return subList;
  }
}
