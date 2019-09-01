package com.walterjwhite.email.modules.exchange.organization;

public class FolderNotFoundException extends RuntimeException {
  public FolderNotFoundException(final String parentPath, final String folderName) {
    super(parentPath + "/" + folderName + " was not found");
  }
}
