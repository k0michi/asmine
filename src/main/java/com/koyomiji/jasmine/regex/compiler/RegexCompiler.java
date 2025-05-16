package com.koyomiji.jasmine.regex.compiler;

import com.koyomiji.jasmine.regex.AbstractRegexInsn;
import com.koyomiji.jasmine.regex.TerminalInsn;

import java.util.List;

public class RegexCompiler {
  public static final Object BOUNDARY_KEY = new Object();

  public List<AbstractRegexInsn> compile(AbstractRegexNode node) {
    node = new ConcatenateNode(
            new StarNode(new AnyNode(), QuantifierType.LAZY),
            new BindNode(BOUNDARY_KEY, node)
    );

    List<AbstractRegexInsn> compiled = node.compile(new RegexCompilerContext());
    compiled.add(new TerminalInsn());
    return compiled;
  }
}
