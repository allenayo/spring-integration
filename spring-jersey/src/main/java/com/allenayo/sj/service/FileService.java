package com.allenayo.sj.service;

import com.allenayo.sj.domain.Resource;
import com.allenayo.sj.resource.FileResource;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Optional;

@Service
public class FileService implements Observer {

    private static final Logger logger = Logger.getLogger(FileService.class);

    @Autowired
    private FileResource fileController;

    @PostConstruct // 在Bean创建之后执行
    public void init() {
        fileController.addObserver(this);
    }

    @Override
    public void update(Observable o, Object arg) {
        if (!(o instanceof FileResource)) return;

        List<Resource> files = (List<Resource>) arg;
        save(files);
    }

    public void save(List<Resource> resources) {
        Optional.of(resources).ifPresent(System.out::println);
        logger.info("文件保存成功！");
    }
}