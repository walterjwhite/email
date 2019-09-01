package com.walterjwhite.email.organization.api.configuration.rule;

import java.util.Arrays;
import java.util.regex.Pattern;

public enum MatchType {
  Equals {
    protected boolean doesMatch(final String value, final String input) {
      return input.equals(value);
    }
  },
  EqualsIgnoreCase {
    protected boolean doesMatch(final String value, final String input) {
      return input.equalsIgnoreCase(value);
    }
  },
  Contains {
    protected boolean doesMatch(final String value, final String input) {
      return input.indexOf(value) >= 0;
    }
  },
  ContainsIgnoreCase {
    protected boolean doesMatch(final String value, final String input) {
      return input.toLowerCase().indexOf(value.toLowerCase()) >= 0;
    }
  },
  Regex {
    protected boolean doesMatch(final String value, final String input) {
      final Pattern pattern = Pattern.compile(value);
      return pattern.matcher(input).matches();
    }
  },
  LessThan {
    protected boolean doesMatch(final String value, final String input) {
      return input.compareTo(value) < 0;
    }
  },
  GreaterThan {
    protected boolean doesMatch(final String value, final String input) {
      return input.compareTo(value) > 0;
    }
  };

  public boolean matches(final String value, final Object input) {
    if (value == null) {
      throw new IllegalArgumentException("Value is null.");
    }
    if (input == null) {
      throw new IllegalArgumentException("Input is null.");
    }

    if (Object[].class.equals(input.getClass())) {
      if (((Object[]) input).length == 0) return false;

      return Arrays.stream(((Object[]) input)).filter(i -> doesMatch(value, (String) i)).count()
          > 0;
    }

    // TODO: leave this as an object, is compareTo included?
    return doesMatch(value, (String) input);
  }

  protected abstract boolean doesMatch(final String value, final String input);
}
