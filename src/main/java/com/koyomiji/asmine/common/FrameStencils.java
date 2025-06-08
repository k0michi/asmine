package com.koyomiji.asmine.common;

import com.koyomiji.asmine.stencil.FrameSetLocalStencil;
import com.koyomiji.asmine.stencil.FrameSetStackStencil;
import com.koyomiji.asmine.stencil.IStencil;
import org.objectweb.asm.tree.FrameNode;

public class FrameStencils {
  public static FrameSetLocalStencil setLocal(IStencil<FrameNode> frame, int index, Object value) {
    return new FrameSetLocalStencil(frame, index, value);
  }

  public static FrameSetStackStencil setStack(IStencil<FrameNode> frame, int index, Object value) {
    return new FrameSetStackStencil(frame, index, value);
  }
}
