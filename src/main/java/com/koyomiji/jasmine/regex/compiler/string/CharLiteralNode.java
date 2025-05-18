package com.koyomiji.jasmine.regex.compiler.string;

import com.koyomiji.jasmine.common.ArrayListHelper;
import com.koyomiji.jasmine.regex.AbstractRegexInsn;
import com.koyomiji.jasmine.regex.string.CharLiteralInsn;
import com.koyomiji.jasmine.regex.compiler.AbstractRegexNode;
import com.koyomiji.jasmine.regex.compiler.RegexCompilerContext;

import java.util.List;

public class CharLiteralNode extends AbstractRegexNode {
  public char literal;

  public CharLiteralNode(char literal) {
    this.literal = literal;
  }

  @Override
  public void compile(RegexCompilerContext context) {
    context.emit(new CharLiteralInsn(literal));
  }
}
