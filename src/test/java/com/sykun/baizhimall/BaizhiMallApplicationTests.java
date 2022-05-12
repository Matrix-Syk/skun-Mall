package com.sykun.baizhimall;


import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.sykun.baizhimall.dao.ResourceDao;
import com.sykun.baizhimall.dao.StudentDao;
import com.sykun.baizhimall.entity.Student;
import com.sykun.baizhimall.service.ResourceService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@SpringBootTest
class BaizhiMallApplicationTests {
    @Resource
    private ResourceService resourceService;
    @Resource
    private ResourceDao resourceDao;
    @Resource
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Resource
    private StudentDao Dao;
    @Test
    void contextLoads() {
        // yourEndpoint填写Bucket所在地域对应的Endpoint。以华东1（杭州）为例，Endpoint填写为https://oss-cn-hangzhou.aliyuncs.com。
        String endpoint = ":";
        // 阿里云账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM用户进行API访问或日常运维，请登录RAM控制台创建RAM用户。
        String accessKeyId = "";
        String accessKeySecret = "";
        String bucketName = "";

        String path = "https:";
        String objectName = "2021-07-06/425c57394a1c45a5b2ba70f354792c13_林山简约风景4k壁纸_彼岸图网.jpg";
        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

// 删除文件或目录。如果要删除目录，目录必须为空。
        ossClient.deleteObject(bucketName, objectName);

// 关闭OSSClient。
        ossClient.shutdown();
    }

    @Test
    void test001() {
        Integer a = 10;
        for (int i = 0; i < 10; i++) {
            a=a--;
            System.out.println(a);
        }
    }
    String str = "bb1a2a3a4a5a";
    @Test
    void test02(){
        String[] str2 = str.split("a");
        for (int i = 0; i < str2.length; i++) {
            System.out.println(str2[i]);     
        }
    }
    @Test
    void test123(){
        Student three = new Student("1","___________");
        Student five = new Student("3","wangwu");
        Student four = new Student("4","wangwu");
        Student two = new Student("1","lisi");
        List<Student> list = new ArrayList<>();
        list.add(five);
        list.add(three);
        list.add(four);
        list.add(two);
        int i = Dao.insertOrUpdateBatch(list);
        System.out.println(i);
    }

    @Test
    void test1233(){
        AdminRoleEntity one = new AdminRoleEntity("20", "10", "100", 1, 2);
        AdminRoleEntity two = new AdminRoleEntity("20", "20", "100", 1, 2);
        AdminRoleEntity three = new AdminRoleEntity("20", "30", "100", 1, 2);
        AdminRoleEntity four = new AdminRoleEntity("20", "10", "100", 1, 2);
        AdminRoleEntity five = new AdminRoleEntity("20", "20", "100", 1, 2);
        List<AdminRoleEntity> newList = new ArrayList<>();
        List<AdminRoleEntity> oldList = new ArrayList<>();
        newList.add(one);
        newList.add(two);
        newList.add(three);
        oldList.add(four);
        oldList.add(five);
        Map<String, AdminRoleEntity> collect = newList.stream().collect(Collectors.toMap(AdminRoleEntity::getAdminId, Function.identity()));
        for (AdminRoleEntity adminRoleEntity : oldList) {
            if (collect.containsKey(adminRoleEntity.getAdminId())){
                System.out.println(adminRoleEntity);
            }
        }
    }
}
