package com.changing.springbatch.processor;

import com.changing.springbatch.model.Trade;

import java.util.Collections;

import org.springframework.batch.item.ItemProcessor;

/**
 * @author chenjun
 * @version V1.0
 * @since 2020-11-25 16:16
 */
public class TradeProcessor implements ItemProcessor<Trade, Trade> {

    @Override
    public Trade process(Trade trade) {
        Trade result = new Trade();

        result.setCustomer(trade.getCustomer());
        result.setIsin(trade.getIsin());
        result.setPrice(trade.getPrice());
        result.setQuantity(trade.getQuantity());
        result.setPriceCustomer(Collections.singletonList(trade.getPrice() + "---" + trade.getCustomer()));

        return result;
    }

}