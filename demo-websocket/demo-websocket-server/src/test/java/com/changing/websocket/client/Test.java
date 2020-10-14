package com.changing.websocket.client;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author chenjun
 * @version V1.0
 * @since 2020-09-28 15:30
 */
public class Test {

    public static void main(String[] args) {
        Date date = new Date(1601362803000L);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSS");
        System.out.println(simpleDateFormat.format(date));
    }
}