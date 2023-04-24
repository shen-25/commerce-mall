package com.zengshen.mvc.advice;

import com.zengshen.common.ApiRestResponse;
import com.zengshen.common.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author word
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionAdvice {

    @ExceptionHandler(BusinessException.class)
    public ApiRestResponse handlerBusinessException(BusinessException e) {
        log.error("自定义业务异常: " + e);
        return ApiRestResponse.errorException(e.getBusinessExceptionEnum());

    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
       public ApiRestResponse handlerMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        BindingResult result = e.getBindingResult();
        Map<String, String> errors = getErrors(result);
        return ApiRestResponse.errorMap(errors);
    }

    @ExceptionHandler(Exception.class)
    public ApiRestResponse handlerException(Exception e) {
        return ApiRestResponse.errorMsg(e.getMessage());
    }

    private Map<String, String> getErrors(BindingResult result) {
        Map<String, String> map = new HashMap<>();
        List<FieldError> errorList = result.getFieldErrors();
        for (FieldError fieldError : errorList) {
            // 错误对应的属性字段
            String field = fieldError.getField();
            // 错误的信息
            String defaultMessage = fieldError.getDefaultMessage();
            map.put(field, defaultMessage);
        }
        return map;
    }


}
