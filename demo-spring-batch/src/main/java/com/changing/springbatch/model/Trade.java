package com.changing.springbatch.model;

import java.math.BigDecimal;
import java.util.List;

import lombok.Data;

/**
 * @author chenjun
 * @version V1.0
 * @since 2020-11-18 09:41
 */
@Data
public class Trade {

    private BigDecimal price;
    private String isin;
    private String customer;
    private Long quantity;
    private List<String> priceCustomer;

}