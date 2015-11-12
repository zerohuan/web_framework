package com.yjh.search.site.crawler;


/**
 * 登录校验器代理类
 * @author yjh
 *
 */
public class VerfierProxy implements Verifier {
	private Verifier proxied;
	
	public VerfierProxy(Verifier proxied) {
		super();
		this.proxied = proxied;
	}

	public boolean loginVerify(BaseFetcher fetcher) {
		// TODO Auto-generated method stub
		return proxied.loginVerify(fetcher);
	}
}
