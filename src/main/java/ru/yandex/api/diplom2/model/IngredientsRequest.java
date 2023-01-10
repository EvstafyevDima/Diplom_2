package ru.yandex.api.diplom2.model;

import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
public class IngredientsRequest {

    List<String> ingredients;
}
