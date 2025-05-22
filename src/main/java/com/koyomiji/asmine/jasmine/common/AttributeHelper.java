package com.koyomiji.asmine.common;

import org.objectweb.asm.Attribute;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

public class AttributeHelper {
  public static Attribute newInstance(String type) {
    try {
      Constructor<Attribute> ctor = Attribute.class.getDeclaredConstructor(String.class);
      ctor.setAccessible(true);
      return ctor.newInstance(type);
    } catch (IllegalAccessException e) {
      throw new RuntimeException(e);
    } catch (NoSuchMethodException | InstantiationException | InvocationTargetException e) {
      throw new RuntimeException(e);
    }
  }

  public static byte[] getContent(Attribute attribute) {
    try {
      Field contentField = Attribute.class.getDeclaredField("content");
      contentField.setAccessible(true);
      return (byte[]) contentField.get(attribute);
    } catch (NoSuchFieldException | IllegalAccessException e) {
      throw new RuntimeException(e);
    }
  }

  public static void setContent(Attribute attribute, byte[] content) {
    try {
      Field contentField = Attribute.class.getDeclaredField("content");
      contentField.setAccessible(true);
      contentField.set(attribute, content);
    } catch (NoSuchFieldException | IllegalAccessException e) {
      throw new RuntimeException(e);
    }
  }
}
