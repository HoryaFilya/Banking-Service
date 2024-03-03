package ru.shaikhraziev.bankingservice.error;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import java.util.List;

@Getter
@AllArgsConstructor
@Builder
public class ApiError {

    private String date;

    private String message;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<String> errors;
}