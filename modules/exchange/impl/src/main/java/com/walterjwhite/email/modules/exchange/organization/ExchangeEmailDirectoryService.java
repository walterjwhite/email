package com.walterjwhite.email.modules.exchange.organization;

import lombok.RequiredArgsConstructor;
import microsoft.exchange.webservices.data.core.ExchangeService;
import microsoft.exchange.webservices.data.core.enumeration.property.WellKnownFolderName;
import microsoft.exchange.webservices.data.core.service.folder.Folder;
import microsoft.exchange.webservices.data.property.complex.FolderId;

@RequiredArgsConstructor
public class ExchangeEmailDirectoryService {
  protected final ExchangeService exchangeService;

  public Folder getParent(final Folder parent, final String pathElement) throws Exception {
    final ExchangeEmailFolderStream exchangeEmailFolderStream =
        new ExchangeEmailFolderStream(exchangeService, getFolderId(exchangeService, parent));

    while (exchangeEmailFolderStream.hasNext()) {
      Folder folder = exchangeEmailFolderStream.next();

      if (pathElement.equals(folder.getDisplayName())) {
        return folder;
      }
    }

    if (parent == null) {
      throw new FolderNotFoundException(WellKnownFolderName.Inbox.name(), pathElement);
    }

    throw new FolderNotFoundException(parent.getDisplayName(), pathElement);
  }

  public static FolderId getFolderId(final ExchangeService exchangeService, final Folder parent)
      throws Exception {
    if (parent != null) return parent.getId();

    return Folder.bind(exchangeService, WellKnownFolderName.Inbox).getId();
  }
}
