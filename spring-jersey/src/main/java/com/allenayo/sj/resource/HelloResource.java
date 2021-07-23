package com.allenayo.sj.resource;

import com.allenayo.sj.annotation.Log;
import com.allenayo.sj.domain.Resource;
import com.allenayo.sj.domain.ResultVO;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("hello")
@Component
public class HelloResource {

    private static final Logger logger = Logger.getLogger(HelloResource.class);

    @GET
    @Produces(MediaType.TEXT_PLAIN) // 指定返回MIME格式
    public String hello() {
        logger.info("Hello World!");
        return "Hello World!";
    }

    @GET
    @Path("getJson")
    @Produces(MediaType.APPLICATION_JSON)
    public ResultVO getJson() {
        return new ResultVO("0", "success", "Hello World!");
    }

    @Log
    @POST
    @Path("postResource")
    @Produces(MediaType.APPLICATION_JSON)
    public ResultVO postResource(Resource resource) {
        System.out.println(resource);
        return new ResultVO("0", "success");
    }

}