package com.yjh.base.site.entities;

import javax.validation.ConstraintViolation;
import java.io.Serializable;
import java.util.Set;

/**
 * When the controller response body,
 * This is a uniform format for response data, use it wrap the entity and other data
 *
 * Created by yjh on 15-10-8.
 */
public class BResponseData implements Serializable {
    private Object data;
    private int errCode;

    public Object getData() {
        return data;
    }

    /**
     * Some type can not be converted to json,
     * converters that kind of data to the type being able to converted to json
     * @param data the data to be wrapped
     */
    @SuppressWarnings("unchecked")
    public void setData(Object data) {
        if(data instanceof Set) {
            Set dataSet = (Set)data;
            if(dataSet.size() > 0 && dataSet.iterator().next() instanceof ConstraintViolation<?>) {
                ConstraintViolation<?> constraintViolation = ((Set<ConstraintViolation<?>>)dataSet).iterator().next();
                BError error = new BError();
                error.setMessage(constraintViolation.getMessage());
                
                this.data = error;
                return;
            }
        } else if(data instanceof Exception) {
            this.data = ((Exception) data).getMessage();
            return;
        }

        this.data = data;
    }

    public int getErrCode() {
        return errCode;
    }

    public void setErrCode(int errCode) {
        this.errCode = errCode;
    }

    @Override
    public String toString() {
        return "BResponseData{" +
                "data=" + data +
                ", errCode=" + errCode +
                '}';
    }

    public static class BResult implements Serializable {
        private boolean success;

        public BResult(boolean success) {
            this.success = success;
        }

        public boolean isSuccess() {
            return success;
        }

        public void setSuccess(boolean success) {
            this.success = success;
        }
    }
}
