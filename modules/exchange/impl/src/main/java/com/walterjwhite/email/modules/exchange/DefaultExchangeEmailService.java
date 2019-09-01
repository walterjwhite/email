package com.walterjwhite.email.modules.exchange;

import com.walterjwhite.datastore.api.model.entity.Tag;
import com.walterjwhite.email.api.model.Email;
import com.walterjwhite.encryption.service.DigestService;
import com.walterjwhite.exchange.AbstractExchangeService;
import com.walterjwhite.exchange.ExchangeUtils;
import com.walterjwhite.file.api.model.File;
import com.walterjwhite.file.api.service.FileStorageService;
import javax.inject.Inject;
import microsoft.exchange.webservices.data.core.ExchangeService;
import microsoft.exchange.webservices.data.core.PropertySet;
import microsoft.exchange.webservices.data.core.enumeration.property.BasePropertySet;
import microsoft.exchange.webservices.data.core.enumeration.property.WellKnownFolderName;
import microsoft.exchange.webservices.data.core.service.folder.Folder;
import microsoft.exchange.webservices.data.core.service.item.EmailMessage;
import microsoft.exchange.webservices.data.core.service.item.Item;
import microsoft.exchange.webservices.data.core.service.schema.EmailMessageSchema;
import microsoft.exchange.webservices.data.property.complex.FolderId;
import microsoft.exchange.webservices.data.property.complex.MessageBody;
import microsoft.exchange.webservices.data.search.FindFoldersResults;
import microsoft.exchange.webservices.data.search.FindItemsResults;
import microsoft.exchange.webservices.data.search.FolderView;
import microsoft.exchange.webservices.data.search.ItemView;

public class DefaultExchangeEmailService
    extends AbstractExchangeService /*implements EmailService*/ {
  protected final DigestService digestService;

  @Inject
  public DefaultExchangeEmailService(
      ExchangeService exchangeService,
      FileStorageService fileStorageService,
      DigestService digestService) {
    super(exchangeService, fileStorageService);
    this.digestService = digestService;
  }

  //  @Override
  public void send(Email email) throws Exception {
    EmailMessage msg = new EmailMessage(exchangeService);
    msg.setSubject(email.getSubject());
    msg.setBody(MessageBody.getMessageBodyFromText(email.getBody()));

    for (File file : email.getFiles()) {
      //      msg.getAttachments()
      //          .addFileAttachment(file.getId() + "." + file.getExtension(),
      // fileStorageService.read(file));
    }

    //    for (Person person : email.getToRecipients()) {
    //      //      msg.getToRecipients().add(person.getEmailAddress());
    //    }
    //
    //    for (Person person : email.getCcRecipients()) {
    //      //      msg.getCcRecipients().add(person.getEmailAddress());
    //    }
    //
    //    for (Person person : email.getBccRecipients()) {
    //      //      msg.getBccRecipients().add(person.getEmailAddress());
    //    }

    msg.send();
  }

  /**
   * intake items from the email box and convert them into our database
   *
   * @throws Exception
   */
  //  @Override
  public void read() throws Exception {
    final ItemView view = new ItemView(50);
    read(new FolderId(WellKnownFolderName.Inbox), view, null);
  }

  protected void readItemsInFolder(FolderId folderId, ItemView view, Tag parent, Tag label)
      throws Exception {
    FindItemsResults<Item> findResults;
    int i = 0;
    do {
      findResults = exchangeService.findItems(folderId, view);

      // LOGGER.info("Folder Name:" + folderId.getDisplayName());
      // FindFoldersResults findFoldersResults = exchangeService.findItems(folderId, ItemView);

      if (findResults != null && findResults.getItems().size() > 0) {
        exchangeService.loadPropertiesForItems(
            findResults,
            new PropertySet(BasePropertySet.FirstClassProperties, EmailMessageSchema.Attachments));
      }

      // email.getTags().add(label);

      for (Item item : findResults.getItems()) {
        processItem(item);
      }

      view.setOffset(view.getOffset() + 50);
    } while (findResults.isMoreAvailable());
  }

  protected void processItem(Item item) throws Exception {
    getAttachments(item);

    // item.delete(DeleteMode.MoveToDeletedItems);
  }

  protected void iterateThroughFolders(FolderId folderId, ItemView view, Tag label)
      throws Exception {
    FindFoldersResults findFoldersResults =
        exchangeService.findFolders(folderId, new FolderView(Integer.MAX_VALUE));

    for (Folder folder : findFoldersResults.getFolders()) {
      view.setOffset(0);
      read(folder.getId(), view, label);
    }
  }

  protected void read(FolderId folderId, ItemView view, Tag parent) throws Exception {
    Tag label = ExchangeUtils.getLabel(folderId, parent);
    readItemsInFolder(folderId, view, parent, label);
    iterateThroughFolders(folderId, view, label);
  }
}
