package com.sykun.baizhimall.controller;

import java.time.LocalDateTime;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sykun.baizhimall.entity.AdminEntity;
import com.sykun.baizhimall.entity.FileEntity;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.PutObjectResult;
import com.sykun.baizhimall.service.FileService;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author sykun
 */
@RestController
@RequestMapping("file")
public class FileUploadController {
    @Resource
    private FileService fileService;
    // yourEndpoint填写Bucket所在地域对应的Endpoint。以华东1（杭州）为例，Endpoint填写为https://oss-cn-hangzhou.aliyuncs.com。
    private final String endpoint = "https://beijing.aliyuncs.com";
    // 阿里云账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM用户进行API访问或日常运维，请登录RAM控制台创建RAM用户。
    private final String accessKeyId = "LTAI5tRk";
    private final String accessKeySecret = "2dfXF7CfTlzFb";
    private final String bucketName = "sykun-mall";

    /**
     * 上传文件到阿里OSS
     *
     * @param file 前端上传的文件
     * @return
     */
    @PostMapping("oss")
    public Map<String, Object> fileUpload(MultipartFile file, HttpSession session) {
        Map<String, Object> resultMap = new HashMap<>();
        String path = "https://sykun-mall.oss-cn-beijing.aliyuncs.com";
        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        AdminEntity user = (AdminEntity) session.getAttribute("user");
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String format = simpleDateFormat.format(date);
        String newFileName = UUID.randomUUID().toString().replaceAll("-", "") + "_" + file.getOriginalFilename();
        String eTag = null;
        try {
            // 填写Bucket名称和Object完整路径。Object完整路径中不能包含Bucket名称。
            PutObjectResult putObjectResult = ossClient.putObject(bucketName, format + "/" + newFileName, file.getInputStream());
            eTag = putObjectResult.getETag();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (eTag != null) {
            FileEntity fileEntity = new FileEntity();
            fileEntity.setFileUrl(path + "/" + format + "/" + newFileName);
            fileEntity.setFileCreateTime(date);
            fileEntity.setFileCreateUser(user.getUsername());
            fileEntity.setFileDirectory(format);
            fileEntity.setFileOldName(file.getOriginalFilename());
            fileEntity.setFileNewName(newFileName);

            boolean save = fileService.save(fileEntity);
            if (save) {
                resultMap.put("code", 200);
                resultMap.put("msg", "success");
                resultMap.put("data", "上传成功");
            } else {
                resultMap.put("code", 300);
                resultMap.put("msg", "error");
                resultMap.put("data", "上传失败请重试");
                ossClient.deleteObject(bucketName, format + "/" + newFileName);
            }
        } else {
            resultMap.put("code", 300);
            resultMap.put("msg", "error");
            resultMap.put("data", "上传失败请重试");
        }
        // 关闭OSSClient。
        ossClient.shutdown();
        return resultMap;
    }

    @GetMapping("/{pageNum}/{pageSize}")
    public Map<String, Object> getImages(@PathVariable("pageNum") Integer pageNum, @PathVariable("pageSize") Integer pageSize) {
        HashMap<String, Object> map = new HashMap<>();
        Page<FileEntity> fileEntityPage = new Page<>(pageNum, pageSize);
        IPage<FileEntity> page = fileService.page(fileEntityPage);
        map.put("code", 200);
        map.put("data", page);
        return map;
    }

    @DeleteMapping("del")
    public Map<String, Object> delImages(@RequestBody Long[] ids) {
        // 创建OSSClient实例。
        HashMap<String, Object> map = new HashMap<>();
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        Collection<FileEntity> fileEntities = fileService.listByIds(Arrays.asList(ids));
        boolean b = fileService.removeByIds(Arrays.asList(ids));
        for (FileEntity fileEntity : fileEntities) {
            ossClient.deleteObject(bucketName, fileEntity.getFileDirectory() + "/" + fileEntity.getFileNewName());
        }
        ossClient.shutdown();
        if (b) {
            map.put("code", 200);
            map.put("msg", "删除成功");
        } else {
            map.put("code", 300);
            map.put("msg", "删除失败");
        }
        return map;
    }
    // 前台上传文件时将OSS认证信息回传
    @GetMapping("/webOss")
    public Map<String,Object> getWebOss(){
        HashMap<String, Object> map = new HashMap<>();
        map.put("region","oss-cn-beijing");
        map.put("accessKeyId",accessKeyId);
        map.put("accessKeySecret",accessKeySecret);
        map.put("bucketName",bucketName);
        map.put("dir",new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        map.put("newFileName",UUID.randomUUID().toString().replaceAll("-", ""));
        map.put("code",200);
        return map;
    }
}
