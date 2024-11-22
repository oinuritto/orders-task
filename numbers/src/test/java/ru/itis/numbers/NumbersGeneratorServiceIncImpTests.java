package ru.itis.numbers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import ru.itis.numbers.service.NumbersGeneratorServiceIncImpl;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class NumbersGeneratorServiceIncImpTests {
    @Mock
    private RedisTemplate<String, String> redisTemplate;
    @Mock
    private ValueOperations<String, String> valueOperations;
    @InjectMocks
    private NumbersGeneratorServiceIncImpl numbersGeneratorService;
    private static final String COUNTER_KEY = "orderNumberCounter";

    @BeforeEach
    void setUp() {
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
    }

    @Test
    void generateUniqueOrderNumber_ShouldGenerateCorrectOrderNumber() {
        var counterValue = 1L;
        when(redisTemplate.opsForValue().increment(COUNTER_KEY)).thenReturn(counterValue);
        String expectedOrderNumber = String.format("%05d%s", counterValue % 100000, LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")));

        String generatedOrderNumber = numbersGeneratorService.generateUniqueOrderNumber();

        assertNotNull(generatedOrderNumber);
        assertEquals(expectedOrderNumber, generatedOrderNumber);
        verify(redisTemplate.opsForValue()).increment(COUNTER_KEY);
    }

    @Test
    void generateUniqueOrderNumber_ShouldThrowException_WhenCounterIsNull() {
        when(redisTemplate.opsForValue().increment(COUNTER_KEY)).thenReturn(null);

        assertThrows(RuntimeException.class, () -> numbersGeneratorService.generateUniqueOrderNumber());
    }
}
