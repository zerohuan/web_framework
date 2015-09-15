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

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yjh on 2015/9/3.
 */
public class Tester {
    private static String BASE_URL = "http://localhost:8080/framework";

    public void test() throws Exception {
        HttpPost httpRequest = new HttpPost(BASE_URL + "/test");//创建HttpPost对象
        String result;

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("str", "I am Post String"));

        httpRequest.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
        HttpResponse httpResponse = new DefaultHttpClient().execute(httpRequest);
        if(httpResponse.getStatusLine().getStatusCode() == 200)
        {
            HttpEntity httpEntity = httpResponse.getEntity();
            result = EntityUtils.toString(httpEntity);//取出应答字符串

            System.out.println(result);
        }
    }
}
