package com.koyomiji.asmine.common;

import java.util.Optional;

public class ModifiedUTF8 {
  public static Optional<String> decode(byte[] bytes) {
    return decode(bytes, 0, bytes.length);
  }

  public static Optional<String> decode(byte[] bytes, int offset, int length) {
    int end = offset + length;
    StringBuilder sb = new StringBuilder();

    while (offset < end) {
      if ((bytes[offset] & 0x80) == 0 && bytes[offset] != 0) {
        sb.append((char) (bytes[offset++] & 0x7F));
      } else if ((bytes[offset] & 0xE0) == 0xC0) {
        sb.append((char) ((bytes[offset++] & 0x1F) << 6 | (bytes[offset++] & 0x3F)));
      } else if ((bytes[offset] & 0xF0) == 0xE0) {
        sb.append((char) ((bytes[offset++] & 0x0F) << 12 | (bytes[offset++] & 0x3F) << 6 | (bytes[offset++] & 0x3F)));
      } else {
        return Optional.empty();
      }
    }

    return Optional.of(sb.toString());
  }

  public static byte[] encode(char c) {
    return encode(new char[]{c});
  }

  public static byte[] encode(String str) {
    return encode(str.toCharArray());
  }

  public static byte[] encode(char[] str) {
    int length = str.length;
    byte[] bytes = new byte[str.length * 3];
    int offset = 0;

    for (int i = 0; i < length; i++) {
      char c = str[i];
      if (c >= 0x0001 && c <= 0x007F) {
        bytes[offset++] = (byte) c;
      } else if (c <= 0x07FF) {
        bytes[offset++] = (byte) (0xC0 | (c >> 6 & 0x1F));
        bytes[offset++] = (byte) (0x80 | (c & 0x3F));
      } else {
        bytes[offset++] = (byte) (0xE0 | (c >> 12 & 0x0F));
        bytes[offset++] = (byte) (0x80 | (c >> 6 & 0x3F));
        bytes[offset++] = (byte) (0x80 | (c & 0x3F));
      }
    }

    byte[] result = new byte[offset];
    System.arraycopy(bytes, 0, result, 0, offset);
    return result;
  }
}
