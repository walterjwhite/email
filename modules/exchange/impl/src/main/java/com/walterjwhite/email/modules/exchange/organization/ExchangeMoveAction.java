package com.walterjwhite.email.modules.exchange.organization;

import com.walterjwhite.email.api.model.Email;
import com.walterjwhite.email.api.model.PrivateEmailAccount;
import com.walterjwhite.email.organization.api.configuration.rule.EmailMatcherRule;
import com.walterjwhite.email.organization.plugins.reply.MoveAction;
import lombok.RequiredArgsConstructor;
import microsoft.exchange.webservices.data.core.ExchangeService;
import microsoft.exchange.webservices.data.core.service.folder.Folder;
import microsoft.exchange.webservices.data.property.complex.FolderId;
import microsoft.exchange.webservices.data.property.complex.ItemId;

@RequiredArgsConstructor
public class ExchangeMoveAction implements MoveAction {
  protected final ExchangeEmailDirectoryService exchangeEmailDirectoryService;
  protected final ExchangeService exchangeService;

  public void execute(
      final String folderName,
      final PrivateEmailAccount privateEmailAccount,
      final EmailMatcherRule emailMatcherRule,
      final Email email) {
    //  public void execute(final PrivateEmailAccount privateEmailAccount, final Email email, final
    // String path, final String folderId) {
    try {
      final ItemId itemId = new ItemId(email.getMessageId());

      final String targetFolderPath = getTargetFolderId(emailMatcherRule);
      mkdir(targetFolderPath);
      final FolderId targetFolderId = new FolderId(targetFolderPath);
      exchangeService.moveItem(itemId, targetFolderId);
    } catch (Exception e) {
      throw new RuntimeException("Error moving email", e);
    }
  }

  protected String mkdir(final String path) {
    if (path == null || "/".equals(path)) return null;

    Folder parent = null;
    for (final String pathElement : path.split("/")) {
      try {
        parent = exchangeEmailDirectoryService.getParent(parent, pathElement);

      } catch (FolderNotFoundException folderNotFoundException) {
        try {
          parent = mkdir(parent, pathElement);
        } catch (Exception makeFolderException) {
          throw new RuntimeException("Error making folder", makeFolderException);
        }
      } catch (Exception e) {
        throw new RuntimeException("Error getting parent", e);
      }
    }

    return parent.getId().getUniqueId();
  }

  protected Folder mkdir(final Folder parentFolder, final String name) throws Exception {
    final Folder newFolder = new Folder(exchangeService);
    newFolder.setDisplayName(name);

    final FolderId parentFolderId =
        ExchangeEmailDirectoryService.getFolderId(exchangeService, parentFolder);
    exchangeService.createFolder(newFolder, parentFolderId);
    return newFolder;
  }
}
