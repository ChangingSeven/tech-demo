package com.changing.redis.model.bo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class RefreshCacheBO {

    /**
     * 字典类型
     */
    private String keyType;
    /**
     * 字典-键
     */
    private String keyName;
    /**
     * 字典-值
     */
    private String keyValue;

}