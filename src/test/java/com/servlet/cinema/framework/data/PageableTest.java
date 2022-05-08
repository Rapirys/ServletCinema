package com.servlet.cinema.framework.data;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class PageableTest {
   @Test
   public void pageableTest(){
       Sort sortPattern;
       sortPattern = Sort.by("aaaaa");
       sortPattern.descending();
       sortPattern.and(Sort.by("bbbb"));
       assertEquals("ORDERBYaaaaaDESC,bbbb",sortPattern.toString().replace(" ",""));
       Pageable pageable = Pageable.of(3,20,sortPattern);
       assertEquals("ORDERBYaaaaaDESC,bbbbLIMIT20OFFSET60",pageable.toString().replace(" ",""));
   }
}