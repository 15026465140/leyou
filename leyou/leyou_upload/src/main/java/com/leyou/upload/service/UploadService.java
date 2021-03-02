package com.leyou.upload.service;

import com.github.tobato.fastdfs.domain.StorePath;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;


@Service
public class UploadService {
    //初始化一个集合常量，放入文件类型白名单
    private static final List<String> content_types= Arrays.asList("image/gif","image/jpeg","image/png");
    //配置日志工具
    private static final Logger logger= LoggerFactory.getLogger(UploadService.class);

    @Autowired
    private FastFileStorageClient storageClient;

    public String uploadImage(MultipartFile file) {
        //校验文件类型
        String originalFilename = file.getOriginalFilename();
        //获取file文件的媒体类型
        String contentType = file.getContentType();
        //{}占位符
        if (!content_types.contains(contentType)) {
            logger.info("文件类型不合法 {}",originalFilename);
            return null;
        }

        //校验文件内容
        try {
            BufferedImage bufferedImage = ImageIO.read(file.getInputStream());

            if (bufferedImage == null) {
                logger.info("文件内容不合法 {}",originalFilename);
            }

   /*         //保存到服务器
            file.transferTo(new File("D:\\project\\image\\"+originalFilename));*/

            String ext = StringUtils.substringAfterLast(originalFilename, ".");
            StorePath storePath = this.storageClient.uploadFile(file.getInputStream(), file.getSize(), ext, null);

            //返回URL，进行回显
            return "http://image.leyou.com/"+ storePath.getFullPath();
        } catch (IOException e) {
            e.printStackTrace();
            logger.info("服务器内部错误");
        }
        return null;
    }
}
