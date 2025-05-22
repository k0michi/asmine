package com.koyomiji.asmine.sexpr;

import com.koyomiji.asmine.common.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Stack;

public class SExprReader {
  private final PeekableReader reader;
  private final Stack<SExprVisitor> stack = new Stack<>();
  private int previousChar = -1;
  private int line = 1;
  private int column = 0;
  private int offset = -1;

  public SExprReader(String string) {
    reader = new PeekableReader(new StringReader(string));
  }

  public SExprReader(InputStream inputStream) {
    reader = new PeekableReader(new InputStreamReader(inputStream));
  }

  public void accept(SExprVisitor visitor) throws IOException {
    parseExpr(visitor);
    skipWhitespaces();
    expect(-1);
  }

  /*
   * [Utilities]
   */

  private int read() throws IOException {
    int read = reader.read();

    if (read == '\r' || previousChar != '\r' && read == '\n') {
      line++;
      column = 1;
    } else {
      column++;
    }

    offset++;
    previousChar = read;
    return read;
  }

  private void skip(int n) throws IOException {
    for (int i = 0; i < n; i++) {
      read();
    }
  }

  private boolean testRegex(char c, String regex) throws IOException {
    return String.valueOf(c).matches(regex);
  }

  private boolean testRegex(int c, String regex) throws IOException {
    return testRegex((char) c, regex);
  }

  private void skipUntil(String regex) throws IOException {
    while (reader.peek() != -1 && !testRegex(reader.peek(), regex)) {
      skip(1);
    }
  }

  private void skipBlockComment() throws IOException {
    while (true) {
      if (consume(-1)) {
        throw new SyntaxException("Unexpected EOF", new SourceLocation(line, column, offset));
      }

      if (consume("(;")) {
        skipBlockComment();
      }

      if (consume(";)")) {
        break;
      }

      skip(1);
    }
  }

  private void skipLineComment() throws IOException {
    skipUntil("\n");
    skip(1);
  }

  private void skipWhitespaces() throws IOException {
    while (true) {
      skipUntil("\\S");

      if (consume("(;")) {
        skipBlockComment();
        continue;
      }

      if (consume(";;")) {
        skipLineComment();
        continue;
      }

      break;
    }
  }

  private void readUntil(StringBuilder sb, String regex) throws IOException {
    while (reader.peek() != -1 && !testRegex(reader.peek(), regex)) {
      sb.append((char) read());
    }
  }

  private void readUntil(List<Byte> bytes, String regex) throws IOException {
    while (reader.peek() != -1 && !testRegex(reader.peek(), regex)) {
      byte[] charBytes = ModifiedUTF8.encode((char) read());
      bytes.addAll(ArrayHelper.toList(charBytes));
    }
  }

  private void expect(int c) throws IOException {
    if (read() != c) {
      throw new SyntaxException("Expected '" + (char) c + "'", new SourceLocation(line, column, offset));
    }
  }

  private boolean consume(int c) throws IOException {
    if (reader.peek() == c) {
      skip(1);
      return true;
    }

    return false;
  }

  private boolean next(int c) throws IOException {
    return reader.peek() == c;
  }

  private void expect(String s) throws IOException {
    if (!consume(s)) {
      throw new SyntaxException("Expected '" + s + "'", new SourceLocation(line, column, offset));
    }
  }

  private boolean consume(String s) throws IOException {
    char[] chars = new char[s.length()];
    int read = reader.peek(chars);

    if (s.length() == read && s.equals(new String(chars))) {
      skip(s.length());
      return true;
    }

    return false;
  }

  /*
   * [Parse]
   */

  private void parseExpr(SExprVisitor visitor) throws IOException {
    skipWhitespaces();

    if (consume(-1)) {
      throw new SyntaxException("Unexpected EOF", new SourceLocation(line, column, offset));
    }

    if (consume('(')) {
      parseList(visitor);
    } else {
      parseAtom(visitor);
    }
  }

  /*
   * Convert Wasm-like integer string to Java-like one
   */
  private Optional<String> preprocessInteger(String string) {
    StringBuilder sb = new StringBuilder();
    int i = 0;

    if (string.startsWith("-", i)) {
      sb.append('-');
      i++;
    } else if (string.startsWith("+", i)) {
      sb.append('+');
      i++;
    }

    boolean hex = false;

    if (string.startsWith("0x", i)) {
      sb.append("0x");
      i += 2;
      hex = true;
    }

    if (!(i < string.length())) {
      return Optional.empty();
    }

    while (i < string.length()) {
      if (string.charAt(i) >= '0' && string.charAt(i) <= '9' || hex && (string.charAt(i) >= 'a' && string.charAt(i) <= 'f' || string.charAt(i) >= 'A' && string.charAt(i) <= 'F')) {
        sb.append(string.charAt(i));

        if (i + 1 < string.length() && string.charAt(i + 1) == '_') {
          i++;
        }
      } else {
        return Optional.empty();
      }

      i++;
    }

    return Optional.of(sb.toString());
  }

  private Optional<Long> tryParseInteger(String string) {
    Optional<String> preprocessed = preprocessInteger(string);

    if (!preprocessed.isPresent()) {
      return Optional.empty();
    }

    string = preprocessed.get();

    if (string.startsWith("0x") || string.startsWith("+0x")) {
      try {
        return Optional.of(Long.parseLong(string.substring(string.indexOf('x') + 1), 16));
      } catch (NumberFormatException e) {
        return Optional.empty();
      }
    } else if (string.startsWith("-0x")) {
      try {
        return Optional.of(-Long.parseLong(string.substring(string.indexOf('x') + 1), 16));
      } catch (NumberFormatException e) {
        return Optional.empty();
      }
    }

    try {
      return Optional.of(Long.parseLong(string));
    } catch (NumberFormatException e) {
      return Optional.empty();
    }
  }

  /*
   * Convert Wasm-like float string to Java-like one
   */
  private Optional<String> preprocessFloatingPoint(String string) {
    StringBuilder sb = new StringBuilder();
    int i = 0;

    if (string.startsWith("nan", i)) {
      sb.append("nan");
      i += 3;

      if (i == string.length()) {
        return Optional.of(sb.toString());
      } else {
        return Optional.empty();
      }
    }

    if (string.startsWith("-", i)) {
      sb.append('-');
      i++;
    } else if (string.startsWith("+", i)) {
      sb.append('+');
      i++;
    }

    if (string.startsWith("inf", i)) {
      sb.append("inf");
      i += 3;

      if (i == string.length()) {
        return Optional.of(sb.toString());
      } else {
        return Optional.empty();
      }
    }

    boolean hex = false;

    if (string.startsWith("0x", i)) {
      sb.append("0x");
      i += 2;
      hex = true;
    }

    if (!(i < string.length())) {
      return Optional.empty();
    }

    boolean point = false;
    boolean exponent = false;

    while (i < string.length()) {
      if (!exponent && (string.charAt(i) >= '0' && string.charAt(i) <= '9' || hex && (string.charAt(i) >= 'a' && string.charAt(i) <= 'f' || string.charAt(i) >= 'A' && string.charAt(i) <= 'F'))) {
        sb.append(string.charAt(i));

        if (i + 1 < string.length() && string.charAt(i + 1) == '_') {
          i++;
        }
      } else if (!point && !exponent && string.charAt(i) == '.') {
        sb.append('.');
        point = true;
      } else if (!exponent && (!hex && (string.charAt(i) == 'e' || string.charAt(i) == 'E') || hex && (string.charAt(i) == 'p' || string.charAt(i) == 'P'))) {
        sb.append(string.charAt(i));
        exponent = true;

        if (i + 1 < string.length() && (string.charAt(i + 1) == '+' || string.charAt(i + 1) == '-')) {
          i++;
          sb.append(string.charAt(i));
        }
      } else if (exponent && string.charAt(i) >= '0' && string.charAt(i) <= '9') {
        sb.append(string.charAt(i));

        if (i + 1 < string.length() && string.charAt(i + 1) == '_') {
          i++;
        }
      } else {
        return Optional.empty();
      }

      i++;
    }

    if (!exponent && hex) {
      sb.append("p0");
    }

    return Optional.of(sb.toString());
  }

  private Optional<Double> tryParseFloatingPoint(String string) {
    Optional<String> preprocessed = preprocessFloatingPoint(string);

    if (!preprocessed.isPresent()) {
      return Optional.empty();
    }

    string = preprocessed.get();

    if (string.equals("nan")) {
      return Optional.of(Double.NaN);
    } else if (string.equals("inf") || string.equals("+inf")) {
      return Optional.of(Double.POSITIVE_INFINITY);
    } else if (string.equals("-inf")) {
      return Optional.of(Double.NEGATIVE_INFINITY);
    }

    try {
      return Optional.of(Double.parseDouble(string));
    } catch (NumberFormatException e) {
      return Optional.empty();
    }
  }

  private void parseAtom(SExprVisitor visitor) throws IOException {
    if (consume('"')) {
      parseString(visitor);
      return;
    }

    StringBuilder sb = new StringBuilder();
    int i = 0;

    while (true) {
      if (next(-1) || next(' ') || next('\t') || next('\n') || next('\r') || next('"') || next('(') || next(')')) {
        break;
      }

      sb.append((char) read());

      if (i == 0) {
        visitor.visitSourceLocation(new SourceLocation(line, column, offset));
      }

      i++;
    }

    String s = sb.toString();

    Optional<Long> integer = tryParseInteger(s);
    Optional<Double> floatingPoint = tryParseFloatingPoint(s);

    if (integer.isPresent()) {
      visitor.visitInteger(integer.get());
    } else if (floatingPoint.isPresent()) {
      visitor.visitFloatingPoint(floatingPoint.get());
    } else {
      visitor.visitSymbol(s);
    }
  }

  private Optional<Byte> consumeHexDigit() throws IOException {
    if (consume('0')) {
      return Optional.of((byte) 0);
    } else if (consume('1')) {
      return Optional.of((byte) 1);
    } else if (consume('2')) {
      return Optional.of((byte) 2);
    } else if (consume('3')) {
      return Optional.of((byte) 3);
    } else if (consume('4')) {
      return Optional.of((byte) 4);
    } else if (consume('5')) {
      return Optional.of((byte) 5);
    } else if (consume('6')) {
      return Optional.of((byte) 6);
    } else if (consume('7')) {
      return Optional.of((byte) 7);
    } else if (consume('8')) {
      return Optional.of((byte) 8);
    } else if (consume('9')) {
      return Optional.of((byte) 9);
    } else if (consume('a')) {
      return Optional.of((byte) 10);
    } else if (consume('b')) {
      return Optional.of((byte) 11);
    } else if (consume('c')) {
      return Optional.of((byte) 12);
    } else if (consume('d')) {
      return Optional.of((byte) 13);
    } else if (consume('e')) {
      return Optional.of((byte) 14);
    } else if (consume('f')) {
      return Optional.of((byte) 15);
    }

    return Optional.empty();
  }

  private void parseString(SExprVisitor visitor) throws IOException {
    List<Byte> bytes = new ArrayList<>();

    visitor.visitSourceLocation(new SourceLocation(line, column, offset));

    while (true) {
      if (consume(-1)) {
        throw new SyntaxException("Unexpected EOF", new SourceLocation(line, column, offset));
      }

      if (consume('"')) {
        break;
      }

      if (consume('\\')) {
        if (consume('t')) {
          bytes.add((byte) '\t');
        } else if (consume('n')) {
          bytes.add((byte) '\n');
        } else if (consume('r')) {
          bytes.add((byte) '\r');
        } else if (consume('\'')) {
          bytes.add((byte) '\'');
        } else if (consume('"')) {
          bytes.add((byte) '"');
        } else if (consume('\\')) {
          bytes.add((byte) '\\');
        } else if (consume('u')) {
          expect('{');
          StringBuilder unicode = new StringBuilder();
          readUntil(unicode, "}");
          expect('}');
          int codePoint = Integer.parseInt(unicode.toString(), 16);
          bytes.addAll(ArrayHelper.toList(ModifiedUTF8.encode(Character.toChars(codePoint))));
        } else {
          Optional<Byte> hexDigit = consumeHexDigit();

          if (hexDigit.isPresent()) {
            Optional<Byte> hexDigit2 = consumeHexDigit();

            if (hexDigit2.isPresent()) {
              byte b = (byte) ((hexDigit.get() << 4) | hexDigit2.get());
              bytes.add(b);
            } else {
              throw new SyntaxException("Unexpected character in byte sequence: " + (char) reader.peek(), new SourceLocation(line, column, offset));
            }
          } else {
            throw new SyntaxException("Unexpected escape sequence: \\" + (char) reader.peek(), new SourceLocation(line, column, offset));
          }
        }
      } else {
        bytes.addAll(ArrayHelper.toList(ModifiedUTF8.encode((char) read())));
      }
    }

    byte[] byteArray = ListHelper.toByteArray(bytes);
    Optional<String> decoded = ModifiedUTF8.decode(byteArray);

    if (decoded.isPresent()) {
      visitor.visitString(decoded.get());
    } else {
      visitor.visitDataString(byteArray);
    }
  }

  private void parseList(SExprVisitor visitor) throws IOException {
    visitor.visitSourceLocation(new SourceLocation(line, column, offset));
    stack.add(visitor.visitList());

    while (true) {
      skipWhitespaces();

      if (consume(-1)) {
        throw new SyntaxException("Unexpected EOF", new SourceLocation(line, column, offset));
      }

      if (consume(')')) {
        break;
      }

      parseExpr(stack.peek());
    }

    stack.peek().visitEnd();
    stack.pop();
  }
}
