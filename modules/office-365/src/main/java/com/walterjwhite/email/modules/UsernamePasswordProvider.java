package com;

public class UsernamePasswordProvider implements Provider<UsernamePasswordProvider> {
  public static final String CLIENT_ID = "";
  public static final String USERNAME = "";
  public static final String PASSWORD = "";
  public static final List<String> SCOPES = null;

  public UsernamePasswordProvider get() {
    return new UsernamePasswordProvider(CLIENT_ID, SCOPES, USERNAME, PASSWORD);
  }
}
