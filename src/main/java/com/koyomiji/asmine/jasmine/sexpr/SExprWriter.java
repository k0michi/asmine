package com.koyomiji.asmine.sexpr;

import com.koyomiji.asmine.common.ModifiedUTF8;
import com.koyomiji.asmine.common.StringHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SExprWriter extends SExprVisitor implements IFormattable {
  private SExprWriter parent;
  private final StringBuilder stringBuilder = new StringBuilder();
  private final List<Object> text = new ArrayList<>();
  private final String separatorString = " ";
  private String indentString = "  ";
  private String lineBreakString = "\n";
  private int indentCount = 0;
  private boolean afterBegin = true;
  private boolean afterBreak = true;
  private boolean inBlock = false;

  public SExprWriter() {
    super(null);
  }

  public SExprWriter(SExprWriter parent) {
    this();
    this.parent = parent;
    indentCount = parent.indentCount;
  }

  public String getIndentString() {
    return indentString;
  }

  public void setIndentString(String indentString) {
    this.indentString = indentString;
  }

  public String getLineBreakString() {
    return lineBreakString;
  }

  public void setLineBreakString(String lineBreakString) {
    this.lineBreakString = lineBreakString;
  }

  private SExprWriter newWriter() {
    return new SExprWriter(this);
  }

  private void appendSpaces() {
    stringBuilder.setLength(0);

    if (inBlock && afterBreak) {
      for (int i = 0; i < indentCount; i++) {
        stringBuilder.append(indentString);
      }
    }

    if ((!inBlock || !afterBreak) && !afterBegin) {
      stringBuilder.append(separatorString);
    }

    text.add(stringBuilder.toString());
    afterBegin = false;
    afterBreak = false;
  }

  @Override
  public void visitSymbol(String value) {
    appendSpaces();
    stringBuilder.setLength(0);

    stringBuilder.append(value);

    text.add(stringBuilder.toString());
    super.visitSymbol(value);
  }

  @Override
  public void visitString(String value) {
    appendSpaces();
    stringBuilder.setLength(0);

    stringBuilder.append('"');
    escapeString(stringBuilder, value);
    stringBuilder.append('"');

    text.add(stringBuilder.toString());
    super.visitString(value);
  }

  private void escapeString(StringBuilder sb, String string) {
    Optional<int[]> codePoints = StringHelper.codePoints(string);

    if (codePoints.isPresent()) {
      for (int c : codePoints.get()) {
        if (c >= 0x20 && c != 0x7F && c != '"' && c != '\\') {
          sb.append((char) c);
        } else if (c == '\t') {
          sb.append("\\t");
        } else if (c == '\n') {
          sb.append("\\n");
        } else if (c == '\r') {
          sb.append("\\r");
        } else if (c == '"') {
          sb.append("\\\"");
        } else if (c == '\\') {
          sb.append("\\\\");
        } else {
          sb.append(String.format("\\u{%X}", c));
        }
      }
    } else {
      appendDataString(sb, ModifiedUTF8.encode(string));
    }
  }

  private static void appendDataString(StringBuilder sb, byte [] bytes) {
    sb.append('"');

    for (byte b : bytes) {
      sb.append("\\");
      sb.append(Integer.toString(Byte.toUnsignedInt(b) >> 4, 16));
      sb.append(Integer.toString(Byte.toUnsignedInt(b) & 0x0F, 16));
    }

    sb.append('"');
  }

  @Override
  public void visitDataString(byte[] value) {
    appendSpaces();
    stringBuilder.setLength(0);

    appendDataString(stringBuilder, value);

    text.add(stringBuilder.toString());
    super.visitDataString(value);
  }

  @Override
  public void visitInteger(long value) {
    appendSpaces();
    stringBuilder.setLength(0);

    stringBuilder.append(value);

    text.add(stringBuilder.toString());
    super.visitInteger(value);
  }

  private String doubleToString(double value) {
    if (Double.isNaN(value)) {
      return "nan";
    } else if (Double.isInfinite(value)) {
      return value > 0 ? "inf" : "-inf";
    } else {
      return String.valueOf(value);
    }
  }

  @Override
  public void visitFloatingPoint(double value) {
    appendSpaces();
    stringBuilder.setLength(0);

    stringBuilder.append(doubleToString(value));

    text.add(stringBuilder.toString());
    super.visitFloatingPoint(value);
  }

  @Override
  public SExprWriter visitList() {
    appendSpaces();
    text.add('(');

    SExprWriter writer = newWriter();
    text.add(writer.getText());

    return writer;
  }

  @Override
  public void visitEnd() {
    if (inBlock) {
      indentCount--;
      visitLineBreak();
      appendSpaces();
    }

    text.add(')');
    super.visitEnd();
  }

  public void visitLineBreak() {
    text.add('\n');

    if (!inBlock) {
      indentCount++;
      inBlock = true;
    }

    afterBreak = true;
  }

  protected List<Object> getText() {
    return text;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    printList(sb, text);
    return sb.toString();
  }

  private void printList(StringBuilder sb, List<?> list) {
    for (Object o : list) {
      if (o instanceof List<?>) {
        printList(sb, (List<?>) o);
      } else {
        sb.append(o.toString());
      }
    }
  }
}
