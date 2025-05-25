package com.koyomiji.asmine.regex.compiler.string;

public class StringRegexes {
  public static CharLiteralNode literal(char literal) {
    return new CharLiteralNode(literal);
  }
}
