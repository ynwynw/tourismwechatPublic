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
import com.example.entity.YudingjiudianInfo;
import com.example.dao.YudingjiudianInfoDao;
import com.example.service.YudingjiudianInfoService;
import com.example.exception.CustomException;
import com.example.common.ResultCode;
import com.example.vo.EchartsData;
import com.example.vo.YudingjiudianInfoVo;
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
@RequestMapping(value = "/yudingjiudianInfo")
public class YudingjiudianInfoController {

    @Resource
    private YudingjiudianInfoService yudingjiudianInfoService;
	@Resource
    private YudingjiudianInfoDao yudingjiudianInfoDao;

    @PostMapping
    public Result<YudingjiudianInfo> add(@RequestBody YudingjiudianInfoVo yudingjiudianInfo) {
        
		//mixmajixami
		yudingjiudianInfoService.add(yudingjiudianInfo);
        return Result.success(yudingjiudianInfo);
    }
	
	//youtixing1
    //youtixing2
	
	@GetMapping("/getByDiqu")
    public Result<List<Map<String,String>>> qidu() {
        return Result.success(yudingjiudianInfoService.findByDiqu());
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Long id) {
        yudingjiudianInfoService.delete(id);
        return Result.success();
    }
	@GetMapping("/getByzhuceyonghuZhanghao/{zhanghao}")    public Result<List<YudingjiudianInfo>> getByzhuceyonghuZhanghao(@PathVariable String zhanghao) {    List<YudingjiudianInfo> yudingjiudianInfo = yudingjiudianInfoService.getByzhuceyonghuZhanghao(zhanghao);    return Result.success(yudingjiudianInfo);    }    

    @PutMapping
    public Result update(@RequestBody YudingjiudianInfoVo yudingjiudianInfo) {
        yudingjiudianInfoService.update(yudingjiudianInfo);
        return Result.success();
    }
    //@PutMapping("/update2")
//    public Result update2(@RequestBody YudingjiudianInfoVo yudingjiudianInfo) {
//        yudingjiudianInfoService.update2(yudingjiudianInfo);
//        return Result.success();
//    }
    @GetMapping("/{id}")
    public Result<YudingjiudianInfo> detail(@PathVariable Long id) {
        YudingjiudianInfo yudingjiudianInfo = yudingjiudianInfoService.findById(id);
        return Result.success(yudingjiudianInfo);
    }
    @GetMapping("/changeStatus/{id}")
    public Result<YudingjiudianInfo> changeStatus(@PathVariable Long id) {
        yudingjiudianInfoService.changeStatus(id);
        return Result.success();
    }
	

    @GetMapping
    public Result<List<YudingjiudianInfoVo>> all() {
        return Result.success(yudingjiudianInfoService.findAll());
    }

    @GetMapping("/page/{name}")
    public Result<PageInfo<YudingjiudianInfoVo>> page(@PathVariable String name,
                                                @RequestParam(defaultValue = "1") Integer pageNum,
                                                @RequestParam(defaultValue = "5") Integer pageSize,
                                                HttpServletRequest request) {
        return Result.success(yudingjiudianInfoService.findPage(name, pageNum, pageSize, request));
    }
	
	 @GetMapping("/pageqt/{name}")
    public Result<PageInfo<YudingjiudianInfoVo>> pageqt(@PathVariable String name,
                                                @RequestParam(defaultValue = "1") Integer pageNum,
                                                @RequestParam(defaultValue = "8") Integer pageSize,
                                                HttpServletRequest request) {
        return Result.success(yudingjiudianInfoService.findPageqt(name, pageNum, pageSize, request));
    }

   // @PostMapping("/register")
//    public Result<YudingjiudianInfo> register(@RequestBody YudingjiudianInfo yudingjiudianInfo) {
//        if (StrUtil.isBlank(yudingjiudianInfo.getName()) || StrUtil.isBlank(yudingjiudianInfo.getPassword())) {
//            throw new CustomException(ResultCode.PARAM_ERROR);
//        }
//        return Result.success(yudingjiudianInfoService.add(yudingjiudianInfo));
//    }

    /**
    * 批量通过excel添加信息
    * @param file excel文件
    * @throws IOException
    */
    @PostMapping("/upload")
    public Result upload(MultipartFile file) throws IOException {

        List<YudingjiudianInfo> infoList = ExcelUtil.getReader(file.getInputStream()).readAll(YudingjiudianInfo.class);
        if (!CollectionUtil.isEmpty(infoList)) {
            // 处理一下空数据
            List<YudingjiudianInfo> resultList = infoList.stream().filter(x -> ObjectUtil.isNotEmpty(x.getJiudianleixing())).collect(Collectors.toList());
            for (YudingjiudianInfo info : resultList) {
                yudingjiudianInfoService.add(info);
            }
        }
        return Result.success();
    }
	@GetMapping("/get/yudingjiudian_tj_jiudianleixing")
    Result<List<EchartsData>> yudingjiudian_tj_jiudianleixing() {
        List<EchartsData> list = new ArrayList<>();
        List<Map<String, Object>> yudingjiudian_tj_jiudianleixingList = yudingjiudianInfoDao.yudingjiudian_tj_jiudianleixing();
        Map<String, Double> typeMap = new HashMap<>();
        for (Map<String, Object> map : yudingjiudian_tj_jiudianleixingList) {

            typeMap.put((String)map.get("aa"), (Double.valueOf((String)map.get("bb").toString())));

        }
        getPieData("预定酒店按酒店类型统计", list, typeMap);
        getBarData("预定酒店按酒店类型统计", list, typeMap);
        return Result.success(list);
    }
    @GetMapping("/getExcelModel")
    public void getExcelModel(HttpServletResponse response) throws IOException {
        // 1. 生成excel
        Map<String, Object> row = new LinkedHashMap<>();
		row.put("jiudianmingcheng", "A酒店名称");row.put("jiudianleixing", "A酒店类型");row.put("yudingshijian", "A预定时间");row.put("yudingtianshu", "A预定天数");row.put("zhanghao", "A账号");row.put("xingming", "A姓名");row.put("lianxihaoma", "A联系号码");
		row.put("status", "是");
		row.put("level", "yudingjiudian");

        List<Map<String, Object>> list = CollUtil.newArrayList(row);

        // 2. 写excel
        ExcelWriter writer = ExcelUtil.getWriter(true);
        writer.write(list, true);

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        response.setHeader("Content-Disposition","attachment;filename=yudingjiudianInfoModel.xlsx");

        ServletOutputStream out = response.getOutputStream();
        writer.flush(out, true);
        writer.close();
        IoUtil.close(System.out);
    }
	@GetMapping("/getExcel")
    public void getExcel(HttpServletResponse response) throws IOException {
        // 1. 生成excel
        Map<String, Object> row = new LinkedHashMap<>();
        row.put("jiudianmingcheng", "A酒店名称");row.put("jiudianleixing", "A酒店类型");row.put("yudingshijian", "A预定时间");row.put("yudingtianshu", "A预定天数");row.put("zhanghao", "A账号");row.put("xingming", "A姓名");row.put("lianxihaoma", "A联系号码");

        row.put("status", "是");
        row.put("level", "权限");
        List<Map<String, Object>> list = CollUtil.newArrayList(row);
        List<Map<String, Object>> daochuexcellist = yudingjiudianInfoDao.daochuexcel();
        Map<String, Double> typeMap = new HashMap<>();
        for (Map<String, Object> map : daochuexcellist) {
            list.add(map);
        }
        // 2. 写excel
        ExcelWriter writer = ExcelUtil.getWriter(true);
        writer.write(list, true);

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        response.setHeader("Content-Disposition","attachment;filename=yudingjiudianInfo.xlsx");

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