package com.koyomiji.asmine.sexpr.reader;

import org.objectweb.asm.tree.LabelNode;

import java.util.HashMap;
import java.util.Map;

public class LabelMap {
  private final Map<Long, LabelNode> offsets = new HashMap<>();
  private final Map<String, LabelNode> symbols = new HashMap<>();

  private final Map<Long, LabelNode> unresolvedOffsets = new HashMap<>();
  private final Map<String, LabelNode> unresolvedSymbols = new HashMap<>();

  public LabelMap() {
  }

  public LabelNode getOffsetLabel(long offset) {
    if (offsets.containsKey(offset)) {
      return offsets.get(offset);
    }

    if (!unresolvedOffsets.containsKey(offset)) {
      unresolvedOffsets.put(offset, new LabelNode());
    }

    return unresolvedOffsets.get(offset);
  }

  public LabelNode getSymbolLabel(String symbol) {
    if (symbols.containsKey(symbol)) {
      return symbols.get(symbol);
    }

    if (!unresolvedSymbols.containsKey(symbol)) {
      unresolvedSymbols.put(symbol, new LabelNode());
    }

    return unresolvedSymbols.get(symbol);
  }

  public LabelNode getUnresolvedOffsetLabel(long offset) {
    return unresolvedOffsets.get(offset);
  }

  public LabelNode getUnresolvedSymbolLabel(String symbol) {
    return unresolvedSymbols.get(symbol);
  }

  public void resolveOffsetLabel(long offset) {
    if (unresolvedOffsets.containsKey(offset)) {
      LabelNode labelNode = unresolvedOffsets.get(offset);
      offsets.put(offset, labelNode);
      unresolvedOffsets.remove(offset);
    }
  }

  public void resolveSymbolLabel(String symbol) {
    if (unresolvedSymbols.containsKey(symbol)) {
      LabelNode labelNode = unresolvedSymbols.get(symbol);
      symbols.put(symbol, labelNode);
      unresolvedSymbols.remove(symbol);
    } else {
      symbols.put(symbol, new LabelNode());
    }
  }
}
