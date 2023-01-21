package ru.yandex.practicum.filmorate.handlers;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ru.yandex.practicum.filmorate.exceptions.OnExceptionResponse;
import ru.yandex.practicum.filmorate.exceptions.UpdateFilmOrUserWithIncorrectIdException;
import ru.yandex.practicum.filmorate.exceptions.UserOrFilmAlreadyExistException;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
public class CustomExceptionsHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", new Date());
        body.put("status", status.value());
        //Получаем все ошибки
        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(x -> x.getDefaultMessage())
                .collect(Collectors.toList());

        body.put("errors", errors);

        return new ResponseEntity<>(body, headers, status);
    }

    @ExceptionHandler(UserOrFilmAlreadyExistException.class)
    public ResponseEntity<OnExceptionResponse> handleAlreadyExistEntities(UserOrFilmAlreadyExistException exception) {
        OnExceptionResponse response = new OnExceptionResponse(exception.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UpdateFilmOrUserWithIncorrectIdException.class)
    public ResponseEntity<OnExceptionResponse> handleIncorrectId(UpdateFilmOrUserWithIncorrectIdException exception) {
        OnExceptionResponse response = new OnExceptionResponse(exception.getMessage());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

}
