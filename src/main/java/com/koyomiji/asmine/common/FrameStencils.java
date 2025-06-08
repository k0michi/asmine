package com.koyomiji.asmine.common;

import com.koyomiji.asmine.stencil.FrameSetLocalStencil;
import com.koyomiji.asmine.stencil.FrameSetStackStencil;
import com.koyomiji.asmine.stencil.IStencil;
import org.objectweb.asm.tree.FrameNode;

import java.util.List;

public class FrameStencils {
  public static FrameSetLocalStencil setLocal(IStencil<FrameNode> frame, int index, List<Object> values) {
    return new FrameSetLocalStencil(frame, index, values);
  }

  public static FrameSetLocalStencil setLocal(IStencil<FrameNode> frame, int index, Object... values) {
    return new FrameSetLocalStencil(frame, index, values);
  }

  public static FrameSetStackStencil setStack(IStencil<FrameNode> frame, int index, List<Object> values) {
    return new FrameSetStackStencil(frame, index, values);
  }

  public static FrameSetStackStencil setStack(IStencil<FrameNode> frame, int index, Object... values) {
    return new FrameSetStackStencil(frame, index, values);
  }
}
