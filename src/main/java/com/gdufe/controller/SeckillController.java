package com.gdufe.controller;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gdufe.dto.Exposer;
import com.gdufe.dto.SeckillExecution;
import com.gdufe.dto.SeckillResult;
import com.gdufe.entity.Seckill;
import com.gdufe.enums.SeckillStatEnum;
import com.gdufe.exception.RepeatKillException;
import com.gdufe.exception.SeckillCloseException;
import com.gdufe.service.SeckillService;

/**
 * 
 * @author song
 *
 */
@Controller
@RequestMapping("/seckill")
public class SeckillController {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	@Autowired
	SeckillService seckillService;
	
	//查询所有商品
	@RequestMapping(name="/list",method=RequestMethod.GET)
	public String list(Model model){
		List<Seckill> list = seckillService.getAllSeckill();
		//list.jsp+model=ModelAndView
		model.addAttribute("list",list);
		return "list";//WEB-INF/jsp/list.jsp
	}
	
	//根据Id查询秒杀商品
	@RequestMapping(name="/{seckillId}/detail",method=RequestMethod.GET)
	public String detail(@PathVariable("seckillId") Long seckillId,Model model){
		if(seckillId==null){
			return "redirect:/seckill/list";
		}
		
		Seckill seckill = seckillService.getById(seckillId);
		if(seckill==null){
			return "forword:/seckill/list";
		}
		model.addAttribute("seckill", seckill);
		return "detail";
	}
	
	//根据seckillId暴露秒杀接口
	/*prproduces = {"application/json;charest=UTF-8"})
	 * charest=UTF-8  	等号中间不能有空格，有空格就报 http500错误*/
	@RequestMapping(name="/{seckillId}/exposer",method=RequestMethod.POST,produces={"application/json;charset=UTF-8"})
	@ResponseBody
	public SeckillResult<Exposer> exposer(@PathVariable("seckillId") Long seckillId,Model model){
		SeckillResult<Exposer> result ;
		try {
			Exposer exposer = seckillService.exportSeckillUrl(seckillId);
			result = new SeckillResult<Exposer>(true, exposer);
			
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			result = new SeckillResult<Exposer>(false, e.getMessage());
		}
		return result;
	}
	
	//秒杀成功明细
	 
	@RequestMapping(name="/{seckillId}/{md5}/execution",method=RequestMethod.POST,produces={"application/json;charset=UTF-8"})
	@ResponseBody
	public SeckillResult<SeckillExecution> execute(@PathVariable("seckillId") Long seckillId,
			@PathVariable("md5") String md5,@CookieValue(value="killPhone",required=false) Long phone){
		// springmvc valid 以后学一下 spring的验证信息
				if (phone == null) {
					return new SeckillResult<SeckillExecution>(false, "未注册");
				}

				SeckillResult<SeckillExecution> result;
				try {
					SeckillExecution execution = seckillService.executeseckill(seckillId, phone, md5);
					result = new SeckillResult<SeckillExecution>(true, execution);
				} catch (RepeatKillException e) {
					SeckillExecution execution = new SeckillExecution(seckillId, SeckillStatEnum.REPEAT_KILL);
					return new SeckillResult<SeckillExecution>(true, execution);
				} catch (SeckillCloseException e) {
					SeckillExecution execution = new SeckillExecution(seckillId, SeckillStatEnum.END);
					return new SeckillResult<SeckillExecution>(true, execution);
				} catch (Exception e) {
					SeckillExecution execution = new SeckillExecution(seckillId, SeckillStatEnum.INNER_ERROR);
					return new SeckillResult<SeckillExecution>(true, execution);
				}
				return result;
			}
	//实时返回系统时间
	@RequestMapping(value = "/time/now", method = RequestMethod.GET,
			produces = {"application/json;charest=UTF-8"})
	@ResponseBody
	public SeckillResult<Long> time() {
		Date now = new Date();
		return new SeckillResult<Long>(true, now.getTime());
	}
}
