package ru.itis.numbers.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class NumbersGeneratorServiceIncImpl implements NumbersGeneratorService {
    private final RedisTemplate<String, String> redisTemplate;
    private static final String COUNTER_KEY = "orderNumberCounter";

    @Override
    public String generateUniqueOrderNumber() {
        String currentDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));

        Long counter = redisTemplate.opsForValue().increment(COUNTER_KEY);

        if (counter == null) {
            throw new RuntimeException("Failed to generate order number");
        }

        return String.format("%05d%s", counter % 100000, currentDate);
    }
}
