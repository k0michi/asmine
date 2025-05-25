package com.koyomiji.asmine.common;

import org.objectweb.asm.Attribute;

public class UnknownAttribute extends Attribute {
  private final boolean codeAttribute;

  public UnknownAttribute(String type, byte[] content, boolean codeAttribute) {
    super(type);
    AttributeHelper.setContent(this, content);
    this.codeAttribute = codeAttribute;
  }

  public byte[] getContent() {
    return AttributeHelper.getContent(this);
  }

  @Override
  public boolean isCodeAttribute() {
    return codeAttribute;
  }
}
