package com.yjh.base.exception;

import com.yjh.base.site.entities.BResponseData;

/**
 * some logic need handle exception wrapped by BRequestHandler
 *
 * Created by yjh on 15-10-9.
 */
public interface BNeedHandle {
    void needHandle(BResponseData responseData);
}
