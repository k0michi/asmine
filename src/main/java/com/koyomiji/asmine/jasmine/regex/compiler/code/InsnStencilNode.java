package com.koyomiji.asmine.regex.compiler.code;

import com.koyomiji.asmine.common.ArrayListHelper;
import com.koyomiji.asmine.regex.AbstractRegexInsn;
import com.koyomiji.asmine.regex.code.InsnStencilInsn;
import com.koyomiji.asmine.regex.compiler.AbstractRegexNode;
import com.koyomiji.asmine.regex.compiler.RegexCompilerContext;
import com.koyomiji.asmine.stencil.insn.AbstractInsnStencil;

import java.util.List;

public class InsnStencilNode extends AbstractRegexNode {
  public AbstractInsnStencil stencil;

  public InsnStencilNode(AbstractInsnStencil stencil) {
    this.stencil = stencil;
  }

  @Override
  public void compile(RegexCompilerContext context) {
    context.emit(new InsnStencilInsn(stencil));
  }
}
