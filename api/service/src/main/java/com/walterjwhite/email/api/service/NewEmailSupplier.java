package com.walterjwhite.email.api.service;

import com.walterjwhite.email.api.model.Email;
import java.util.function.Supplier;

public interface NewEmailSupplier extends Supplier<Email> {}
