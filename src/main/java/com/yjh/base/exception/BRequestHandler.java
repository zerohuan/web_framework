package com.yjh.base.exception;

import com.yjh.base.site.entities.BResponseData;

import javax.validation.ConstraintViolationException;

/**
 * A Handler for common exception in handling request.
 * Add exception message responseData which could be serialized.
 *
 * Created by yjh on 15-10-9.
 */
public class BRequestHandler {

    private BNeedHandle handle;

    public BRequestHandler() {
    }

    public BRequestHandler(BNeedHandle handle) {
        this.handle = handle;
    }

    public BResponseData execute() {
        BResponseData responseData = new BResponseData();

        try {
            this.getHandle().needHandle(responseData);
        } catch (ConstraintViolationException e) {
            //Validation exception
            responseData.setData(e.getConstraintViolations());
            responseData.setErrCode(BEnumError.VALIDATION_ERROR.getCode());
        } catch (Exception e) {
            throw new BSystemException(e);
        }

        return responseData;
    }

    public BNeedHandle getHandle() {
        return handle;
    }

    public void setHandle(BNeedHandle handle) {
        this.handle = handle;
    }
}
