package com.sykun.baizhimall.controller;


import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sykun.baizhimall.entity.BrandEntity;
import com.sykun.baizhimall.entity.CategoryBrandRelationEntity;
import com.sykun.baizhimall.entity.CategoryEntity;
import com.sykun.baizhimall.service.BrandService;
import com.sykun.baizhimall.service.CategoryBrandRelationService;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author sykun
 * @since 2021-07-09
 */
@RestController
@RequestMapping("/brand")
public class BrandController {
    @Resource
    private BrandService brandService;
    @Resource
    private CategoryBrandRelationService relationService;
    // yourEndpoint填写Bucket所在地域对应的Endpoint。以华东1（杭州）为例，Endpoint填写为https://oss-cn-hangzhou.aliyuncs.com。
    private final String endpoint = "https://oss-cn-beijing.aliyuncs.com";
    // 阿里云账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM用户进行API访问或日常运维，请登录RAM控制台创建RAM用户。
    private final String accessKeyId = "LTAI5tR";
    private final String accessKeySecret = "2dfXF7";
    private final String bucketName = "sy";

    @PostMapping("/list")
    public Map<String, Object> showBrand(@RequestBody BrandEntity brandEntity) {
        HashMap<String, Object> map = new HashMap<>();
        IPage<BrandEntity> brandEntityPage = new Page<>(brandEntity.getPageNum(), brandEntity.getPageSize());
        if (brandEntity.getName() != null && !"".equals(brandEntity.getName())) {
            IPage<BrandEntity> page = brandService.page(brandEntityPage, new QueryWrapper<BrandEntity>().eq("name", brandEntity.getName()));
            map.put("data", page);
        } else {
            IPage<BrandEntity> page = brandService.page(brandEntityPage);
            map.put("data", page);
        }
        return map;
    }

    @PostMapping("save")
    public Map<String, Object> saveBrand(@RequestBody BrandEntity brandEntity) {
        HashMap<String, Object> map = new HashMap<>();
        brandEntity.setBrandId(null);
        List<BrandEntity> nameList = brandService.list(new QueryWrapper<BrandEntity>().eq("name", brandEntity.getName()));
        if (nameList.isEmpty()) {
            boolean save = brandService.save(brandEntity);
            if (save) {
                map.put("code", 10000);
            } else {
                map.put("code", 300);
                delOne(brandEntity.getLogo());
            }
        } else {
            delOne(brandEntity.getLogo());
            map.put("code", 300);
        }
        return map;
    }

    @PostMapping("modify")
    public Map<String, Object> modifyBrand(@RequestBody @NotNull BrandEntity brandEntity) {
        HashMap<String, Object> map = new HashMap<>();
        // 根据提交的品牌Id查询是否存在该品牌
        BrandEntity brandEntityOld = brandService.getById(brandEntity.getBrandId());
        // 若果提交的品牌名发生了改变,查询库中是否已存在新的品牌名
        List<BrandEntity> sameNameBrand = new ArrayList<>();
        // 品牌名称发生变化，关联的分类中品牌名改变
        if (!brandEntityOld.getName().equals(brandEntity.getName())) {
            relationService.update(new UpdateWrapper<CategoryBrandRelationEntity>().eq("brand_name", brandEntityOld.getName()).set("brand_name", brandEntity.getName()));
            sameNameBrand = brandService.list(new QueryWrapper<BrandEntity>().eq("name", brandEntity.getName()));
        }
        boolean b = false;
        if (sameNameBrand.size()==0) {
            b = brandService.updateById(brandEntity);
        }
        if (b) {
            map.put("code", 10000);
        } else {
            map.put("code", 300);
        }
        if (!brandEntityOld.getLogo().equals(brandEntity.getLogo())) {
            delOne(brandEntityOld.getLogo());
        }
        return map;
    }

    @GetMapping("/info/{id}")
    public Map<String, Object> readyUpdate(@PathVariable("id") String id) {
        HashMap<String, Object> map = new HashMap<>();
        BrandEntity byId = brandService.getById(id);
        map.put("code", 10000);
        map.put("data", byId);
        return map;
    }

    /**
     * 删除
     *
     * @param ids
     * @return
     */
    @DeleteMapping("remove")
    public Map<String, Object> delImages(@RequestBody Long[] ids) {
        HashMap<String, Object> map = new HashMap<>();
        // 创建OSSClient实例。
        boolean b = brandService.removeByIds(Arrays.asList(ids));
        for (Long id : ids) {
            relationService.remove(new QueryWrapper<CategoryBrandRelationEntity>().eq("brand_id", id));
        }
        if (b) {
            map.put("code", 10000);
            map.put("msg", "删除成功");
        } else {
            map.put("code", 300);
            map.put("msg", "删除失败");
        }
        return map;
    }

    // 处理上传失败删除,上传图片后点了取消或者上传失败
    @PostMapping("delByLogo")
    public Map<String, Object> delImage(@RequestBody BrandEntity logo) {
        HashMap<String, Object> map = new HashMap<>();
        delOne(logo.getLogo());
        map.put("code", 200);
        return map;
    }

    // 删除OSS无用图片
    public void delOne(String logo) {
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        String replace = logo.replace("http://sykun-mall.oss-cn-beijing.aliyuncs.com/", "");
        ossClient.deleteObject(bucketName, replace);
        ossClient.shutdown();
    }

    @PostMapping("cate")
    public Map<String, Object> getCategory(@RequestBody BrandEntity brandEntity) {
        HashMap<String, Object> map = new HashMap<>();
        List<CategoryEntity> categoryList = brandEntity.getCategoryList();
        relationService.remove(new QueryWrapper<CategoryBrandRelationEntity>().eq("brand_id", brandEntity.getBrandId()));
        List<CategoryBrandRelationEntity> list = new ArrayList<>();
        if (categoryList != null && categoryList.size() > 0) {
            for (CategoryEntity categoryEntity : categoryList) {
                CategoryBrandRelationEntity relation = new CategoryBrandRelationEntity();
                relation.setBrandId(brandEntity.getBrandId());
                relation.setBrandName(brandEntity.getName());
                relation.setCategoryId(categoryEntity.getCategoryId());
                relation.setCategoryName(categoryEntity.getCategoryName());
                list.add(relation);
            }
            relationService.saveBatch(list);
        }
        map.put("code", 10000);
        return map;
    }

    /**
     * 导出
     * @param ids
     * @param response
     */
    @PostMapping("export")
    public void exportOut(@RequestBody Long[] ids, HttpServletResponse response) {
        Collection<BrandEntity> brandEntities = brandService.listByIds(Arrays.asList(ids));
        ExportParams params = new ExportParams("品牌信息", "品牌");
        Workbook excel = ExcelExportUtil.exportExcel(params, BrandEntity.class, brandEntities);
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-Disposition", "attachment;filename=Brand.xlsx");
        ServletOutputStream outputStream = null;
        try {
            outputStream = response.getOutputStream();
            excel.write(outputStream);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                excel.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    // 品牌从Excel表格中导入
    @PostMapping("import")
    public void importIn(MultipartFile file) {
        ImportParams params = new ImportParams();
        params.setHeadRows(1);
        params.setTitleRows(1);
        params.setSheetNum(1);
        List<BrandEntity> brands = new ArrayList<>();
        try {
            brands = ExcelImportUtil.importExcel(file.getInputStream(), BrandEntity.class, params);
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (BrandEntity brand : brands) {
            BrandEntity name = brandService.getOne(new QueryWrapper<BrandEntity>().eq("name", brand.getName()));
            if (name == null) {
                brandService.save(brand);
            }
        }
    }

}

