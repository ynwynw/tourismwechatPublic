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
import com.example.entity.JingdianxinxiInfo;
import com.example.dao.JingdianxinxiInfoDao;
import com.example.service.JingdianxinxiInfoService;
import com.example.exception.CustomException;
import com.example.common.ResultCode;
import com.example.vo.EchartsData;
import com.example.vo.JingdianxinxiInfoVo;
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
@RequestMapping(value = "/jingdianxinxiInfo")
public class JingdianxinxiInfoController {

    @Resource
    private JingdianxinxiInfoService jingdianxinxiInfoService;
	@Resource
    private JingdianxinxiInfoDao jingdianxinxiInfoDao;

    @PostMapping
    public Result<JingdianxinxiInfo> add(@RequestBody JingdianxinxiInfoVo jingdianxinxiInfo) {
        
		//mixmajixami
		jingdianxinxiInfoService.add(jingdianxinxiInfo);
        return Result.success(jingdianxinxiInfo);
    }
	
	//youtixing1
    //youtixing2
	
	@GetMapping("/getByDiqu")
    public Result<List<Map<String,String>>> qidu() {
        return Result.success(jingdianxinxiInfoService.findByDiqu());
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Long id) {
        jingdianxinxiInfoService.delete(id);
        return Result.success();
    }
	

    @PutMapping
    public Result update(@RequestBody JingdianxinxiInfoVo jingdianxinxiInfo) {
        jingdianxinxiInfoService.update(jingdianxinxiInfo);
        return Result.success();
    }
    //@PutMapping("/update2")
//    public Result update2(@RequestBody JingdianxinxiInfoVo jingdianxinxiInfo) {
//        jingdianxinxiInfoService.update2(jingdianxinxiInfo);
//        return Result.success();
//    }
    @GetMapping("/{id}")
    public Result<JingdianxinxiInfo> detail(@PathVariable Long id) {
        JingdianxinxiInfo jingdianxinxiInfo = jingdianxinxiInfoService.findById(id);
        return Result.success(jingdianxinxiInfo);
    }
    @GetMapping("/changeStatus/{id}")
    public Result<JingdianxinxiInfo> changeStatus(@PathVariable Long id) {
        jingdianxinxiInfoService.changeStatus(id);
        return Result.success();
    }
	

    @GetMapping
    public Result<List<JingdianxinxiInfoVo>> all() {
        return Result.success(jingdianxinxiInfoService.findAll());
    }

    @GetMapping("/page/{name}")
    public Result<PageInfo<JingdianxinxiInfoVo>> page(@PathVariable String name,
                                                @RequestParam(defaultValue = "1") Integer pageNum,
                                                @RequestParam(defaultValue = "5") Integer pageSize,
                                                HttpServletRequest request) {
        return Result.success(jingdianxinxiInfoService.findPage(name, pageNum, pageSize, request));
    }
	
	 @GetMapping("/pageqt/{name}")
    public Result<PageInfo<JingdianxinxiInfoVo>> pageqt(@PathVariable String name,
                                                @RequestParam(defaultValue = "1") Integer pageNum,
                                                @RequestParam(defaultValue = "8") Integer pageSize,
                                                HttpServletRequest request) {
        return Result.success(jingdianxinxiInfoService.findPageqt(name, pageNum, pageSize, request));
    }

   // @PostMapping("/register")
//    public Result<JingdianxinxiInfo> register(@RequestBody JingdianxinxiInfo jingdianxinxiInfo) {
//        if (StrUtil.isBlank(jingdianxinxiInfo.getName()) || StrUtil.isBlank(jingdianxinxiInfo.getPassword())) {
//            throw new CustomException(ResultCode.PARAM_ERROR);
//        }
//        return Result.success(jingdianxinxiInfoService.add(jingdianxinxiInfo));
//    }

    /**
    * 批量通过excel添加信息
    * @param file excel文件
    * @throws IOException
    */
    @PostMapping("/upload")
    public Result upload(MultipartFile file) throws IOException {

        List<JingdianxinxiInfo> infoList = ExcelUtil.getReader(file.getInputStream()).readAll(JingdianxinxiInfo.class);
        if (!CollectionUtil.isEmpty(infoList)) {
            // 处理一下空数据
            List<JingdianxinxiInfo> resultList = infoList.stream().filter(x -> ObjectUtil.isNotEmpty(x.getJibie())).collect(Collectors.toList());
            for (JingdianxinxiInfo info : resultList) {
                jingdianxinxiInfoService.add(info);
            }
        }
        return Result.success();
    }
	@GetMapping("/get/jingdianxinxi_tj_jibie")
    Result<List<EchartsData>> jingdianxinxi_tj_jibie() {
        List<EchartsData> list = new ArrayList<>();
        List<Map<String, Object>> jingdianxinxi_tj_jibieList = jingdianxinxiInfoDao.jingdianxinxi_tj_jibie();
        Map<String, Double> typeMap = new HashMap<>();
        for (Map<String, Object> map : jingdianxinxi_tj_jibieList) {

            typeMap.put((String)map.get("aa"), (Double.valueOf((String)map.get("bb").toString())));

        }
        getPieData("景点信息按级别统计", list, typeMap);
        getBarData("景点信息按级别统计", list, typeMap);
        return Result.success(list);
    }
    @GetMapping("/getExcelModel")
    public void getExcelModel(HttpServletResponse response) throws IOException {
        // 1. 生成excel
        Map<String, Object> row = new LinkedHashMap<>();
		row.put("jingdianmingcheng", "A景点名称");
		row.put("status", "是");
		row.put("level", "jingdianxinxi");

        List<Map<String, Object>> list = CollUtil.newArrayList(row);

        // 2. 写excel
        ExcelWriter writer = ExcelUtil.getWriter(true);
        writer.write(list, true);

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        response.setHeader("Content-Disposition","attachment;filename=jingdianxinxiInfoModel.xlsx");

        ServletOutputStream out = response.getOutputStream();
        writer.flush(out, true);
        writer.close();
        IoUtil.close(System.out);
    }
	@GetMapping("/getExcel")
    public void getExcel(HttpServletResponse response) throws IOException {
        // 1. 生成excel
        Map<String, Object> row = new LinkedHashMap<>();
        row.put("jingdianmingcheng", "A景点名称");

        row.put("status", "是");
        row.put("level", "权限");
        List<Map<String, Object>> list = CollUtil.newArrayList(row);
        List<Map<String, Object>> daochuexcellist = jingdianxinxiInfoDao.daochuexcel();
        Map<String, Double> typeMap = new HashMap<>();
        for (Map<String, Object> map : daochuexcellist) {
            list.add(map);
        }
        // 2. 写excel
        ExcelWriter writer = ExcelUtil.getWriter(true);
        writer.write(list, true);

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        response.setHeader("Content-Disposition","attachment;filename=jingdianxinxiInfo.xlsx");

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