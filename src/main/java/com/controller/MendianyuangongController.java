package com.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;

import com.utils.ValidatorUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.annotation.IgnoreAuth;

import com.entity.MendianyuangongEntity;
import com.entity.view.MendianyuangongView;

import com.service.MendianyuangongService;
import com.service.TokenService;
import com.utils.PageUtils;
import com.utils.R;
import com.utils.MD5Util;
import com.utils.MPUtil;
import com.utils.CommonUtil;


/**
 * 门店员工
 * 后端接口
 * @author 
 * @email 
 * @date 2021-04-27 11:11:19
 */
@RestController
@RequestMapping("/mendianyuangong")
public class MendianyuangongController {
    @Autowired
    private MendianyuangongService mendianyuangongService;
    


    /**
     * 后端列表
     */
    @RequestMapping("/page")
    public R page(@RequestParam Map<String, Object> params,MendianyuangongEntity mendianyuangong,
		HttpServletRequest request){
		String tableName = request.getSession().getAttribute("tableName").toString();
		if(tableName.equals("dianzhu")) {
			mendianyuangong.setDianzhuzhanghao((String)request.getSession().getAttribute("username"));
		}
        EntityWrapper<MendianyuangongEntity> ew = new EntityWrapper<MendianyuangongEntity>();
		PageUtils page = mendianyuangongService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, mendianyuangong), params), params));

        return R.ok().put("data", page);
    }
    
    /**
     * 前端列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params,MendianyuangongEntity mendianyuangong, 
		HttpServletRequest request){
        EntityWrapper<MendianyuangongEntity> ew = new EntityWrapper<MendianyuangongEntity>();
		PageUtils page = mendianyuangongService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, mendianyuangong), params), params));
        return R.ok().put("data", page);
    }

	/**
     * 列表
     */
    @RequestMapping("/lists")
    public R list( MendianyuangongEntity mendianyuangong){
       	EntityWrapper<MendianyuangongEntity> ew = new EntityWrapper<MendianyuangongEntity>();
      	ew.allEq(MPUtil.allEQMapPre( mendianyuangong, "mendianyuangong")); 
        return R.ok().put("data", mendianyuangongService.selectListView(ew));
    }

	 /**
     * 查询
     */
    @RequestMapping("/query")
    public R query(MendianyuangongEntity mendianyuangong){
        EntityWrapper< MendianyuangongEntity> ew = new EntityWrapper< MendianyuangongEntity>();
 		ew.allEq(MPUtil.allEQMapPre( mendianyuangong, "mendianyuangong")); 
		MendianyuangongView mendianyuangongView =  mendianyuangongService.selectView(ew);
		return R.ok("查询门店员工成功").put("data", mendianyuangongView);
    }
	
    /**
     * 后端详情
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
        MendianyuangongEntity mendianyuangong = mendianyuangongService.selectById(id);
        return R.ok().put("data", mendianyuangong);
    }

    /**
     * 前端详情
     */
    @RequestMapping("/detail/{id}")
    public R detail(@PathVariable("id") Long id){
        MendianyuangongEntity mendianyuangong = mendianyuangongService.selectById(id);
        return R.ok().put("data", mendianyuangong);
    }
    



    /**
     * 后端保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody MendianyuangongEntity mendianyuangong, HttpServletRequest request){
    	mendianyuangong.setId(new Date().getTime()+new Double(Math.floor(Math.random()*1000)).longValue());
    	//ValidatorUtils.validateEntity(mendianyuangong);
        mendianyuangongService.insert(mendianyuangong);
        return R.ok();
    }
    
    /**
     * 前端保存
     */
    @RequestMapping("/add")
    public R add(@RequestBody MendianyuangongEntity mendianyuangong, HttpServletRequest request){
    	mendianyuangong.setId(new Date().getTime()+new Double(Math.floor(Math.random()*1000)).longValue());
    	//ValidatorUtils.validateEntity(mendianyuangong);
        mendianyuangongService.insert(mendianyuangong);
        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody MendianyuangongEntity mendianyuangong, HttpServletRequest request){
        //ValidatorUtils.validateEntity(mendianyuangong);
        mendianyuangongService.updateById(mendianyuangong);//全部更新
        return R.ok();
    }
    

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids){
        mendianyuangongService.deleteBatchIds(Arrays.asList(ids));
        return R.ok();
    }
    
    /**
     * 提醒接口
     */
	@RequestMapping("/remind/{columnName}/{type}")
	public R remindCount(@PathVariable("columnName") String columnName, HttpServletRequest request, 
						 @PathVariable("type") String type,@RequestParam Map<String, Object> map) {
		map.put("column", columnName);
		map.put("type", type);
		
		if(type.equals("2")) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Calendar c = Calendar.getInstance();
			Date remindStartDate = null;
			Date remindEndDate = null;
			if(map.get("remindstart")!=null) {
				Integer remindStart = Integer.parseInt(map.get("remindstart").toString());
				c.setTime(new Date()); 
				c.add(Calendar.DAY_OF_MONTH,remindStart);
				remindStartDate = c.getTime();
				map.put("remindstart", sdf.format(remindStartDate));
			}
			if(map.get("remindend")!=null) {
				Integer remindEnd = Integer.parseInt(map.get("remindend").toString());
				c.setTime(new Date());
				c.add(Calendar.DAY_OF_MONTH,remindEnd);
				remindEndDate = c.getTime();
				map.put("remindend", sdf.format(remindEndDate));
			}
		}
		
		Wrapper<MendianyuangongEntity> wrapper = new EntityWrapper<MendianyuangongEntity>();
		if(map.get("remindstart")!=null) {
			wrapper.ge(columnName, map.get("remindstart"));
		}
		if(map.get("remindend")!=null) {
			wrapper.le(columnName, map.get("remindend"));
		}

		String tableName = request.getSession().getAttribute("tableName").toString();
		if(tableName.equals("dianzhu")) {
			wrapper.eq("dianzhuzhanghao", (String)request.getSession().getAttribute("username"));
		}

		int count = mendianyuangongService.selectCount(wrapper);
		return R.ok().put("count", count);
	}
	


}
