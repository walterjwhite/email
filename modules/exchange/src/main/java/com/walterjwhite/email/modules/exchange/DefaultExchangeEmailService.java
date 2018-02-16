package com.walterjwhite.email.modules.exchange;

import com.walterjwhite.datastore.api.model.entity.Tag;
import com.walterjwhite.email.api.model.Email;
import com.walterjwhite.encryption.api.service.DigestService;
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
import microsoft.exchange.webservices.data.property.complex.*;
import microsoft.exchange.webservices.data.search.FindFoldersResults;
import microsoft.exchange.webservices.data.search.FindItemsResults;
import microsoft.exchange.webservices.data.search.FolderView;
import microsoft.exchange.webservices.data.search.ItemView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultExchangeEmailService /*implements ExchangeEmailService*/ {
  private static final Logger LOGGER = LoggerFactory.getLogger(DefaultExchangeEmailService.class);

  protected final ExchangeService exchangeService;

  protected final FileStorageService fileStorageService;
  protected final DigestService digestService;

  @Inject
  public DefaultExchangeEmailService(
      ExchangeService exchangeService,
      FileStorageService fileStorageService,
      DigestService digestService) {
    super();
    this.exchangeService = exchangeService;
    this.fileStorageService = fileStorageService;
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
    LOGGER.info("reading:" + folderId.getFolderName() + ":" + folderId.getUniqueId());

    FindItemsResults<Item> findResults;
    int i = 0;
    do {
      findResults = exchangeService.findItems(folderId, view);
      LOGGER.info("found:" + findResults.getItems().size() + ":" + findResults.getTotalCount());

      // LOGGER.info("Folder Name:" + folderId.getDisplayName());
      // FindFoldersResults findFoldersResults = exchangeService.findItems(folderId, ItemView);

      if (findResults != null && findResults.getItems().size() > 0) {
        exchangeService.loadPropertiesForItems(
            findResults,
            new PropertySet(BasePropertySet.FirstClassProperties, EmailMessageSchema.Attachments));
      }

      // email.getTags().add(label);

      for (Item item : findResults.getItems()) {
        // Do something with the item.
        LOGGER.info("read item:(" + i++ + ")" + item.getSubject());
        //                LOGGER.info("read item:" + item.getCulture());
        //                LOGGER.info("read item:" + item.getItemClass());
        //                LOGGER.info("read item:" + item.getConversationId());
        //                LOGGER.info("read item:" + item.getInReplyTo());

        getAttachments(item);

        // item.delete(DeleteMode.MoveToDeletedItems);
      }

      view.setOffset(view.getOffset() + 50);
    } while (findResults.isMoreAvailable());
  }

  protected void getAttachments(Item item) throws Exception {

    /*
    item.getDateTimeSent()
            created
                    updated
    */
    if (item.getHasAttachments()) {
      LOGGER.info("has attachments");

      // item.getAllowedResponseActions();

      final AttachmentCollection attachments = item.getAttachments();
      // LOGGER.info("attachments:" + attachments.getCount());

      for (Attachment attachment : attachments.getItems()) {
        getAttachment(attachment);
      }
    }
  }

  protected void getAttachment(Attachment attachment) throws Exception {
    // attachments.
    if (attachment instanceof FileAttachment) {
      attachment.load();

      try {
        FileAttachment fileAttachment = (FileAttachment) attachment;
        if (fileAttachment.getSize() > 65) {
          // String filename = attachment.hashCode() + "." + fileAttachment.getName();
          // TODO: inject a property here
          // final File f = new File("" + filename);
          // fileAttachment.load(f.getAbsolutePath());

          final byte[] data = fileAttachment.getContent();
          final String hash = digestService.compute(data);

          final int index = fileAttachment.getName().lastIndexOf(".");
          final String extension;
          if (index >= 0) {
            extension = fileAttachment.getName().substring(index + 1);
          } else {
            extension = "";
          }

          final File file = new File(hash, extension);

          //          fileStorageService.write(file, fileAttachment.getContent());

          // LOGGER.info("wrote contents (" + fileAttachment.getSize() + ") to:" +
          // f.getAbsolutePath());
        } else {
          LOGGER.warn("file size is 0, aborting download.");
        }
      } catch (ClassCastException e) {
        LOGGER.error("Unable to cast item as file:", e);
      }
    }
  }

  protected void iterateThroughFolders(FolderId folderId, ItemView view, Tag label)
      throws Exception {
    // go through the folders
    FindFoldersResults findFoldersResults =
        exchangeService.findFolders(folderId, new FolderView(Integer.MAX_VALUE));

    for (Folder folder : findFoldersResults.getFolders()) {
      // LOGGER.info("Count======" + folder.getChildFolderCount());
      LOGGER.info("Folder Name:" + folder.getDisplayName());

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
