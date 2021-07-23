package com.allenayo.sj.util;

import com.allenayo.sj.domain.ResultVO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
public class JsonUtilTest {

    @Test
    public void testToJson() throws Exception {
        ResultVO resultVO = new ResultVO("0", "success", "Hello World!");
        System.out.println(JsonUtil.toJson(resultVO));
    }

    @Test
    public void testToBean() throws Exception {
        String jsonString = "{\"code\":\"0\",\"msg\":\"success\",\"data\":\"Hello World!\"}";
        JsonUtil.toBean(jsonString, ResultVO.class);
    }
}