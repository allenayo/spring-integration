package com.allenayo.sj.util;

import com.allenayo.sj.domain.Book;
import com.allenayo.sj.domain.Chapter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
public class JsonUtilTest {

    @Test
    public void testToJson() throws Exception {
        List<Chapter> chapters = new ArrayList<>(Arrays.asList(
                new Chapter(1L, "chapter1", "chapter1", 8),
                new Chapter(2L, "chapter2", "chapter2", 8)));
        System.out.println(JsonUtil.toJson(chapters));
    }

    @Test
    public void testToBean() throws Exception {
        String jsonString = "{\n" +
                "  \"id\" : 1,\n" +
                "  \"name\" : \"book1\",\n" +
                "  \"author\" : \"allenAyo\",\n" +
                "  \"lang\" : \"eng\",\n" +
                "  \"price\" : 10.5,\n" +
                "  \"totalPages\" : 50,\n" +
                "  \"chapters\" : [ {\n" +
                "    \"id\" : 1,\n" +
                "    \"title\" : \"chapter1\",\n" +
                "    \"content\" : \"chapter1\",\n" +
                "    \"words\" : 8\n" +
                "  }, {\n" +
                "    \"id\" : 2,\n" +
                "    \"title\" : \"chpater2\",\n" +
                "    \"content\" : \"chapter2\",\n" +
                "    \"words\" : 8\n" +
                "  } ]\n" +
                "}";
        JsonUtil.toBean(jsonString, Book.class);
    }
}
