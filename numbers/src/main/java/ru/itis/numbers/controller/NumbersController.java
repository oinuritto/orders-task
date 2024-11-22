package ru.itis.numbers.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.itis.numbers.controller.api.NumbersApi;
import ru.itis.numbers.service.NumbersGeneratorService;

@RequiredArgsConstructor
@RestController
public class NumbersController implements NumbersApi {
    private final NumbersGeneratorService numbersGeneratorService;

    @Override
    public ResponseEntity<String> getOrderNumbers() {
        return ResponseEntity.ok(numbersGeneratorService.generateUniqueOrderNumber());
    }
}
