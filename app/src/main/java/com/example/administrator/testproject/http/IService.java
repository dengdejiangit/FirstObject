package com.example.administrator.testproject.http;

import com.example.administrator.testproject.bean.FemaleNameBean;
import com.example.administrator.testproject.bean.WelfareBean;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface IService {

    //http://gank.io/api/data/%E7%A6%8F%E5%88%A9/10/1
    @GET("data/福利/{limit}/{page}")
    @Headers("Content-Type:application/x-www-form-urlencoded; charset=utf-8")
    Observable<WelfareBean> getWelfareData(@Path("limit") int limit, @Path("page") int page);

    //https://www.apiopen.top/femaleNameApi?page=1
    @FormUrlEncoded
    @POST("femaleNameApi")
    Observable<FemaleNameBean> getFemaleName(@Field("page") int page);

    /*
     * get @Query()
     * post @Field()
     * 替换{} @Path()
     *
     * */
}
