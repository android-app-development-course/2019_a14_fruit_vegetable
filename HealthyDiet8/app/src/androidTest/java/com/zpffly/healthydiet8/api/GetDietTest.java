package com.zpffly.healthydiet8.api;

import org.junit.Test;

import static org.junit.Assert.*;

public class GetDietTest {

    @Test
    public void request() {
        System.out.println(GetDiet.request("苹果"));
    }
}