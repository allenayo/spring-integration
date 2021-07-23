package com.allenayo.sj.resource;

import com.allenayo.sj.domain.Resource;
import com.allenayo.sj.domain.ResultVO;
import com.allenayo.sj.util.DateUtil;
import com.allenayo.sj.util.FileUtil;
import com.allenayo.sj.util.RandomUtil;
import com.sun.jersey.multipart.BodyPart;
import com.sun.jersey.multipart.BodyPartEntity;
import com.sun.jersey.multipart.FormDataMultiPart;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

/**
 * 上传文件
 */
@Path("file")
@Component
public class FileResource extends Observable {

    private static final Logger logger = Logger.getLogger(FileResource.class);

    private static final String BASE_DIR = "/data/files"; // 上传文件存储的根路径

    private static final String MODULE = "default"; // 上传文件所属的模块

    private static final String FILE_ACCESS_URL_PREFIX = ""; // 上传文件访问路径前缀

    @Path("upload")
    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA) // 指定接收MIME格式
    @Produces("application/json;charset=UTF-8") // 指定返回MIME格式
    public ResultVO upload(FormDataMultiPart formDataMultiPart) {
        // 上传文件存储的目录
        String dirPath = BASE_DIR + File.separator + MODULE + File.separator + DateUtil.getSysDate(DateUtil.YEAR_MONTH_DAY);
        File dir = new File(dirPath);
        if (!dir.exists()) dir.mkdirs();

        List<String> fileUrls = new ArrayList<>(); // 上传文件访问路径集合
        List<Resource> resources = new ArrayList<>(); // 上传文件集合

        List<BodyPart> bodyParts = formDataMultiPart.getBodyParts();
        for (BodyPart bodyPart : bodyParts) {
            // 上传文件的原名称
            String originalName = bodyPart.getContentDisposition().getFileName();
            // 上传文件的格式
            String ext = FileUtil.getExt(originalName);
            // 上传文件的新名称
            String newName = RandomUtil.uuid() + "." + ext;
            // 上传文件存储在服务器的地址
            String path = dir + File.separator + newName;
            // 上传文件访问路径
            String url = FILE_ACCESS_URL_PREFIX + newName;
            // 上传文件的实体
            BodyPartEntity entity = (BodyPartEntity) bodyPart.getEntity();
            try {
                // 将上传文件存储在服务器中指定的位置
                FileUtil.transferTo(entity.getInputStream(), path);
                fileUrls.add(url);
                resources.add(new Resource(newName, ext, new File(path).length(), path, url, DateUtil.getSysDateTime(), "system"));
                logger.info("文件上传成功，文件存储在：" + path);
            } catch (IOException e) {
                e.printStackTrace();
                return new ResultVO("1", "文件上传失败，原因: " + e.getMessage());
            }
        }

        if (fileUrls.size() == 0) {
            return new ResultVO("1", "上传文件为空！");
        }

        // 状态更新，通知所有观察对象
        this.setChanged();
        this.notifyObservers(resources);

        return new ResultVO("0", "文件上传成功！", fileUrls);
    }

}