package com.sykun.baizhimall;

import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CodeGenerator {
    private static final String SERVICE = "service_path";
    private static final String SERVICE_IMPL = "service_impl_path";
    // dao
    private static final String MAPPER = "mapper_path";
    // dao 的xml文件
    private static final String XML = "xml_path";
    // controller 控制器
    private static final String CONTROLLER = "controller_path";
    private static List<String> delKey;
    static {
        /**
         * 不需要生成的功能类
         */
        //delKey = Arrays.asList(CONTROLLER,XML);
    }
    public static void main(String[] args) {
        // 代码生成器
        BzAutoGenerator mpg = new BzAutoGenerator();
        // 数据源配置
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setUrl("jdbc:mysql://localhost:3306/baizhi?characterEncoding=utf-8&serverTimezone=GMT%2B8&userSSL=false");
        // dsc.setSchemaName("public");
        dsc.setDriverName("com.mysql.cj.jdbc.Driver");
        dsc.setUsername("root");
        dsc.setPassword("root");
        mpg.setDataSource(dsc);

        // 全局配置
        GlobalConfig gc = new GlobalConfig();
        String projectPath = System.getProperty("user.dir");
        String path=projectPath+"/src/main";
        gc.setOutputDir(path+"/java");
        gc.setAuthor("sykun");
        gc.setOpen(false);//是否在代码生成后自动打开目录
        gc.setEntityName("%sEntity");
        gc.setServiceName("%sService");
        gc.setMapperName("%sDao");
        gc.setControllerName("%sController");
        mpg.setGlobalConfig(gc);
        // 包配置
        PackageConfig pc = new PackageConfig();
        System.out.println("请输入根目录: ");
        Scanner packageScanner = new Scanner(System.in);
        String packageName = packageScanner.next();
        pc.setParent(packageName);
        pc.setMapper("dao");
        pc.setEntity("entity");
        pc.setService("service");
        pc.setController("controller");

        mpg.setPackageInfo(pc);

        // 生成策略配置
        StrategyConfig sc = new StrategyConfig();
        System.out.println("请输入表名称: ");
        Scanner scanner = new Scanner(System.in);
        String tableName = scanner.next();
        sc.setInclude(tableName);//哪些表进行自动生成，如果不指定，默认库中所有表
        sc.setEntityLombokModel(true);
        sc.setRestControllerStyle(true);
        sc.setControllerMappingHyphenStyle(true);
        sc.setNaming(NamingStrategy.underline_to_camel);//数据库表映射到实体的命名策略
        sc.setColumnNaming(NamingStrategy.underline_to_camel);//数据库表字段映射到实体的命名策略
        sc.setEntityTableFieldAnnotationEnable(true);
        System.out.println("命名规则中是否携带表前缀: 0 是 1 否");
        Scanner tablePrefix = new Scanner(System.in);
        int i = tablePrefix.nextInt();
        if (i == 1) {
            sc.setTablePrefix(tableName.split("_")[0] + "_"); //设置表的前缀，比如t_person的前缀是t_
        }
        mpg.setStrategy(sc);
        // 初始化自動生成配置
        mpg.init();
        if (!CollectionUtils.isEmpty(delKey)) {
            for (String s : delKey) {
                mpg.getConfig().getPathInfo().remove(s);
            }
        }
        System.out.println("请输入mapper 文件的存放目录，注意格式 com/sykun/baizhimall/mapper ");
        Scanner mapperScanner = new Scanner(System.in);
        String mapperPath = mapperScanner.next();
        InjectionConfig cfg = getInjectionConfig(path+"/resources/"+mapperPath +"/");
        mpg.setCfg(cfg);
        mpg.execute();//开始生成

        scanner.close();
        mapperScanner.close();
        packageScanner.close();
        tablePrefix.close();
    }

    private static InjectionConfig getInjectionConfig (String outputFilePath) {
        // 自定义配置
        InjectionConfig cfg = new InjectionConfig() {
            @Override
            public void initMap() {
                // to do nothing
            }
        };
        // 如果模板引擎是 freemarker
//        String templatePath = "/templates/mapper.xml.ftl";
//         如果模板引擎是 velocity
        String templatePath = "/templates/mapper.xml.vm";

        // 自定义输出配置
        List<FileOutConfig> focList = new ArrayList<>();
        // 自定义配置会被优先输出
        focList.add(new FileOutConfig(templatePath) {
            @Override
            public String outputFile(TableInfo tableInfo) {
                // 自定义输出文件名 ， 如果你 Entity 设置了前后缀、此处注意 xml 的名称会跟着发生变化！！
                return  outputFilePath + tableInfo.getMapperName() + StringPool.DOT_XML;
            }
        });

        cfg.setFileOutConfigList(focList);
        return cfg;
    }
}
