package com.koyomiji.asmine.tree;

import org.objectweb.asm.tree.*;

import java.util.Arrays;
import java.util.Objects;

public class AbstractInsnNodeHelper {
  public static AbstractInsnNode clone(AbstractInsnNode insnNode) {
    MethodNode clonedTo = new MethodNode();
    insnNode.accept(clonedTo);
    return clonedTo.instructions.get(0);
  }

  public static boolean isReal(AbstractInsnNode insn) {
    return insn.getOpcode() != -1;
  }

  public static boolean isPseudo(AbstractInsnNode insn) {
    return insn.getOpcode() == -1;
  }

  public static boolean equals(AbstractInsnNode insn1, AbstractInsnNode insn2) {
    if (insn1.getOpcode() != insn2.getOpcode()) {
      return false;
    }

    if (insn1 instanceof InsnNode && insn2 instanceof InsnNode) {
      return true;
    }

    if (insn1 instanceof IntInsnNode && insn2 instanceof IntInsnNode) {
      IntInsnNode intInsn1 = (IntInsnNode) insn1;
      IntInsnNode intInsn2 = (IntInsnNode) insn2;
      return intInsn1.operand == intInsn2.operand;
    }

    if (insn1 instanceof VarInsnNode && insn2 instanceof VarInsnNode) {
      VarInsnNode varInsn1 = (VarInsnNode) insn1;
      VarInsnNode varInsn2 = (VarInsnNode) insn2;
      return varInsn1.var == varInsn2.var;
    }

    if (insn1 instanceof TypeInsnNode && insn2 instanceof TypeInsnNode) {
      TypeInsnNode typeInsn1 = (TypeInsnNode) insn1;
      TypeInsnNode typeInsn2 = (TypeInsnNode) insn2;
      return Objects.equals(typeInsn1.desc, typeInsn2.desc);
    }

    if (insn1 instanceof FieldInsnNode && insn2 instanceof FieldInsnNode) {
      FieldInsnNode fieldInsn1 = (FieldInsnNode) insn1;
      FieldInsnNode fieldInsn2 = (FieldInsnNode) insn2;
      return Objects.equals(fieldInsn1.owner, fieldInsn2.owner)
              && Objects.equals(fieldInsn1.name, fieldInsn2.name)
              && Objects.equals(fieldInsn1.desc, fieldInsn2.desc);
    }

    if (insn1 instanceof MethodInsnNode && insn2 instanceof MethodInsnNode) {
      MethodInsnNode methodInsn1 = (MethodInsnNode) insn1;
      MethodInsnNode methodInsn2 = (MethodInsnNode) insn2;
      return Objects.equals(methodInsn1.owner, methodInsn2.owner)
              && Objects.equals(methodInsn1.name, methodInsn2.name)
              && Objects.equals(methodInsn1.desc, methodInsn2.desc)
              && methodInsn1.itf == methodInsn2.itf;
    }

    if (insn1 instanceof InvokeDynamicInsnNode && insn2 instanceof InvokeDynamicInsnNode) {
      InvokeDynamicInsnNode invokeDynamicInsn1 = (InvokeDynamicInsnNode) insn1;
      InvokeDynamicInsnNode invokeDynamicInsn2 = (InvokeDynamicInsnNode) insn2;
      return Objects.equals(invokeDynamicInsn1.name, invokeDynamicInsn2.name)
              && Objects.equals(invokeDynamicInsn1.desc, invokeDynamicInsn2.desc)
              && Objects.equals(invokeDynamicInsn1.bsm, invokeDynamicInsn2.bsm)
              && Arrays.equals(invokeDynamicInsn1.bsmArgs, invokeDynamicInsn2.bsmArgs);
    }

    if (insn1 instanceof JumpInsnNode && insn2 instanceof JumpInsnNode) {
      JumpInsnNode jumpInsn1 = (JumpInsnNode) insn1;
      JumpInsnNode jumpInsn2 = (JumpInsnNode) insn2;
      return jumpInsn1.label == jumpInsn2.label;
    }

    if (insn1 instanceof LabelNode && insn2 instanceof LabelNode) {
      return insn1 == insn2;
    }

    if (insn1 instanceof LdcInsnNode && insn2 instanceof LdcInsnNode) {
      LdcInsnNode ldcInsn1 = (LdcInsnNode) insn1;
      LdcInsnNode ldcInsn2 = (LdcInsnNode) insn2;
      return Objects.equals(ldcInsn1.cst, ldcInsn2.cst);
    }

    if (insn1 instanceof IincInsnNode && insn2 instanceof IincInsnNode) {
      IincInsnNode iincInsn1 = (IincInsnNode) insn1;
      IincInsnNode iincInsn2 = (IincInsnNode) insn2;
      return iincInsn1.var == iincInsn2.var && iincInsn1.incr == iincInsn2.incr;
    }

    if (insn1 instanceof TableSwitchInsnNode && insn2 instanceof TableSwitchInsnNode) {
      TableSwitchInsnNode tableSwitchInsn1 = (TableSwitchInsnNode) insn1;
      TableSwitchInsnNode tableSwitchInsn2 = (TableSwitchInsnNode) insn2;
      return tableSwitchInsn1.min == tableSwitchInsn2.min
              && tableSwitchInsn1.max == tableSwitchInsn2.max
              && tableSwitchInsn1.dflt == tableSwitchInsn2.dflt
              && Objects.equals(tableSwitchInsn1.labels, tableSwitchInsn2.labels);
    }

    if (insn1 instanceof LookupSwitchInsnNode && insn2 instanceof LookupSwitchInsnNode) {
      LookupSwitchInsnNode lookupSwitchInsn1 = (LookupSwitchInsnNode) insn1;
      LookupSwitchInsnNode lookupSwitchInsn2 = (LookupSwitchInsnNode) insn2;
      return Objects.equals(lookupSwitchInsn1.keys, lookupSwitchInsn2.keys)
              && lookupSwitchInsn1.dflt == lookupSwitchInsn2.dflt
              && Objects.equals(lookupSwitchInsn1.labels, lookupSwitchInsn2.labels);
    }

    if (insn1 instanceof MultiANewArrayInsnNode && insn2 instanceof MultiANewArrayInsnNode) {
      MultiANewArrayInsnNode multiANewArrayInsn1 = (MultiANewArrayInsnNode) insn1;
      MultiANewArrayInsnNode multiANewArrayInsn2 = (MultiANewArrayInsnNode) insn2;
      return Objects.equals(multiANewArrayInsn1.desc, multiANewArrayInsn2.desc)
              && multiANewArrayInsn1.dims == multiANewArrayInsn2.dims;
    }

    if (insn1 instanceof FrameNode && insn2 instanceof FrameNode) {
      FrameNode frameInsn1 = (FrameNode) insn1;
      FrameNode frameInsn2 = (FrameNode) insn2;
      return frameInsn1.type == frameInsn2.type
              && Objects.equals(frameInsn1.local, frameInsn2.local)
              && Objects.equals(frameInsn1.stack, frameInsn2.stack);
    }

    if (insn1 instanceof LineNumberNode && insn2 instanceof LineNumberNode) {
      LineNumberNode lineNumberInsn1 = (LineNumberNode) insn1;
      LineNumberNode lineNumberInsn2 = (LineNumberNode) insn2;
      return lineNumberInsn1.line == lineNumberInsn2.line
              && lineNumberInsn1.start == lineNumberInsn2.start;
    }

    return false;
  }
}
