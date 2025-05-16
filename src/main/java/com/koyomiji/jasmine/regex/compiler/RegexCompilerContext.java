package com.koyomiji.jasmine.regex.compiler;

import java.util.HashMap;
import java.util.Map;

public class RegexCompilerContext {
  public Map<Object, BindNode> bindMap = new HashMap<>();
  public boolean insideBound = false;
}
