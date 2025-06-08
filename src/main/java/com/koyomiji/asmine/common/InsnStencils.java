package com.koyomiji.asmine.common;

import com.koyomiji.asmine.stencil.IStencil;
import com.koyomiji.asmine.stencil.ConstStencil;
import com.koyomiji.asmine.stencil.insn.*;
import org.objectweb.asm.Handle;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

import java.util.Arrays;
import java.util.List;

public class InsnStencils {
  public static InsnStencil insn(IStencil<Integer> opcode) {
    return new InsnStencil(opcode);
  }

  public static InsnStencil insn(int opcode) {
    return insn(new ConstStencil<>(opcode));
  }

  public static IntInsnStencil intInsn(IStencil<Integer> opcode, IStencil<Integer> operand) {
    return new IntInsnStencil(opcode, operand);
  }

  public static IntInsnStencil intInsn(int opcode, int operand) {
    return intInsn(new ConstStencil<>(opcode), new ConstStencil<>(operand));
  }

  public static VarInsnStencil varInsn(IStencil<Integer> opcode, IStencil<Integer> varIndex) {
    return new VarInsnStencil(opcode, varIndex);
  }

  public static VarInsnStencil varInsn(int opcode, int varIndex) {
    return varInsn(new ConstStencil<>(opcode), new ConstStencil<>(varIndex));
  }

  public static TypeInsnStencil typeInsn(IStencil<Integer> opcode, IStencil<String> type) {
    return new TypeInsnStencil(opcode, type);
  }

  public static TypeInsnStencil typeInsn(int opcode, String type) {
    return typeInsn(new ConstStencil<>(opcode), new ConstStencil<>(type));
  }

  public static FieldInsnStencil fieldInsn(IStencil<Integer> opcode, IStencil<String> owner, IStencil<String> name, IStencil<String> desc) {
    return new FieldInsnStencil(opcode, owner, name, desc);
  }

  public static FieldInsnStencil fieldInsn(int opcode, String owner, String name, String desc) {
    return fieldInsn(new ConstStencil<>(opcode), new ConstStencil<>(owner), new ConstStencil<>(name), new ConstStencil<>(desc));
  }

  public static MethodInsnStencil methodInsn(IStencil<Integer> opcode, IStencil<String> owner, IStencil<String> name, IStencil<String> desc, IStencil<Boolean> itf) {
    return new MethodInsnStencil(opcode, owner, name, desc, itf);
  }

  public static MethodInsnStencil methodInsn(int opcode, String owner, String name, String desc, boolean itf) {
    return methodInsn(new ConstStencil<>(opcode), new ConstStencil<>(owner), new ConstStencil<>(name), new ConstStencil<>(desc), new ConstStencil<>(itf));
  }

  public static InvokeDynamicInsnStencil invokeDynamicInsn(IStencil<String> name, IStencil<String> desc, IStencil<Handle> bsm, IStencil<List<Object>> bsmArgs) {
    return new InvokeDynamicInsnStencil(name, desc, bsm, bsmArgs);
  }

  public static InvokeDynamicInsnStencil invokeDynamicInsn(String name, String desc, Handle bsm, Object... bsmArgs) {
    return invokeDynamicInsn(new ConstStencil<>(name), new ConstStencil<>(desc), new ConstStencil<>(bsm), new ConstStencil<>(Arrays.asList(bsmArgs)));
  }

  public static JumpInsnStencil jumpInsn(IStencil<Integer> opcode, IStencil<LabelNode> label) {
    return new JumpInsnStencil(opcode, label);
  }

  public static JumpInsnStencil jumpInsn(int opcode, LabelNode label) {
    return jumpInsn(new ConstStencil<>(opcode), new ConstStencil<>(label));
  }

  public static LabelStencil label(IStencil<LabelNode> label) {
    return new LabelStencil(label);
  }

  public static LabelStencil label(LabelNode label) {
    return label(new ConstStencil<>(label));
  }

  public static LdcInsnStencil ldcInsn(IStencil<Object> cst) {
    return new LdcInsnStencil(cst);
  }

  public static LdcInsnStencil ldcInsn(Object cst) {
    return ldcInsn(new ConstStencil<>(cst));
  }

  public static IincInsnStencil iincInsn(IStencil<Integer> varIndex, IStencil<Integer> incr) {
    return new IincInsnStencil(varIndex, incr);
  }

  public static IincInsnStencil iincInsn(int varIndex, int incr) {
    return iincInsn(new ConstStencil<>(varIndex), new ConstStencil<>(incr));
  }

  public static TableSwitchInsnStencil tableSwitchInsn(IStencil<Integer> min, IStencil<Integer> max, IStencil<LabelNode> dflt, IStencil<List<LabelNode>> labels) {
    return new TableSwitchInsnStencil(min, max, dflt, labels);
  }

  public static TableSwitchInsnStencil tableSwitchInsn(int min, int max, LabelNode dflt, LabelNode... labels) {
    return tableSwitchInsn(new ConstStencil<>(min), new ConstStencil<>(max), new ConstStencil<>(dflt), new ConstStencil<>(ArrayHelper.toList(labels)));
  }

  public static LookupSwitchInsnStencil lookupSwitchInsn(IStencil<LabelNode> dflt, IStencil<List<Integer>> keys, IStencil<List<LabelNode>> labels) {
    return new LookupSwitchInsnStencil(dflt, keys, labels);
  }

  public static LookupSwitchInsnStencil lookupSwitchInsn(LabelNode dflt, int[] keys, LabelNode[] labels) {
    return lookupSwitchInsn(new ConstStencil<>(dflt), new ConstStencil<>(ArrayHelper.toList(keys)), new ConstStencil<>(ArrayHelper.toList(labels)));
  }

  public static MultiANewArrayInsnStencil multiANewArrayInsn(IStencil<String> desc, IStencil<Integer> dims) {
    return new MultiANewArrayInsnStencil(desc, dims);
  }

  public static MultiANewArrayInsnStencil multiANewArrayInsn(String desc, int dims) {
    return multiANewArrayInsn(new ConstStencil<>(desc), new ConstStencil<>(dims));
  }

  public static FrameStencil frame(IStencil<FrameNode> frame) {
    return new FrameStencil(frame);
  }

  public static LineNumberStencil lineNumber(IStencil<Integer> line, IStencil<LabelNode> start) {
    return new LineNumberStencil(line, start);
  }

  public static LineNumberStencil lineNumber(int line, LabelNode start) {
    return lineNumber(new ConstStencil<>(line), new ConstStencil<>(start));
  }

  public static InsnStencil nop() {
    return insn(new ConstStencil<>(Opcodes.NOP));
  }

  public static InsnStencil aconst_null() {
    return insn(new ConstStencil<>(Opcodes.ACONST_NULL));
  }

  public static InsnStencil iconst_m1() {
    return insn(new ConstStencil<>(Opcodes.ICONST_M1));
  }

  public static InsnStencil iconst_0() {
    return insn(new ConstStencil<>(Opcodes.ICONST_0));
  }

  public static InsnStencil iconst_1() {
    return insn(new ConstStencil<>(Opcodes.ICONST_1));
  }

  public static InsnStencil iconst_2() {
    return insn(new ConstStencil<>(Opcodes.ICONST_2));
  }

  public static InsnStencil iconst_3() {
    return insn(new ConstStencil<>(Opcodes.ICONST_3));
  }

  public static InsnStencil iconst_4() {
    return insn(new ConstStencil<>(Opcodes.ICONST_4));
  }

  public static InsnStencil iconst_5() {
    return insn(new ConstStencil<>(Opcodes.ICONST_5));
  }

  public static InsnStencil lconst_0() {
    return insn(new ConstStencil<>(Opcodes.LCONST_0));
  }

  public static InsnStencil lconst_1() {
    return insn(new ConstStencil<>(Opcodes.LCONST_1));
  }

  public static InsnStencil fconst_0() {
    return insn(new ConstStencil<>(Opcodes.FCONST_0));
  }

  public static InsnStencil fconst_1() {
    return insn(new ConstStencil<>(Opcodes.FCONST_1));
  }

  public static InsnStencil fconst_2() {
    return insn(new ConstStencil<>(Opcodes.FCONST_2));
  }

  public static InsnStencil dconst_0() {
    return insn(new ConstStencil<>(Opcodes.DCONST_0));
  }

  public static InsnStencil dconst_1() {
    return insn(new ConstStencil<>(Opcodes.DCONST_1));
  }

  public static IntInsnStencil bipush(IStencil<Integer> operand) {
    return intInsn(new ConstStencil<>(Opcodes.BIPUSH), operand);
  }

  public static IntInsnStencil bipush(int operand) {
    return bipush(new ConstStencil<>(operand));
  }

  public static IntInsnStencil sipush(IStencil<Integer> operand) {
    return intInsn(new ConstStencil<>(Opcodes.SIPUSH), operand);
  }

  public static IntInsnStencil sipush(int operand) {
    return sipush(new ConstStencil<>(operand));
  }

  public static LdcInsnStencil ldc(IStencil<Object> cst) {
    return ldcInsn(cst);
  }

  public static LdcInsnStencil ldc(Object cst) {
    return ldcInsn(new ConstStencil<>(cst));
  }

  public static VarInsnStencil iload(IStencil<Integer> varIndex) {
    return varInsn(new ConstStencil<>(Opcodes.ILOAD), varIndex);
  }

  public static VarInsnStencil iload(int varIndex) {
    return iload(new ConstStencil<>(varIndex));
  }

  public static VarInsnStencil lload(IStencil<Integer> varIndex) {
    return varInsn(new ConstStencil<>(Opcodes.LLOAD), varIndex);
  }

  public static VarInsnStencil lload(int varIndex) {
    return lload(new ConstStencil<>(varIndex));
  }

  public static VarInsnStencil fload(IStencil<Integer> varIndex) {
    return varInsn(new ConstStencil<>(Opcodes.FLOAD), varIndex);
  }

  public static VarInsnStencil fload(int varIndex) {
    return fload(new ConstStencil<>(varIndex));
  }

  public static VarInsnStencil dload(IStencil<Integer> varIndex) {
    return varInsn(new ConstStencil<>(Opcodes.DLOAD), varIndex);
  }

  public static VarInsnStencil dload(int varIndex) {
    return dload(new ConstStencil<>(varIndex));
  }

  public static VarInsnStencil aload(IStencil<Integer> varIndex) {
    return varInsn(new ConstStencil<>(Opcodes.ALOAD), varIndex);
  }

  public static VarInsnStencil aload(int varIndex) {
    return aload(new ConstStencil<>(varIndex));
  }

  public static InsnStencil iaload() {
    return insn(new ConstStencil<>(Opcodes.IALOAD));
  }

  public static InsnStencil laload() {
    return insn(new ConstStencil<>(Opcodes.LALOAD));
  }

  public static InsnStencil faload() {
    return insn(new ConstStencil<>(Opcodes.FALOAD));
  }

  public static InsnStencil daload() {
    return insn(new ConstStencil<>(Opcodes.DALOAD));
  }

  public static InsnStencil aaload() {
    return insn(new ConstStencil<>(Opcodes.AALOAD));
  }

  public static InsnStencil baload() {
    return insn(new ConstStencil<>(Opcodes.BALOAD));
  }

  public static InsnStencil caload() {
    return insn(new ConstStencil<>(Opcodes.CALOAD));
  }

  public static InsnStencil saload() {
    return insn(new ConstStencil<>(Opcodes.SALOAD));
  }

  public static VarInsnStencil istore(IStencil<Integer> varIndex) {
    return varInsn(new ConstStencil<>(Opcodes.ISTORE), varIndex);
  }

  public static VarInsnStencil istore(int varIndex) {
    return istore(new ConstStencil<>(varIndex));
  }

  public static VarInsnStencil lstore(IStencil<Integer> varIndex) {
    return varInsn(new ConstStencil<>(Opcodes.LSTORE), varIndex);
  }

  public static VarInsnStencil lstore(int varIndex) {
    return lstore(new ConstStencil<>(varIndex));
  }

  public static VarInsnStencil fstore(IStencil<Integer> varIndex) {
    return varInsn(new ConstStencil<>(Opcodes.FSTORE), varIndex);
  }

  public static VarInsnStencil fstore(int varIndex) {
    return fstore(new ConstStencil<>(varIndex));
  }

  public static VarInsnStencil dstore(IStencil<Integer> varIndex) {
    return varInsn(new ConstStencil<>(Opcodes.DSTORE), varIndex);
  }

  public static VarInsnStencil dstore(int varIndex) {
    return dstore(new ConstStencil<>(varIndex));
  }

  public static VarInsnStencil astore(IStencil<Integer> varIndex) {
    return varInsn(new ConstStencil<>(Opcodes.ASTORE), varIndex);
  }

  public static VarInsnStencil astore(int varIndex) {
    return astore(new ConstStencil<>(varIndex));
  }

  public static InsnStencil iastore() {
    return insn(new ConstStencil<>(Opcodes.IASTORE));
  }

  public static InsnStencil lastore() {
    return insn(new ConstStencil<>(Opcodes.LASTORE));
  }

  public static InsnStencil fastore() {
    return insn(new ConstStencil<>(Opcodes.FASTORE));
  }

  public static InsnStencil dastore() {
    return insn(new ConstStencil<>(Opcodes.DASTORE));
  }

  public static InsnStencil aastore() {
    return insn(new ConstStencil<>(Opcodes.AASTORE));
  }

  public static InsnStencil bastore() {
    return insn(new ConstStencil<>(Opcodes.BASTORE));
  }

  public static InsnStencil castore() {
    return insn(new ConstStencil<>(Opcodes.CASTORE));
  }

  public static InsnStencil sastore() {
    return insn(new ConstStencil<>(Opcodes.SASTORE));
  }

  public static InsnStencil pop() {
    return insn(new ConstStencil<>(Opcodes.POP));
  }

  public static InsnStencil pop2() {
    return insn(new ConstStencil<>(Opcodes.POP2));
  }

  public static InsnStencil dup() {
    return insn(new ConstStencil<>(Opcodes.DUP));
  }

  public static InsnStencil dup_x1() {
    return insn(new ConstStencil<>(Opcodes.DUP_X1));
  }

  public static InsnStencil dup_x2() {
    return insn(new ConstStencil<>(Opcodes.DUP_X2));
  }

  public static InsnStencil dup2() {
    return insn(new ConstStencil<>(Opcodes.DUP2));
  }

  public static InsnStencil dup2_x1() {
    return insn(new ConstStencil<>(Opcodes.DUP2_X1));
  }

  public static InsnStencil dup2_x2() {
    return insn(new ConstStencil<>(Opcodes.DUP2_X2));
  }

  public static InsnStencil swap() {
    return insn(new ConstStencil<>(Opcodes.SWAP));
  }

  public static InsnStencil iadd() {
    return insn(new ConstStencil<>(Opcodes.IADD));
  }

  public static InsnStencil ladd() {
    return insn(new ConstStencil<>(Opcodes.LADD));
  }

  public static InsnStencil fadd() {
    return insn(new ConstStencil<>(Opcodes.FADD));
  }

  public static InsnStencil dadd() {
    return insn(new ConstStencil<>(Opcodes.DADD));
  }

  public static InsnStencil isub() {
    return insn(new ConstStencil<>(Opcodes.ISUB));
  }

  public static InsnStencil lsub() {
    return insn(new ConstStencil<>(Opcodes.LSUB));
  }

  public static InsnStencil fsub() {
    return insn(new ConstStencil<>(Opcodes.FSUB));
  }

  public static InsnStencil dsub() {
    return insn(new ConstStencil<>(Opcodes.DSUB));
  }

  public static InsnStencil imul() {
    return insn(new ConstStencil<>(Opcodes.IMUL));
  }

  public static InsnStencil lmul() {
    return insn(new ConstStencil<>(Opcodes.LMUL));
  }

  public static InsnStencil fmul() {
    return insn(new ConstStencil<>(Opcodes.FMUL));
  }

  public static InsnStencil dmul() {
    return insn(new ConstStencil<>(Opcodes.DMUL));
  }

  public static InsnStencil idiv() {
    return insn(new ConstStencil<>(Opcodes.IDIV));
  }

  public static InsnStencil ldiv() {
    return insn(new ConstStencil<>(Opcodes.LDIV));
  }

  public static InsnStencil fdiv() {
    return insn(new ConstStencil<>(Opcodes.FDIV));
  }

  public static InsnStencil ddiv() {
    return insn(new ConstStencil<>(Opcodes.DDIV));
  }

  public static InsnStencil irem() {
    return insn(new ConstStencil<>(Opcodes.IREM));
  }

  public static InsnStencil lrem() {
    return insn(new ConstStencil<>(Opcodes.LREM));
  }

  public static InsnStencil frem() {
    return insn(new ConstStencil<>(Opcodes.FREM));
  }

  public static InsnStencil drem() {
    return insn(new ConstStencil<>(Opcodes.DREM));
  }

  public static InsnStencil ineg() {
    return insn(new ConstStencil<>(Opcodes.INEG));
  }

  public static InsnStencil lneg() {
    return insn(new ConstStencil<>(Opcodes.LNEG));
  }

  public static InsnStencil fneg() {
    return insn(new ConstStencil<>(Opcodes.FNEG));
  }

  public static InsnStencil dneg() {
    return insn(new ConstStencil<>(Opcodes.DNEG));
  }

  public static InsnStencil ishl() {
    return insn(new ConstStencil<>(Opcodes.ISHL));
  }

  public static InsnStencil lshl() {
    return insn(new ConstStencil<>(Opcodes.LSHL));
  }

  public static InsnStencil ishr() {
    return insn(new ConstStencil<>(Opcodes.ISHR));
  }

  public static InsnStencil lshr() {
    return insn(new ConstStencil<>(Opcodes.LSHR));
  }

  public static InsnStencil iushr() {
    return insn(new ConstStencil<>(Opcodes.IUSHR));
  }

  public static InsnStencil lushr() {
    return insn(new ConstStencil<>(Opcodes.LUSHR));
  }

  public static InsnStencil iand() {
    return insn(new ConstStencil<>(Opcodes.IAND));
  }

  public static InsnStencil land() {
    return insn(new ConstStencil<>(Opcodes.LAND));
  }

  public static InsnStencil ior() {
    return insn(new ConstStencil<>(Opcodes.IOR));
  }

  public static InsnStencil lor() {
    return insn(new ConstStencil<>(Opcodes.LOR));
  }

  public static InsnStencil ixor() {
    return insn(new ConstStencil<>(Opcodes.IXOR));
  }

  public static InsnStencil lxor() {
    return insn(new ConstStencil<>(Opcodes.LXOR));
  }

  public static IincInsnStencil iinc(IStencil<Integer> varIndex, IStencil<Integer> incr) {
    return iincInsn(varIndex, incr);
  }

  public static IincInsnStencil iinc(int varIndex, int incr) {
    return iincInsn(new ConstStencil<>(varIndex), new ConstStencil<>(incr));
  }

  public static InsnStencil i2l() {
    return insn(new ConstStencil<>(Opcodes.I2L));
  }

  public static InsnStencil i2f() {
    return insn(new ConstStencil<>(Opcodes.I2F));
  }

  public static InsnStencil i2d() {
    return insn(new ConstStencil<>(Opcodes.I2D));
  }

  public static InsnStencil l2i() {
    return insn(new ConstStencil<>(Opcodes.L2I));
  }

  public static InsnStencil l2f() {
    return insn(new ConstStencil<>(Opcodes.L2F));
  }

  public static InsnStencil l2d() {
    return insn(new ConstStencil<>(Opcodes.L2D));
  }

  public static InsnStencil f2i() {
    return insn(new ConstStencil<>(Opcodes.F2I));
  }

  public static InsnStencil f2l() {
    return insn(new ConstStencil<>(Opcodes.F2L));
  }

  public static InsnStencil f2d() {
    return insn(new ConstStencil<>(Opcodes.F2D));
  }

  public static InsnStencil d2i() {
    return insn(new ConstStencil<>(Opcodes.D2I));
  }

  public static InsnStencil d2l() {
    return insn(new ConstStencil<>(Opcodes.D2L));
  }

  public static InsnStencil d2f() {
    return insn(new ConstStencil<>(Opcodes.D2F));
  }

  public static InsnStencil i2b() {
    return insn(new ConstStencil<>(Opcodes.I2B));
  }

  public static InsnStencil i2c() {
    return insn(new ConstStencil<>(Opcodes.I2C));
  }

  public static InsnStencil i2s() {
    return insn(new ConstStencil<>(Opcodes.I2S));
  }

  public static InsnStencil lcmp() {
    return insn(new ConstStencil<>(Opcodes.LCMP));
  }

  public static InsnStencil fcmpl() {
    return insn(new ConstStencil<>(Opcodes.FCMPL));
  }

  public static InsnStencil fcmpg() {
    return insn(new ConstStencil<>(Opcodes.FCMPG));
  }

  public static InsnStencil dcmpl() {
    return insn(new ConstStencil<>(Opcodes.DCMPL));
  }

  public static InsnStencil dcmpg() {
    return insn(new ConstStencil<>(Opcodes.DCMPG));
  }

  public static JumpInsnStencil ifeq(IStencil<LabelNode> label) {
    return jumpInsn(new ConstStencil<>(Opcodes.IFEQ), label);
  }

  public static JumpInsnStencil ifeq(LabelNode label) {
    return ifeq(new ConstStencil<>(label));
  }

  public static JumpInsnStencil ifne(IStencil<LabelNode> label) {
    return jumpInsn(new ConstStencil<>(Opcodes.IFNE), label);
  }

  public static JumpInsnStencil ifne(LabelNode label) {
    return ifne(new ConstStencil<>(label));
  }

  public static JumpInsnStencil iflt(IStencil<LabelNode> label) {
    return jumpInsn(new ConstStencil<>(Opcodes.IFLT), label);
  }

  public static JumpInsnStencil iflt(LabelNode label) {
    return iflt(new ConstStencil<>(label));
  }

  public static JumpInsnStencil ifge(IStencil<LabelNode> label) {
    return jumpInsn(new ConstStencil<>(Opcodes.IFGE), label);
  }

  public static JumpInsnStencil ifge(LabelNode label) {
    return ifge(new ConstStencil<>(label));
  }

  public static JumpInsnStencil ifgt(IStencil<LabelNode> label) {
    return jumpInsn(new ConstStencil<>(Opcodes.IFGT), label);
  }

  public static JumpInsnStencil ifgt(LabelNode label) {
    return ifgt(new ConstStencil<>(label));
  }

  public static JumpInsnStencil ifle(IStencil<LabelNode> label) {
    return jumpInsn(new ConstStencil<>(Opcodes.IFLE), label);
  }

  public static JumpInsnStencil ifle(LabelNode label) {
    return ifle(new ConstStencil<>(label));
  }

  public static JumpInsnStencil if_icmpeq(IStencil<LabelNode> label) {
    return jumpInsn(new ConstStencil<>(Opcodes.IF_ICMPEQ), label);
  }

  public static JumpInsnStencil if_icmpeq(LabelNode label) {
    return if_icmpeq(new ConstStencil<>(label));
  }

  public static JumpInsnStencil if_icmpne(IStencil<LabelNode> label) {
    return jumpInsn(new ConstStencil<>(Opcodes.IF_ICMPNE), label);
  }

  public static JumpInsnStencil if_icmpne(LabelNode label) {
    return if_icmpne(new ConstStencil<>(label));
  }

  public static JumpInsnStencil if_icmplt(IStencil<LabelNode> label) {
    return jumpInsn(new ConstStencil<>(Opcodes.IF_ICMPLT), label);
  }

  public static JumpInsnStencil if_icmplt(LabelNode label) {
    return if_icmplt(new ConstStencil<>(label));
  }

  public static JumpInsnStencil if_icmpge(IStencil<LabelNode> label) {
    return jumpInsn(new ConstStencil<>(Opcodes.IF_ICMPGE), label);
  }

  public static JumpInsnStencil if_icmpge(LabelNode label) {
    return if_icmpge(new ConstStencil<>(label));
  }

  public static JumpInsnStencil if_icmpgt(IStencil<LabelNode> label) {
    return jumpInsn(new ConstStencil<>(Opcodes.IF_ICMPGT), label);
  }

  public static JumpInsnStencil if_icmpgt(LabelNode label) {
    return if_icmpgt(new ConstStencil<>(label));
  }

  public static JumpInsnStencil if_icmple(IStencil<LabelNode> label) {
    return jumpInsn(new ConstStencil<>(Opcodes.IF_ICMPLE), label);
  }

  public static JumpInsnStencil if_icmple(LabelNode label) {
    return if_icmple(new ConstStencil<>(label));
  }

  public static JumpInsnStencil if_acmpeq(IStencil<LabelNode> label) {
    return jumpInsn(new ConstStencil<>(Opcodes.IF_ACMPEQ), label);
  }

  public static JumpInsnStencil if_acmpeq(LabelNode label) {
    return if_acmpeq(new ConstStencil<>(label));
  }

  public static JumpInsnStencil if_acmpne(IStencil<LabelNode> label) {
    return jumpInsn(new ConstStencil<>(Opcodes.IF_ACMPNE), label);
  }

  public static JumpInsnStencil if_acmpne(LabelNode label) {
    return if_acmpne(new ConstStencil<>(label));
  }

  public static JumpInsnStencil goto_(IStencil<LabelNode> label) {
    return jumpInsn(new ConstStencil<>(Opcodes.GOTO), label);
  }

  public static JumpInsnStencil goto_(LabelNode label) {
    return goto_(new ConstStencil<>(label));
  }

  public static JumpInsnStencil jsr(IStencil<LabelNode> label) {
    return jumpInsn(new ConstStencil<>(Opcodes.JSR), label);
  }

  public static JumpInsnStencil jsr(LabelNode label) {
    return jsr(new ConstStencil<>(label));
  }

  public static VarInsnStencil ret(IStencil<Integer> varIndex) {
    return varInsn(new ConstStencil<>(Opcodes.RET), varIndex);
  }

  public static VarInsnStencil ret(int varIndex) {
    return ret(new ConstStencil<>(varIndex));
  }

  public static TableSwitchInsnStencil tableswitch(IStencil<Integer> min, IStencil<Integer> max, IStencil<LabelNode> dflt, IStencil<List<LabelNode>> labels) {
    return tableSwitchInsn(min, max, dflt, labels);
  }

  public static TableSwitchInsnStencil tableswitch(int min, int max, LabelNode dflt, LabelNode... labels) {
    return tableSwitchInsn(new ConstStencil<>(min), new ConstStencil<>(max), new ConstStencil<>(dflt), new ConstStencil<>(ArrayHelper.toList(labels)));
  }

  public static LookupSwitchInsnStencil lookupswitch(IStencil<LabelNode> dflt, IStencil<List<Integer>> keys, IStencil<List<LabelNode>> labels) {
    return lookupSwitchInsn(dflt, keys, labels);
  }

  public static LookupSwitchInsnStencil lookupswitch(LabelNode dflt, int[] keys, LabelNode[] labels) {
    return lookupSwitchInsn(new ConstStencil<>(dflt), new ConstStencil<>(ArrayHelper.toList(keys)), new ConstStencil<>(ArrayHelper.toList(labels)));
  }

  public static InsnStencil ireturn() {
    return insn(new ConstStencil<>(Opcodes.IRETURN));
  }

  public static InsnStencil lreturn() {
    return insn(new ConstStencil<>(Opcodes.LRETURN));
  }

  public static InsnStencil freturn() {
    return insn(new ConstStencil<>(Opcodes.FRETURN));
  }

  public static InsnStencil dreturn() {
    return insn(new ConstStencil<>(Opcodes.DRETURN));
  }

  public static InsnStencil areturn() {
    return insn(new ConstStencil<>(Opcodes.ARETURN));
  }

  public static InsnStencil return_() {
    return insn(new ConstStencil<>(Opcodes.RETURN));
  }

  public static FieldInsnStencil getstatic(IStencil<String> owner, IStencil<String> name, IStencil<String> desc) {
    return fieldInsn(new ConstStencil<>(Opcodes.GETSTATIC), owner, name, desc);
  }

  public static FieldInsnStencil getstatic(String owner, String name, String desc) {
    return getstatic(new ConstStencil<>(owner), new ConstStencil<>(name), new ConstStencil<>(desc));
  }

  public static FieldInsnStencil putstatic(IStencil<String> owner, IStencil<String> name, IStencil<String> desc) {
    return fieldInsn(new ConstStencil<>(Opcodes.PUTSTATIC), owner, name, desc);
  }

  public static FieldInsnStencil putstatic(String owner, String name, String desc) {
    return putstatic(new ConstStencil<>(owner), new ConstStencil<>(name), new ConstStencil<>(desc));
  }

  public static FieldInsnStencil getfield(IStencil<String> owner, IStencil<String> name, IStencil<String> desc) {
    return fieldInsn(new ConstStencil<>(Opcodes.GETFIELD), owner, name, desc);
  }

  public static FieldInsnStencil getfield(String owner, String name, String desc) {
    return getfield(new ConstStencil<>(owner), new ConstStencil<>(name), new ConstStencil<>(desc));
  }

  public static FieldInsnStencil putfield(IStencil<String> owner, IStencil<String> name, IStencil<String> desc) {
    return fieldInsn(new ConstStencil<>(Opcodes.PUTFIELD), owner, name, desc);
  }

  public static FieldInsnStencil putfield(String owner, String name, String desc) {
    return putfield(new ConstStencil<>(owner), new ConstStencil<>(name), new ConstStencil<>(desc));
  }

  public static MethodInsnStencil invokevirtual(IStencil<String> owner, IStencil<String> name, IStencil<String> desc, IStencil<Boolean> itf) {
    return methodInsn(new ConstStencil<>(Opcodes.INVOKEVIRTUAL), owner, name, desc, itf);
  }

  public static MethodInsnStencil invokevirtual(String owner, String name, String desc, boolean itf) {
    return invokevirtual(new ConstStencil<>(owner), new ConstStencil<>(name), new ConstStencil<>(desc), new ConstStencil<>(itf));
  }

  public static MethodInsnStencil invokespecial(IStencil<String> owner, IStencil<String> name, IStencil<String> desc, IStencil<Boolean> itf) {
    return methodInsn(new ConstStencil<>(Opcodes.INVOKESPECIAL), owner, name, desc, itf);
  }

  public static MethodInsnStencil invokespecial(String owner, String name, String desc, boolean itf) {
    return invokespecial(new ConstStencil<>(owner), new ConstStencil<>(name), new ConstStencil<>(desc), new ConstStencil<>(itf));
  }

  public static MethodInsnStencil invokestatic(IStencil<String> owner, IStencil<String> name, IStencil<String> desc, IStencil<Boolean> itf) {
    return methodInsn(new ConstStencil<>(Opcodes.INVOKESTATIC), owner, name, desc, itf);
  }

  public static MethodInsnStencil invokestatic(String owner, String name, String desc, boolean itf) {
    return invokestatic(new ConstStencil<>(owner), new ConstStencil<>(name), new ConstStencil<>(desc), new ConstStencil<>(itf));
  }

  public static MethodInsnStencil invokeinterface(IStencil<String> owner, IStencil<String> name, IStencil<String> desc, IStencil<Boolean> itf) {
    return methodInsn(new ConstStencil<>(Opcodes.INVOKEINTERFACE), owner, name, desc, itf);
  }

  public static MethodInsnStencil invokeinterface(String owner, String name, String desc, boolean itf) {
    return invokeinterface(new ConstStencil<>(owner), new ConstStencil<>(name), new ConstStencil<>(desc), new ConstStencil<>(itf));
  }

  public static InvokeDynamicInsnStencil invokedynamic(IStencil<String> name, IStencil<String> desc, IStencil<Handle> bsm, IStencil<List<Object>> bsmArgs) {
    return invokeDynamicInsn(name, desc, bsm, bsmArgs);
  }

  public static InvokeDynamicInsnStencil invokedynamic(String name, String desc, Handle bsm, Object... bsmArgs) {
    return invokeDynamicInsn(new ConstStencil<>(name), new ConstStencil<>(desc), new ConstStencil<>(bsm), new ConstStencil<>(Arrays.asList(bsmArgs)));
  }

  public static TypeInsnStencil new_(IStencil<String> type) {
    return typeInsn(new ConstStencil<>(Opcodes.NEW), type);
  }

  public static TypeInsnStencil new_(String type) {
    return new_(new ConstStencil<>(type));
  }

  public static IntInsnStencil newarray(IStencil<Integer> type) {
    return intInsn(new ConstStencil<>(Opcodes.NEWARRAY), type);
  }

  public static IntInsnStencil newarray(int type) {
    return newarray(new ConstStencil<>(type));
  }

  public static TypeInsnStencil anewarray(IStencil<String> type) {
    return typeInsn(new ConstStencil<>(Opcodes.ANEWARRAY), type);
  }

  public static TypeInsnStencil anewarray(String type) {
    return anewarray(new ConstStencil<>(type));
  }

  public static InsnStencil arraylength() {
    return insn(new ConstStencil<>(Opcodes.ARRAYLENGTH));
  }

  public static InsnStencil athrow() {
    return insn(new ConstStencil<>(Opcodes.ATHROW));
  }

  public static TypeInsnStencil checkcast(IStencil<String> type) {
    return typeInsn(new ConstStencil<>(Opcodes.CHECKCAST), type);
  }

  public static TypeInsnStencil checkcast(String type) {
    return checkcast(new ConstStencil<>(type));
  }

  public static TypeInsnStencil instanceof_(IStencil<String> type) {
    return typeInsn(new ConstStencil<>(Opcodes.INSTANCEOF), type);
  }

  public static TypeInsnStencil instanceof_(String type) {
    return instanceof_(new ConstStencil<>(type));
  }

  public static InsnStencil monitorenter() {
    return insn(new ConstStencil<>(Opcodes.MONITORENTER));
  }

  public static InsnStencil monitorexit() {
    return insn(new ConstStencil<>(Opcodes.MONITOREXIT));
  }

  public static MultiANewArrayInsnStencil multianewarray(IStencil<String> desc, IStencil<Integer> dims) {
    return multiANewArrayInsn(desc, dims);
  }

  public static MultiANewArrayInsnStencil multianewarray(String desc, int dims) {
    return multiANewArrayInsn(new ConstStencil<>(desc), new ConstStencil<>(dims));
  }

  public static JumpInsnStencil ifnull(IStencil<LabelNode> label) {
    return jumpInsn(new ConstStencil<>(Opcodes.IFNULL), label);
  }

  public static JumpInsnStencil ifnull(LabelNode label) {
    return ifnull(new ConstStencil<>(label));
  }

  public static JumpInsnStencil ifnonnull(IStencil<LabelNode> label) {
    return jumpInsn(new ConstStencil<>(Opcodes.IFNONNULL), label);
  }

  public static JumpInsnStencil ifnonnull(LabelNode label) {
    return ifnonnull(new ConstStencil<>(label));
  }
}
