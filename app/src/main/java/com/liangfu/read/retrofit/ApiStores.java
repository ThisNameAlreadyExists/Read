package com.liangfu.read.retrofit;


import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by WuXiaolong
 * on 2016/3/24.
 */
public interface ApiStores {
    //baseUrl
    String API_SERVER_URL = "http://121.40.22.93/novel/192/945/";

    //获取小说内容
    @GET("{url}")
    Observable<String> getContentDetail(@Path("url") String url);
}
