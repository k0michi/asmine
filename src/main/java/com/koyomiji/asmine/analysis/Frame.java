package com.koyomiji.asmine.analysis;

import com.koyomiji.asmine.common.FrameHelper;
import com.koyomiji.asmine.common.ListHelper;
import org.objectweb.asm.ConstantDynamic;
import org.objectweb.asm.Handle;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.*;

import java.util.*;

public class Frame implements Cloneable {
  private ArrayList<Object> locals = new ArrayList<>();
  private LinkedList<Object> stack = new LinkedList<>();

  public Frame() {
  }

  public Frame(Object[] locals, Object[] stack) {
    this.stack.addAll(Arrays.asList(stack));
    this.locals.addAll(Arrays.asList(locals));
  }

  public Frame(String className, MethodNode methodNode) {
    if ((methodNode.access & Opcodes.ACC_STATIC) == 0) {
      if (Objects.equals(methodNode.name, "<init>")) {
        locals.add(Opcodes.UNINITIALIZED_THIS);
      } else {
        locals.add(className);
      }
    }

    addArgumentLocals(methodNode.desc);
  }

  public Frame(FrameNode frameNode) {
    for (Object local : frameNode.local) {
      locals.add(local);

      if (FrameHelper.getSize(local) == 2) {
        locals.add(Opcodes.TOP);
      }
    }

    for (Object stackItem : frameNode.stack) {
      stack.add(stackItem);

      if (FrameHelper.getSize(stackItem) == 2) {
        stack.add(Opcodes.TOP);
      }
    }
  }

  public List<Object> getLocals() {
    List<Object> results = new ArrayList<>(locals.size());

    for (int i = 0; i < locals.size(); ) {
      Object local = locals.get(i);
      results.add(local);
      i += FrameHelper.getSize(local);
    }

    return results;
  }

  public List<Object> getLocalsRaw() {
    return locals;
  }

  public int getLocalsSize() {
    return locals.size();
  }

  public List<Object> getStack() {
    List<Object> results = new ArrayList<>(stack.size());

    for (int i = 0; i < stack.size(); ) {
      Object stackItem = stack.get(i);
      results.add(stackItem);
      i += FrameHelper.getSize(stackItem);
    }

    return results;
  }

  public List<Object> getStackRaw() {
    return stack;
  }

  public int getStackSize() {
    return stack.size();
  }

  public FrameNode toFrameNode() {
    List<Object> local = getLocals();
    List<Object> stack = getStack();

    return new FrameNode(
        Opcodes.F_NEW,
        local.size(),
        local.toArray(new Object[0]),
        stack.size(),
        stack.toArray(new Object[0])
    );
  }

  private Object getLocal(int index) {
    if (index < 0 || index >= locals.size()) {
      throw new IndexOutOfBoundsException("Local variable index out of bounds: " + index);
    }

    return locals.get(index);
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

  public void addArgumentLocals(String methodDescriptor) {
    Type[] argumentTypes = Type.getArgumentTypes(methodDescriptor);
    int firstIndex = locals.size();

    for (Type type : argumentTypes) {
      for (Object verificationType : getVerificationTypesForType(type)) {
        int index = locals.size();
        allocateLocals(index);
        locals.set(index, verificationType);
      }
    }

    if (locals.size() > firstIndex) {
      modifyPreIndex(firstIndex - 1);
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

  public void execute(LabelNode label, AbstractInsnNode insn) {
    switch (insn.getOpcode()) {
      case Opcodes.NOP:
      case Opcodes.INEG:
      case Opcodes.LNEG:
      case Opcodes.FNEG:
      case Opcodes.DNEG:
      case Opcodes.I2B:
      case Opcodes.I2C:
      case Opcodes.I2S:
      case Opcodes.GOTO:
      case Opcodes.RETURN:
        break;
      case Opcodes.ACONST_NULL:
        push(Opcodes.NULL);
        break;
      case Opcodes.ICONST_M1:
      case Opcodes.ICONST_0:
      case Opcodes.ICONST_1:
      case Opcodes.ICONST_2:
      case Opcodes.ICONST_3:
      case Opcodes.ICONST_4:
      case Opcodes.ICONST_5:
      case Opcodes.BIPUSH:
      case Opcodes.SIPUSH:
      case Opcodes.ILOAD:
        push(Opcodes.INTEGER);
        break;
      case Opcodes.LCONST_0:
      case Opcodes.LCONST_1:
      case Opcodes.LLOAD:
        push2(Opcodes.LONG);
        break;
      case Opcodes.FCONST_0:
      case Opcodes.FCONST_1:
      case Opcodes.FCONST_2:
      case Opcodes.FLOAD:
        push(Opcodes.FLOAT);
        break;
      case Opcodes.DCONST_0:
      case Opcodes.DCONST_1:
      case Opcodes.DLOAD:
        push2(Opcodes.DOUBLE);
        break;
      case Opcodes.LDC: {
        Object value = ((LdcInsnNode) insn).cst;

        if (value instanceof Integer) {
          push(Opcodes.INTEGER);
        } else if (value instanceof Long) {
          push2(Opcodes.LONG);
        } else if (value instanceof Float) {
          push(Opcodes.FLOAT);
        } else if (value instanceof Double) {
          push2(Opcodes.DOUBLE);
        } else if (value instanceof String) {
          push("java/lang/String");
        } else if (value instanceof Type) {
          switch (((Type) value).getSort()) {
            case Type.METHOD:
              push("java/lang/invoke/MethodType");
              break;
            default:
              push("java/lang/Class");
              break;
          }
        } else if (value instanceof Handle) {
          push("java/lang/invoke/MethodHandle");
        } else if (value instanceof ConstantDynamic) {
          push(((ConstantDynamic) value).getDescriptor());
        } else {
          throw new IllegalArgumentException("Unsupported LDC value: " + value);
        }
        break;
      }
      case Opcodes.ALOAD:
        push(getLocal(((VarInsnNode) insn).var));
        break;
      case Opcodes.LALOAD:
      case Opcodes.D2L:
        pop2();
        push2(Opcodes.LONG);
        break;
      case Opcodes.DALOAD:
      case Opcodes.L2D:
        pop2();
        push2(Opcodes.DOUBLE);
        break;
      case Opcodes.AALOAD: {
        pop();
        Object arrayref = pop();
        if (arrayref == Opcodes.NULL) {
          push(Opcodes.NULL);
        } else {
          push(Type.getType(((String) arrayref)).getElementType().getDescriptor());
        }
        break;
      }
      case Opcodes.ISTORE:
      case Opcodes.FSTORE:
      case Opcodes.ASTORE: {
        Object value = pop();
        setLocal(((VarInsnNode) insn).var, value);
        break;
      }
      case Opcodes.LSTORE:
      case Opcodes.DSTORE: {
        List<Object> value = pop2();
        setLocal2(((VarInsnNode) insn).var, value.get(0));
        break;
      }
      case Opcodes.IASTORE:
      case Opcodes.BASTORE:
      case Opcodes.CASTORE:
      case Opcodes.SASTORE:
      case Opcodes.FASTORE:
      case Opcodes.AASTORE:
        pop();
        pop();
        pop();
        break;
      case Opcodes.LASTORE:
      case Opcodes.DASTORE:
        pop2();
        pop();
        pop();
        break;
      case Opcodes.POP:
      case Opcodes.IFEQ:
      case Opcodes.IFNE:
      case Opcodes.IFLT:
      case Opcodes.IFGE:
      case Opcodes.IFGT:
      case Opcodes.IFLE:
      case Opcodes.IRETURN:
      case Opcodes.FRETURN:
      case Opcodes.ARETURN:
      case Opcodes.TABLESWITCH:
      case Opcodes.LOOKUPSWITCH:
      case Opcodes.ATHROW:
      case Opcodes.MONITORENTER:
      case Opcodes.MONITOREXIT:
      case Opcodes.IFNULL:
      case Opcodes.IFNONNULL:
        pop();
        break;
      case Opcodes.POP2:
      case Opcodes.IF_ICMPEQ:
      case Opcodes.IF_ICMPNE:
      case Opcodes.IF_ICMPLT:
      case Opcodes.IF_ICMPGE:
      case Opcodes.IF_ICMPGT:
      case Opcodes.IF_ICMPLE:
      case Opcodes.IF_ACMPEQ:
      case Opcodes.IF_ACMPNE:
      case Opcodes.LRETURN:
      case Opcodes.DRETURN:
        pop();
        pop();
        break;
      case Opcodes.DUP: {
        Object value = pop();
        push(value);
        push(value);
        break;
      }
      case Opcodes.DUP_X1: {
        Object value1 = pop();
        Object value2 = pop();
        push(value1);
        push(value2);
        push(value1);
        break;
      }
      case Opcodes.DUP_X2: {
        Object value1 = pop();
        Object value2 = pop();
        Object value3 = pop();
        push(value1);
        push(value3);
        push(value2);
        push(value1);
        break;
      }
      case Opcodes.DUP2: {
        Object value1 = pop();
        Object value2 = pop();
        push(value2);
        push(value1);
        push(value2);
        push(value1);
        break;
      }
      case Opcodes.DUP2_X1: {
        Object value1 = pop();
        Object value2 = pop();
        Object value3 = pop();
        push(value2);
        push(value1);
        push(value3);
        push(value2);
        push(value1);
        break;
      }
      case Opcodes.DUP2_X2: {
        Object value1 = pop();
        Object value2 = pop();
        Object value3 = pop();
        Object value4 = pop();
        push(value2);
        push(value1);
        push(value4);
        push(value3);
        push(value2);
        push(value1);
        break;
      }
      case Opcodes.SWAP: {
        Object value1 = pop();
        Object value2 = pop();
        push(value1);
        push(value2);
        break;
      }
      case Opcodes.IALOAD:
      case Opcodes.BALOAD:
      case Opcodes.CALOAD:
      case Opcodes.SALOAD:
      case Opcodes.IADD:
      case Opcodes.ISUB:
      case Opcodes.IMUL:
      case Opcodes.IDIV:
      case Opcodes.IREM:
      case Opcodes.IAND:
      case Opcodes.IOR:
      case Opcodes.IXOR:
      case Opcodes.ISHL:
      case Opcodes.ISHR:
      case Opcodes.IUSHR:
      case Opcodes.L2I:
      case Opcodes.D2I:
      case Opcodes.FCMPL:
      case Opcodes.FCMPG:
        pop();
        pop();
        push(Opcodes.INTEGER);
        break;
      case Opcodes.LADD:
      case Opcodes.LSUB:
      case Opcodes.LMUL:
      case Opcodes.LDIV:
      case Opcodes.LREM:
      case Opcodes.LAND:
      case Opcodes.LOR:
      case Opcodes.LXOR:
        pop2();
        pop2();
        push2(Opcodes.LONG);
        break;
      case Opcodes.FALOAD:
      case Opcodes.FADD:
      case Opcodes.FSUB:
      case Opcodes.FMUL:
      case Opcodes.FDIV:
      case Opcodes.FREM:
      case Opcodes.L2F:
      case Opcodes.D2F:
        pop();
        pop();
        push(Opcodes.FLOAT);
        break;
      case Opcodes.DADD:
      case Opcodes.DSUB:
      case Opcodes.DMUL:
      case Opcodes.DDIV:
      case Opcodes.DREM:
        pop2();
        pop2();
        push2(Opcodes.DOUBLE);
        break;
      case Opcodes.LSHL:
      case Opcodes.LSHR:
      case Opcodes.LUSHR:
        pop();
        pop2();
        push2(Opcodes.LONG);
        break;
      case Opcodes.IINC:
        setLocal(((VarInsnNode) insn).var, Opcodes.INTEGER);
        break;
      case Opcodes.I2L:
      case Opcodes.F2L:
        pop();
        push2(Opcodes.LONG);
        break;
      case Opcodes.I2F:
        pop();
        push(Opcodes.FLOAT);
        break;
      case Opcodes.I2D:
      case Opcodes.F2D:
        pop();
        push2(Opcodes.DOUBLE);
        break;
      case Opcodes.F2I:
      case Opcodes.ARRAYLENGTH:
      case Opcodes.INSTANCEOF:
        pop();
        push(Opcodes.INTEGER);
        break;
      case Opcodes.LCMP:
      case Opcodes.DCMPL:
      case Opcodes.DCMPG:
        pop2();
        pop2();
        push(Opcodes.INTEGER);
        break;
      case Opcodes.JSR:
      case Opcodes.RET:
        throw new IllegalArgumentException("JSR/RET are not supported");
      case Opcodes.GETSTATIC:
        pushDescriptor(((FieldInsnNode) insn).desc);
        break;
      case Opcodes.PUTSTATIC:
        popDescriptor(((FieldInsnNode) insn).desc);
        break;
      case Opcodes.GETFIELD:
        pop();
        pushDescriptor(((FieldInsnNode) insn).desc);
        break;
      case Opcodes.PUTFIELD:
        popDescriptor(((FieldInsnNode) insn).desc);
        pop();
        break;
      case Opcodes.INVOKEVIRTUAL:
      case Opcodes.INVOKESPECIAL:
      case Opcodes.INVOKEINTERFACE: {
        popMethodArguments(((MethodInsnNode) insn).desc);
        pop();
        pushMethodReturn(((MethodInsnNode) insn).desc);
        break;
      }
      case Opcodes.INVOKESTATIC:
      case Opcodes.INVOKEDYNAMIC: {
        popMethodArguments(((MethodInsnNode) insn).desc);
        pushMethodReturn(((MethodInsnNode) insn).desc);
        break;
      }
      case Opcodes.NEW:
        push(label);
        break;
      case Opcodes.NEWARRAY:
        pop();
        switch (((IntInsnNode) insn).operand) {
          case Opcodes.T_BOOLEAN:
            push("[Z");
            break;
          case Opcodes.T_CHAR:
            push("[C");
            break;
          case Opcodes.T_BYTE:
            push("[B");
            break;
          case Opcodes.T_SHORT:
            push("[S");
            break;
          case Opcodes.T_INT:
            push("[I");
            break;
          case Opcodes.T_FLOAT:
            push("[F");
            break;
          case Opcodes.T_DOUBLE:
            push("[D");
            break;
          case Opcodes.T_LONG:
            push("[J");
            break;
          default:
            throw new IllegalArgumentException("Unsupported new array type: " + ((IntInsnNode) insn).operand);
        }
        break;
      case Opcodes.ANEWARRAY: {
        pop();
        push("[" + ((TypeInsnNode) insn).desc);
        break;
      }
      case Opcodes.CHECKCAST:
        pop();
        push(((TypeInsnNode) insn).desc);
        break;
      case Opcodes.MULTIANEWARRAY:
        for (int i = 0; i < ((MultiANewArrayInsnNode) insn).dims; i++) {
          pop();
        }
        push(((MultiANewArrayInsnNode) insn).desc);
        break;
      default:
        throw new IllegalArgumentException("Unsupported opcode: " + insn.getOpcode());
    }
  }

  @Override
  public Frame clone() {
    try {
      Frame cloned = (Frame) super.clone();
      cloned.locals = new ArrayList<>(this.locals);
      cloned.stack = new LinkedList<>(this.stack);
      return cloned;
    } catch (CloneNotSupportedException e) {
      throw new AssertionError();
    }
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Frame frame = (Frame) o;
    return Objects.equals(locals, frame.locals) && Objects.equals(stack, frame.stack);
  }

  @Override
  public int hashCode() {
    return Objects.hash(locals, stack);
  }
}
