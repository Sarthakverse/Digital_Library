package com.example.digital_library.controller;

import com.example.digital_library.entity.Author;
import com.example.digital_library.payload.request.author.CreateAuthorRequest;
import com.example.digital_library.service.RedisService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/redis")
@RequiredArgsConstructor
public class RedisController {

    private final RedisService redisService;
    @PostMapping
    public void addNewAuthor(@RequestBody @Valid Author author)
    {
        redisService.addNewAuthor(author);
    }

    @GetMapping("/{id}")
    public Author getAuthorByName(@PathVariable("id") Integer id)
    {
        return redisService.getAuthorById(id);
    }

    @PostMapping("/List")
    public void addNewDataToList(@RequestBody @Valid Author author)
    {
        redisService.addNewDataToList(author);
    }

    @GetMapping("/getList/{start}/{end}")
    public List<Author> getDataFromList(@PathVariable("start") int start, @PathVariable("end") int end)
    {
        return redisService.getDataFromListUsingRange(start, end);
    }

}
