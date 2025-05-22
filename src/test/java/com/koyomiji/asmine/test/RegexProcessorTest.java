package com.koyomiji.asmine.test;

import com.koyomiji.asmine.common.ArrayListHelper;
import com.koyomiji.asmine.regex.*;
import com.koyomiji.asmine.regex.compiler.*;
import com.koyomiji.asmine.regex.compiler.string.StringRegexes;
import com.koyomiji.asmine.regex.string.CharLiteralInsn;
import com.koyomiji.asmine.tuple.Pair;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class RegexProcessorTest {
  private List<Object> split(String s) {
    List<Object> string = new ArrayList<>();

    for (int i = 0; i < s.length(); i++) {
      string.add(s.charAt(i));
    }

    return string;
  }

  @Test
  void test_0() {
    ArrayList<AbstractRegexInsn> insns = ArrayListHelper.of(
            new ReturnInsn()
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
            new ReturnInsn()
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
            new ReturnInsn()
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
            new ReturnInsn()
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
            new ReturnInsn()
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
            new ReturnInsn()
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
            new ReturnInsn()
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

  private RegexModule compile(AbstractRegexNode node) {
    return new RegexCompiler().compile(node);
  }

  // concatenate
  @Test
  void test_compiler_0() {
    RegexModule insns = compile(Regexes.concatenate(
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
    RegexModule insns = compile(Regexes.concatenate(
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
    RegexModule insns = compile(Regexes.concatenate(
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
  void test_compiler_3() {
    RegexModule insns = compile(Regexes.concatenate(
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

  // lazy star
  @Test
  void test_compiler_4() {
    RegexModule insns = compile(Regexes.concatenate(
            Regexes.bind(0,
                    Regexes.star(
                            Regexes.alternate(
                                    StringRegexes.literal('a'),
                                    StringRegexes.literal('b')
                            ),
                            QuantifierType.LAZY)
            )
    ));
    ArrayList<Object> string = ArrayListHelper.of(
            'a',
            'b'
    );
    RegexProcessor vm = new RegexProcessor(insns, string);
    Assertions.assertEquals(Pair.of(0, 0), vm.execute().getBoundLast(0));
  }

  // plus
  @Test
  void test_compiler_5() {
    RegexModule insns = compile(Regexes.concatenate(
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

  // lazy plus
  @Test
  void test_compiler_6() {
    RegexModule insns = compile(Regexes.concatenate(
            Regexes.bind(0,
                    Regexes.plus(
                            Regexes.alternate(
                                    StringRegexes.literal('a'),
                                    StringRegexes.literal('b')
                            ),
                            QuantifierType.LAZY)
            )
    ));
    ArrayList<Object> string = ArrayListHelper.of(
            'a',
            'b'
    );
    RegexProcessor vm = new RegexProcessor(insns, string);
    Assertions.assertEquals(Pair.of(0, 1), vm.execute().getBoundLast(0));
  }

  // question
  @Test
  void test_compiler_7() {
    RegexModule insns = compile(Regexes.concatenate(
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

  // lazy question
  @Test
  void test_compiler_8() {
    RegexModule insns = compile(Regexes.concatenate(
            Regexes.bind(0,
                    Regexes.question(
                            Regexes.alternate(
                                    StringRegexes.literal('a'),
                                    StringRegexes.literal('b')
                            ),
                            QuantifierType.LAZY)
            )
    ));
    ArrayList<Object> string = ArrayListHelper.of(
            'a',
            'b'
    );
    RegexProcessor vm = new RegexProcessor(insns, string);
    Assertions.assertEquals(Pair.of(0, 0), vm.execute().getBoundLast(0));
  }

  @Test
  void test_compiler_beginEnd_0() {
    RegexModule insns = compile(Regexes.bind(0, Regexes.concatenate(
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
    Assertions.assertEquals(Pair.of(0, 2), vm.execute().getBoundLast(0));
  }

  @Test
  void test_compiler_beginEnd_1() {
    RegexModule insns = compile(Regexes.bind(0, Regexes.plus(
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
    Assertions.assertEquals(Pair.of(0, 4), vm.execute().getBoundLast(0));
  }

  @Test
  void test_compiler_bound_m3() {
    RegexModule insns = compile(Regexes.concatenate(
            Regexes.bind(0,
                    Regexes.concatenate(
                            StringRegexes.literal('a'),
                            StringRegexes.literal('b')
                    )
            ),
            Regexes.bound(0)
    ));
    ArrayList<Object> string = ArrayListHelper.of(
            'a',
            'b',
            'a',
            'b'
    );
    RegexProcessor vm = new RegexProcessor(insns, string);
    Assertions.assertEquals(Pair.of(0, 2), vm.execute().getBoundLast(0));
  }

  @Test
  void test_compiler_bound_m2() {
    RegexModule insns = compile(Regexes.concatenate(
            Regexes.bind(0,
                    Regexes.concatenate(
                            Regexes.alternate(
                                    StringRegexes.literal('a'),
                                    StringRegexes.literal('b')
                            ),
                            StringRegexes.literal('c')
                    )
            ),
            Regexes.bound(0)
    ));
    ArrayList<Object> string = ArrayListHelper.of(
            'a',
            'c',
            'a',
            'c'
    );
    RegexProcessor vm = new RegexProcessor(insns, string);
    Assertions.assertEquals(Pair.of(0, 2), vm.execute().getBoundLast(0));
  }

  @Test
  void test_compiler_bound_m1() {
    RegexModule insns = compile(Regexes.concatenate(
            Regexes.bind(0,
                    Regexes.concatenate(
                            Regexes.alternate(
                                    StringRegexes.literal('a'),
                                    StringRegexes.literal('b')
                            ),
                            StringRegexes.literal('c')
                    )
            ),
            Regexes.bound(0)
    ));
    ArrayList<Object> string = ArrayListHelper.of(
            'a',
            'c',
            'b',
            'c'
    );
    RegexProcessor vm = new RegexProcessor(insns, string);
    Assertions.assertNull(vm.execute());
  }

  @Test
  void test_compiler_bound_0() {
    RegexModule insns = compile(Regexes.concatenate(
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
    Assertions.assertEquals(Pair.of(0, 2), vm.execute().getBoundLast(0));
  }

  // bind nest
  @Test
  void test_compiler_bound_1() {
    RegexModule insns = compile(Regexes.concatenate(
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
    Assertions.assertEquals(Pair.of(0, 2), vm.execute().getBoundLast(1));
  }

  // bound is nested inside bind
  @Test
  void test_compiler_bound_2() {
    RegexModule insns = compile(Regexes.concatenate(
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
    Assertions.assertEquals(Pair.of(2, 4), vm.execute().getBoundLast(1));
  }

  // nested bound
  @Test
  void test_compiler_bound_3() {
    RegexModule insns = compile(Regexes.concatenate(
            Regexes.bind(0,
                    Regexes.alternate(
                            StringRegexes.literal('a'),
                            StringRegexes.literal('b')
                    )),
            Regexes.bind(1,
                    Regexes.concatenate(
                            Regexes.bound(0),
                            Regexes.bind(2, Regexes.alternate(
                                    StringRegexes.literal('c'),
                                    StringRegexes.literal('d')
                            )))),
            Regexes.bound(1),
            Regexes.bound(2)

    ));
    List<Object> string = split("aacacc");
    RegexProcessor vm = new RegexProcessor(insns, string);
    Assertions.assertNotNull(vm.execute());
  }

  // bind nest with concat
  @Test
  void test_compiler_bound_4() {
    RegexModule insns = compile(Regexes.concatenate(
            Regexes.bind(0,
                    Regexes.concatenate(
                            Regexes.bind(1, Regexes.plus(
                                            Regexes.concatenate(
                                                    Regexes.alternate(
                                                            StringRegexes.literal('a'),
                                                            StringRegexes.literal('b')
                                                    ),
                                                    StringRegexes.literal('c')
                                            )
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
    Assertions.assertEquals(Pair.of(0, 2), vm.execute().getBoundLast(1));
  }

  // trace
  @Test
  void test_7() {
    ArrayList<AbstractRegexInsn> insns = ArrayListHelper.of(
            new TraceInsn(0),
            new ReturnInsn()
    );
    ArrayList<Object> string = ArrayListHelper.of();
    RegexProcessor vm = new RegexProcessor(insns, string);
    Assertions.assertEquals(ArrayListHelper.of(
            0
    ), vm.execute().getTrace());
  }

  // duplicate bind key
  @Test
  void test_8() {
    Assertions.assertThrows(RegexCompilerException.class, () -> {
      compile(Regexes.concatenate(
              Regexes.bind(0, Regexes.any()),
              Regexes.bind(0, Regexes.any())

      ));
    });
  }

  // missing bind key
  @Test
  void test_9() {
    Assertions.assertThrows(RegexCompilerException.class, () -> {
      compile(Regexes.concatenate(
              Regexes.bound(0)

      ));
    });
  }

  // invoke
  @Test
  void test_10() {
    RegexModule module = new RegexModule(ArrayListHelper.of(
            new RegexFunction(0, ArrayListHelper.of(
                    new InvokeInsn(1),
                    new ReturnInsn()
            )),
            new RegexFunction(1, ArrayListHelper.of(
                    new TraceInsn(0),
                    new ReturnInsn()
            ))
    ));
    ArrayList<Object> string = ArrayListHelper.of();
    RegexProcessor vm = new RegexProcessor(module, string);
    Assertions.assertEquals(
            ArrayListHelper.of(
                    0
            ), vm.execute().getTrace());
  }

  // recursive call
  @Test
  void test_11() {
    ConcatenateNode c0;

    RegexModule insns = compile((c0 = Regexes.concatenate()).setChildren(
            StringRegexes.literal('('),
            Regexes.question(c0),
            StringRegexes.literal(')')
    ));
    List<Object> string = split("((()))");
    RegexProcessor vm = new RegexProcessor(insns, string);
    Assertions.assertNotNull(vm.execute());
  }

  // multiple binds
  @Test
  void test_12() {
    ConcatenateNode c0;

    RegexModule insns = compile((c0 = Regexes.concatenate()).setChildren(
            Regexes.bind(0,
                    Regexes.concatenate(
                            StringRegexes.literal('('),
                            Regexes.question(c0),
                            StringRegexes.literal(')')
                    )
            )
    ));
    List<Object> string = split("((()))");
    RegexProcessor vm = new RegexProcessor(insns, string);
    RegexThread thread = vm.execute();
    Assertions.assertEquals(ArrayListHelper.of(
            Pair.of(2, 4),
            Pair.of(1, 5),
            Pair.of(0, 6)
    ), thread.getBounds(0));
  }

  // multiple binds - 2
  @Test
  void test_13() {
    ConcatenateNode c0;

    RegexModule insns = compile((c0 = Regexes.concatenate()).setChildren(
            Regexes.star(
                    Regexes.bind(0,
                            Regexes.concatenate(
                                    StringRegexes.literal('('),
                                    Regexes.question(c0),
                                    StringRegexes.literal(')')
                            )
                    )
            )
    ));
    List<Object> string = split("(()())");
    RegexProcessor vm = new RegexProcessor(insns, string);
    RegexThread thread = vm.execute();
    Assertions.assertEquals(ArrayListHelper.of(
            Pair.of(1, 3),
            Pair.of(3, 5),
            Pair.of(0, 6)
    ), thread.getBounds(0));
  }

  // multiple binds - 3
  @Test
  void test_14() {
    ConcatenateNode c0;

    RegexModule insns = compile((c0 = Regexes.concatenate()).setChildren(
            Regexes.star(
                    Regexes.bind(0,
                            Regexes.concatenate(
                                    StringRegexes.literal('('),
                                    c0,
                                    StringRegexes.literal(')')
                            )
                    )
            )
    ));
    List<Object> string = split("(()())");
    RegexProcessor vm = new RegexProcessor(insns, string);
    RegexThread thread = vm.execute();
    Assertions.assertEquals(ArrayListHelper.of(
            Pair.of(1, 3),
            Pair.of(3, 5),
            Pair.of(0, 6)
    ), thread.getBounds(0));
  }
}
