package com.koyomiji.asmine.analysis;

import com.koyomiji.asmine.common.FrameHelper;
import com.koyomiji.asmine.common.ListHelper;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.FrameNode;

import java.util.*;

public class FlowAnalyzerThread implements Cloneable {
  private AbstractInsnNode currentInsn;
  private ArrayList<Object> locals;
  private LinkedList<Object> stack;

  public FlowAnalyzerThread(AbstractInsnNode currentInsn) {
    this.currentInsn = currentInsn;
    this.locals = new ArrayList<>();
    this.stack = new LinkedList<>();
  }

  public FlowAnalyzerThread(AbstractInsnNode currentInsn, List<Object> locals, List<Object> stack) {
    this.currentInsn = currentInsn;
    this.locals = new ArrayList<>(locals);
    this.stack = new LinkedList<>(stack);
  }

  public AbstractInsnNode getCurrentInsn() {
    return currentInsn;
  }

  public void setCurrentInsn(AbstractInsnNode currentInsn) {
    this.currentInsn = currentInsn;
  }

  public FrameNode toFrameNode() {
    List<Object> local = FrameHelper.toLogicalForm(this.locals);
    List<Object> stack = FrameHelper.toLogicalForm(this.stack);

    return new FrameNode(
            Opcodes.F_NEW,
            local.size(),
            local.toArray(new Object[0]),
            stack.size(),
            stack.toArray(new Object[0])
    );
  }

  public int getLocalSize() {
    return locals.size();
  }

  public int getStackSize() {
    return stack.size();
  }

  public List<Object> getLocals() {
    return FrameHelper.toLogicalForm(locals);
  }

  public List<Object> getStack() {
    return FrameHelper.toLogicalForm(stack);
  }

  public Object getLocal(int index) {
    if (index < 0) {
      throw new IndexOutOfBoundsException("Local variable index out of bounds: " + index);
    }

    if (index >= locals.size()) {
      return Opcodes.TOP;
    }

    return locals.get(index);
  }

  public Object getStack(int index) {
    if (index < 0) {
      throw new IndexOutOfBoundsException("Stack index out of bounds: " + index);
    }

    if (index >= stack.size()) {
      return Opcodes.TOP;
    }

    return stack.get(index);
  }

  public void setLocals(List<Object> locals) {
    this.locals = new ArrayList<>(locals);
  }

  public void setStack(List<Object> stack) {
    this.stack = new LinkedList<>(stack);
  }

  private void allocateLocals(int index) {
    while (index >= locals.size()) {
      locals.add(Opcodes.TOP);
    }
  }

  private void allocateStack(int index) {
    while (index >= stack.size()) {
      stack.add(Opcodes.TOP);
    }
  }

  public void compactLocals(Object defaultValue) {
    while (locals.size() >= 1 && ListHelper.at(locals, -1) == defaultValue &&
            (locals.size() == 1 || (locals.size() >= 2 && FrameHelper.getSize(ListHelper.at(locals, -2)) == 1))) {
      locals.remove(locals.size() - 1);
    }
  }

  private void modifyPreIndex(int index) {
    if (index >= 0 && FrameHelper.getSize(getLocal(index)) == 2) {
      locals.set(index, Opcodes.TOP);
    }
  }

  public void setLocal(int index, Object value) {
    allocateLocals(index);
    locals.set(index, value);
    modifyPreIndex(index - 1);
  }

  public void setLocal2(int index, Object value) {
    allocateLocals(index + 1);
    locals.set(index, value);
    locals.set(index + 1, Opcodes.TOP);
    modifyPreIndex(index - 1);
  }

  public void setStack(int index, Object value) {
    allocateStack(index);
    stack.set(index, value);
  }

  public void addLocal(Object value) {
    int index = locals.size();
    setLocal(index, value);
  }

  public void addArgumentLocals(String methodDescriptor) {
    Type[] argumentTypes = Type.getArgumentTypes(methodDescriptor);

    for (Type type : argumentTypes) {
      for (Object verificationType : getVerificationTypesForType(type)) {
        addLocal(verificationType);
      }
    }
  }

  public void push(Object value) {
    stack.add(value);
  }

  public void pushDescriptor(String descriptor) {
    Type type = Type.getType(descriptor);

    for (Object verificationType : getVerificationTypesForType(type)) {
      push(verificationType);
    }
  }

  public void pushMethodReturn(String methodDescriptor) {
    Type returnType = Type.getReturnType(methodDescriptor);

    for (Object verificationType : getVerificationTypesForType(returnType)) {
      push(verificationType);
    }
  }

  public void setStackTops(List<Object> verificationTypes) {
    int begin = stack.size() - verificationTypes.size();

    for (int i = 0; i < verificationTypes.size(); i++) {
      stack.set(begin + i, verificationTypes.get(i));
    }
  }

  private List<Object> getVerificationTypesForType(Type type) {
    if (Objects.equals(type, Type.VOID_TYPE)) {
      return Arrays.asList();
    } else if (Objects.equals(type, Type.BOOLEAN_TYPE) ||
            Objects.equals(type, Type.CHAR_TYPE) ||
            Objects.equals(type, Type.BYTE_TYPE) ||
            Objects.equals(type, Type.SHORT_TYPE) ||
            Objects.equals(type, Type.INT_TYPE)) {
      return Arrays.asList(Opcodes.INTEGER);
    } else if (Objects.equals(type, Type.FLOAT_TYPE)) {
      return Arrays.asList(Opcodes.FLOAT);
    } else if (Objects.equals(type, Type.LONG_TYPE)) {
      return Arrays.asList(Opcodes.LONG, Opcodes.TOP);
    } else if (Objects.equals(type, Type.DOUBLE_TYPE)) {
      return Arrays.asList(Opcodes.DOUBLE, Opcodes.TOP);
    } else if (type.getSort() == Type.OBJECT || type.getSort() == Type.ARRAY) {
      return Arrays.asList(type.getInternalName());
    } else {
      throw new IllegalArgumentException("Unsupported type: " + type);
    }
  }

  public void push2(int value) {
    push(value);
    push(Opcodes.TOP);
  }

  public Object pop() {
    if (stack.isEmpty()) {
      throw new IllegalStateException("Stack is empty");
    }

    return stack.removeLast();
  }

  public void popDescriptor(String descriptor) {
    Type type = Type.getType(descriptor);

    for (Object verificationType : getVerificationTypesForType(type)) {
      pop();
    }
  }

  public void popMethodArguments(String methodDescriptor) {
    Type[] argumentTypes = Type.getArgumentTypes(methodDescriptor);

    for (Type type : argumentTypes) {
      for (Object verificationType : getVerificationTypesForType(type)) {
        pop();
      }
    }
  }

  public List<Object> pop2() {
    if (stack.size() < 2) {
      throw new IllegalStateException("Stack does not have enough elements to pop two");
    }

    Object value2 = stack.removeLast();
    Object value1 = stack.removeLast();
    return Arrays.asList(value1, value2);
  }

  @Override
  protected FlowAnalyzerThread clone() {
    try {
      FlowAnalyzerThread clone = (FlowAnalyzerThread) super.clone();
      clone.locals = (ArrayList<Object>) this.locals.clone();
      clone.stack = (LinkedList<Object>) this.stack.clone();
      return clone;
    } catch (CloneNotSupportedException e) {
      throw new AssertionError(e);
    }
  }
}
