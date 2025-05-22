package com.koyomiji.asmine.regex.compiler.string;

import com.koyomiji.asmine.common.ArrayListHelper;
import com.koyomiji.asmine.regex.AbstractRegexInsn;
import com.koyomiji.asmine.regex.string.CharLiteralInsn;
import com.koyomiji.asmine.regex.compiler.AbstractRegexNode;
import com.koyomiji.asmine.regex.compiler.RegexCompilerContext;

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
