package com.koyomiji.asmine.test;

import com.koyomiji.asmine.common.ArrayListHelper;
import com.koyomiji.asmine.common.InsnStencils;
import com.koyomiji.asmine.common.Insns;
import com.koyomiji.asmine.regex.AbstractRegexInsn;
import com.koyomiji.asmine.regex.RegexModule;
import com.koyomiji.asmine.regex.code.CodeRegexProcessor;
import com.koyomiji.asmine.regex.code.CodeRegexThread;
import com.koyomiji.asmine.regex.compiler.*;
import com.koyomiji.asmine.regex.compiler.code.CodeRegexes;
import com.koyomiji.asmine.stencil.Parameters;
import com.koyomiji.asmine.stencil.ResolutionExeption;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.LabelNode;

import java.util.List;

public class CodeRegexProcessorTest {
  private RegexModule compile(AbstractRegexNode node) {
    return new RegexCompiler().compile(node);
  }

  private LabelNode l0;
  private LabelNode l1;
  private LabelNode l2;
  private LabelNode l3;

  private final Object p0 = 0;
  private final Object p1 = 1;
  private final Object p2 = 2;
  private final Object p3 = 3;

  // Stencil match
  @Test
  void test_0() {
    RegexModule regex= compile(
            Regexes.concatenate(
                    CodeRegexes.stencil(InsnStencils.iconst_0())
            )
    );
    List<AbstractInsnNode> string = ArrayListHelper.of(
            Insns.iconst_0()
    );
    CodeRegexProcessor vm = new CodeRegexProcessor(regex, string);
    Assertions.assertNotNull(vm.execute());
  }

  // Literal match
  @Test
  void test_1() {
    RegexModule regex= compile(
            Regexes.concatenate(
                    CodeRegexes.literal(Insns.iconst_0())
            )
    );
    List<AbstractInsnNode> string = ArrayListHelper.of(
            Insns.iconst_0()
    );
    CodeRegexProcessor vm = new CodeRegexProcessor(regex, string);
    Assertions.assertNotNull(vm.execute());
  }

  // Binding
  @Test
  void test_2() {
    RegexModule regex= compile(
            Regexes.concatenate(
                    Regexes.bind(0, CodeRegexes.literal(Insns.iconst_0())),
                    Regexes.bound(0)
            )
    );
    List<AbstractInsnNode> string = ArrayListHelper.of(
            Insns.iconst_0(),
            Insns.iconst_0()
    );
    CodeRegexProcessor vm = new CodeRegexProcessor(regex, string);
    Assertions.assertNotNull(vm.execute());
  }

  // Stencil match failure
  @Test
  void test_3() {
    RegexModule regex= compile(
            Regexes.concatenate(
                    CodeRegexes.stencil(InsnStencils.iconst_0())
            )
    );
    List<AbstractInsnNode> string = ArrayListHelper.of(
            Insns.iconst_1()
    );
    CodeRegexProcessor vm = new CodeRegexProcessor(regex, string);
    Assertions.assertNull(vm.execute());
  }

  // Literal match failure
  @Test
  void test_4() {
    RegexModule regex= compile(
            Regexes.concatenate(
                    CodeRegexes.literal(Insns.iconst_0())
            )
    );
    List<AbstractInsnNode> string = ArrayListHelper.of(
            Insns.iconst_1()
    );
    CodeRegexProcessor vm = new CodeRegexProcessor(regex, string);
    Assertions.assertNull(vm.execute());
  }

  // Ignoring label
  @Test
  void test_5() {
    RegexModule regex= compile(
            Regexes.concatenate(
                    CodeRegexes.literal(Insns.iconst_0())
            )
    );
    List<AbstractInsnNode> string = ArrayListHelper.of(
            Insns.label(),
            Insns.iconst_0()
    );
    CodeRegexProcessor vm = new CodeRegexProcessor(regex, string);
    Assertions.assertNotNull(vm.execute());
  }

  // Label match
  @Test
  void test_6() {
    RegexModule regex= compile(
            Regexes.concatenate(
                    CodeRegexes.stencil(InsnStencils.label(Parameters.any())),
                    CodeRegexes.literal(Insns.iconst_0())
            )
    );
    List<AbstractInsnNode> string = ArrayListHelper.of(
            Insns.label(),
            Insns.iconst_0()
    );
    CodeRegexProcessor vm = new CodeRegexProcessor(regex, string);
    Assertions.assertNotNull(vm.execute());
  }

  // Label and jump
  @Test
  void test_7() {
    RegexModule regex= compile(
            Regexes.concatenate(
                    CodeRegexes.stencil(InsnStencils.goto_(Parameters.bind(p0))),
                    CodeRegexes.stencil(InsnStencils.label(Parameters.bound(p0))),
                    CodeRegexes.literal(Insns.iconst_0())
            )
    );
    List<AbstractInsnNode> string = ArrayListHelper.of(
            Insns.goto_(l0 = Insns.label()),
            l0,
            Insns.iconst_0()
    );
    CodeRegexProcessor vm = new CodeRegexProcessor(regex, string);
    Assertions.assertNotNull(vm.execute());
  }

  // Label located at the end
  @Test
  void test_8() {
    RegexModule regex= compile(
            Regexes.concatenate(
                    CodeRegexes.literal(Insns.iconst_0()),
                    CodeRegexes.stencil(InsnStencils.label(Parameters.bind(p0)))
            )
    );
    List<AbstractInsnNode> string = ArrayListHelper.of(
            Insns.iconst_0(),
            Insns.label()
    );
    CodeRegexProcessor vm = new CodeRegexProcessor(regex, string);
    Assertions.assertNotNull(vm.execute());
  }

  // Label resolves to the final label
  @Test
  void test_9() throws ResolutionExeption {
    RegexModule regex= compile(
            Regexes.concatenate(
                    CodeRegexes.stencil(InsnStencils.label(Parameters.bind(p0))),
                    CodeRegexes.stencil(InsnStencils.label(Parameters.bind(p1))),
                    CodeRegexes.literal(Insns.iconst_0())
            )
    );
    List<AbstractInsnNode> string = ArrayListHelper.of(
            l0 = Insns.label(),
            l1 = Insns.label(),
            Insns.iconst_0()
    );
    CodeRegexProcessor vm = new CodeRegexProcessor(regex, string);
    CodeRegexThread thread = vm.execute();
    Assertions.assertNotNull(thread);
    Assertions.assertSame(thread.resolveParameter(p0), l1);
    Assertions.assertSame(thread.resolveParameter(p1), l1);
  }

  // Line number and frame
  @Test
  void test_10() throws ResolutionExeption {
    RegexModule regex= compile(
            Regexes.concatenate(
                    CodeRegexes.stencil(InsnStencils.label(Parameters.bind(p0))),
                    CodeRegexes.stencil(InsnStencils.lineNumber(Parameters.bind(p1), Parameters.bound(p0))),
                    CodeRegexes.stencil(InsnStencils.frame(Parameters.any(), Parameters.any(), Parameters.any(), Parameters.any(), Parameters.any())),
                    CodeRegexes.literal(Insns.iconst_0())
            )
    );
    List<AbstractInsnNode> string = ArrayListHelper.of(
            l0 = Insns.label(),
            Insns.lineNumber(1, l0),
            Insns.frame(0, 0, null, 0, null),
            Insns.iconst_0()
    );
    CodeRegexProcessor vm = new CodeRegexProcessor(regex, string);
    CodeRegexThread thread = vm.execute();
    Assertions.assertNotNull(thread);
  }

  // Binding non-existent label
  @Test
  void test_11() throws ResolutionExeption {
    RegexModule regex= compile(
            Regexes.concatenate(
                    CodeRegexes.stencil(InsnStencils.label(Parameters.bind(p0))),
                    CodeRegexes.literal(Insns.iconst_0())
            )
    );
    List<AbstractInsnNode> string = ArrayListHelper.of(
            Insns.iconst_0()
    );
    CodeRegexProcessor vm = new CodeRegexProcessor(regex, string);
    CodeRegexThread thread = vm.execute();
    Assertions.assertNull(thread);
  }

  // Only label
  @Test
  void test_12() throws ResolutionExeption {
    RegexModule regex= compile(
            Regexes.concatenate(
                    CodeRegexes.stencil(InsnStencils.label(Parameters.bind(p0)))
            )
    );
    List<AbstractInsnNode> string = ArrayListHelper.of(
            Insns.label()
    );
    CodeRegexProcessor vm = new CodeRegexProcessor(regex, string);
    CodeRegexThread thread = vm.execute();
    Assertions.assertNotNull(thread);
  }

  // Label literal match
  @Test
  void test_13() throws ResolutionExeption {
    RegexModule regex= compile(
            Regexes.concatenate(
                    CodeRegexes.literal(l0 = Insns.label())
            )
    );
    List<AbstractInsnNode> string = ArrayListHelper.of(
            l0
    );
    CodeRegexProcessor vm = new CodeRegexProcessor(regex, string);
    CodeRegexThread thread = vm.execute();
    Assertions.assertNotNull(thread);
  }
}
