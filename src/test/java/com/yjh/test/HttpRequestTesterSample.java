package com.yjh.test;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * a sample of http request test.
 * Using this method to test listener, filter, servlet and some aspects.
 * .
 * Created by yjh on 2015/9/3.
 */
public class HttpRequestTesterSample {
    @Test
    public void test() throws Exception {
        HttpPost httpRequest = new HttpPost(Contants.BASE_URL + "/test");
        String result;

        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("str", "I am Post String"));

        httpRequest.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
        HttpResponse httpResponse = new DefaultHttpClient().execute(httpRequest);
        if(httpResponse.getStatusLine().getStatusCode() == 200) {
            HttpEntity httpEntity = httpResponse.getEntity();
            result = EntityUtils.toString(httpEntity);

            System.out.println(result);
        }
    }
}
