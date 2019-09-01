package com.walterjwhite.data.pipe.modules.email.javamail.utils;

import com.walterjwhite.file.api.service.FileEntityOutputStream;
import com.walterjwhite.file.api.service.FileStorageService;
import java.io.IOException;
import java.io.InputStream;
import javax.mail.BodyPart;
import javax.mail.MessagingException;
import javax.mail.Part;
import org.apache.commons.io.IOUtils;

public class AttachmentUtils {
  private AttachmentUtils() {}

  public static boolean isAttachment(BodyPart bodyPart) throws MessagingException {
    return Part.ATTACHMENT.equalsIgnoreCase(bodyPart.getDisposition())
        && (bodyPart.getFileName() != null && !bodyPart.getFileName().isEmpty());
  }

  // FolderClosedIOException
  // AutoCreate a temporary placeholder to a "file" and let the repository manage persistence and
  // merging later
  public static com.walterjwhite.file.api.model.File getAttachment(
      FileStorageService fileStorageService, BodyPart bodyPart)
      throws IOException, MessagingException {
    try (final InputStream is = bodyPart.getInputStream()) {
      try (final FileEntityOutputStream feos = new FileEntityOutputStream(fileStorageService)) {
        IOUtils.copy(is, feos);
        feos.flush();

        com.walterjwhite.file.api.model.File file = feos.getFile();
        file.withFilename(bodyPart.getFileName());

        return file;
      }
    }
  }
}
