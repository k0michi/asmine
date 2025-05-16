package com.koyomiji.jasmine.regex.compiler.code;

import com.koyomiji.jasmine.common.ArrayListHelper;
import com.koyomiji.jasmine.regex.AbstractRegexInsn;
import com.koyomiji.jasmine.regex.code.InsnStencilInsn;
import com.koyomiji.jasmine.regex.compiler.AbstractRegexNode;
import com.koyomiji.jasmine.regex.compiler.RegexCompilerContext;
import com.koyomiji.jasmine.stencil.insn.AbstractInsnStencil;

import java.util.List;

public class InsnStencilNode extends AbstractRegexNode {
  public AbstractInsnStencil stencil;

  public InsnStencilNode(AbstractInsnStencil stencil) {
    this.stencil = stencil;
  }

  @Override
  public List<AbstractRegexInsn> compile(RegexCompilerContext context) {
    return ArrayListHelper.of(new InsnStencilInsn(stencil));
  }
}
