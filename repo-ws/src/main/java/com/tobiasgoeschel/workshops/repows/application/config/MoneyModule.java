package com.tobiasgoeschel.workshops.repows.application.config;

import com.fasterxml.jackson.databind.module.SimpleModule;
import com.tobiasgoeschel.workshops.repows.domain.Order;
import org.joda.money.Money;

public class MoneyModule extends SimpleModule {
  public MoneyModule() {
    super("MoneyModule");
    this.addSerializer( Money.class, new MoneySerializer() );
    this.addDeserializer( Money.class, new Order.MoneyDeserializer() );
  }
}
