package com.example.digital_library.service;

import com.example.digital_library.entity.Author;
import com.example.digital_library.payload.request.author.CreateAuthorRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RedisService {

    private final RedisTemplate redisTemplate;
    private final String AUTHOR_KEY = "AUTHOR :: ";

    public void addNewAuthor(Author author) {
        redisTemplate.opsForValue().set(AUTHOR_KEY+author.getAuthorId(), author  /*, Duration.ofSeconds(30) */  );
    }

    public Author getAuthorById(Integer id) {
        return (Author) redisTemplate.opsForValue().get(AUTHOR_KEY+id);
    }

    public void addNewDataToList(Author author)
    {
        redisTemplate.opsForList().leftPush(AUTHOR_KEY+"_List", author );
    }

    public List<Author> getDataFromListUsingRange(int start, int end)
    {
       return  redisTemplate.opsForList().range(AUTHOR_KEY+"_List", start, end);
    }
}
