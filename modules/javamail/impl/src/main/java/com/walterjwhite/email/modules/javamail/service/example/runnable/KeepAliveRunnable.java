package com.walterjwhite.email.modules.javamail.service.example.runnable;

import com.sun.mail.iap.ProtocolException;
import com.sun.mail.imap.IMAPFolder;
import com.sun.mail.imap.protocol.IMAPProtocol;
import javax.mail.MessagingException;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class KeepAliveRunnable implements Runnable {
  protected final long keepAliveTimeout;
  protected final IMAPFolder folder;

  @Override
  public void run() {
    while (!Thread.interrupted()) {
      try {
        Thread.sleep(keepAliveTimeout);

        // Perform a NOOP just to keep alive the connection
        //                LOGGER.debug("Performing a NOOP to keep alvie the connection");
        folder.doCommand(
            new IMAPFolder.ProtocolCommand() {
              public Object doCommand(IMAPProtocol p) throws ProtocolException {
                p.simpleCommand("NOOP", null);
                return null;
              }
            });
      } catch (InterruptedException e) {
        // Ignore, just aborting the thread...
      } catch (MessagingException e) {
        // Shouldn't really happen...
        //                LOGGER.warn("Unexpected exception while keeping alive the IDLE
        // connection", e);
      }
    }
  }
}
