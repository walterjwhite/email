// TODO: move this to index plugin
// package com.walterjwhite.email.organization;
//
//
// import com.walterjwhite.email.api.model.Email;
// import com.walterjwhite.email.organization.api.Action;
// import com.walterjwhite.email.organization.api.configuration.rule.EmailMatcherRule;
//
// public class IndexAction implements Action {
//    protected final EmailMessageMover emailMessageMover;
//    protected final EmailMessageFolderMake emailMessageFolderMake;
//
//    @Override
//    public void execute(EmailMatcherRule emailMatcherRule, final Email email){
//        final ElasticSearchConfiguration elasticSearchConfiguration = null;
//
//        new IndexRecordCommand<>(elasticSearchConfiguration, Email.class, email).execute();
//    }
// }
