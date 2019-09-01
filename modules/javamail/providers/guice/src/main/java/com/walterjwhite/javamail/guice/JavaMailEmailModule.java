package com.walterjwhite.javamail.guice;

import com.google.inject.AbstractModule;
import com.walterjwhite.email.api.service.ExternalEmailSendService;
import com.walterjwhite.email.modules.javamail.organization.JavaMailEmailMessageMarkAsRead;
import com.walterjwhite.email.modules.javamail.organization.JavaMailEmailMessageMover;
import com.walterjwhite.email.modules.javamail.provider.SessionProvider;
import com.walterjwhite.email.modules.javamail.provider.StoreProvider;
import com.walterjwhite.email.modules.javamail.service.JavaMailEmailMessageReadService;
import com.walterjwhite.email.modules.javamail.service.JavaMailEmailSendService;
import com.walterjwhite.email.organization.plugins.reply.MarkAsReadAction;
import com.walterjwhite.email.organization.plugins.reply.MoveAction;
import javax.mail.Session;
import javax.mail.Store;

public class JavaMailEmailModule extends AbstractModule {
  @Override
  protected void configure() {
    bind(Session.class).toProvider(SessionProvider.class);
    bind(Store.class).toProvider(StoreProvider.class);

    bind(JavaMailEmailMessageReadService.class);

    bind(ExternalEmailSendService.class).to(JavaMailEmailSendService.class);
    // bind(EmailReadService.class).to(JavaMailFolderEmailSupplier.class);

    // bind(PrivateEmailAccount.class).toProvider(PrivateEmailAccountProvider.class);

    bind(MoveAction.class).to(JavaMailEmailMessageMover.class);
    bind(MarkAsReadAction.class).to(JavaMailEmailMessageMarkAsRead.class);
  }
}
