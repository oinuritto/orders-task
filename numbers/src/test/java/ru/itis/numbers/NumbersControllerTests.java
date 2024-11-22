package ru.itis.numbers;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.itis.numbers.controller.NumbersController;
import ru.itis.numbers.service.NumbersGeneratorService;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(NumbersController.class)
public class NumbersControllerTests {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private NumbersGeneratorService numbersGeneratorService;

    @Test
    void getOrderById_ShouldReturn200() throws Exception {
        var number = "0000120241111";
        when(numbersGeneratorService.generateUniqueOrderNumber()).thenReturn(number);

        mockMvc.perform(get("/numbers"))
                .andExpect(status().isOk())
                .andExpect(content().string(number));

        verify(numbersGeneratorService).generateUniqueOrderNumber();
    }
}
