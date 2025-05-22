package com.koyomiji.asmine.common;

import org.objectweb.asm.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

public class AttributeBuilder {
  private String type;
  private byte[] content;

  public AttributeBuilder() {
  }

  public AttributeBuilder setType(String type) {
    this.type = type;
    return this;
  }

  public AttributeBuilder setContent(byte[] content) {
    this.content = content;
    return this;
  }

  public Attribute build() {
    try {
      Constructor<Attribute> ctor = Attribute.class.getDeclaredConstructor(String.class);
      ctor.setAccessible(true);
      Attribute attribute = ctor.newInstance(type);
      Field contentField = Attribute.class.getDeclaredField("content");
      contentField.setAccessible(true);
      contentField.set(attribute, content);
      return attribute;
    } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException |
             NoSuchFieldException e) {
      throw new RuntimeException(e);
    }
  }
}
