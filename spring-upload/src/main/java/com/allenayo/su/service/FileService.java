package com.allenayo.su.service;

import com.allenayo.su.controller.FileController;
import com.allenayo.su.domain.Resource;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

@Service
public class FileService implements Observer {

    private static final Logger logger = Logger.getLogger(FileService.class);

    @Autowired
    private FileController fileController;

    @PostConstruct // 在Bean创建之后执行
    public void init() {
        fileController.addObserver(this);
    }

    @Override
    public void update(Observable o, Object arg) {
        if (!(o instanceof FileController)) return;

        List<Resource> files = (List<Resource>) arg;
        save(files);
    }

    public void save(List<Resource> resources) {
        for (Resource resource : resources) {
            System.out.println(resource);
        }
        logger.info("文件保存成功！");
    }
}