package com.walterjwhite.email.modules.javamail.service.example.runnable;

import com.sun.mail.imap.IMAPFolder;
import javax.mail.MessagingException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class IdleRunnable implements Runnable {
  private static final long KEEP_ALIVE_FREQ = 300000; // 5 minutes

  protected final IMAPFolder imapFolder;

  public void run() {
    // We need to create a new thread to keep alive the connection
    final Thread t =
        new Thread(new KeepAliveRunnable(KEEP_ALIVE_FREQ, imapFolder), "IdleConnectionKeepAlive");

    try {
      t.start();

      while (!Thread.interrupted()) {
        // LOGGER.debug("Starting IDLE");
        try {
          imapFolder.idle();
        } catch (MessagingException e) {
          // LOGGER.warn("Messaging exception during IDLE", e);
          throw new RuntimeException(e);
        }
      }
    } finally {
      // Shutdown keep alive thread
      if (t.isAlive()) {
        t.interrupt();
      }
    }
  }
}
