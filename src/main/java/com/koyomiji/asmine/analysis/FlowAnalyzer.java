package com.koyomiji.asmine.analysis;

import com.koyomiji.asmine.common.ArrayListHelper;
import com.koyomiji.asmine.tree.AbstractInsnNodeHelper;
import com.koyomiji.asmine.tuple.Pair;
import org.objectweb.asm.ConstantDynamic;
import org.objectweb.asm.Handle;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.*;

import java.util.*;

public class FlowAnalyzer {
  private String className;
  private MethodNode methodNode;
  private Map<LabelNode, Integer> labelIndices;

  public FlowAnalyzer(String className, MethodNode methodNode) {
    this.className = className;
    this.methodNode = methodNode;
    this.labelIndices = getLabelIndices();
  }

  /*
   * Entry points
   */

  public AbstractInsnNode getEntryPoint() {
    return AbstractInsnNodeHelper.skipPseudo(methodNode.instructions.getFirst());
  }

  public List<AbstractInsnNode> getAllEntryPoints() {
    Stack<AbstractInsnNode> stack = new Stack<>();
    stack.push(AbstractInsnNodeHelper.skipPseudo(methodNode.instructions.getFirst()));
    Set<AbstractInsnNode> visited = new HashSet<>();
    List<AbstractInsnNode> entries = new ArrayList<>();

    while (true) {
      AbstractInsnNode entry = null;

      for (AbstractInsnNode insn : methodNode.instructions) {
        if (AbstractInsnNodeHelper.isReal(insn) && !visited.contains(insn)) {
          entry = insn;
          break;
        }
      }

      if (entry == null) {
        break;
      }

      entries.add(entry);
      stack.push(entry);

      while (!stack.isEmpty()) {
        AbstractInsnNode insn = stack.pop();

        if (visited.contains(insn)) {
          continue;
        }

        visited.add(insn);

        for (AbstractInsnNode successor : getAllSuccessors(insn)) {
          stack.push(successor);
        }
      }
    }

    return entries;
  }

  public FlowAnalyzerThread getEntryThread() {
    FlowAnalyzerThread thread = new FlowAnalyzerThread(
            getEntryPoint(),
            new ArrayList<>(),
            new LinkedList<>()
    );

    if ((methodNode.access & Opcodes.ACC_STATIC) == 0) {
      if (Objects.equals(methodNode.name, "<init>")) {
        thread.addLocal(Opcodes.UNINITIALIZED_THIS);
      } else {
        thread.addLocal(className);
      }
    }

    thread.addArgumentLocals(methodNode.desc);
    return thread;
  }

  public List<FlowAnalyzerThread> getAllEntryThreads() {
    List<AbstractInsnNode> entryPoints = getAllEntryPoints();
    List<FlowAnalyzerThread> threads = new ArrayList<>();

    for (AbstractInsnNode entry : entryPoints) {
      if (entry == getEntryPoint()) {
        threads.add(getEntryThread());
      } else {
        FlowAnalyzerThread thread = new FlowAnalyzerThread(
                entry,
                new ArrayList<>(),
                new LinkedList<>()
        );
        threads.add(thread);
      }
    }

    return threads;
  }

  /*
   * Control flow
   */

  public List<AbstractInsnNode> getSuccessors(AbstractInsnNode insn) {
    List<AbstractInsnNode> results = new ArrayList<>();

    if (!AbstractInsnNodeHelper.isUnconditionalJump(insn)) {
      results.add(AbstractInsnNodeHelper.skipPseudo(insn.getNext()));
    }

    if (insn instanceof JumpInsnNode) {
      JumpInsnNode jumpInsn = (JumpInsnNode) insn;
      results.add(AbstractInsnNodeHelper.skipPseudo(jumpInsn.label));
    }

    if (insn instanceof LookupSwitchInsnNode) {
      LookupSwitchInsnNode switchInsn = (LookupSwitchInsnNode) insn;

      for (LabelNode label : switchInsn.labels) {
        results.add(AbstractInsnNodeHelper.skipPseudo(label));
      }

      results.add(AbstractInsnNodeHelper.skipPseudo(switchInsn.dflt));
    }

    if (insn instanceof TableSwitchInsnNode) {
      TableSwitchInsnNode switchInsn = (TableSwitchInsnNode) insn;

      for (LabelNode label : switchInsn.labels) {
        results.add(AbstractInsnNodeHelper.skipPseudo(label));
      }

      results.add(AbstractInsnNodeHelper.skipPseudo(switchInsn.dflt));
    }

    return results;
  }

  public List<Pair<AbstractInsnNode, String>> getExceptionSuccessors(AbstractInsnNode insn) {
    List<Pair<AbstractInsnNode, String>> results = new ArrayList<>();

    for (TryCatchBlockNode tryCatch : methodNode.tryCatchBlocks) {
      int beginIndex = labelIndices.get(tryCatch.start);
      int endIndex = labelIndices.get(tryCatch.end);
      int insnIndex = methodNode.instructions.indexOf(insn);

      if (insnIndex >= beginIndex && insnIndex < endIndex) {
        results.add(Pair.of(AbstractInsnNodeHelper.skipPseudo(tryCatch.handler), tryCatch.type));
      }
    }

    return results;
  }

  public List<AbstractInsnNode> getAllSuccessors(AbstractInsnNode insn) {
    List<AbstractInsnNode> successors = new ArrayList<>(getSuccessors(insn));
    List<Pair<AbstractInsnNode, String>> exceptionSuccessors = getExceptionSuccessors(insn);

    for (Pair<AbstractInsnNode, String> exceptionSuccessor : exceptionSuccessors) {
      successors.add(exceptionSuccessor.first);
    }

    return successors;
  }

  /*
   * Simulation
   */

  public List<FlowAnalyzerThread> step(FlowAnalyzerThread thread) {
    AbstractInsnNode insn = thread.getCurrentInsn();

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
        thread.push(Opcodes.NULL);
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
        thread.push(Opcodes.INTEGER);
        break;
      case Opcodes.LCONST_0:
      case Opcodes.LCONST_1:
      case Opcodes.LLOAD:
        thread.push2(Opcodes.LONG);
        break;
      case Opcodes.FCONST_0:
      case Opcodes.FCONST_1:
      case Opcodes.FCONST_2:
      case Opcodes.FLOAD:
        thread.push(Opcodes.FLOAT);
        break;
      case Opcodes.DCONST_0:
      case Opcodes.DCONST_1:
      case Opcodes.DLOAD:
        thread.push2(Opcodes.DOUBLE);
        break;
      case Opcodes.LDC: {
        Object value = ((LdcInsnNode) insn).cst;

        if (value instanceof Integer) {
          thread.push(Opcodes.INTEGER);
        } else if (value instanceof Long) {
          thread.push2(Opcodes.LONG);
        } else if (value instanceof Float) {
          thread.push(Opcodes.FLOAT);
        } else if (value instanceof Double) {
          thread.push2(Opcodes.DOUBLE);
        } else if (value instanceof String) {
          thread.push("java/lang/String");
        } else if (value instanceof Type) {
          switch (((Type) value).getSort()) {
            case Type.METHOD:
              thread.push("java/lang/invoke/MethodType");
              break;
            default:
              thread.push("java/lang/Class");
              break;
          }
        } else if (value instanceof Handle) {
          thread.push("java/lang/invoke/MethodHandle");
        } else if (value instanceof ConstantDynamic) {
          thread.push(((ConstantDynamic) value).getDescriptor());
        } else {
          throw new IllegalArgumentException("Unsupported LDC value: " + value);
        }
        break;
      }
      case Opcodes.ALOAD:
        thread.push(thread.getLocal(((VarInsnNode) insn).var));
        break;
      case Opcodes.LALOAD:
      case Opcodes.D2L:
        thread.pop2();
        thread.push2(Opcodes.LONG);
        break;
      case Opcodes.DALOAD:
      case Opcodes.L2D:
        thread.pop2();
        thread.push2(Opcodes.DOUBLE);
        break;
      case Opcodes.AALOAD: {
        thread.pop();
        Object arrayref = thread.pop();
        if (arrayref == Opcodes.NULL) {
          thread.push(Opcodes.NULL);
        } else {
          thread.pushType(Type.getType(arrayref.toString()).getElementType());
        }
        break;
      }
      case Opcodes.ISTORE:
      case Opcodes.FSTORE:
      case Opcodes.ASTORE: {
        Object value = thread.pop();
        thread.setLocal(((VarInsnNode) insn).var, value);
        break;
      }
      case Opcodes.LSTORE:
      case Opcodes.DSTORE: {
        List<Object> value = thread.pop2();
        thread.setLocal2(((VarInsnNode) insn).var, value.get(0));
        break;
      }
      case Opcodes.IASTORE:
      case Opcodes.BASTORE:
      case Opcodes.CASTORE:
      case Opcodes.SASTORE:
      case Opcodes.FASTORE:
      case Opcodes.AASTORE:
        thread.pop();
        thread.pop();
        thread.pop();
        break;
      case Opcodes.LASTORE:
      case Opcodes.DASTORE:
        thread.pop2();
        thread.pop();
        thread.pop();
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
        thread.pop();
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
        thread.pop();
        thread.pop();
        break;
      case Opcodes.DUP: {
        Object value = thread.pop();
        thread.push(value);
        thread.push(value);
        break;
      }
      case Opcodes.DUP_X1: {
        Object value1 = thread.pop();
        Object value2 = thread.pop();
        thread.push(value1);
        thread.push(value2);
        thread.push(value1);
        break;
      }
      case Opcodes.DUP_X2: {
        Object value1 = thread.pop();
        Object value2 = thread.pop();
        Object value3 = thread.pop();
        thread.push(value1);
        thread.push(value3);
        thread.push(value2);
        thread.push(value1);
        break;
      }
      case Opcodes.DUP2: {
        Object value1 = thread.pop();
        Object value2 = thread.pop();
        thread.push(value2);
        thread.push(value1);
        thread.push(value2);
        thread.push(value1);
        break;
      }
      case Opcodes.DUP2_X1: {
        Object value1 = thread.pop();
        Object value2 = thread.pop();
        Object value3 = thread.pop();
        thread.push(value2);
        thread.push(value1);
        thread.push(value3);
        thread.push(value2);
        thread.push(value1);
        break;
      }
      case Opcodes.DUP2_X2: {
        Object value1 = thread.pop();
        Object value2 = thread.pop();
        Object value3 = thread.pop();
        Object value4 = thread.pop();
        thread.push(value2);
        thread.push(value1);
        thread.push(value4);
        thread.push(value3);
        thread.push(value2);
        thread.push(value1);
        break;
      }
      case Opcodes.SWAP: {
        Object value1 = thread.pop();
        Object value2 = thread.pop();
        thread.push(value1);
        thread.push(value2);
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
        thread.pop();
        thread.pop();
        thread.push(Opcodes.INTEGER);
        break;
      case Opcodes.LADD:
      case Opcodes.LSUB:
      case Opcodes.LMUL:
      case Opcodes.LDIV:
      case Opcodes.LREM:
      case Opcodes.LAND:
      case Opcodes.LOR:
      case Opcodes.LXOR:
        thread.pop2();
        thread.pop2();
        thread.push2(Opcodes.LONG);
        break;
      case Opcodes.FALOAD:
      case Opcodes.FADD:
      case Opcodes.FSUB:
      case Opcodes.FMUL:
      case Opcodes.FDIV:
      case Opcodes.FREM:
      case Opcodes.L2F:
      case Opcodes.D2F:
        thread.pop();
        thread.pop();
        thread.push(Opcodes.FLOAT);
        break;
      case Opcodes.DADD:
      case Opcodes.DSUB:
      case Opcodes.DMUL:
      case Opcodes.DDIV:
      case Opcodes.DREM:
        thread.pop2();
        thread.pop2();
        thread.push2(Opcodes.DOUBLE);
        break;
      case Opcodes.LSHL:
      case Opcodes.LSHR:
      case Opcodes.LUSHR:
        thread.pop();
        thread.pop2();
        thread.push2(Opcodes.LONG);
        break;
      case Opcodes.IINC:
        thread.setLocal(((IincInsnNode) insn).var, Opcodes.INTEGER);
        break;
      case Opcodes.I2L:
      case Opcodes.F2L:
        thread.pop();
        thread.push2(Opcodes.LONG);
        break;
      case Opcodes.I2F:
        thread.pop();
        thread.push(Opcodes.FLOAT);
        break;
      case Opcodes.I2D:
      case Opcodes.F2D:
        thread.pop();
        thread.push2(Opcodes.DOUBLE);
        break;
      case Opcodes.F2I:
      case Opcodes.ARRAYLENGTH:
      case Opcodes.INSTANCEOF:
        thread.pop();
        thread.push(Opcodes.INTEGER);
        break;
      case Opcodes.LCMP:
      case Opcodes.DCMPL:
      case Opcodes.DCMPG:
        thread.pop2();
        thread.pop2();
        thread.push(Opcodes.INTEGER);
        break;
      case Opcodes.JSR:
      case Opcodes.RET:
        throw new IllegalArgumentException("JSR/RET are not supported");
      case Opcodes.GETSTATIC:
        thread.pushDescriptor(((FieldInsnNode) insn).desc);
        break;
      case Opcodes.PUTSTATIC:
        thread.popDescriptor(((FieldInsnNode) insn).desc);
        break;
      case Opcodes.GETFIELD:
        thread.pop();
        thread.pushDescriptor(((FieldInsnNode) insn).desc);
        break;
      case Opcodes.PUTFIELD:
        thread.popDescriptor(((FieldInsnNode) insn).desc);
        thread.pop();
        break;
      case Opcodes.INVOKEVIRTUAL:
      case Opcodes.INVOKESPECIAL:
      case Opcodes.INVOKEINTERFACE: {
        thread.popMethodArguments(((MethodInsnNode) insn).desc);
        thread.pop();
        thread.pushMethodReturn(((MethodInsnNode) insn).desc);
        break;
      }
      case Opcodes.INVOKESTATIC:
      case Opcodes.INVOKEDYNAMIC: {
        thread.popMethodArguments(((MethodInsnNode) insn).desc);
        thread.pushMethodReturn(((MethodInsnNode) insn).desc);
        break;
      }
      case Opcodes.NEW:
        thread.push(AbstractInsnNodeHelper.getLabel(insn));
        break;
      case Opcodes.NEWARRAY:
        thread.pop();
        switch (((IntInsnNode) insn).operand) {
          case Opcodes.T_BOOLEAN:
            thread.push("[Z");
            break;
          case Opcodes.T_CHAR:
            thread.push("[C");
            break;
          case Opcodes.T_BYTE:
            thread.push("[B");
            break;
          case Opcodes.T_SHORT:
            thread.push("[S");
            break;
          case Opcodes.T_INT:
            thread.push("[I");
            break;
          case Opcodes.T_FLOAT:
            thread.push("[F");
            break;
          case Opcodes.T_DOUBLE:
            thread.push("[D");
            break;
          case Opcodes.T_LONG:
            thread.push("[J");
            break;
          default:
            throw new IllegalArgumentException("Unsupported new array type: " + ((IntInsnNode) insn).operand);
        }
        break;
      case Opcodes.ANEWARRAY: {
        thread.pop();
        thread.push("[L" + ((TypeInsnNode) insn).desc + ";");
        break;
      }
      case Opcodes.CHECKCAST:
        thread.pop();
        thread.push(((TypeInsnNode) insn).desc);
        break;
      case Opcodes.MULTIANEWARRAY:
        for (int i = 0; i < ((MultiANewArrayInsnNode) insn).dims; i++) {
          thread.pop();
        }
        thread.push(((MultiANewArrayInsnNode) insn).desc);
        break;
      default:
        if (insn.getOpcode() < 0) {
          // Ignore
        } else {
          throw new IllegalArgumentException("Unknown opcode: " + insn.getOpcode());
        }
    }

    List<AbstractInsnNode> successors = getSuccessors(insn);
    List<FlowAnalyzerThread> threads = new ArrayList<>();

    for (AbstractInsnNode successor : successors) {
      FlowAnalyzerThread newThread = thread.clone();
      newThread.setCurrentInsn(successor);
      threads.add(newThread);
    }

    List<Pair<AbstractInsnNode, String>> exceptionSuccessors = getExceptionSuccessors(insn);

    for (Pair<AbstractInsnNode, String> exceptionSuccessor : exceptionSuccessors) {
      FlowAnalyzerThread newThread = new FlowAnalyzerThread(
              exceptionSuccessor.first,
              ArrayListHelper.of(),
              ArrayListHelper.of(exceptionSuccessor.second)
      );
      threads.add(newThread);
    }

    return threads;
  }

  /*
   * Internal
   */

  private Map<LabelNode, Integer> getLabelIndices() {
    Map<LabelNode, Integer> labelIndices = new HashMap<>();

    for (int i = 0; i < methodNode.instructions.size(); i++) {
      AbstractInsnNode insn = methodNode.instructions.get(i);

      if (insn instanceof LabelNode) {
        labelIndices.put((LabelNode) insn, i);
      }
    }

    return labelIndices;
  }
}
