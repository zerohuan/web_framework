package com.yjh.search.site.crawler;


/**
 * 登录校验器接口
 * @author yjh
 *
 */
public interface Verifier {
	public boolean loginVerify(BaseFetcher fetcher);
}
