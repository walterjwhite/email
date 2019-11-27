package com.walterjwhite.email.organization.plugins.craigslist;

import com.walterjwhite.email.api.model.Email;
import com.walterjwhite.email.api.model.PrivateEmailAccount;
import com.walterjwhite.email.organization.api.Action;
import com.walterjwhite.email.organization.api.configuration.rule.EmailMatcherRule;
import com.walterjwhite.property.impl.annotation.Property;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import javax.inject.Inject;
import lombok.Data;
import lombok.ToString;
import org.apache.commons.io.IOUtils;

@Data
@ToString(doNotUseGetters = true)
// @RequiredArgsConstructor(onConstructor_ = {@Inject})
public class CraigslistLinkClickerAction implements Action {
  protected final String craigslistLinkPath;
  protected final String craigslistLinkPattern;

  @Inject
  public CraigslistLinkClickerAction(
      @Property(CraigslistLinkWritePath.class) final String craigslistLinkPath,
      @Property(CraigslistLinkPattern.class) final String craigslistLinkPattern) {
    this.craigslistLinkPath = craigslistLinkPath;
    this.craigslistLinkPattern = craigslistLinkPattern;
  }

  @Override
  public void execute(
      final String folderName,
      final PrivateEmailAccount privateEmailAccount,
      EmailMatcherRule emailMatcherRule,
      Email email) {
    // migrate from Java to GO
    writeLink(getLinkAddress(email));
  }

  protected String getLinkAddress(final Email email) {
    // get link in email body (after being matched by a rule)
    String link =
        Arrays.stream(email.getBody().split("\n"))
            .filter(line -> line != null && line.contains(craigslistLinkPattern))
            .findFirst()
            .get();

    link = link.replace("<a href=\"", "");
    int index = link.indexOf("\"");

    if (index > 0) {
      return link.substring(0, index);
    }

    return link;
  }

  protected void writeLink(final String link) {
    final File targetFile = new File(getPath(link));

    targetFile.getParentFile().mkdirs();
    try (final FileWriter fileWriter = new FileWriter(targetFile)) {
      IOUtils.write(link, fileWriter);
    } catch (IOException e) {
      throw new RuntimeException("Error writing file: " + targetFile, e);
    }
  }

  protected String getPath(final String link) {
    return craigslistLinkPath + File.separator + getLinkId(link);
  }

  protected int getLinkId(final String link) {
    return link.replace(craigslistLinkPattern, "").hashCode();
  }
}
