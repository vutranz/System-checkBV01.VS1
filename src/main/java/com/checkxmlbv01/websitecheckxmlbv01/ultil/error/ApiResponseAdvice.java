package com.checkxmlbv01.websitecheckxmlbv01.ultil.error;

import org.springframework.http.MediaType;
import org.springframework.core.MethodParameter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;
import com.checkxmlbv01.websitecheckxmlbv01.domain.DTO.RestResponse;
import com.checkxmlbv01.websitecheckxmlbv01.ultil.annotation.ApiMessage;

@RestControllerAdvice
public class ApiResponseAdvice implements ResponseBodyAdvice<Object> {

    @Override
    public boolean supports(
            MethodParameter returnType,
            Class<? extends HttpMessageConverter<?>> converterType) {

        return returnType.hasMethodAnnotation(ApiMessage.class);
    }

    @Override
    public Object beforeBodyWrite(
            Object body,
            MethodParameter returnType,
            MediaType contentType,
            Class<? extends HttpMessageConverter<?>> converterType,
            ServerHttpRequest request,
            ServerHttpResponse response) {

        if (body instanceof RestResponse<?>) {
            return body;
        }

        ApiMessage apiMessage =
                returnType.getMethodAnnotation(ApiMessage.class);

        RestResponse<Object> res = new RestResponse<>();
        res.setSuccess(true);
        res.setMessage(apiMessage.value());
        res.setData(body);

        return res;
    }
}





