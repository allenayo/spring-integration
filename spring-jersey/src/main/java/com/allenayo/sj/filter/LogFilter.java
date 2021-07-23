package com.allenayo.sj.filter;


import com.allenayo.sj.annotation.Log;
import com.allenayo.sj.util.JsonUtil;
import com.sun.jersey.api.model.AbstractMethod;
import com.sun.jersey.spi.container.ContainerRequest;
import com.sun.jersey.spi.container.ContainerRequestFilter;
import org.apache.log4j.Logger;

import java.io.ByteArrayInputStream;
import java.util.Optional;

public class LogFilter implements ContainerRequestFilter {

    private static final org.apache.log4j.Logger logger = Logger.getLogger(LogFilter.class);

    private final AbstractMethod method;

    public LogFilter(AbstractMethod method) {
        this.method = method;
    }

    @Override
    public ContainerRequest filter(ContainerRequest request) {
        Log logAnn = method.getAnnotation(Log.class);
        if (logAnn == null) return request;

        Optional.of(method.getMethod().getParameterTypes()).ifPresent(classes -> {
            Class<?> aClass = classes[0];
            Object entity = request.getEntity(aClass);
            logger.info(entity);
            try {
                request.setEntityInputStream(new ByteArrayInputStream(JsonUtil.toJson(entity).getBytes()));
            } catch (Exception e) {
            }
        });
        return request;
    }
}