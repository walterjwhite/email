package com.walterjwhite.data.pipe.modules.email.javamail.utils;

import com.walterjwhite.datastore.api.model.entity.Tag;
import com.walterjwhite.email.api.model.Email;
import java.util.HashSet;
import java.util.Set;
import javax.mail.Folder;

public class TagUtils {
  private TagUtils() {}

  public static Set<Tag> get(Folder folder, Email email) {
    final Set<Tag> tags = new HashSet<>();

    if (folder != null) {
      for (final String tagName : folder.getFullName().split("/")) {
        tags.add(new Tag(tagName));
      }
    }

    return tags;
  }
}
