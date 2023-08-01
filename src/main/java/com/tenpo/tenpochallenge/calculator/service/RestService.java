package com.tenpo.tenpochallenge.calculator.service;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.springframework.stereotype.Service;

@Service
public class RestService {

    public HttpResponse<String> get(String url) throws UnirestException {
        return Unirest.get(url).asString();
    }
}
