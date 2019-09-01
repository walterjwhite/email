package com.walterjwhite.email.modules.exchange.organization;

import java.util.Iterator;
import java.util.List;
import lombok.RequiredArgsConstructor;
import microsoft.exchange.webservices.data.core.ExchangeService;
import microsoft.exchange.webservices.data.core.PropertySet;
import microsoft.exchange.webservices.data.core.service.folder.Folder;
import microsoft.exchange.webservices.data.property.complex.FolderId;
import microsoft.exchange.webservices.data.search.FindFoldersResults;
import microsoft.exchange.webservices.data.search.FolderView;

@RequiredArgsConstructor
public class ExchangeEmailFolderStream implements Iterator<Folder> {
  protected final ExchangeService exchangeService;
  protected final FolderId parentFolderId;

  protected final FolderView folderView = new FolderView(ExchangeEmailMessageStream.PAGE_SIZE);
  protected transient FindFoldersResults findFoldersResults;
  protected transient List<Folder> currentPageItems;
  protected transient int currentPageIndex = 0;
  protected transient int currentIndex = 0;

  @Override
  public boolean hasNext() {
    if (!wasInitialized()) reset();

    return !fetchedAllFoldersInCurrentPage() || hasMoreTotalResults();
  }

  protected boolean wasInitialized() {
    return findFoldersResults != null;
  }

  public void reset() {
    try {
      fetchResultsPage();
    } catch (Exception e) {
      throw new RuntimeException("Error during initialization", e);
    }
  }

  protected boolean fetchedAllFoldersInCurrentPage() {
    return currentPageIndex > (currentPageItems.size() - 1);
  }

  protected boolean hasMoreTotalResults() {
    return currentIndex < findFoldersResults.getTotalCount();
  }

  @Override
  public Folder next() {
    if (!wasInitialized()) {
      reset();
    }

    try {
      return getAndIncrement();
    } catch (Exception e) {
      throw new RuntimeException("Error fetching message", e);
    }
  }

  protected Folder getAndIncrement() throws Exception {
    if (fetchedAllFoldersInCurrentPage()) {
      folderView.setOffset(folderView.getOffset() + ExchangeEmailMessageStream.PAGE_SIZE);
      fetchResultsPage();
    }

    final Folder folder = currentPageItems.get(currentPageIndex++);
    currentIndex++;
    exchangeService.loadPropertiesForFolder(folder, PropertySet.FirstClassProperties);

    return folder;
  }

  protected void fetchResultsPage() throws Exception {
    findFoldersResults = exchangeService.findFolders(parentFolderId, folderView);

    currentPageIndex = 0;
    currentPageItems = findFoldersResults.getFolders();
  }
}
