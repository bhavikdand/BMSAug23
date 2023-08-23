package com.example.BMSAug23.dtos;

import lombok.Data;

@Data
public class Response {
    private ResponseStatus responseStatus;
    private String message;

    public static Response getSuccessResponse(String message){
        Response response = new Response();
        response.setResponseStatus(ResponseStatus.SUCCESS);
        response.setMessage(message);
        return response;
    }

    public static Response getFailureResponse(String message){
        Response response = new Response();
        response.setResponseStatus(ResponseStatus.FAILURE);
        response.setMessage(message);
        return response;
    }
}
