package com.koyomiji.asmine.regex.compiler.code;

import com.koyomiji.asmine.common.ArrayListHelper;
import com.koyomiji.asmine.regex.AbstractRegexInsn;
import com.koyomiji.asmine.regex.code.InsnLiteralInsn;
import com.koyomiji.asmine.regex.compiler.AbstractRegexNode;
import com.koyomiji.asmine.regex.compiler.RegexCompilerContext;
import org.objectweb.asm.tree.AbstractInsnNode;

import java.util.List;

public class InsnLiteralNode extends AbstractRegexNode {
  public AbstractInsnNode literal;

  public InsnLiteralNode(AbstractInsnNode literal) {
    this.literal = literal;
  }

  @Override
  public void compile(RegexCompilerContext context) {
    context.emit(new InsnLiteralInsn(literal));
  }
}
