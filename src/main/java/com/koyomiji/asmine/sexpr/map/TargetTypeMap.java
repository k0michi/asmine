package com.koyomiji.asmine.sexpr.map;

import org.objectweb.asm.TypeReference;

import java.util.HashMap;
import java.util.Map;

/*
 * See java.lang.classfile.TypeAnnotation
 */
public class TargetTypeMap {
  private static final Map<Integer, String> intToString = new HashMap<>();
  private static final Map<String, Integer> stringToInt = new HashMap<>();

  static {
    intToString.put(TypeReference.CLASS_TYPE_PARAMETER, "CLASS_TYPE_PARAMETER");
    intToString.put(TypeReference.METHOD_TYPE_PARAMETER, "METHOD_TYPE_PARAMETER");
    intToString.put(TypeReference.CLASS_EXTENDS, "CLASS_EXTENDS");
    intToString.put(TypeReference.CLASS_TYPE_PARAMETER_BOUND, "CLASS_TYPE_PARAMETER_BOUND");
    intToString.put(TypeReference.METHOD_TYPE_PARAMETER_BOUND, "METHOD_TYPE_PARAMETER_BOUND");
    intToString.put(TypeReference.FIELD, "FIELD");
    intToString.put(TypeReference.METHOD_RETURN, "METHOD_RETURN");
    intToString.put(TypeReference.METHOD_RECEIVER, "METHOD_RECEIVER");
    intToString.put(TypeReference.METHOD_FORMAL_PARAMETER, "METHOD_FORMAL_PARAMETER");
    intToString.put(TypeReference.THROWS, "THROWS");
    intToString.put(TypeReference.LOCAL_VARIABLE, "LOCAL_VARIABLE");
    intToString.put(TypeReference.RESOURCE_VARIABLE, "RESOURCE_VARIABLE");
    intToString.put(TypeReference.EXCEPTION_PARAMETER, "EXCEPTION_PARAMETER");
    intToString.put(TypeReference.INSTANCEOF, "INSTANCEOF");
    intToString.put(TypeReference.NEW, "NEW");
    intToString.put(TypeReference.CONSTRUCTOR_REFERENCE, "CONSTRUCTOR_REFERENCE");
    intToString.put(TypeReference.METHOD_REFERENCE, "METHOD_REFERENCE");
    intToString.put(TypeReference.CAST, "CAST");
    intToString.put(TypeReference.CONSTRUCTOR_INVOCATION_TYPE_ARGUMENT, "CONSTRUCTOR_INVOCATION_TYPE_ARGUMENT");
    intToString.put(TypeReference.METHOD_INVOCATION_TYPE_ARGUMENT, "METHOD_INVOCATION_TYPE_ARGUMENT");
    intToString.put(TypeReference.CONSTRUCTOR_REFERENCE_TYPE_ARGUMENT, "CONSTRUCTOR_REFERENCE_TYPE_ARGUMENT");
    intToString.put(TypeReference.METHOD_REFERENCE_TYPE_ARGUMENT, "METHOD_REFERENCE_TYPE_ARGUMENT");

    stringToInt.put("CLASS_TYPE_PARAMETER", TypeReference.CLASS_TYPE_PARAMETER);
    stringToInt.put("METHOD_TYPE_PARAMETER", TypeReference.METHOD_TYPE_PARAMETER);
    stringToInt.put("CLASS_EXTENDS", TypeReference.CLASS_EXTENDS);
    stringToInt.put("CLASS_TYPE_PARAMETER_BOUND", TypeReference.CLASS_TYPE_PARAMETER_BOUND);
    stringToInt.put("METHOD_TYPE_PARAMETER_BOUND", TypeReference.METHOD_TYPE_PARAMETER_BOUND);
    stringToInt.put("FIELD", TypeReference.FIELD);
    stringToInt.put("METHOD_RETURN", TypeReference.METHOD_RETURN);
    stringToInt.put("METHOD_RECEIVER", TypeReference.METHOD_RECEIVER);
    stringToInt.put("METHOD_FORMAL_PARAMETER", TypeReference.METHOD_FORMAL_PARAMETER);
    stringToInt.put("THROWS", TypeReference.THROWS);
    stringToInt.put("LOCAL_VARIABLE", TypeReference.LOCAL_VARIABLE);
    stringToInt.put("RESOURCE_VARIABLE", TypeReference.RESOURCE_VARIABLE);
    stringToInt.put("EXCEPTION_PARAMETER", TypeReference.EXCEPTION_PARAMETER);
    stringToInt.put("INSTANCEOF", TypeReference.INSTANCEOF);
    stringToInt.put("NEW", TypeReference.NEW);
    stringToInt.put("CONSTRUCTOR_REFERENCE", TypeReference.CONSTRUCTOR_REFERENCE);
    stringToInt.put("METHOD_REFERENCE",    TypeReference.METHOD_REFERENCE);
    stringToInt.put("CAST", TypeReference.CAST);
    stringToInt.put("CONSTRUCTOR_INVOCATION_TYPE_ARGUMENT", TypeReference.CONSTRUCTOR_INVOCATION_TYPE_ARGUMENT);
    stringToInt.put("METHOD_REFERENCE_TYPE_ARGUMENT", TypeReference.METHOD_REFERENCE_TYPE_ARGUMENT);
    stringToInt.put("CONSTRUCTOR_REFERENCE_TYPE_ARGUMENT", TypeReference.CONSTRUCTOR_REFERENCE_TYPE_ARGUMENT);
    stringToInt.put("METHOD_INVOCATION_TYPE_ARGUMENT", TypeReference.METHOD_INVOCATION_TYPE_ARGUMENT);
  }

  public static String toString(int type) {
    return intToString.get(type);
  }

  public static int toInt(String type) {
    return stringToInt.get(type);
  }
}
