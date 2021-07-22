package com.allenayo.sj.controller;

import com.allenayo.sj.domain.Book;
import com.allenayo.sj.domain.Chapter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RequestMapping("book")
@Controller
public class BookController {

    @ResponseBody
    @RequestMapping(value = "getBook", method = RequestMethod.GET)
    public Book getUser() {
        List<Chapter> chapters = new ArrayList<>(Arrays.asList(
                new Chapter(1L, "chapter1", "chapter1", 8),
                new Chapter(2L, "chapter2", "chapter2", 8)));
        return new Book(1L, "book1", "allenAyo", "eng", 10.50, 50, chapters);
    }

    @ResponseBody
    @RequestMapping(value = "postBook", method = RequestMethod.POST)
    public Book postUser(@RequestBody Book book) {
        Optional.of(book).flatMap(b -> Optional.of(b.getChapters())).ifPresent(System.out::println);
        return book;
    }
}