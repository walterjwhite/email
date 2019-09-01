package com.walterjwhite.email.modules.exchange.organization;

import com.walterjwhite.email.api.model.Email;
import com.walterjwhite.email.organization.api.EmailProvider;
import java.util.List;
import java.util.Spliterator;
import java.util.function.Consumer;
import lombok.RequiredArgsConstructor;
import microsoft.exchange.webservices.data.core.ExchangeService;
import microsoft.exchange.webservices.data.core.PropertySet;
import microsoft.exchange.webservices.data.core.enumeration.property.BasePropertySet;
import microsoft.exchange.webservices.data.core.enumeration.property.BodyType;
import microsoft.exchange.webservices.data.core.enumeration.property.WellKnownFolderName;
import microsoft.exchange.webservices.data.core.service.item.EmailMessage;
import microsoft.exchange.webservices.data.core.service.item.Item;
import microsoft.exchange.webservices.data.search.FindItemsResults;
import microsoft.exchange.webservices.data.search.ItemView;

@RequiredArgsConstructor
public class ExchangeEmailMessageStream implements EmailProvider {
  public static final int PAGE_SIZE = 10;
  protected final ExchangeService exchangeService;
  protected final ItemView itemView = new ItemView(PAGE_SIZE);

  protected transient int currentPageIndex = 0;
  protected transient FindItemsResults<Item> findItemsResults;
  protected transient List<Item> currentPageItems;

  protected boolean hasNext() {
    if (!wasInitialized()) reset();

    return !fetchedAllRecordsInCurrentPage() || findItemsResults.isMoreAvailable();
  }

  protected boolean wasInitialized() {
    return findItemsResults != null;
  }

  protected Email getAndIncrement() throws Exception {
    if (fetchedAllRecordsInCurrentPage()) {
      itemView.setOffset(itemView.getOffset() + PAGE_SIZE);
      fetchResultsPage();
    }

    if (findItemsResults != null
        && findItemsResults.getItems() != null
        && !findItemsResults.getItems().isEmpty())
      exchangeService.loadPropertiesForItems(findItemsResults, PropertySet.FirstClassProperties);

    return ExchangeEmailConverterUtil.build(
        (EmailMessage) findItemsResults.getItems().get(currentPageIndex++));
  }

  protected void fetchResultsPage() throws Exception {
    findItemsResults = exchangeService.findItems(WellKnownFolderName.Inbox, itemView);

    currentPageIndex = 0;
    currentPageItems = findItemsResults.getItems();
  }

  protected boolean fetchedAllRecordsInCurrentPage() {
    return currentPageIndex > (currentPageItems.size() - 1);
  }

  public void reset() {
    try {
      final PropertySet propertySet = new PropertySet(BasePropertySet.FirstClassProperties);

      // TODO: this appears to have no effect
      propertySet.setRequestedBodyType(BodyType.Text);
      itemView.setPropertySet(propertySet);

      itemView.setOffset(0);

      fetchResultsPage();
    } catch (Exception e) {
      throw new RuntimeException("Error during initialization", e);
    }
  }

  @Override
  public boolean tryAdvance(Consumer<? super Email> consumer) {
    if (!wasInitialized()) reset();

    try {
      if (hasNext()) {
        consumer.accept(getAndIncrement());
        return true;
      }

    } catch (Exception e) {
      throw new RuntimeException("Error fetching message", e);
    }

    return false;
  }

  @Override
  public Spliterator<Email> trySplit() {
    return null;
  }

  @Override
  public long estimateSize() {
    return 0;
  }

  @Override
  public int characteristics() {
    return 0;
  }
}
