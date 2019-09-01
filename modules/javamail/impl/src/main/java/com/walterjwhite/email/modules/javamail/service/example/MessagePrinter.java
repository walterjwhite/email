// package com.walterjwhite.email.modules.javamail.service;
//
// import lombok.Getter;
// import lombok.RequiredArgsConstructor;
// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;
//
// import javax.mail.Folder;
// import javax.mail.MessagingException;
//
// the underlying store is the bottleneck, this doesn't help with performance
// @Getter
// @RequiredArgsConstructor
// public class MessagePrinter implements Runnable {
//    private static final Logger LOGGER = LoggerFactory.getLogger(MessagePrinter.class);
//
//    protected final Folder folder;
//    protected final int index;
//
//    @Override
//    public void run() {
//        if(index < 1)
//            return;
//
//        try {
//            folder.open(Folder.READ_ONLY);
//
//            LOGGER.info("read: " + folder.getMessage(index).getSubject());
//        } catch (MessagingException e) {
//            LOGGER.error("error fetching:", e);
//        } finally {
//            try {
//                folder.close();
//            } catch (MessagingException e) {
//                LOGGER.error("error closing folder", e);
//            }
//        }
//    }
// }
