package com.koyomiji.jasmine.test;

import com.koyomiji.jasmine.common.InsnStencils;
import com.koyomiji.jasmine.common.Insns;
import com.koyomiji.jasmine.query.MethodQuery;
import com.koyomiji.jasmine.regex.compiler.Regexes;
import com.koyomiji.jasmine.regex.compiler.code.CodeRegexes;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

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
}
