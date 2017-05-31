package com.wrh.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Author：wojiushimogui
 * @Description:
 * @Date:Created by 上午10:34 on 2017/5/30.
 */
@Controller
public class UpLoadController {
    /**
     * 上传图片，保存在相应的目录下。
     * */
    @RequestMapping(value = "/upload.do" ,method = RequestMethod.POST)
    public String upload(@RequestParam MultipartFile[] myfiles, HttpServletRequest request) throws IOException {

        for(MultipartFile file : myfiles){
            //此处MultipartFile[]表明是多文件,如果是单文件MultipartFile就行了
            if(file.isEmpty()){
                System.out.println("文件未上传!");
            }
            else{
                //得到上传的文件名
                String fileName = file.getOriginalFilename();
                //得到服务器项目发布运行所在地址
                String path1 = request.getSession().getServletContext().getRealPath("WEB-INF/views")+ File.separator;
                //  此处未使用UUID来生成唯一标识,用日期做为标识
                String path = path1+ new SimpleDateFormat("yyyyMMddHHmmss").format(new Date())+ fileName;
                //打印文件上传路径，方便查看是否上传成功
                System.out.println(path);
                //把文件上传至path的路径
                File localFile = new File(path);
                file.transferTo(localFile);
            }
        }
        return "uploadSuccess";
    }
    /**
     * 下载指定图片
     * */
    @RequestMapping("/download.do")
    public String download(String fileName, HttpServletRequest request,
                           HttpServletResponse response) {
        response.setCharacterEncoding("utf-8");
        response.setContentType("multipart/form-data");
        response.setHeader("Content-Disposition", "attachment;fileName="
                + fileName);
        try {
            String path = request.getSession().getServletContext().getRealPath
                    ("WEB-INF/views")+File.separator;
            InputStream inputStream = new FileInputStream(new File(path
                    + fileName));

            OutputStream os = response.getOutputStream();
            byte[] b = new byte[2048];
            int length;
            while ((length = inputStream.read(b)) > 0) {
                os.write(b, 0, length);
            }

            // 这里主要关闭。
            os.close();

            inputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //  返回值要注意，要不然就出现下面这句错误！
        //java+getOutputStream() has already been called for this response
        return null;
    }
}
