package cn.fleey.xposed.myapplication.util;

import okhttp3.*;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.*;

public class HttpRequestUtil {
    public static String get(String url) {
        return get(url, null);
    }

    public static String get(String url, Map<String, String> param) {
        String strParam = "";
        if (param != null)
            strParam = getMapToString(param, true);
        if (!strParam.isEmpty())
            url += "?" + strParam;
        //build param
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();
        try {
            Response response = client.newCall(request).execute();
            return response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String post(String url, Map<String, String> param) {
        OkHttpClient mOkHttpClient = new OkHttpClient();
        FormBody.Builder formBodyBuilder = new FormBody.Builder();
        Set<String> keySet = param.keySet();
        for (String key : keySet) {
            String value = param.get(key);
            formBodyBuilder.add(key, value);
        }
        FormBody formBody = formBodyBuilder.build();
        Request request = new Request
                .Builder()
                .post(formBody)
                .url(url)
                .build();
        try (Response response = mOkHttpClient.newCall(request).execute()) {
            return response.body().string();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getMapToString(Map<String, String> map) {
        return getMapToString(map, false);
    }

    public static String getMapToString(Map<String, String> map, boolean isUrlencode) {
        Set<String> keySet = map.keySet();
        //将set集合转换为数组
        String[] keyArray = keySet.toArray(new String[0]);
        //给数组排序(升序)
        Arrays.sort(keyArray);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < keyArray.length; i++) {
            if (map.get(keyArray[i]).trim().length() > 0) {
                sb.append(keyArray[i]).append("=").append(map.get(keyArray[i]).trim());
            }
            if (i != keyArray.length - 1) {
                sb.append("&");
            }
        }
        if (isUrlencode) {
            try {
                return URLEncoder.encode(sb.toString(), "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

    public static Map<String, String> getStringToMao(String str) {
        return getStringToMap(str, false);
    }

    public static Map<String, String> getStringToMap(String str, boolean isUrldecode) {
        //感谢bojueyou指出的问题
        //判断str是否有值
        if (null == str || "".equals(str)) {
            return null;
        }
        //根据&截取
        String[] strings = str.split("&");
        //设置HashMap长度
        int mapLength = strings.length;
        //判断hashMap的长度是否是2的幂。
        if ((strings.length % 2) != 0) {
            mapLength = mapLength + 1;
        }

        Map<String, String> map = new HashMap<>(mapLength);
        //循环加入map集合
        for (String string : strings) {
            //截取一组字符串
            String[] strArray = string.split("=");
            //strArray[0]为KEY  strArray[1]为值
            if (isUrldecode) {
                try {
                    map.put(URLDecoder.decode(strArray[0], "UTF-8"), URLDecoder.decode(strArray[1], "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            } else {
                map.put(strArray[0], strArray[1]);
            }
        }
        return map;
    }

    public static int getSecondTimestamp(Date date) {
        if (null == date) {
            return 0;
        }
        String timestamp = String.valueOf(date.getTime() / 1000);
        return Integer.valueOf(timestamp);
    }
}
