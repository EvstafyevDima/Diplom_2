package ru.yandex.api.diplom2;

import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
public class IngredientsRequest {

    List<String> ingredients;
}
