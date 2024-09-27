package com.example.controller;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.example.common.Result;
import com.example.common.ResultCode;
import com.example.entity.MeirituijianInfo;
import com.example.dao.MeirituijianInfoDao;
import com.example.service.MeirituijianInfoService;
import com.example.exception.CustomException;
import com.example.common.ResultCode;
import com.example.vo.EchartsData;
import com.example.vo.MeirituijianInfoVo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.example.service.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Value;
import cn.hutool.core.util.StrUtil;
import org.springframework.web.multipart.MultipartFile;
import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/meirituijianInfo")
public class MeirituijianInfoController {

    @Resource
    private MeirituijianInfoService meirituijianInfoService;
	@Resource
    private MeirituijianInfoDao meirituijianInfoDao;

    @PostMapping
    public Result<MeirituijianInfo> add(@RequestBody MeirituijianInfoVo meirituijianInfo) {
        
		//mixmajixami
		meirituijianInfoService.add(meirituijianInfo);
        return Result.success(meirituijianInfo);
    }
	
	//youtixing1
    //youtixing2
	
	@GetMapping("/getByDiqu")
    public Result<List<Map<String,String>>> qidu() {
        return Result.success(meirituijianInfoService.findByDiqu());
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Long id) {
        meirituijianInfoService.delete(id);
        return Result.success();
    }
	

    @PutMapping
    public Result update(@RequestBody MeirituijianInfoVo meirituijianInfo) {
        meirituijianInfoService.update(meirituijianInfo);
        return Result.success();
    }
    //@PutMapping("/update2")
//    public Result update2(@RequestBody MeirituijianInfoVo meirituijianInfo) {
//        meirituijianInfoService.update2(meirituijianInfo);
//        return Result.success();
//    }
    @GetMapping("/{id}")
    public Result<MeirituijianInfo> detail(@PathVariable Long id) {
        MeirituijianInfo meirituijianInfo = meirituijianInfoService.findById(id);
        return Result.success(meirituijianInfo);
    }
    @GetMapping("/changeStatus/{id}")
    public Result<MeirituijianInfo> changeStatus(@PathVariable Long id) {
        meirituijianInfoService.changeStatus(id);
        return Result.success();
    }
	

    @GetMapping
    public Result<List<MeirituijianInfoVo>> all() {
        return Result.success(meirituijianInfoService.findAll());
    }

    @GetMapping("/page/{name}")
    public Result<PageInfo<MeirituijianInfoVo>> page(@PathVariable String name,
                                                @RequestParam(defaultValue = "1") Integer pageNum,
                                                @RequestParam(defaultValue = "5") Integer pageSize,
                                                HttpServletRequest request) {
        return Result.success(meirituijianInfoService.findPage(name, pageNum, pageSize, request));
    }
	
	 @GetMapping("/pageqt/{name}")
    public Result<PageInfo<MeirituijianInfoVo>> pageqt(@PathVariable String name,
                                                @RequestParam(defaultValue = "1") Integer pageNum,
                                                @RequestParam(defaultValue = "8") Integer pageSize,
                                                HttpServletRequest request) {
        return Result.success(meirituijianInfoService.findPageqt(name, pageNum, pageSize, request));
    }

   // @PostMapping("/register")
//    public Result<MeirituijianInfo> register(@RequestBody MeirituijianInfo meirituijianInfo) {
//        if (StrUtil.isBlank(meirituijianInfo.getName()) || StrUtil.isBlank(meirituijianInfo.getPassword())) {
//            throw new CustomException(ResultCode.PARAM_ERROR);
//        }
//        return Result.success(meirituijianInfoService.add(meirituijianInfo));
//    }

    /**
    * 批量通过excel添加信息
    * @param file excel文件
    * @throws IOException
    */
    @PostMapping("/upload")
    public Result upload(MultipartFile file) throws IOException {

        List<MeirituijianInfo> infoList = ExcelUtil.getReader(file.getInputStream()).readAll(MeirituijianInfo.class);
        if (!CollectionUtil.isEmpty(infoList)) {
            // 处理一下空数据
            List<MeirituijianInfo> resultList = infoList.stream().filter(x -> ObjectUtil.isNotEmpty(x.getJibie())).collect(Collectors.toList());
            for (MeirituijianInfo info : resultList) {
                meirituijianInfoService.add(info);
            }
        }
        return Result.success();
    }
	@GetMapping("/get/meirituijian_tj_jibie")
    Result<List<EchartsData>> meirituijian_tj_jibie() {
        List<EchartsData> list = new ArrayList<>();
        List<Map<String, Object>> meirituijian_tj_jibieList = meirituijianInfoDao.meirituijian_tj_jibie();
        Map<String, Double> typeMap = new HashMap<>();
        for (Map<String, Object> map : meirituijian_tj_jibieList) {

            typeMap.put((String)map.get("aa"), (Double.valueOf((String)map.get("bb").toString())));

        }
        getPieData("每日推荐按级别统计", list, typeMap);
        getBarData("每日推荐按级别统计", list, typeMap);
        return Result.success(list);
    }
    @GetMapping("/getExcelModel")
    public void getExcelModel(HttpServletResponse response) throws IOException {
        // 1. 生成excel
        Map<String, Object> row = new LinkedHashMap<>();
		row.put("jingdianmingcheng", "A景点名称");row.put("jibie", "A级别");row.put("danjiwangji", "A淡季旺季");row.put("yingyeshijian", "A营业时间");row.put("piaojia", "A票价");row.put("weizhi", "A位置");row.put("tupian", "A图片");row.put("jianjie", "A简介");
		row.put("status", "是");
		row.put("level", "meirituijian");

        List<Map<String, Object>> list = CollUtil.newArrayList(row);

        // 2. 写excel
        ExcelWriter writer = ExcelUtil.getWriter(true);
        writer.write(list, true);

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        response.setHeader("Content-Disposition","attachment;filename=meirituijianInfoModel.xlsx");

        ServletOutputStream out = response.getOutputStream();
        writer.flush(out, true);
        writer.close();
        IoUtil.close(System.out);
    }
	@GetMapping("/getExcel")
    public void getExcel(HttpServletResponse response) throws IOException {
        // 1. 生成excel
        Map<String, Object> row = new LinkedHashMap<>();
        row.put("jingdianmingcheng", "A景点名称");row.put("jibie", "A级别");row.put("danjiwangji", "A淡季旺季");row.put("yingyeshijian", "A营业时间");row.put("piaojia", "A票价");row.put("weizhi", "A位置");row.put("tupian", "A图片");row.put("jianjie", "A简介");

        row.put("status", "是");
        row.put("level", "权限");
        List<Map<String, Object>> list = CollUtil.newArrayList(row);
        List<Map<String, Object>> daochuexcellist = meirituijianInfoDao.daochuexcel();
        Map<String, Double> typeMap = new HashMap<>();
        for (Map<String, Object> map : daochuexcellist) {
            list.add(map);
        }
        // 2. 写excel
        ExcelWriter writer = ExcelUtil.getWriter(true);
        writer.write(list, true);

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        response.setHeader("Content-Disposition","attachment;filename=meirituijianInfo.xlsx");

        ServletOutputStream out = response.getOutputStream();
        writer.flush(out, true);
        writer.close();
        IoUtil.close(System.out);
    }
	private void getPieData(String name, List<EchartsData> pieList, Map<String, Double> dataMap) {
        EchartsData pieData = new EchartsData();
        EchartsData.Series series = new EchartsData.Series();

        Map<String, String> titleMap = new HashMap<>(2);
        titleMap.put("text", name);
        pieData.setTitle(titleMap);

        series.setName(name + "比例");
        series.setType("pie");
        series.setRadius("55%");

        List<Object> objects = new ArrayList<>();
        List<Object> legendList = new ArrayList<>();
        for (String key : dataMap.keySet()) {
            Double value = dataMap.get(key);
            objects.add(new JSONObject().putOpt("name", key).putOpt("value", value));
            legendList.add(key);
        }
        series.setData(objects);

        pieData.setSeries(Collections.singletonList(series));
        Map<String, Boolean> map = new HashMap<>();
        map.put("show", true);
        pieData.setTooltip(map);

        Map<String, Object> legendMap = new HashMap<>(4);
        legendMap.put("orient", "vertical");
        legendMap.put("x", "left");
        legendMap.put("y", "center");
        legendMap.put("data", legendList);
        pieData.setLegend(legendMap);

        pieList.add(pieData);
    }

    private void getBarData(String name, List<EchartsData> barList, Map<String, Double> dataMap) {
        EchartsData barData = new EchartsData();
        EchartsData.Series series = new EchartsData.Series();

        List<Object> seriesObjs = new ArrayList<>();
        List<Object> xAxisObjs = new ArrayList<>();
        for (String key : dataMap.keySet()) {
            Double value = dataMap.get(key);
            xAxisObjs.add(key);
            seriesObjs.add(value);
        }

        series.setType("bar");
        series.setName(name);
        series.setData(seriesObjs);
        barData.setSeries(Collections.singletonList(series));

        Map<String, Object> xAxisMap = new HashMap<>(1);
        xAxisMap.put("data", xAxisObjs);
        barData.setxAxis(xAxisMap);

        barData.setyAxis(new HashMap<>());

        Map<String, Object> legendMap = new HashMap<>(1);
        legendMap.put("data", Collections.singletonList(name));
        barData.setLegend(legendMap);

        Map<String, Boolean> map = new HashMap<>(1);
        map.put("show", true);
        barData.setTooltip(map);

        Map<String, String> titleMap = new HashMap<>(1);
        titleMap.put("text", name);
        barData.setTitle(titleMap);

        barList.add(barData);
    }
	//xixugaixmixma
}