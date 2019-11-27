package com.zpffly.healthydiet8.api;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;

import static org.junit.Assert.*;

public class IngredientTest {

    @Test
    public void Test() throws IOException {
        FileInputStream inputStream = new FileInputStream("2.jpg");

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] temp = new byte[1024];
        int n = 0;
        while ((n = inputStream.read(temp, 0, 1024)) != -1){
            out.write(temp, 0, n);
        }
        byte[] byteArray = out.toByteArray();

        String ingredient = Ingredient.ingredient(byteArray);
        System.out.println(ingredient);
    }
}
