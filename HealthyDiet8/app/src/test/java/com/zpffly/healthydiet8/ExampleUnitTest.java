package com.zpffly.healthydiet8;

import com.zpffly.healthydiet8.api.Ingredient;
import com.zpffly.healthydiet8.utils.AuthService;

import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void authTest(){
        System.out.println(AuthService.getAuth());
    }

    @Test
    public void Test() throws IOException {
        FileInputStream inputStream = new FileInputStream("/home/zpffly/Desktop/2.jpg");

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