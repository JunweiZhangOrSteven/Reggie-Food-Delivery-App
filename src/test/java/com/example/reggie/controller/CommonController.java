package com.example.reggie.controller;

import com.example.reggie.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.UUID;

/**
 * File Upload and Download
 */
@RestController
@RequestMapping("/common")
@Slf4j
public class CommonController {

    @Value("${reggie.path}")
    private String basePath;

    /**
     * File Upload
     * @param file
     * @return
     */
    @PostMapping("/upload")
    public R<String> upload(MultipartFile file){
        //file is a temporary file that needs to be dumped to a specified location,
        //otherwise the temporary file will be deleted after the completion of this request
        log.info(file.toString());

        //Original filename
        String originalFilename = file.getOriginalFilename();//abc.jpg
        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));

        //Use UUID to regenerate file names to prevent file overwriting due to duplicate file names
        String fileName = UUID.randomUUID().toString() + suffix;//dfsdfdfd.jpg

        //Creating a Catalog Object
        File dir = new File(basePath);
        //Determine if the current directory exists
        if(!dir.exists()){
            //directory does not exist and needs to be created
            dir.mkdirs();
        }

        try {
            //Transfer temporary files to a specified location
            file.transferTo(new File(basePath + fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return R.success(fileName);
    }

    /**
     * File Download
     * @param name
     * @param response
     */
    @GetMapping("/download")
    public void download(String name, HttpServletResponse response){

        try {
            //Input stream, read the content of the file through the input stream
            FileInputStream fileInputStream = new FileInputStream(new File(basePath + name));

            //Output streams, through the output stream to write the file back to the browser
            ServletOutputStream outputStream = response.getOutputStream();

            response.setContentType("image/jpeg");

            int len = 0;
            byte[] bytes = new byte[1024];
            while ((len = fileInputStream.read(bytes)) != -1){
                outputStream.write(bytes,0,len);
                outputStream.flush();
            }

            //Close output Stream
            outputStream.close();
            fileInputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
