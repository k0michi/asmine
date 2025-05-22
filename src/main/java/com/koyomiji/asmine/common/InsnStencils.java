package com.koyomiji.asmine.common;

import com.koyomiji.asmine.stencil.AbstractParameter;
import com.koyomiji.asmine.stencil.ConstParameter;
import com.koyomiji.asmine.stencil.insn.*;
import org.objectweb.asm.Handle;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

import java.util.List;

public class InsnStencils {
  public static InsnStencil insn(AbstractParameter<Integer> opcode) {
    return new InsnStencil(opcode);
  }

  public static IntInsnStencil intInsn(AbstractParameter<Integer> opcode, AbstractParameter<Integer> operand) {
    return new IntInsnStencil(opcode, operand);
  }

  public static VarInsnStencil varInsn(AbstractParameter<Integer> opcode, AbstractParameter<Integer> varIndex) {
    return new VarInsnStencil(opcode, varIndex);
  }

  public static TypeInsnStencil typeInsn(AbstractParameter<Integer> opcode, AbstractParameter<String> type) {
    return new TypeInsnStencil(opcode, type);
  }

  public static FieldInsnStencil fieldInsn(AbstractParameter<Integer> opcode, AbstractParameter<String> owner, AbstractParameter<String> name, AbstractParameter<String> desc) {
    return new FieldInsnStencil(opcode, owner, name, desc);
  }

  public static MethodInsnStencil methodInsn(AbstractParameter<Integer> opcode, AbstractParameter<String> owner, AbstractParameter<String> name, AbstractParameter<String> desc, AbstractParameter<Boolean> itf) {
    return new MethodInsnStencil(opcode, owner, name, desc, itf);
  }

  public static InvokeDynamicInsnStencil invokeDynamicInsn(AbstractParameter<String> name, AbstractParameter<String> desc, AbstractParameter<Handle> bsm, AbstractParameter<List<Object>> bsmArgs) {
    return new InvokeDynamicInsnStencil(name, desc, bsm, bsmArgs);
  }

  public static JumpInsnStencil jumpInsn(AbstractParameter<Integer> opcode, AbstractParameter<LabelNode> label) {
    return new JumpInsnStencil(opcode, label);
  }

  public static LabelStencil label(AbstractParameter<LabelNode> label) {
    return new LabelStencil(label);
  }

  public static LdcInsnStencil ldcInsn(AbstractParameter<Object> cst) {
    return new LdcInsnStencil(cst);
  }

  public static IincInsnStencil iincInsn(AbstractParameter<Integer> varIndex, AbstractParameter<Integer> incr) {
    return new IincInsnStencil(varIndex, incr);
  }

  public static TableSwitchInsnStencil tableSwitchInsn(AbstractParameter<Integer> min, AbstractParameter<Integer> max, AbstractParameter<LabelNode> dflt, AbstractParameter<List<LabelNode>> labels) {
    return new TableSwitchInsnStencil(min, max, dflt, labels);
  }

  public static LookupSwitchInsnStencil lookupSwitchInsn(AbstractParameter<LabelNode> dflt, AbstractParameter<List<Integer>> keys, AbstractParameter<List<LabelNode>> labels) {
    return new LookupSwitchInsnStencil(dflt, keys, labels);
  }

  public static MultiANewArrayInsnStencil multiANewArrayInsn(AbstractParameter<String> desc, AbstractParameter<Integer> dims) {
    return new MultiANewArrayInsnStencil(desc, dims);
  }

  public static FrameStencil frame(AbstractParameter<Integer> type, AbstractParameter<Integer> numLocal, AbstractParameter<List<Object>> local, AbstractParameter<Integer> numStack, AbstractParameter<List<Object>> stack) {
    return new FrameStencil(type, numLocal, local, numStack, stack);
  }

  public static LineNumberStencil lineNumber(AbstractParameter<Integer> line, AbstractParameter<LabelNode> start) {
    return new LineNumberStencil(line, start);
  }

  public static InsnStencil nop() {
    return insn(new ConstParameter<>(Opcodes.NOP));
  }

  public static InsnStencil aconst_null() {
    return insn(new ConstParameter<>(Opcodes.ACONST_NULL));
  }

  public static InsnStencil iconst_m1() {
    return insn(new ConstParameter<>(Opcodes.ICONST_M1));
  }

  public static InsnStencil iconst_0() {
    return insn(new ConstParameter<>(Opcodes.ICONST_0));
  }

  public static InsnStencil iconst_1() {
    return insn(new ConstParameter<>(Opcodes.ICONST_1));
  }

  public static InsnStencil iconst_2() {
    return insn(new ConstParameter<>(Opcodes.ICONST_2));
  }

  public static InsnStencil iconst_3() {
    return insn(new ConstParameter<>(Opcodes.ICONST_3));
  }

  public static InsnStencil iconst_4() {
    return insn(new ConstParameter<>(Opcodes.ICONST_4));
  }

  public static InsnStencil iconst_5() {
    return insn(new ConstParameter<>(Opcodes.ICONST_5));
  }

  public static InsnStencil lconst_0() {
    return insn(new ConstParameter<>(Opcodes.LCONST_0));
  }

  public static InsnStencil lconst_1() {
    return insn(new ConstParameter<>(Opcodes.LCONST_1));
  }

  public static InsnStencil fconst_0() {
    return insn(new ConstParameter<>(Opcodes.FCONST_0));
  }

  public static InsnStencil fconst_1() {
    return insn(new ConstParameter<>(Opcodes.FCONST_1));
  }

  public static InsnStencil fconst_2() {
    return insn(new ConstParameter<>(Opcodes.FCONST_2));
  }

  public static InsnStencil dconst_0() {
    return insn(new ConstParameter<>(Opcodes.DCONST_0));
  }

  public static InsnStencil dconst_1() {
    return insn(new ConstParameter<>(Opcodes.DCONST_1));
  }

  public static IntInsnStencil bipush(AbstractParameter<Integer> operand) {
    return intInsn(new ConstParameter<>(Opcodes.BIPUSH), operand);
  }

  public static IntInsnStencil sipush(AbstractParameter<Integer> operand) {
    return intInsn(new ConstParameter<>(Opcodes.SIPUSH), operand);
  }

  public static LdcInsnStencil ldc(AbstractParameter<Object> cst) {
    return ldcInsn(cst);
  }

  public static VarInsnStencil iload(AbstractParameter<Integer> varIndex) {
    return varInsn(new ConstParameter<>(Opcodes.ILOAD), varIndex);
  }

  public static VarInsnStencil lload(AbstractParameter<Integer> varIndex) {
    return varInsn(new ConstParameter<>(Opcodes.LLOAD), varIndex);
  }

  public static VarInsnStencil fload(AbstractParameter<Integer> varIndex) {
    return varInsn(new ConstParameter<>(Opcodes.FLOAD), varIndex);
  }

  public static VarInsnStencil dload(AbstractParameter<Integer> varIndex) {
    return varInsn(new ConstParameter<>(Opcodes.DLOAD), varIndex);
  }

  public static VarInsnStencil aload(AbstractParameter<Integer> varIndex) {
    return varInsn(new ConstParameter<>(Opcodes.ALOAD), varIndex);
  }

  public static InsnStencil iaload() {
    return insn(new ConstParameter<>(Opcodes.IALOAD));
  }

  public static InsnStencil laload() {
    return insn(new ConstParameter<>(Opcodes.LALOAD));
  }

  public static InsnStencil faload() {
    return insn(new ConstParameter<>(Opcodes.FALOAD));
  }

  public static InsnStencil daload() {
    return insn(new ConstParameter<>(Opcodes.DALOAD));
  }

  public static InsnStencil aaload() {
    return insn(new ConstParameter<>(Opcodes.AALOAD));
  }

  public static InsnStencil baload() {
    return insn(new ConstParameter<>(Opcodes.BALOAD));
  }

  public static InsnStencil caload() {
    return insn(new ConstParameter<>(Opcodes.CALOAD));
  }

  public static InsnStencil saload() {
    return insn(new ConstParameter<>(Opcodes.SALOAD));
  }

  public static VarInsnStencil istore(AbstractParameter<Integer> varIndex) {
    return varInsn(new ConstParameter<>(Opcodes.ISTORE), varIndex);
  }

  public static VarInsnStencil lstore(AbstractParameter<Integer> varIndex) {
    return varInsn(new ConstParameter<>(Opcodes.LSTORE), varIndex);
  }

  public static VarInsnStencil fstore(AbstractParameter<Integer> varIndex) {
    return varInsn(new ConstParameter<>(Opcodes.FSTORE), varIndex);
  }

  public static VarInsnStencil dstore(AbstractParameter<Integer> varIndex) {
    return varInsn(new ConstParameter<>(Opcodes.DSTORE), varIndex);
  }

  public static VarInsnStencil astore(AbstractParameter<Integer> varIndex) {
    return varInsn(new ConstParameter<>(Opcodes.ASTORE), varIndex);
  }

  public static InsnStencil iastore() {
    return insn(new ConstParameter<>(Opcodes.IASTORE));
  }

  public static InsnStencil lastore() {
    return insn(new ConstParameter<>(Opcodes.LASTORE));
  }

  public static InsnStencil fastore() {
    return insn(new ConstParameter<>(Opcodes.FASTORE));
  }

  public static InsnStencil dastore() {
    return insn(new ConstParameter<>(Opcodes.DASTORE));
  }

  public static InsnStencil aastore() {
    return insn(new ConstParameter<>(Opcodes.AASTORE));
  }

  public static InsnStencil bastore() {
    return insn(new ConstParameter<>(Opcodes.BASTORE));
  }

  public static InsnStencil castore() {
    return insn(new ConstParameter<>(Opcodes.CASTORE));
  }

  public static InsnStencil sastore() {
    return insn(new ConstParameter<>(Opcodes.SASTORE));
  }

  public static InsnStencil pop() {
    return insn(new ConstParameter<>(Opcodes.POP));
  }

  public static InsnStencil pop2() {
    return insn(new ConstParameter<>(Opcodes.POP2));
  }

  public static InsnStencil dup() {
    return insn(new ConstParameter<>(Opcodes.DUP));
  }

  public static InsnStencil dup_x1() {
    return insn(new ConstParameter<>(Opcodes.DUP_X1));
  }

  public static InsnStencil dup_x2() {
    return insn(new ConstParameter<>(Opcodes.DUP_X2));
  }

  public static InsnStencil dup2() {
    return insn(new ConstParameter<>(Opcodes.DUP2));
  }

  public static InsnStencil dup2_x1() {
    return insn(new ConstParameter<>(Opcodes.DUP2_X1));
  }

  public static InsnStencil dup2_x2() {
    return insn(new ConstParameter<>(Opcodes.DUP2_X2));
  }

  public static InsnStencil swap() {
    return insn(new ConstParameter<>(Opcodes.SWAP));
  }

  public static InsnStencil iadd() {
    return insn(new ConstParameter<>(Opcodes.IADD));
  }

  public static InsnStencil ladd() {
    return insn(new ConstParameter<>(Opcodes.LADD));
  }

  public static InsnStencil fadd() {
    return insn(new ConstParameter<>(Opcodes.FADD));
  }

  public static InsnStencil dadd() {
    return insn(new ConstParameter<>(Opcodes.DADD));
  }

  public static InsnStencil isub() {
    return insn(new ConstParameter<>(Opcodes.ISUB));
  }

  public static InsnStencil lsub() {
    return insn(new ConstParameter<>(Opcodes.LSUB));
  }

  public static InsnStencil fsub() {
    return insn(new ConstParameter<>(Opcodes.FSUB));
  }

  public static InsnStencil dsub() {
    return insn(new ConstParameter<>(Opcodes.DSUB));
  }

  public static InsnStencil imul() {
    return insn(new ConstParameter<>(Opcodes.IMUL));
  }

  public static InsnStencil lmul() {
    return insn(new ConstParameter<>(Opcodes.LMUL));
  }

  public static InsnStencil fmul() {
    return insn(new ConstParameter<>(Opcodes.FMUL));
  }

  public static InsnStencil dmul() {
    return insn(new ConstParameter<>(Opcodes.DMUL));
  }

  public static InsnStencil idiv() {
    return insn(new ConstParameter<>(Opcodes.IDIV));
  }

  public static InsnStencil ldiv() {
    return insn(new ConstParameter<>(Opcodes.LDIV));
  }

  public static InsnStencil fdiv() {
    return insn(new ConstParameter<>(Opcodes.FDIV));
  }

  public static InsnStencil ddiv() {
    return insn(new ConstParameter<>(Opcodes.DDIV));
  }

  public static InsnStencil irem() {
    return insn(new ConstParameter<>(Opcodes.IREM));
  }

  public static InsnStencil lrem() {
    return insn(new ConstParameter<>(Opcodes.LREM));
  }

  public static InsnStencil frem() {
    return insn(new ConstParameter<>(Opcodes.FREM));
  }

  public static InsnStencil drem() {
    return insn(new ConstParameter<>(Opcodes.DREM));
  }

  public static InsnStencil ineg() {
    return insn(new ConstParameter<>(Opcodes.INEG));
  }

  public static InsnStencil lneg() {
    return insn(new ConstParameter<>(Opcodes.LNEG));
  }

  public static InsnStencil fneg() {
    return insn(new ConstParameter<>(Opcodes.FNEG));
  }

  public static InsnStencil dneg() {
    return insn(new ConstParameter<>(Opcodes.DNEG));
  }

  public static InsnStencil ishl() {
    return insn(new ConstParameter<>(Opcodes.ISHL));
  }

  public static InsnStencil lshl() {
    return insn(new ConstParameter<>(Opcodes.LSHL));
  }

  public static InsnStencil ishr() {
    return insn(new ConstParameter<>(Opcodes.ISHR));
  }

  public static InsnStencil lshr() {
    return insn(new ConstParameter<>(Opcodes.LSHR));
  }

  public static InsnStencil iushr() {
    return insn(new ConstParameter<>(Opcodes.IUSHR));
  }

  public static InsnStencil lushr() {
    return insn(new ConstParameter<>(Opcodes.LUSHR));
  }

  public static InsnStencil iand() {
    return insn(new ConstParameter<>(Opcodes.IAND));
  }

  public static InsnStencil land() {
    return insn(new ConstParameter<>(Opcodes.LAND));
  }

  public static InsnStencil ior() {
    return insn(new ConstParameter<>(Opcodes.IOR));
  }

  public static InsnStencil lor() {
    return insn(new ConstParameter<>(Opcodes.LOR));
  }

  public static InsnStencil ixor() {
    return insn(new ConstParameter<>(Opcodes.IXOR));
  }

  public static InsnStencil lxor() {
    return insn(new ConstParameter<>(Opcodes.LXOR));
  }

  public static IincInsnStencil iinc(AbstractParameter<Integer> varIndex, AbstractParameter<Integer> incr) {
    return iincInsn(varIndex, incr);
  }

  public static InsnStencil i2l() {
    return insn(new ConstParameter<>(Opcodes.I2L));
  }

  public static InsnStencil i2f() {
    return insn(new ConstParameter<>(Opcodes.I2F));
  }

  public static InsnStencil i2d() {
    return insn(new ConstParameter<>(Opcodes.I2D));
  }

  public static InsnStencil l2i() {
    return insn(new ConstParameter<>(Opcodes.L2I));
  }

  public static InsnStencil l2f() {
    return insn(new ConstParameter<>(Opcodes.L2F));
  }

  public static InsnStencil l2d() {
    return insn(new ConstParameter<>(Opcodes.L2D));
  }

  public static InsnStencil f2i() {
    return insn(new ConstParameter<>(Opcodes.F2I));
  }

  public static InsnStencil f2l() {
    return insn(new ConstParameter<>(Opcodes.F2L));
  }

  public static InsnStencil f2d() {
    return insn(new ConstParameter<>(Opcodes.F2D));
  }

  public static InsnStencil d2i() {
    return insn(new ConstParameter<>(Opcodes.D2I));
  }

  public static InsnStencil d2l() {
    return insn(new ConstParameter<>(Opcodes.D2L));
  }

  public static InsnStencil d2f() {
    return insn(new ConstParameter<>(Opcodes.D2F));
  }

  public static InsnStencil i2b() {
    return insn(new ConstParameter<>(Opcodes.I2B));
  }

  public static InsnStencil i2c() {
    return insn(new ConstParameter<>(Opcodes.I2C));
  }

  public static InsnStencil i2s() {
    return insn(new ConstParameter<>(Opcodes.I2S));
  }

  public static InsnStencil lcmp() {
    return insn(new ConstParameter<>(Opcodes.LCMP));
  }

  public static InsnStencil fcmpl() {
    return insn(new ConstParameter<>(Opcodes.FCMPL));
  }

  public static InsnStencil fcmpg() {
    return insn(new ConstParameter<>(Opcodes.FCMPG));
  }

  public static InsnStencil dcmpl() {
    return insn(new ConstParameter<>(Opcodes.DCMPL));
  }

  public static InsnStencil dcmpg() {
    return insn(new ConstParameter<>(Opcodes.DCMPG));
  }

  public static JumpInsnStencil ifeq(AbstractParameter<LabelNode> label) {
    return jumpInsn(new ConstParameter<>(Opcodes.IFEQ), label);
  }

  public static JumpInsnStencil ifne(AbstractParameter<LabelNode> label) {
    return jumpInsn(new ConstParameter<>(Opcodes.IFNE), label);
  }

  public static JumpInsnStencil iflt(AbstractParameter<LabelNode> label) {
    return jumpInsn(new ConstParameter<>(Opcodes.IFLT), label);
  }

  public static JumpInsnStencil ifge(AbstractParameter<LabelNode> label) {
    return jumpInsn(new ConstParameter<>(Opcodes.IFGE), label);
  }

  public static JumpInsnStencil ifgt(AbstractParameter<LabelNode> label) {
    return jumpInsn(new ConstParameter<>(Opcodes.IFGT), label);
  }

  public static JumpInsnStencil ifle(AbstractParameter<LabelNode> label) {
    return jumpInsn(new ConstParameter<>(Opcodes.IFLE), label);
  }

  public static JumpInsnStencil if_icmpeq(AbstractParameter<LabelNode> label) {
    return jumpInsn(new ConstParameter<>(Opcodes.IF_ICMPEQ), label);
  }

  public static JumpInsnStencil if_icmpne(AbstractParameter<LabelNode> label) {
    return jumpInsn(new ConstParameter<>(Opcodes.IF_ICMPNE), label);
  }

  public static JumpInsnStencil if_icmplt(AbstractParameter<LabelNode> label) {
    return jumpInsn(new ConstParameter<>(Opcodes.IF_ICMPLT), label);
  }

  public static JumpInsnStencil if_icmpge(AbstractParameter<LabelNode> label) {
    return jumpInsn(new ConstParameter<>(Opcodes.IF_ICMPGE), label);
  }

  public static JumpInsnStencil if_icmpgt(AbstractParameter<LabelNode> label) {
    return jumpInsn(new ConstParameter<>(Opcodes.IF_ICMPGT), label);
  }

  public static JumpInsnStencil if_icmple(AbstractParameter<LabelNode> label) {
    return jumpInsn(new ConstParameter<>(Opcodes.IF_ICMPLE), label);
  }

  public static JumpInsnStencil if_acmpeq(AbstractParameter<LabelNode> label) {
    return jumpInsn(new ConstParameter<>(Opcodes.IF_ACMPEQ), label);
  }

  public static JumpInsnStencil if_acmpne(AbstractParameter<LabelNode> label) {
    return jumpInsn(new ConstParameter<>(Opcodes.IF_ACMPNE), label);
  }

  public static JumpInsnStencil goto_(AbstractParameter<LabelNode> label) {
    return jumpInsn(new ConstParameter<>(Opcodes.GOTO), label);
  }

  public static JumpInsnStencil jsr(AbstractParameter<LabelNode> label) {
    return jumpInsn(new ConstParameter<>(Opcodes.JSR), label);
  }

  public static VarInsnStencil ret(AbstractParameter<Integer> varIndex) {
    return varInsn(new ConstParameter<>(Opcodes.RET), varIndex);
  }

  public static TableSwitchInsnStencil tableSwitch(AbstractParameter<Integer> min, AbstractParameter<Integer> max, AbstractParameter<LabelNode> dflt, AbstractParameter<List<LabelNode>> labels) {
    return tableSwitchInsn(min, max, dflt, labels);
  }

  public static LookupSwitchInsnStencil lookupSwitch(AbstractParameter<LabelNode> dflt, AbstractParameter<List<Integer>> keys, AbstractParameter<List<LabelNode>> labels) {
    return lookupSwitchInsn(dflt, keys, labels);
  }

  public static InsnStencil ireturn() {
    return insn(new ConstParameter<>(Opcodes.IRETURN));
  }

  public static InsnStencil lreturn() {
    return insn(new ConstParameter<>(Opcodes.LRETURN));
  }

  public static InsnStencil freturn() {
    return insn(new ConstParameter<>(Opcodes.FRETURN));
  }

  public static InsnStencil dreturn() {
    return insn(new ConstParameter<>(Opcodes.DRETURN));
  }

  public static InsnStencil areturn() {
    return insn(new ConstParameter<>(Opcodes.ARETURN));
  }

  public static InsnStencil return_() {
    return insn(new ConstParameter<>(Opcodes.RETURN));
  }

  public static FieldInsnStencil getstatic(AbstractParameter<String> owner, AbstractParameter<String> name, AbstractParameter<String> desc) {
    return fieldInsn(new ConstParameter<>(Opcodes.GETSTATIC), owner, name, desc);
  }

  public static FieldInsnStencil putstatic(AbstractParameter<String> owner, AbstractParameter<String> name, AbstractParameter<String> desc) {
    return fieldInsn(new ConstParameter<>(Opcodes.PUTSTATIC), owner, name, desc);
  }

  public static FieldInsnStencil getfield(AbstractParameter<String> owner, AbstractParameter<String> name, AbstractParameter<String> desc) {
    return fieldInsn(new ConstParameter<>(Opcodes.GETFIELD), owner, name, desc);
  }

  public static FieldInsnStencil putfield(AbstractParameter<String> owner, AbstractParameter<String> name, AbstractParameter<String> desc) {
    return fieldInsn(new ConstParameter<>(Opcodes.PUTFIELD), owner, name, desc);
  }

  public static MethodInsnStencil invokevirtual(AbstractParameter<String> owner, AbstractParameter<String> name, AbstractParameter<String> desc, AbstractParameter<Boolean> itf) {
    return methodInsn(new ConstParameter<>(Opcodes.INVOKEVIRTUAL), owner, name, desc, itf);
  }

  public static MethodInsnStencil invokespecial(AbstractParameter<String> owner, AbstractParameter<String> name, AbstractParameter<String> desc, AbstractParameter<Boolean> itf) {
    return methodInsn(new ConstParameter<>(Opcodes.INVOKESPECIAL), owner, name, desc, itf);
  }

  public static MethodInsnStencil invokestatic(AbstractParameter<String> owner, AbstractParameter<String> name, AbstractParameter<String> desc, AbstractParameter<Boolean> itf) {
    return methodInsn(new ConstParameter<>(Opcodes.INVOKESTATIC), owner, name, desc, itf);
  }

  public static MethodInsnStencil invokeinterface(AbstractParameter<String> owner, AbstractParameter<String> name, AbstractParameter<String> desc, AbstractParameter<Boolean> itf) {
    return methodInsn(new ConstParameter<>(Opcodes.INVOKEINTERFACE), owner, name, desc, itf);
  }

  public static InvokeDynamicInsnStencil invokedynamic(AbstractParameter<String> name, AbstractParameter<String> desc, AbstractParameter<Handle> bsm, AbstractParameter<List<Object>> bsmArgs) {
    return invokeDynamicInsn(name, desc, bsm, bsmArgs);
  }

  public static TypeInsnStencil new_(AbstractParameter<String> type) {
    return typeInsn(new ConstParameter<>(Opcodes.NEW), type);
  }

  public static IntInsnStencil newarray(AbstractParameter<Integer> type) {
    return intInsn(new ConstParameter<>(Opcodes.NEWARRAY), type);
  }

  public static TypeInsnStencil anewarray(AbstractParameter<String> type) {
    return typeInsn(new ConstParameter<>(Opcodes.ANEWARRAY), type);
  }

  public static InsnStencil arraylength() {
    return insn(new ConstParameter<>(Opcodes.ARRAYLENGTH));
  }

  public static InsnStencil athrow() {
    return insn(new ConstParameter<>(Opcodes.ATHROW));
  }

  public static TypeInsnStencil checkcast(AbstractParameter<String> type) {
    return typeInsn(new ConstParameter<>(Opcodes.CHECKCAST), type);
  }

  public static TypeInsnStencil instanceof_(AbstractParameter<String> type) {
    return typeInsn(new ConstParameter<>(Opcodes.INSTANCEOF), type);
  }

  public static InsnStencil monitorenter() {
    return insn(new ConstParameter<>(Opcodes.MONITORENTER));
  }

  public static InsnStencil monitorexit() {
    return insn(new ConstParameter<>(Opcodes.MONITOREXIT));
  }

  public static MultiANewArrayInsnStencil multianewarray(AbstractParameter<String> desc, AbstractParameter<Integer> dims) {
    return multiANewArrayInsn(desc, dims);
  }

  public static JumpInsnStencil ifnull(AbstractParameter<LabelNode> label) {
    return jumpInsn(new ConstParameter<>(Opcodes.IFNULL), label);
  }

  public static JumpInsnStencil ifnonnull(AbstractParameter<LabelNode> label) {
    return jumpInsn(new ConstParameter<>(Opcodes.IFNONNULL), label);
  }
}
