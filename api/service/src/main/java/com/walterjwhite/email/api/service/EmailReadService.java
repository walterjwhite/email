package com.walterjwhite.email.api.service;

import com.walterjwhite.email.api.model.Email;
import java.util.Spliterator;

@Deprecated
public interface EmailReadService extends Spliterator<Email> {
  //  /**
  //   * Read all messages in all folders, recursively
  //   *
  //   * @param privateEmailAccount
  //   * @throws IOException
  //   */
  //  void read(PrivateEmailAccount privateEmailAccount) throws IOException;
  //
  //  void read(
  //      PrivateEmailAccount privateEmailAccount, final String folderName, final boolean recursive)
  //      throws IOException;
}
