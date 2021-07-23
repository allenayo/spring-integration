package com.allenayo.sj.config;

import com.allenayo.sj.filter.LogFilter;
import com.sun.jersey.api.model.AbstractMethod;
import com.sun.jersey.spi.container.ContainerRequestFilter;
import com.sun.jersey.spi.container.ContainerResponseFilter;
import com.sun.jersey.spi.container.ResourceFilter;
import com.sun.jersey.spi.container.ResourceFilterFactory;

import java.util.Arrays;
import java.util.List;

public class JerseyResourceFilterFactory implements ResourceFilterFactory {

    @Override
    public List<ResourceFilter> create(AbstractMethod abstractMethod) {
        return Arrays.asList(this.createLogFilter(abstractMethod));
    }

    private ResourceFilter createLogFilter(final AbstractMethod abstractMethod) {
        return new ResourceFilter() {
            @Override
            public ContainerRequestFilter getRequestFilter() {
                return new LogFilter(abstractMethod);
            }

            @Override
            public ContainerResponseFilter getResponseFilter() {
                return null;
            }
        };
    }
}