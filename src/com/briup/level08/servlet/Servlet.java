package com.briup.level08.servlet;

import com.briup.level08.request.Request;
import com.briup.level08.response.Response;

import java.io.IOException;

public interface Servlet {
    public void servletHandler(Response response, Request request) throws IOException;
}