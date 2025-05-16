package com.koyomiji.jasmine.regex.compiler.code;

import com.koyomiji.jasmine.common.ArrayListHelper;
import com.koyomiji.jasmine.regex.AbstractRegexInsn;
import com.koyomiji.jasmine.regex.code.InsnLiteralInsn;
import com.koyomiji.jasmine.regex.compiler.AbstractRegexNode;
import com.koyomiji.jasmine.regex.compiler.RegexCompilerContext;
import org.objectweb.asm.tree.AbstractInsnNode;

import java.util.List;

public class InsnLiteralNode extends AbstractRegexNode {
  public AbstractInsnNode literal;

  public InsnLiteralNode(AbstractInsnNode literal) {
    this.literal = literal;
  }

  @Override
  public List<AbstractRegexInsn> compile(RegexCompilerContext context) {
    return ArrayListHelper.of(new InsnLiteralInsn(literal));
  }
}
