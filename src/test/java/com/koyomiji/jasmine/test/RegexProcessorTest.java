package com.koyomiji.jasmine.test;

import com.koyomiji.jasmine.common.ArrayListHelper;
import com.koyomiji.jasmine.regex.*;
import com.koyomiji.jasmine.regex.compiler.*;
import com.koyomiji.jasmine.regex.compiler.string.StringRegexes;
import com.koyomiji.jasmine.regex.string.CharLiteralInsn;
import com.koyomiji.jasmine.tuple.Pair;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class RegexProcessorTest {
  @Test
  void test_0() {
    ArrayList<AbstractRegexInsn> insns = ArrayListHelper.of(
            new TerminalInsn()
    );
    ArrayList<Object> string = ArrayListHelper.of(
            'a'
    );
    RegexProcessor vm = new RegexProcessor(insns, string);
    Assertions.assertEquals(0, vm.execute().getProgramCounter());
  }

  @Test
  void test_1() {
    ArrayList<AbstractRegexInsn> insns = ArrayListHelper.of(
            new CharLiteralInsn('a'),
            new TerminalInsn()
    );
    ArrayList<Object> string = ArrayListHelper.of(
            'a'
    );
    RegexProcessor vm = new RegexProcessor(insns, string);
    Assertions.assertEquals(1, vm.execute().getProgramCounter());
  }

  @Test
  void test_2() {
    ArrayList<AbstractRegexInsn> insns = ArrayListHelper.of(
            new ForkInsn(1, 3),
            new CharLiteralInsn('a'),
            new JumpInsn(-2),
            new TerminalInsn()
    );
    ArrayList<Object> string = ArrayListHelper.of(
            'a'
    );
    RegexProcessor vm = new RegexProcessor(insns, string);
    Assertions.assertEquals(3, vm.execute().getProgramCounter());
  }

  @Test
  void test_3() {
    ArrayList<AbstractRegexInsn> insns = ArrayListHelper.of(
            new ForkInsn(1, 3),
            new CharLiteralInsn('a'),
            new JumpInsn(-2),
            new ForkInsn(1, 3),
            new CharLiteralInsn('a'),
            new JumpInsn(-2),
            new TerminalInsn()
    );
    ArrayList<Object> string = ArrayListHelper.of(
            'a',
            'b'
    );
    RegexProcessor vm = new RegexProcessor(insns, string);
    Assertions.assertEquals(6, vm.execute().getProgramCounter());
  }

  @Test
  void test_4() {
    ArrayList<AbstractRegexInsn> insns = ArrayListHelper.of(
            new ForkInsn(1, 3),
            new CharLiteralInsn('a'),
            new JumpInsn(-2),
            new ForkInsn(1, 3),
            new CharLiteralInsn('a'),
            new JumpInsn(-2),
            new TerminalInsn()
    );
    ArrayList<Object> string = ArrayListHelper.of(
            'a',
            'a',
            'a',
            'b'
    );
    RegexProcessor vm = new RegexProcessor(insns, string);
    Assertions.assertEquals(6, vm.execute().getProgramCounter());
  }

  @Test
  void test_5() {
    ArrayList<AbstractRegexInsn> insns = ArrayListHelper.of(
            new CharLiteralInsn('a'),
            new ForkInsn(-1, 1),
            new TerminalInsn()
    );
    ArrayList<Object> string = ArrayListHelper.of(
            'b'
    );
    RegexProcessor vm = new RegexProcessor(insns, string);
    Assertions.assertNull(vm.execute());
  }

  @Test
  void test_6() {
    ArrayList<AbstractRegexInsn> insns = ArrayListHelper.of(
            new CharLiteralInsn('a'),
            new ForkInsn(-1, 1),
            new TerminalInsn()
    );
    ArrayList<Object> string = ArrayListHelper.of(
            'a',
            'a',
            'a',
            'a',
            'a',
            'a'
    );
    RegexProcessor vm = new RegexProcessor(insns, string);
    Assertions.assertEquals(2, vm.execute().getProgramCounter());
  }

  private List<AbstractRegexInsn> compile(AbstractRegexNode node) {
    return new RegexCompiler().compile(node);
  }

  // concatenate
  @Test
  void test_compiler_0() {
    List<AbstractRegexInsn> insns = compile(Regexes.concatenate(
            StringRegexes.literal('a'),
            StringRegexes.literal('b'),
            StringRegexes.literal('c')
    ));
    ArrayList<Object> string = ArrayListHelper.of(
            'a',
            'b',
            'c'
    );
    RegexProcessor vm = new RegexProcessor(insns, string);
    Assertions.assertNotNull(vm.execute());
  }

  // alternate
  @Test
  void test_compiler_1() {
    List<AbstractRegexInsn> insns = compile(Regexes.concatenate(
            Regexes.alternate(
                    StringRegexes.literal('a'),
                    StringRegexes.literal('b')
            ),
            StringRegexes.literal('c')
    ));
    ArrayList<Object> string = ArrayListHelper.of(
            'a',
            'c'
    );
    RegexProcessor vm = new RegexProcessor(insns, string);
    Assertions.assertNotNull(vm.execute());
  }

  // anchor begin, anchor end
  @Test
  void test_compiler_2() {
    List<AbstractRegexInsn> insns = compile(Regexes.concatenate(
            Regexes.anchorBegin(),
            Regexes.alternate(
                    StringRegexes.literal('a'),
                    StringRegexes.literal('b')
            ),
            StringRegexes.literal('c'),
            Regexes.anchorEnd()
    ));
    ArrayList<Object> string = ArrayListHelper.of(
            'a',
            'c'
    );
    RegexProcessor vm = new RegexProcessor(insns, string);
    Assertions.assertNotNull(vm.execute());
  }

  // star
  @Test
  void test_compiler_4() {
    List<AbstractRegexInsn> insns = compile(Regexes.concatenate(
            Regexes.star(
                    Regexes.alternate(
                            StringRegexes.literal('a'),
                            StringRegexes.literal('b')
                    )
            )
    ));
    ArrayList<Object> string = ArrayListHelper.of(
            'a',
            'b'
    );
    RegexProcessor vm = new RegexProcessor(insns, string);
    Assertions.assertNotNull(vm.execute());
  }

  // plus
  @Test
  void test_compiler_5() {
    List<AbstractRegexInsn> insns = compile(Regexes.concatenate(
            Regexes.plus(
                    Regexes.alternate(
                            StringRegexes.literal('a'),
                            StringRegexes.literal('b')
                    )
            )
    ));
    ArrayList<Object> string = ArrayListHelper.of(
            'a',
            'b'
    );
    RegexProcessor vm = new RegexProcessor(insns, string);
    Assertions.assertNotNull(vm.execute());
  }

  // question
  @Test
  void test_compiler_6() {
    List<AbstractRegexInsn> insns = compile(Regexes.concatenate(
            Regexes.question(
                    StringRegexes.literal('a')
            )
    ));
    ArrayList<Object> string = ArrayListHelper.of(
            'a'
    );
    RegexProcessor vm = new RegexProcessor(insns, string);
    Assertions.assertNotNull(vm.execute());
  }

  // star
  @Test
  void test_compiler_3() {
    List<AbstractRegexInsn> insns = compile(Regexes.concatenate(
            Regexes.anchorBegin(),
            Regexes.alternate(
                    StringRegexes.literal('a'),
                    StringRegexes.literal('b')
            ),
            StringRegexes.literal('c'),
            Regexes.anchorEnd()
    ));
    ArrayList<Object> string = ArrayListHelper.of(
            'a',
            'c',
            'c'
    );
    RegexProcessor vm = new RegexProcessor(insns, string);
    Assertions.assertNull(vm.execute());
  }

  @Test
  void test_compiler_beginEnd_0() {
    List<AbstractRegexInsn> insns = compile(Regexes.bind(0, Regexes.concatenate(
            Regexes.alternate(
                    StringRegexes.literal('a'),
                    StringRegexes.literal('b')
            ),
            StringRegexes.literal('c')
    )));
    ArrayList<Object> string = ArrayListHelper.of(
            'a',
            'c',
            'a',
            'c'
    );
    RegexProcessor vm = new RegexProcessor(insns, string);
    Assertions.assertEquals(Pair.of(0, 2), vm.execute().getBoundRange(0));
  }

  @Test
  void test_compiler_beginEnd_1() {
    List<AbstractRegexInsn> insns = compile(Regexes.bind(0, Regexes.plus(
            Regexes.concatenate(
                    Regexes.alternate(
                            StringRegexes.literal('a'),
                            StringRegexes.literal('b')
                    ),
                    StringRegexes.literal('c')
            )
    )));
    ArrayList<Object> string = ArrayListHelper.of(
            'a',
            'c',
            'b',
            'c'
    );
    RegexProcessor vm = new RegexProcessor(insns, string);
    Assertions.assertEquals(Pair.of(0, 4), vm.execute().getBoundRange(0));
  }

  @Test
  void test_compiler_bound_0() {
    List<AbstractRegexInsn> insns = compile(Regexes.concatenate(
            Regexes.bind(0, Regexes.plus(
                    Regexes.concatenate(
                            Regexes.alternate(
                                    StringRegexes.literal('a'),
                                    StringRegexes.literal('b')
                            ),
                            StringRegexes.literal('c')
                    )
            )),
            Regexes.bound(0)
    ));
    ArrayList<Object> string = ArrayListHelper.of(
            'a',
            'c',
            'a',
            'c'
    );
    RegexProcessor vm = new RegexProcessor(insns, string);
    Assertions.assertEquals(Pair.of(0, 2), vm.execute().getBoundRange(0));
  }

  // bind nest
  @Test
  void test_compiler_bound_1() {
    List<AbstractRegexInsn> insns = compile(Regexes.concatenate(
            Regexes.bind(0, Regexes.bind(1, Regexes.plus(
                    Regexes.concatenate(
                            Regexes.alternate(
                                    StringRegexes.literal('a'),
                                    StringRegexes.literal('b')
                            ),
                            StringRegexes.literal('c')
                    )
            ))),
            Regexes.bound(0)
    ));
    ArrayList<Object> string = ArrayListHelper.of(
            'a',
            'c',
            'a',
            'c'
    );
    RegexProcessor vm = new RegexProcessor(insns, string);
    Assertions.assertEquals(Pair.of(0, 2), vm.execute().getBoundRange(1));
  }

  // bound is nested inside bind
  @Test
  void test_compiler_bound_2() {
    List<AbstractRegexInsn> insns = compile(Regexes.concatenate(
            Regexes.bind(0, Regexes.plus(
                    Regexes.concatenate(
                            Regexes.alternate(
                                    StringRegexes.literal('a'),
                                    StringRegexes.literal('b')
                            ),
                            StringRegexes.literal('c')
                    )
            )),
            Regexes.bind(1, Regexes.bound(0)),
            Regexes.bound(1)
    ));
    ArrayList<Object> string = ArrayListHelper.of(
            'a',
            'c',
            'a',
            'c',
            'a',
            'c'
    );
    RegexProcessor vm = new RegexProcessor(insns, string);
    Assertions.assertEquals(Pair.of(2, 4), vm.execute().getBoundRange(1));
  }

  // trace
  @Test
  void test_7() {
    ArrayList<AbstractRegexInsn> insns = ArrayListHelper.of(
            new TraceInsn(0),
            new TerminalInsn()
    );
    ArrayList<Object> string = ArrayListHelper.of();
    RegexProcessor vm = new RegexProcessor(insns, string);
    Assertions.assertEquals(ArrayListHelper.of(
            0
    ), vm.execute().getTrace());
  }
}
