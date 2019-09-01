package com.walterjwhite.email.modules.javamail.service.example;

import com.sun.mail.imap.IMAPFolder;
import com.walterjwhite.email.modules.javamail.service.example.runnable.IdleRunnable;
import java.util.HashMap;
import java.util.Map;

public class Listener {

  protected final Map<IMAPFolder, Thread> folderThreadMap = new HashMap<>();

  //      if(IMAPFolder.class.isInstance(folder)) {
  //        LOGGER.warn("an imap folder - listening:", folder);
  //        startListening((IMAPFolder) folder);
  //      } else{
  //        LOGGER.warn("not an imap folder:", folder);
  //      }

  public void start(IMAPFolder imapFolder) {
    if (folderThreadMap.containsKey(imapFolder)) return;

    final Thread thread =
        new Thread(new IdleRunnable(imapFolder), "Idle:" + imapFolder.getFullName());
    thread.start();
    folderThreadMap.put(imapFolder, thread);
  }

  public void stop(IMAPFolder imapFolder) {
    final Thread thread = folderThreadMap.get(imapFolder);
    if (thread != null) {
      if (thread.isAlive()) {
        thread.interrupt();
      }

      folderThreadMap.remove(imapFolder);
    }
  }
}
