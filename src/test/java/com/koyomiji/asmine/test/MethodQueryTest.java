package com.koyomiji.asmine.test;

import com.koyomiji.asmine.common.*;
import com.koyomiji.asmine.compat.OpcodesCompat;
import com.koyomiji.asmine.query.MethodQuery;
import com.koyomiji.asmine.regex.compiler.Regexes;
import com.koyomiji.asmine.regex.compiler.code.CodeRegexes;
import com.koyomiji.asmine.stencil.Stencils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.FrameNode;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.MethodNode;

public class MethodQueryTest {
  @Test
  void test_0() {
    boolean present = MethodQuery.ofNew()
            .addInsns(Insns.return_())
            .selectCodeFragment(
                    Regexes.concatenate(
                            CodeRegexes.stencil(InsnStencils.return_())
                    )
            )
            .isPresent();

    Assertions.assertTrue(present);
  }

  @Test
  void test_1() {
    boolean present = MethodQuery.ofNew()
            .addInsns(Insns.return_())
            .selectCodeFragment(
                    Regexes.concatenate(
                            CodeRegexes.stencil(InsnStencils.return_()),
                            CodeRegexes.stencil(InsnStencils.return_())
                    )
            )
            .isPresent();

    Assertions.assertFalse(present);
  }

  @Test
  void test_2() {
    boolean present = MethodQuery.ofNew()
            .addInsns(Insns.return_())
            .selectCodeFragment(
                    Regexes.concatenate(
                            CodeRegexes.stencil(InsnStencils.return_()),
                            CodeRegexes.stencil(InsnStencils.return_())
                    )
            )
            .isPresent();

    Assertions.assertFalse(present);
  }

  @Test
  void test_replace_0() {
    boolean present = MethodQuery.ofNew()
            .addInsns(Insns.return_())
            .selectCodeFragment(
                    Regexes.concatenate(
                            CodeRegexes.stencil(InsnStencils.return_())
                    )
            )
            .replaceWith(
                    InsnStencils.areturn()
            )
            .done()
            .selectCodeFragment(
                    Regexes.concatenate(
                            Regexes.anchorBegin(),
                            CodeRegexes.stencil(InsnStencils.areturn()),
                            Regexes.anchorEnd()
                    )
            )
            .isPresent();

    Assertions.assertTrue(present);
  }

  // Replace first
  @Test
  void test_replace_1() {
    boolean present = MethodQuery.ofNew()
            .addInsns(
                    Insns.return_(),
                    Insns.return_()
            )
            .selectCodeFragment(
                    Regexes.concatenate(
                            CodeRegexes.stencil(InsnStencils.return_())
                    )
            )
            .replaceWith(
                    InsnStencils.areturn()
            )
            .done()
            .selectCodeFragment(
                    Regexes.concatenate(
                            Regexes.anchorBegin(),
                            CodeRegexes.stencil(InsnStencils.areturn()),
                            CodeRegexes.stencil(InsnStencils.return_()),
                            Regexes.anchorEnd()
                    )
            )
            .isPresent();

    Assertions.assertTrue(present);
  }

  @Test
  void test_replace_2() {
    boolean present = MethodQuery.ofNew()
            .addInsns(
                    Insns.return_(),
                    Insns.return_()
            )
            .selectCodeFragment(
                    Regexes.concatenate(
                            CodeRegexes.stencil(InsnStencils.return_())
                    )
            )
            .replaceWith(
                    InsnStencils.areturn()
            )
            .done()
            .selectCodeFragment(
                    Regexes.concatenate(
                            Regexes.anchorBegin(),
                            CodeRegexes.stencil(InsnStencils.return_()),
                            CodeRegexes.stencil(InsnStencils.return_()),
                            Regexes.anchorEnd()
                    )
            )
            .isPresent();

    Assertions.assertFalse(present);
  }

  // Replace all
  @Test
  void test_replace_3() {
    boolean present = MethodQuery.ofNew()
            .addInsns(
                    Insns.return_(),
                    Insns.return_()
            )
            .selectCodeFragments(
                    Regexes.concatenate(
                            CodeRegexes.stencil(InsnStencils.return_())
                    )
            )
            .replaceWith(
                    InsnStencils.areturn()
            )
            .done()
            .selectCodeFragment(
                    Regexes.concatenate(
                            Regexes.anchorBegin(),
                            CodeRegexes.stencil(InsnStencils.areturn()),
                            CodeRegexes.stencil(InsnStencils.areturn()),
                            Regexes.anchorEnd()
                    )
            )
            .isPresent();

    Assertions.assertTrue(present);
  }

  // Replace with two
  @Test
  void test_replace_4() {
    boolean present = MethodQuery.ofNew()
            .addInsns(
                    Insns.iconst_0(),
                    Insns.return_(),
                    Insns.iconst_0(),
                    Insns.return_(),
                    Insns.iconst_0()
            )
            .selectCodeFragments(
                    Regexes.concatenate(
                            CodeRegexes.stencil(InsnStencils.return_())
                    )
            )
            .replaceWith(
                    InsnStencils.areturn(),
                    InsnStencils.areturn()
            )
            .done()
            .selectCodeFragment(
                    Regexes.concatenate(
                            Regexes.anchorBegin(),
                            CodeRegexes.stencil(InsnStencils.iconst_0()),
                            CodeRegexes.stencil(InsnStencils.areturn()),
                            CodeRegexes.stencil(InsnStencils.areturn()),
                            CodeRegexes.stencil(InsnStencils.iconst_0()),
                            CodeRegexes.stencil(InsnStencils.areturn()),
                            CodeRegexes.stencil(InsnStencils.areturn()),
                            CodeRegexes.stencil(InsnStencils.iconst_0()),
                            Regexes.anchorEnd()
                    )
            )
            .isPresent();

    Assertions.assertTrue(present);
  }

  // Replace twice
  @Test
  void test_replace_5() {
    boolean present = MethodQuery.ofNew()
            .addInsns(
                    Insns.iconst_0(),
                    Insns.return_(),
                    Insns.iconst_0(),
                    Insns.return_(),
                    Insns.iconst_0()
            )
            .selectCodeFragments(
                    Regexes.concatenate(
                            CodeRegexes.stencil(InsnStencils.return_())
                    )
            )
            .replaceWith(
                    InsnStencils.areturn()
            )
            .replaceWith(
                    InsnStencils.ireturn()
            )
            .done()
            .selectCodeFragment(
                    Regexes.concatenate(
                            Regexes.anchorBegin(),
                            CodeRegexes.stencil(InsnStencils.iconst_0()),
                            CodeRegexes.stencil(InsnStencils.ireturn()),
                            CodeRegexes.stencil(InsnStencils.iconst_0()),
                            CodeRegexes.stencil(InsnStencils.ireturn()),
                            CodeRegexes.stencil(InsnStencils.iconst_0()),
                            Regexes.anchorEnd()
                    )
            )
            .isPresent();

    Assertions.assertTrue(present);
  }

  @Test
  void test_remove_0() {
    boolean present = MethodQuery.ofNew()
            .addInsns(
                    Insns.iconst_0(),
                    Insns.return_(),
                    Insns.iconst_0(),
                    Insns.return_(),
                    Insns.iconst_0()
            )
            .selectCodeFragments(
                    Regexes.concatenate(
                            CodeRegexes.stencil(InsnStencils.return_())
                    )
            )
            .remove()
            .done()
            .selectCodeFragment(
                    Regexes.concatenate(
                            Regexes.anchorBegin(),
                            CodeRegexes.stencil(InsnStencils.iconst_0()),
                            CodeRegexes.stencil(InsnStencils.iconst_0()),
                            CodeRegexes.stencil(InsnStencils.iconst_0()),
                            Regexes.anchorEnd()
                    )
            )
            .isPresent();

    Assertions.assertTrue(present);
  }

  // Restore after removing
  @Test
  void test_remove_1() {
    boolean present = MethodQuery.ofNew()
            .addInsns(
                    Insns.iconst_0(),
                    Insns.return_(),
                    Insns.iconst_0(),
                    Insns.return_(),
                    Insns.iconst_0()
            )
            .selectCodeFragments(
                    Regexes.concatenate(
                            CodeRegexes.stencil(InsnStencils.return_())
                    )
            )
            .remove()
            .replaceWith(
                    InsnStencils.return_()
            )
            .done()
            .selectCodeFragment(
                    Regexes.concatenate(
                            Regexes.anchorBegin(),
                            CodeRegexes.stencil(InsnStencils.iconst_0()),
                            CodeRegexes.stencil(InsnStencils.return_()),
                            CodeRegexes.stencil(InsnStencils.iconst_0()),
                            CodeRegexes.stencil(InsnStencils.return_()),
                            CodeRegexes.stencil(InsnStencils.iconst_0()),
                            Regexes.anchorEnd()
                    )
            )
            .isPresent();

    Assertions.assertTrue(present);
  }

  // Remove after replacing
  @Test
  void test_remove_2() {
    boolean present = MethodQuery.ofNew()
            .addInsns(
                    Insns.iconst_0(),
                    Insns.return_(),
                    Insns.iconst_0(),
                    Insns.return_(),
                    Insns.iconst_0()
            )
            .selectCodeFragments(
                    Regexes.concatenate(
                            CodeRegexes.stencil(InsnStencils.return_())
                    )
            )
            .replaceWith(
                    InsnStencils.iconst_0()
            )
            .remove()
            .done()
            .selectCodeFragment(
                    Regexes.concatenate(
                            Regexes.anchorBegin(),
                            CodeRegexes.stencil(InsnStencils.iconst_0()),
                            CodeRegexes.stencil(InsnStencils.iconst_0()),
                            CodeRegexes.stencil(InsnStencils.iconst_0()),
                            Regexes.anchorEnd()
                    )
            )
            .isPresent();

    Assertions.assertTrue(present);
  }

  // Initialize from MethodNode
  @Test
  void test_3() {
    MethodNode methodNode = new MethodNode(OpcodesCompat.ASM_LATEST);
    methodNode.instructions.add(new InsnNode(Opcodes.NOP));

    boolean present = MethodQuery.of(methodNode)
            .selectCodeFragments(
                    Regexes.concatenate(
                            CodeRegexes.stencil(InsnStencils.nop())
                    )
            )
            .replaceWith(
                    InsnStencils.iconst_0()
            )
            .done()
            .selectCodeFragment(
                    Regexes.concatenate(
                            Regexes.anchorBegin(),
                            CodeRegexes.stencil(InsnStencils.iconst_0()),
                            Regexes.anchorEnd()
                    )
            )
            .isPresent();

    Assertions.assertTrue(present);
  }

  @Test
  void test_insert_0() {
    boolean present = MethodQuery.ofNew()
            .addInsns(
                    Insns.nop()
            )
            .selectCodeFragments(
                    Regexes.concatenate(
                            CodeRegexes.stencil(InsnStencils.nop())
                    )
            )
            .insertBefore(
                    InsnStencils.iconst_0()
            )
            .insertAfter(
                    InsnStencils.iconst_1()
            )
            .done()
            .selectCodeFragment(
                    Regexes.concatenate(
                            Regexes.anchorBegin(),
                            CodeRegexes.stencil(InsnStencils.iconst_0()),
                            CodeRegexes.stencil(InsnStencils.nop()),
                            CodeRegexes.stencil(InsnStencils.iconst_1()),
                            Regexes.anchorEnd()
                    )
            )
            .isPresent();

    Assertions.assertTrue(present);
  }

  @Test
  void test_insert_1() {
    MethodNode methodNode = new MethodNode(OpcodesCompat.ASM_LATEST);
    methodNode.instructions.add(new InsnNode(Opcodes.NOP));

    boolean present = MethodQuery.ofNew()
            .addInsns(
                    Insns.nop()
            )
            .selectCodeFragments(
                    Regexes.concatenate(
                            CodeRegexes.stencil(InsnStencils.nop())
                    )
            )
            .addFirst(
                    InsnStencils.iconst_0()
            )
            .addLast(
                    InsnStencils.iconst_1()
            )
            .done()
            .selectCodeFragment(
                    Regexes.concatenate(
                            Regexes.anchorBegin(),
                            CodeRegexes.stencil(InsnStencils.iconst_0()),
                            CodeRegexes.stencil(InsnStencils.nop()),
                            CodeRegexes.stencil(InsnStencils.iconst_1()),
                            Regexes.anchorEnd()
                    )
            )
            .isPresent();

    Assertions.assertTrue(present);
  }

  // Insert multiple times
  @Test
  void test_insert_2() {
    boolean present = MethodQuery.ofNew()
            .addInsns(
                    Insns.nop()
            )
            .selectCodeFragments(
                    Regexes.concatenate(
                            CodeRegexes.stencil(InsnStencils.nop())
                    )
            )
            .insertBefore(
                    InsnStencils.iconst_1()
            )
            .insertBefore(
                    InsnStencils.iconst_0()
            )
            .insertAfter(
                    InsnStencils.iconst_2()
            )
            .insertAfter(
                    InsnStencils.iconst_3()
            )
            .done()
            .selectCodeFragment(
                    Regexes.concatenate(
                            Regexes.anchorBegin(),
                            CodeRegexes.stencil(InsnStencils.iconst_1()),
                            CodeRegexes.stencil(InsnStencils.iconst_0()),
                            CodeRegexes.stencil(InsnStencils.nop()),
                            CodeRegexes.stencil(InsnStencils.iconst_2()),
                            CodeRegexes.stencil(InsnStencils.iconst_3()),
                            Regexes.anchorEnd()
                    )
            )
            .isPresent();

    Assertions.assertTrue(present);
  }

  @Test
  void test_bound_0() {
    boolean present = MethodQuery.ofNew()
            .addInsns(
                    Insns.nop(),
                    Insns.iconst_0()
            )
            .selectCodeFragments(
                    Regexes.bind(0,
                            Regexes.concatenate(
                                    CodeRegexes.stencil(InsnStencils.nop()),
                                    Regexes.bind(1,
                                            Regexes.any()
                                    )
                            )
                    )
            )
            .selectBound(1)
            .replaceWith(
                    InsnStencils.iconst_1()
            )
            .done()
            .done()
            .selectCodeFragment(
                    Regexes.concatenate(
                            Regexes.anchorBegin(),
                            CodeRegexes.stencil(InsnStencils.nop()),
                            CodeRegexes.stencil(InsnStencils.iconst_1()),
                            Regexes.anchorEnd()
                    )
            )
            .isPresent();

    Assertions.assertTrue(present);
  }

  // Nested bound
  @Test
  void test_bound_1() {
    boolean present = MethodQuery.ofNew()
            .addInsns(
                    Insns.nop(),
                    Insns.iconst_0()
            )
            .selectCodeFragments(
                    Regexes.bind(0,
                            Regexes.concatenate(
                                    CodeRegexes.stencil(InsnStencils.nop()),
                                    Regexes.bind(1,
                                            Regexes.any()
                                    )
                            )
                    )
            )
            .selectBound(0)
            .selectBound(1)
            .replaceWith(
                    InsnStencils.iconst_1()
            )
            .done()
            .done()
            .done()
            .selectCodeFragment(
                    Regexes.concatenate(
                            Regexes.anchorBegin(),
                            CodeRegexes.stencil(InsnStencils.nop()),
                            CodeRegexes.stencil(InsnStencils.iconst_1()),
                            Regexes.anchorEnd()
                    )
            )
            .isPresent();

    Assertions.assertTrue(present);
  }

  // Nested bound
  @Test
  void test_bound_2() {
    boolean present = MethodQuery.ofNew()
            .addInsns(
                    Insns.nop(),
                    Insns.iconst_0()
            )
            .selectCodeFragments(
                    Regexes.concatenate(
                            Regexes.bind(0,
                                    Regexes.concatenate(
                                            CodeRegexes.stencil(InsnStencils.nop())
                                    )
                            ),
                            Regexes.bind(1,
                                    Regexes.any()
                            )
                    )
            )
            .selectBound(0)
            .selectBound(1)
            .isPresent();

    Assertions.assertFalse(present);
  }

  @Test
  void test_before_0() {
    boolean present = MethodQuery.ofNew()
            .addInsns(
                    Insns.iconst_0(),
                    Insns.iconst_1(),
                    Insns.iconst_2()
            )
            .selectCodeFragments(
                    Regexes.concatenate(
                            CodeRegexes.stencil(InsnStencils.iconst_0()),
                            Regexes.bind(0,
                                    Regexes.concatenate(
                                            CodeRegexes.stencil(InsnStencils.iconst_1())
                                    )
                            ),
                            CodeRegexes.stencil(InsnStencils.iconst_2())
                    )
            )
            .selectBound(0)
            .before()
            .insertAfter(
                    InsnStencils.iconst_3()
            )
            .done()
            .done()
            .selectCodeFragment(
                    Regexes.concatenate(
                            Regexes.anchorBegin(),
                            CodeRegexes.stencil(InsnStencils.iconst_0()),
                            CodeRegexes.stencil(InsnStencils.iconst_3()),
                            CodeRegexes.stencil(InsnStencils.iconst_1()),
                            CodeRegexes.stencil(InsnStencils.iconst_2()),
                            Regexes.anchorEnd()
                    )
            )
            .isPresent();

    Assertions.assertTrue(present);
  }

  @Test
  void test_after_0() {
    boolean present = MethodQuery.ofNew()
            .addInsns(
                    Insns.iconst_0(),
                    Insns.iconst_1(),
                    Insns.iconst_2()
            )
            .selectCodeFragments(
                    Regexes.concatenate(
                            CodeRegexes.stencil(InsnStencils.iconst_0()),
                            Regexes.bind(0,
                                    Regexes.concatenate(
                                            CodeRegexes.stencil(InsnStencils.iconst_1())
                                    )
                            ),
                            CodeRegexes.stencil(InsnStencils.iconst_2())
                    )
            )
            .selectBound(0)
            .after()
            .insertAfter(
                    InsnStencils.iconst_3()
            )
            .done()
            .done()
            .selectCodeFragment(
                    Regexes.concatenate(
                            Regexes.anchorBegin(),
                            CodeRegexes.stencil(InsnStencils.iconst_0()),
                            CodeRegexes.stencil(InsnStencils.iconst_1()),
                            CodeRegexes.stencil(InsnStencils.iconst_2()),
                            CodeRegexes.stencil(InsnStencils.iconst_3()),
                            Regexes.anchorEnd()
                    )
            )
            .isPresent();

    Assertions.assertTrue(present);
  }

  @Test
  void test_4() {
    Object result = MethodQuery.ofNew()
            .setAccess(1)
            .addAccess(2)
            .removeAccess(2)
            .getAccess();
    Assertions.assertEquals(1, result);
  }

  @Test
  void test_5() {
    Object result = MethodQuery.ofNew()
            .setName("A")
            .getName();
    Assertions.assertEquals("A", result);
  }

  @Test
  void test_6() {
    Object result = MethodQuery.ofNew()
            .setDescriptor("()V")
            .getDescriptor();
    Assertions.assertEquals("()V", result);
  }

  @Test
  void test_7() {
    Object result = MethodQuery.ofNew()
            .setSignature("()V")
            .getSignature();
    Assertions.assertEquals("()V", result);
  }

  @Test
  void test_8() {
    Object result = MethodQuery.ofNew()
            .setExceptions("java/lang/Exception")
            .addExceptions("java/lang/Throwable")
            .removeExceptions("java/lang/Exception")
            .getExceptions();
    Assertions.assertEquals(
            ArrayListHelper.of("java/lang/Throwable"),
            result
    );
  }

  @Test
  void test_9() {
    Object result = MethodQuery.ofNew()
            .setAnnotationDefault(1)
            .getAnnotationDefault();
    Assertions.assertEquals(1, result);
  }

//  @Test
//  void test_10() {
//    Object result = MethodQuery.ofNew()
//            .setVisibleAnnotableParameterCount(1)
//            .getVisibleAnnotableParameterCount();
//    Assertions.assertEquals(1, result);
//  }
//
//  @Test
//  void test_11() {
//    Object result = MethodQuery.ofNew()
//            .setInvisibleAnnotableParameterCount(1)
//            .getInvisibleAnnotableParameterCount();
//    Assertions.assertEquals(1, result);
//  }

  @Test
  void test_12() {
    Object result = MethodQuery.ofNew()
            .setMaxStack(1)
            .getMaxStack();
    Assertions.assertEquals(1, result);
  }

  @Test
  void test_13() {
    Object result = MethodQuery.ofNew()
            .setMaxLocals(1)
            .getMaxLocals();
    Assertions.assertEquals(1, result);
  }

  // trailing label
  @Test
  void test_14() {
    boolean present = MethodQuery.ofNew()
            .addInsns(
                    Insns.iconst_1(),
                    Insns.label()
            )
            .selectCodeFragments(
                    Regexes.concatenate(
                            CodeRegexes.stencil(InsnStencils.iconst_1()),
                            CodeRegexes.stencil(InsnStencils.label(Stencils.any()))
                    )
            )
            .isPresent();

    Assertions.assertTrue(present);
  }

  // label only
  @Test
  void test_15() {
    boolean present = MethodQuery.ofNew()
            .addInsns(
                    Insns.iconst_1(),
                    Insns.label()
            )
            .selectCodeFragments(
                    Regexes.concatenate(
                            CodeRegexes.stencil(InsnStencils.label(Stencils.any()))
                    )
            )
            .isPresent();

    Assertions.assertTrue(present);
  }

  // setLocal, setStack
  @Test
  void test_16() {
    MethodNode mn = MethodQuery.ofNew()
            .addInsns(
                    Insns.return_(),
                    Insns.frame(Opcodes.F_NEW, 0, new Object[0], 1, new Object[]{"A"}),
                    Insns.areturn()
            )
            .selectCodeFragment(
                    Regexes.concatenate(
                            CodeRegexes.stencil(InsnStencils.frame(Stencils.bind(0))),
                            CodeRegexes.stencil(InsnStencils.areturn())
                    )
            )
            .replaceWith(
                    InsnStencils.frame(FrameStencils.setLocal(FrameStencils.setStack(Stencils.bound(0), 2, "B"), 1, "C")),
                    InsnStencils.areturn()
            )
            .done()
            .done();

    Assertions.assertEquals(ArrayListHelper.of("A", OpcodesHelper.AUTO, "B"), ((FrameNode) mn.instructions.get(1)).stack);
    Assertions.assertEquals(ArrayListHelper.of(OpcodesHelper.AUTO, "C"), ((FrameNode) mn.instructions.get(1)).local);
  }

  // setLocal, setStack
  @Test
  void test_17() {
    MethodNode mn = MethodQuery.ofNew()
            .addInsns(
                    Insns.return_(),
                    Insns.frame(Opcodes.F_NEW, 1, new Object[]{Opcodes.LONG}, 1, new Object[]{Opcodes.LONG}),
                    Insns.areturn()
            )
            .selectCodeFragment(
                    Regexes.concatenate(
                            CodeRegexes.stencil(InsnStencils.frame(Stencils.bind(0))),
                            CodeRegexes.stencil(InsnStencils.areturn())
                    )
            )
            .replaceWith(
                    InsnStencils.frame(FrameStencils.setLocal(FrameStencils.setStack(Stencils.bound(0), 2, Opcodes.INTEGER), 2, Opcodes.INTEGER)),
                    InsnStencils.areturn()
            )
            .done()
            .done();

    Assertions.assertEquals(ArrayListHelper.of(Opcodes.LONG, Opcodes.INTEGER), ((FrameNode) mn.instructions.get(1)).stack);
    Assertions.assertEquals(ArrayListHelper.of(Opcodes.LONG, Opcodes.INTEGER), ((FrameNode) mn.instructions.get(1)).local);
  }

  @Test
  void test_18() {
    LabelNode l0 = null, l1 = null;

    MethodNode mn = MethodQuery.ofNew()
            .addInsns(
                    l0 = Insns.label(),
                    Insns.return_(),
                    l1 = Insns.label()
            )
            .selectCodeFragment(
                    Regexes.concatenate(
                            CodeRegexes.stencil(InsnStencils.label(Stencils.bind(0))),
                            CodeRegexes.stencil(InsnStencils.return_()),
                            CodeRegexes.stencil(InsnStencils.label(Stencils.bind(1)))
                    )
            )
            .replaceWith(
                    InsnStencils.label(Stencils.bound(0)),
                    InsnStencils.ireturn(),
                    InsnStencils.label(Stencils.bound(1))
            )
            .done()
            .done();

    Assertions.assertEquals(l0, mn.instructions.get(0));
    Assertions.assertEquals(Opcodes.IRETURN, mn.instructions.get(1).getOpcode());
    Assertions.assertEquals(l1, mn.instructions.get(2));
  }
}
