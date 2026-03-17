package com.kodlabs.doktorumyanimda.api.filter;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.ext.Provider;

@Provider
public class CrossFilter implements ContainerResponseFilter {
    @Override
    public void filter(ContainerRequestContext containerRequestContext, ContainerResponseContext containerResponseContext){
        containerResponseContext.getHeaders().putSingle("Access-Control-Allow-Origin", "*");
        containerResponseContext.getHeaders().putSingle("Access-Control-Allow-Methods", "OPTIONS, GET, POST, PUT, DELETE");
        containerResponseContext.getHeaders().putSingle("Access-Control-Allow-Headers", "Content-Type");
    }
}
