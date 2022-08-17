package com.lingsha.reggie.controller;

import com.lingsha.reggie.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.UUID;

/**
 * 文件上传下载
 * @auther Ling.Sha
 * @date 2022/8/7 - 17:20
 */
@RestController
@Slf4j
@RequestMapping("/common")
public class CommonController {

        @Value("${reggie.path}")
        private String basePath;
        /**
         * 文件上传
         * @param file
         * @return
         */
        @PostMapping("/upload")
        public R<String> upload(MultipartFile file)//这里的参数名字必须和form-data,name='file'一致
        {
            String originalFilename = file.getOriginalFilename();//原始文件名，不推荐
            String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
            //使用uuid，防止文件名字重复
            String fileName = UUID.randomUUID().toString()+suffix;

            //创建一个目录对象
            File dir = new File(basePath);
            if(!dir.exists())
            {
                //目录不存在，创建
                dir.mkdirs();
            }
            //file是临时文件，请求结束后file就会销毁 ,需要保存到其他位置
            log.info(file.toString());
            try {
                file.transferTo(new File(basePath+fileName));//名
            } catch (IOException e) {
                e.printStackTrace();
            }
            return R.success(fileName);
        }

    /**
     * 文件下载
     * @param name
     * @param response
     */
    @GetMapping("download")
        public void download(String name, HttpServletResponse response)
        {

            try {
                //输入流，读取文件内容
                FileInputStream fileInputStream = new FileInputStream(new File(basePath+name));

                //输出流,文件写回浏览器，在浏览器展示图片
                ServletOutputStream outputStream =response.getOutputStream();

                response.setContentType("image/jpeg");//固定的，表示是图片格式
                int len =0;
                byte[] bytes = new byte[1024];

                while ((len=fileInputStream.read(bytes))!=-1)
                {
                outputStream.write(bytes,0,len);
                outputStream.flush();
                }

                outputStream.close();
                fileInputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
}
