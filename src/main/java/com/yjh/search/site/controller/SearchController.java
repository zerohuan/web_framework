package com.yjh.search.site.controller;

import com.yjh.base.exception.BRequestHandler;
import com.yjh.base.site.entities.BResponseData;
import com.yjh.search.site.index.HtmlIndexer;
import com.yjh.search.site.model.TLinkEx;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.inject.Inject;
import java.util.List;

@Controller
public class SearchController {
	private static final Logger logger = LogManager.getLogger();

	@Inject
	private HtmlIndexer htmlIndexer;
	
	@RequestMapping("/searchbykey")
	@ResponseBody
	public BResponseData search(String keys, Integer page, Integer limit) throws Exception {
		return new BRequestHandler((responseData -> {
			List<TLinkEx> list = htmlIndexer.searchByKeyWords(keys, page, limit);
			responseData.setData(list);
		})).execute();
	}
}
