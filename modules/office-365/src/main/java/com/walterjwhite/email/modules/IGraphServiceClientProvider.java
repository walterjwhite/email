package com;

public class IGraphServiceClientProvider implements Provider<IGraphServiceClient> {
  protected final IAuthenticationProvider authenticationProvider;

  public IGraphServiceClient get() {
    return GraphServiceClient.builder()
        .authenticationProvider(authenticationProvider)
        .buildClient();
  }
}
