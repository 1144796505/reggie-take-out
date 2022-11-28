package com.reggie;

import org.junit.jupiter.api.Test;

/**
 * @author 侯文柯
 * @version 1.0
 */
public class FileTest {
    @Test
    public void test1(){
    String fileName = "reggie.jpg";
        String suffix = fileName.substring(fileName.lastIndexOf("."));
        System.out.println(suffix);
    }
}
