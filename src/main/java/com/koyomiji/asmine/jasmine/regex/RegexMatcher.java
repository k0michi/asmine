package com.koyomiji.asmine.regex;

import com.koyomiji.asmine.regex.compiler.*;

import java.util.List;

public class RegexMatcher {
  public static final Object BOUNDARY_KEY = new Object();
  private AbstractRegexNode regexNode;

  public RegexMatcher(AbstractRegexNode node) {
    this.regexNode = node;
  }

  protected RegexCompiler newCompiler() {
    return new RegexCompiler();
  }

  protected RegexProcessor newProcessor(RegexModule module, List<?> string) {
    return new RegexProcessor(module, string);
  }

  protected MatchResult newMatchResult(RegexThread thread) {
    return new MatchResult(thread);
  }

  private RegexModule compile(AbstractRegexNode regexNode) {
    return newCompiler().compile(regexNode);
  }

  private RegexThread execute(RegexModule regexModule, List<?> string, int begin) {
    return newProcessor(regexModule, string).execute(begin);
  }

  public MatchResult match(List<?> string, int begin) {
    AbstractRegexNode regexNode = this.regexNode;

    regexNode = new ConcatenateNode(
            new StarNode(new AnyNode(), QuantifierType.LAZY),
            new BindNode(BOUNDARY_KEY, regexNode)
    );

    RegexModule module = compile(regexNode);
    RegexThread thread = execute(module, string, begin);

    if (thread == null) {
      return null;
    }

    return newMatchResult(thread);
  }

  public MatchResult matchAll(List<?> string, int begin) {
    AbstractRegexNode regexNode = this.regexNode;

    regexNode = new StarNode(
            new ConcatenateNode(
                    new StarNode(new AnyNode(), QuantifierType.LAZY),
                    new BindNode(BOUNDARY_KEY, regexNode)
            )
    );

    RegexModule module = compile(regexNode);
    RegexThread thread = execute(module, string, begin);

    if (thread == null) {
      return null;
    }

    return newMatchResult(thread);
  }
}
