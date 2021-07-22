package com.allenayo.su.controller;

import com.allenayo.su.domain.Resource;
import com.allenayo.su.domain.ResultVO;
import com.allenayo.su.util.DateUtil;
import com.allenayo.su.util.FileUtil;
import com.allenayo.su.util.RandomUtil;
import org.apache.log4j.Logger;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Observable;

/**
 * 上传文件
 */
@RequestMapping("file")
@Controller
public class FileController extends Observable {

    private static final Logger logger = Logger.getLogger(FileController.class);

    private static final String BASE_DIR = "/data/files"; // 上传文件存储的根路径

    private static final String MODULE = "default"; // 上传文件所属的模块

    private static final String FILE_ACCESS_URL_PREFIX = ""; // 上传文件访问路径前缀

    @RequestMapping(
            value = "upload",
            method = RequestMethod.POST,
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE, // 指定接收MIME格式
            produces = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8" // 指定返回MIME格式
    )
    @ResponseBody
    public ResultVO upload(HttpServletRequest request) {
        // 上传文件存储的目录
        String dirPath = BASE_DIR + File.separator + MODULE + File.separator + DateUtil.getSysDate(DateUtil.YEAR_MONTH_DAY);
        File dir = new File(dirPath);
        if (!dir.exists()) dir.mkdirs();

        List<String> fileUrls = new ArrayList<>(); // 上传文件访问路径集合
        List<Resource> resources = new ArrayList<>(); // 上传文件集合

        // 创建一个通用的多部分解析器
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
        // 判断request是否有文件上传，即是否有多部分请求
        if (!multipartResolver.isMultipart(request)) {
            return new ResultVO(0, "请求错误！");
        }

        // 转换成多部分请求
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        // 取得request中所有的文件名
        Iterator<String> iter = multipartRequest.getFileNames();
        while (iter.hasNext()) {
            // 取得上传文件
            MultipartFile file = multipartRequest.getFile(iter.next());
            if (file == null) continue;

            // 上传文件的原名称
            String originalName = file.getOriginalFilename();
            // 上传文件的格式
            String ext = FileUtil.getExt(originalName);
            // 上传文件的新名称
            String newName = RandomUtil.uuid() + "." + ext;
            // 上传文件存储在服务器的地址
            String path = dir + File.separator + newName;
            // 上传文件访问路径
            String url = FILE_ACCESS_URL_PREFIX + newName;
            try {
                // 将上传文件存储在服务器中指定的位置
                file.transferTo(new File(path));
            } catch (IOException e) {
                e.printStackTrace();
                return new ResultVO(1, "文件上传失败，原因: " + e.getMessage());
            }
            fileUrls.add(url);
            resources.add(new Resource(newName, ext, file.getSize(), path, url, DateUtil.getSysDateTime(), "system"));
            logger.info("文件上传成功，文件存储在：" + path);
        }

        if (fileUrls.size() == 0) {
            return new ResultVO(0, "请求错误！");
        }

        // 状态更新，通知所有观察对象
        this.setChanged();
        this.notifyObservers(resources);

        return new ResultVO(0, "文件上传成功！", fileUrls);
    }
}