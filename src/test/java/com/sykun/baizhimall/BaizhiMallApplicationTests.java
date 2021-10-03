package com.sykun.baizhimall;


import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.sykun.baizhimall.dao.ResourceDao;
import com.sykun.baizhimall.service.ResourceService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.annotation.Resource;
import java.math.BigInteger;
import java.util.Scanner;

@SpringBootTest
class BaizhiMallApplicationTests {
    @Resource
    private ResourceService resourceService;
    @Resource
    private ResourceDao resourceDao;
    @Resource
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Test
    void contextLoads() {
        // yourEndpoint填写Bucket所在地域对应的Endpoint。以华东1（杭州）为例，Endpoint填写为https://oss-cn-hangzhou.aliyuncs.com。
        String endpoint = "https://oss-cn-beijing.aliyuncs.com";
        // 阿里云账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM用户进行API访问或日常运维，请登录RAM控制台创建RAM用户。
        String accessKeyId = "LTAI5tRkRRdAQjiXDD7Jjq42";
        String accessKeySecret = "2dfXF7CfTlzF60QiVHUtR89gML8YYb";
        String bucketName = "sykun-mall";

        String path = "https://sykun-mall.oss-cn-beijing.aliyuncs.com";
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

    public static void main(String[] args) {
        Scanner scanner=new Scanner(System.in);
        String str1="";
        String str2="";
        System.out.println("请输入第一个大数");
        str1=scanner.next();
        System.out.println("请输入第二个大数");
        str2=scanner.next();
        BigInteger n1=new BigInteger(str1),
                n2=new BigInteger(str2),
                result=null;
        result=n1.add(n2);
        System.out.println("和:"+result.toString());
    }
}
