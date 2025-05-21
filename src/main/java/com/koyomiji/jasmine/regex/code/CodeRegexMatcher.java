package com.koyomiji.jasmine.regex.code;

import com.koyomiji.jasmine.common.InsnListListAdapter;
import com.koyomiji.jasmine.regex.*;
import com.koyomiji.jasmine.regex.compiler.AbstractRegexNode;
import com.koyomiji.jasmine.regex.compiler.RegexCompiler;
import org.objectweb.asm.tree.InsnList;

import java.util.List;

public class CodeRegexMatcher extends RegexMatcher {
  public CodeRegexMatcher(AbstractRegexNode node) {
    super(node);
  }

  @Override
  protected CodeRegexProcessor newProcessor(RegexModule module, List<?> string) {
    return new CodeRegexProcessor(module, string);
  }

  @Override
  protected CodeMatchResult newMatchResult(RegexThread thread) {
    return new CodeMatchResult((CodeRegexThread) thread);
  }

  public CodeMatchResult match(InsnList string, int begin) {
    return (CodeMatchResult) super.match(new InsnListListAdapter(string), begin);
  }

  public CodeMatchResult matchAll(InsnList string, int begin) {
    return (CodeMatchResult) super.matchAll(new InsnListListAdapter(string), begin);
  }
}
