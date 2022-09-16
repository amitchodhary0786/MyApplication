package com.example.myapplication.utils;
import static com.example.myapplication.utils.WebUrls.DownloadDocument;
import static com.example.myapplication.utils.WebUrls.FailureCountMobileAPI;
import static com.example.myapplication.utils.WebUrls.GetAllTypeDocument;
import static com.example.myapplication.utils.WebUrls.GetDashBoardData;
import static com.example.myapplication.utils.WebUrls.GetDigitalDataForApi;
import static com.example.myapplication.utils.WebUrls.GetDocumentDetailByRule;
import static com.example.myapplication.utils.WebUrls.GetDocumentType;
import static com.example.myapplication.utils.WebUrls.GetDocumentTypeRules;
import static com.example.myapplication.utils.WebUrls.GetEvaultData;
import static com.example.myapplication.utils.WebUrls.GetMasterData;
import static com.example.myapplication.utils.WebUrls.GetPendingDocument;
import static com.example.myapplication.utils.WebUrls.GetRejectedDocument;
import static com.example.myapplication.utils.WebUrls.GetUserProfile;
import static com.example.myapplication.utils.WebUrls.GetVendorRules;
import static com.example.myapplication.utils.WebUrls.GetVerifiedDocument;
import static com.example.myapplication.utils.WebUrls.SSOAuthentication;
import static com.example.myapplication.utils.WebUrls.UpdateUserProfile;
import static com.example.myapplication.utils.WebUrls.ViewDocumentByRuleId;
import static com.example.myapplication.utils.WebUrls.checkAadhaarApis;
import static com.example.myapplication.utils.WebUrls.genratetokan;
import static com.example.myapplication.utils.WebUrls.getRajOTp;
import static com.example.myapplication.utils.WebUrls.getallreport;
import static com.example.myapplication.utils.WebUrls.getreport;
import static com.example.myapplication.utils.WebUrls.getruleID;
import static com.example.myapplication.utils.WebUrls.getssologinn;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.security.cert.CertificateException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.CacheControl;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by user on 6/7/2017.
 */

public class WebServiceHandler {

    private OkHttpClient okHttpClient;
    private OkHttpClient client = new OkHttpClient();
    private RequestBody requestBody;
    private Request request;
    private Context context;
    private static final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");
    private ProgressDialog pDialog;
    public WebServiceListener serviceListener;
    public static final MediaType JSON = MediaType.parse("application/x-www-form-urlencoded; charset=utf-8");



    public WebServiceHandler(Context context) {
        this.context = context;


        try {
            // Create a trust manager that does not validate certificate chains
            final TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        @SuppressLint("TrustAllX509TrustManager")
                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @SuppressLint("TrustAllX509TrustManager")
                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return new java.security.cert.X509Certificate[]{};
                        }
                    }
            };
            // Install the all-trusting trust manager
            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            // Create an ssl socket factory with our all-trusting manager
            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            okHttpClient = new OkHttpClient.Builder()
                    .sslSocketFactory(sslSocketFactory, (X509TrustManager) trustAllCerts[0])
                    .hostnameVerifier(new HostnameVerifier() {
                        @SuppressLint("BadHostnameVerifier")
                        @Override
                        public boolean verify(String hostname, SSLSession session) {
                            return true;
                        }
                    })
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .writeTimeout(30, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .retryOnConnectionFailure(true)
                    .build();

        } catch (Exception e) {
            e.printStackTrace();
        }




//        okHttpClient = new OkHttpClient.Builder()
//                .connectTimeout(30, TimeUnit.SECONDS)
//                .writeTimeout(30, TimeUnit.SECONDS)
//                .readTimeout(30, TimeUnit.SECONDS)
//                .addInterceptor(new LoggingInterceptor())
//                .build();
        try {
            pDialog = new ProgressDialog(context);
            pDialog.setMessage("please wait..");
            // pDialog = new ProgressDialog (context,SweetAlertDialog.PROGRESS_TYPE);
            pDialog.setCancelable(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void  submitdetails(String userid,String name,String mobile,String email,String district,String tehsilname){
       // Log.e("H1N1",userid+" " +name+" "+mobile+" "+email+" "+district+" "+tehsilname);
        RequestBody formBody = new FormBody.Builder()
                .addEncoded("UserId", userid)
                .addEncoded("FullName", name)
                .addEncoded("Mobile", mobile)
                .addEncoded("Email", email)
                .addEncoded("DistrictId", district)
                .addEncoded("TehsilId", tehsilname)
                .build();
     //   Log.e("UpdateUserProfile_Req", String.valueOf(formBody));
        pDialog.show();
        request = new Request.Builder()
                .url(UpdateUserProfile)
                .post(formBody)
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (pDialog.isShowing())
                    pDialog.dismiss();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (pDialog.isShowing())
                    pDialog.dismiss();
                try {
                //    Log.e("UpdateUserProfile_Resp", String.valueOf(formBody));
                    serviceListener.onResponse(response.body().string());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    public void  getMasterdata(String tabliid){
        RequestBody formBody = new FormBody.Builder()
                .addEncoded("TableId", tabliid)
                .build();
        pDialog.show();
        request = new Request.Builder()
                .url(GetMasterData)
                .post(formBody)
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (pDialog.isShowing())
                    pDialog.dismiss();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (pDialog.isShowing())
                    pDialog.dismiss();
                try {
                    serviceListener.onResponse(response.body().string());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    public void  getMasterdatateh(String tableid,String distid){
        RequestBody formBody = new FormBody.Builder()
                .addEncoded("TableId", tableid)
                .addEncoded("District_ID", distid)

                .build();
        pDialog.show();
        request = new Request.Builder()
                .url(GetMasterData)
                .post(formBody)
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (pDialog.isShowing())
                    pDialog.dismiss();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (pDialog.isShowing())
                    pDialog.dismiss();
                try {
                    serviceListener.onResponse(response.body().string());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    public void getotprajsewadwar(String message,String mobilenum) throws JSONException {
        //   Log.e("mobilenum",mobilenum);
        String b = "[" + mobilenum + "]";
        JSONArray jsonArray1 = new JSONArray(b);

        //pDialog.show();
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        HttpUrl.Builder urlBuilder = HttpUrl.parse(getRajOTp).newBuilder();
        JSONObject params = new JSONObject();
        params.put("UniqueID", "UIDAI_OTP");
        params.put("serviceName", "aadhaar new portal");
        params.put("language", "ENG");
        params.put("message", message);
        params.put("mobileNo", jsonArray1);

      //  Log.e("requestamit", String.valueOf(params));
//        JSONObject params = new JSONObject();
//        try {
//            params.put("UniqueID", serviceName.getUniqueID());
//            params.put("serviceName", serviceName.getServiceName());
//            params.put("language", serviceName.getLanguage());
//            params.put("message", serviceName.getMessage());
//            params.put("mobileNo",serviceName.getMobilenum());
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }

        //    Log.e("json", String.valueOf(params));
        RequestBody body = RequestBody.create(JSON, params.toString());
        String url = urlBuilder.build().toString();
        //urlBuilder.addQueryParameter("SSOID", SSOID);
        // String url = urlBuilder.build().toString();
        //   Log.e("logONs",SSOID);
        request = new Request.Builder()
                .url(url)
                .addHeader("username","UidaiOtp")
                .addHeader("password","ZktGr67@9_0k")
                .post(body)
                .build();
        //   Log.e("URL",url);
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
//                if (pDialog.isShowing())
//                    pDialog.dismiss();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
//                if (pDialog.isShowing()) {
//                    pDialog.dismiss();
                try {
                    serviceListener.onResponse(response.body().string());

                } catch (JSONException e) {
                    e.printStackTrace();
                    //   }
                }
            }
        });
    }

    public void getssologindetails(String SSOID,String tokan) {
      //  Log.e("loginss",SSOID);
        //pDialog.show();
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        HttpUrl.Builder urlBuilder = HttpUrl.parse(SSOAuthentication).newBuilder();
        Map<String, String> params = new HashMap<String, String>();
        params.put("SSOID", SSOID);
        JSONObject jsonObject = new JSONObject(params);
        RequestBody body = RequestBody.create(JSON, jsonObject.toString());
        String url = urlBuilder.build().toString();
        //urlBuilder.addQueryParameter("SSOID", SSOID);
        // String url = urlBuilder.build().toString();
        //   Log.e("logONs",SSOID);
        request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        //   Log.e("URL",url);

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
//                if (pDialog.isShowing())
//                    pDialog.dismiss();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
//                if (pDialog.isShowing()) {
//                    pDialog.dismiss();
                try {
                    serviceListener.onResponse(response.body().string());

                } catch (JSONException e) {
                    e.printStackTrace();
                    //   }
                }
            }
        });
    }


    public void gettokan1(String username,String Password,String granttype){
          // Log.e("imputparms",username+" "+Password+" "+granttype);
        pDialog.show();
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        HttpUrl.Builder urlBuilder = HttpUrl.parse(genratetokan).newBuilder();
        RequestBody body1 = RequestBody.create(JSON, "&Username="+username+"&Password="+Password+"&grant_type="+granttype);
        String url = urlBuilder.build().toString();
        request = new Request.Builder()
                .url(url)
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .post(body1)
                .build();
        //       Log.e("URL",body1.toString());
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (pDialog.isShowing())
                    pDialog.dismiss();
                Log.e("error","urlConnectionError");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (pDialog.isShowing()) {
                    pDialog.dismiss();

                    try {
                       // Log.e("sending", "urlConnectionError");
                        serviceListener.onResponse(response.body().string());

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public void getdashboarddoc(String UserId ,String SelectYear,String tokan,String usertype){
        // Log.e("Vendorsss",UserId+" "+SelectYear+" "+usertype);
        pDialog.show();
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        HttpUrl.Builder urlBuilder = HttpUrl.parse(GetDashBoardData).newBuilder();
        Map<String, String> params = new HashMap<String, String>();
        params.put("year", SelectYear);
        params.put("UserType", usertype);
        params.put("SSOID", UserId);
       // Log.e("parms001",params.toString());
        JSONObject jsonObject = new JSONObject(params);
        RequestBody body = RequestBody.create(JSON, jsonObject.toString());
        String url = urlBuilder.build().toString();
        request = new Request.Builder()
                .url(url)
                .addHeader("Authorization",tokan)
                .post(body)
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
//                if (pDialog.isShowing())
                pDialog.dismiss();
                Log.e("error","urlConnectionError");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
//                if (pDialog.isShowing())
                pDialog.dismiss();

                try {
                    Log.e("sending","urlConnectionError");
                    serviceListener.onResponse(response.body().string());

                } catch (JSONException e) {
                    pDialog.dismiss();
                    e.printStackTrace();
                }
            }
        });
    }
    public void GetAllTypeDocument(String UserId ,String SelectYear,String tokan,String usertype){
        //  Log.e("AllDOC",UserId+" "+SelectYear+" "+usertype);
        pDialog.show();
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        HttpUrl.Builder urlBuilder = HttpUrl.parse(GetAllTypeDocument).newBuilder();
        Map<String, String> params = new HashMap<String, String>();
        params.put("year",SelectYear);
        params.put("UserType", usertype);
        params.put("SSOID", UserId);
        JSONObject jsonObject = new JSONObject(params);
        RequestBody body = RequestBody.create(JSON, jsonObject.toString());
        String url = urlBuilder.build().toString();
       // Log.e("params2015", String.valueOf(params));
        request = new Request.Builder()
                .url(url)
//                .cacheControl(CacheControl.FORCE_CACHE)
                .addHeader("Authorization",tokan)
                .post(body)
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (pDialog.isShowing())
                    pDialog.dismiss();
                Log.e("error","urlConnectionError");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (pDialog.isShowing())
                    pDialog.dismiss();

                try {
                    Log.e("sending","urlConnectionError");
                    serviceListener.onResponse(response.body().string());

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    public void GetVerifiedDocument(String UserId ,String SelectYear, String tokan ,String usertype){
     //   Log.e("AllDOC1",UserId+" "+SelectYear+" "+usertype);
        pDialog.show();
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        HttpUrl.Builder urlBuilder = HttpUrl.parse(GetVerifiedDocument).newBuilder();
        Map<String, String> params = new HashMap<String, String>();
        params.put("year", SelectYear);
        params.put("UserType",usertype);
        params.put("SSOID", UserId);
        JSONObject jsonObject = new JSONObject(params);
        RequestBody body = RequestBody.create(JSON, jsonObject.toString());
        String url = urlBuilder.build().toString();
     //   Log.e("parms002", String.valueOf(params));
        request = new Request.Builder()

                .url(url)
                .addHeader("Authorization",tokan)
                .post(body)
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (pDialog.isShowing())
                    pDialog.dismiss();
                Log.e("error","urlConnectionError");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (pDialog.isShowing())
                    pDialog.dismiss();

                try {
                    Log.e("sending","urlConnectionError");
                    serviceListener.onResponse(response.body().string());

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void GetPendingDocument(String UserId ,String SelectYear ,String tokan ,String usertype){
      //  Log.e("AllDOC2",UserId+" "+SelectYear+" "+usertype);
        pDialog.show();
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        HttpUrl.Builder urlBuilder = HttpUrl.parse(GetPendingDocument).newBuilder();
        Map<String, String> params = new HashMap<String, String>();
        params.put("year", SelectYear);
        params.put("UserType", usertype);
        params.put("SSOID", UserId);
        JSONObject jsonObject = new JSONObject(params);
        RequestBody body = RequestBody.create(JSON, jsonObject.toString());
        String url = urlBuilder.build().toString();

        request = new Request.Builder()

                .url(url)
                .addHeader("Authorization",tokan)
                .post(body)
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (pDialog.isShowing())
                    pDialog.dismiss();
                Log.e("error","urlConnectionError");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (pDialog.isShowing())
                    pDialog.dismiss();

                try {
                    Log.e("sending","urlConnectionError");
                    serviceListener.onResponse(response.body().string());

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    public void getAllreports(String VenderId,String UserId, String DocTypeID,String Year){
        pDialog.show();
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        HttpUrl.Builder urlBuilder = HttpUrl.parse(getreport).newBuilder();
        Map<String, String> params = new HashMap<String, String>();
        params.put("DocTypeId", DocTypeID);
        params.put("VendorID",VenderId);
        params.put("UserId",UserId);
        params.put("Year",Year);
        //   Log.e("GETDOCUMENTTYPERULES",userid+" "+doctypeid);
        JSONObject jsonObject = new JSONObject(params);
        RequestBody body = RequestBody.create(JSON, jsonObject.toString());
        String url = urlBuilder.build().toString();

      //  Log.e("parms003", String.valueOf(params));
        request = new Request.Builder()
                .url(url)
                //   .addHeader("Authorization",tokan)
                .post(body)
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (pDialog.isShowing())
                    pDialog.dismiss();
                Log.e("error","urlConnectionError");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (pDialog.isShowing())
                    pDialog.dismiss();

                try {
                    Log.e("sending","urlConnectionError");
                    serviceListener.onResponse(response.body().string());

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    public void getAllreport(String VenderId,String UserId, String mode){
        pDialog.show();
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        HttpUrl.Builder urlBuilder = HttpUrl.parse(getallreport).newBuilder();
        Map<String, String> params = new HashMap<String, String>();
        params.put("VendorID", VenderId);
        params.put("UserId",UserId);
        params.put("Mode",mode);
        //   Log.e("GETDOCUMENTTYPERULES",userid+" "+doctypeid);
        JSONObject jsonObject = new JSONObject(params);
        RequestBody body = RequestBody.create(JSON, jsonObject.toString());
        String url = urlBuilder.build().toString();

        request = new Request.Builder()
                .url(url)
                //   .addHeader("Authorization",tokan)
                .post(body)
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (pDialog.isShowing())
                    pDialog.dismiss();
                Log.e("error","urlConnectionError");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (pDialog.isShowing())
                    pDialog.dismiss();

                try {
                    Log.e("sending","urlConnectionError");
                    serviceListener.onResponse(response.body().string());

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void GetRejectedDocument(String UserId ,String SelectYear, String tokan ,String usertype){
       // Log.e("AllDOC3",UserId+" "+SelectYear+" "+usertype);
        pDialog.show();
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        HttpUrl.Builder urlBuilder = HttpUrl.parse(GetRejectedDocument).newBuilder();
        Map<String, String> params = new HashMap<String, String>();
        params.put("year", SelectYear);
        params.put("UserType", usertype);
        params.put("SSOID", UserId);
        JSONObject jsonObject = new JSONObject(params);
        RequestBody body = RequestBody.create(JSON, jsonObject.toString());
        String url = urlBuilder.build().toString();

        request = new Request.Builder()

                .url(url)
                .addHeader("Authorization",tokan)
                .post(body)
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (pDialog.isShowing())
                    pDialog.dismiss();
                Log.e("error","urlConnectionError");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (pDialog.isShowing())
                    pDialog.dismiss();

                try {
                    Log.e("sending","urlConnectionError");
                    serviceListener.onResponse(response.body().string());

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    //
    public void getdocument_type(String UserId,String tokan){
        //     Log.e("tokenss",tokan);
        pDialog.show();
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        HttpUrl.Builder urlBuilder = HttpUrl.parse(GetDocumentType).newBuilder();
        Map<String, String> params = new HashMap<String, String>();
        params.put("UserId", UserId);
        JSONObject jsonObject = new JSONObject(params);
        RequestBody body = RequestBody.create(JSON, jsonObject.toString());
        String url = urlBuilder.build().toString();

      //  Log.e("parms004", String.valueOf(params));
        request = new Request.Builder()

                .url(url)
                .addHeader("Authorization",tokan)
                .post(body)
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (pDialog.isShowing())
                    pDialog.dismiss();
                Log.e("error","urlConnectionError");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (pDialog.isShowing())
                    pDialog.dismiss();

                try {
                    Log.e("sending","urlConnectionError");
                    serviceListener.onResponse(response.body().string());

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void getuserprofile(String tokan , String ssoID,String staticRoleID){
        //  pDialog.show();
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        HttpUrl.Builder urlBuilder = HttpUrl.parse(GetUserProfile).newBuilder();
        Map<String, String> params = new HashMap<String, String>();
        params.put("SSOID", ssoID);
        params.put("RoleID",staticRoleID);
        JSONObject jsonObject = new JSONObject(params);
        RequestBody body = RequestBody.create(JSON, jsonObject.toString());
        String url = urlBuilder.build().toString();
      //  Log.e("parms005", String.valueOf(params));
        request = new Request.Builder()

                .url(url)
                //   .addHeader("Authorization",tokan)
                .post(body)
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
//                if (pDialog.isShowing())
//                    pDialog.dismiss();
                Log.e("error","urlConnectionError");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
//                if (pDialog.isShowing())
//                    pDialog.dismiss();

                try {
                    Log.e("sending","urlConnectionError");
                    serviceListener.onResponse(response.body().string());

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    public void FailureCountMobileAPI(String email,String mode){
//        OkHttpClient.Builder builder = new OkHttpClient.Builder();
//        try {
//            // Create a trust manager that does not validate certificate chains
//            final TrustManager[] trustAllCerts = new TrustManager[]{
//                    new X509TrustManager() {
//                        @SuppressLint("TrustAllX509TrustManager")
//                        @Override
//                        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
//                        }
//
//                        @SuppressLint("TrustAllX509TrustManager")
//                        @Override
//                        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
//                        }
//
//                        @Override
//                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
//                            return new java.security.cert.X509Certificate[]{};
//                        }
//                    }
//            };
//            // Install the all-trusting trust manager
//            final SSLContext sslContext = SSLContext.getInstance("SSL");
//            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
//            // Create an ssl socket factory with our all-trusting manager
//            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
//
//            okHttpClient = new OkHttpClient.Builder()
//                    .sslSocketFactory(sslSocketFactory, (X509TrustManager) trustAllCerts[0])
//                    .hostnameVerifier(new HostnameVerifier() {
//                @SuppressLint("BadHostnameVerifier")
//                @Override
//                public boolean verify(String hostname, SSLSession session) {
//                    return true;
//                }
//            })
//                    .connectTimeout(30, TimeUnit.SECONDS)
//                    .writeTimeout(30, TimeUnit.SECONDS)
//                    .readTimeout(30, TimeUnit.SECONDS)
//                    .retryOnConnectionFailure(true)
//                    .build();
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        RequestBody formBody = new FormBody.Builder()
                .addEncoded("SSOID", email)
                .addEncoded("Mode", mode)
                .build();

        pDialog.show();
        request = new Request.Builder()
                .url(FailureCountMobileAPI)
                .post(formBody)
                .build();

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (pDialog.isShowing())
                    pDialog.dismiss();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (pDialog.isShowing())
                    pDialog.dismiss();
                try {
                    serviceListener.onResponse(response.body().string());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void  Ssologinfunction(String email,String password){
        //   Log.e("janaadhaarnumber",janaadhaarnumber);
        RequestBody formBody = new FormBody.Builder()
                .addEncoded("SSOID", email)
                .addEncoded("Password", password)
                .build();

        pDialog.show();
        request = new Request.Builder()
                .url(getssologinn)
                .post(formBody)
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (pDialog.isShowing())
                    pDialog.dismiss();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (pDialog.isShowing())
                    pDialog.dismiss();
                try {
                    serviceListener.onResponse(response.body().string());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void  Ssologinfunctiondirect(String email,String password){

        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
//        Map<String, String> params = new HashMap<String, String>();
//        params.put("Application", "RU");
//        params.put("UserName", email);
//        params.put("Password", password);
//        JSONObject jsonObject = new JSONObject(params);
//        RequestBody body = RequestBody.create(JSON, jsonObject.toString());
          // Log.e("password@@",password);
        RequestBody formBody = new FormBody.Builder()
                .add("Application", "RU")
                .add("UserName", email)
                .add("Password", password)
                .build();

        //Log.e("req@@", String.valueOf(formBody));

       // RequestBody body = RequestBody.create(mediaType, "Application=RU&UserName=amit.chodhary007&Password="+password);
        pDialog.show();
        request = new Request.Builder()
                .url(getssologinn)
                .post(formBody)
                .addHeader("content-type", "application/x-www-form-urlencoded")
                .addHeader("cache-control", "no-cache")
                .addHeader("postman-token", "2760b013-440f-e0c5-afb0-c442ee75bf98")
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (pDialog.isShowing())
                    pDialog.dismiss();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (pDialog.isShowing())
                    pDialog.dismiss();
                try {
                    serviceListener.onResponse(response.body().string());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void getRuleid(String VendorID,String DocTypeId){
        pDialog.show();
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        HttpUrl.Builder urlBuilder = HttpUrl.parse(getruleID).newBuilder();
        Map<String, String> params = new HashMap<String, String>();
        params.put("VendorID", VendorID);
        params.put("DocTypeId",DocTypeId);
     //   Log.e("GETDOCUMENTTYPERULES",userid+" "+doctypeid);
        JSONObject jsonObject = new JSONObject(params);
        RequestBody body = RequestBody.create(JSON, jsonObject.toString());
        String url = urlBuilder.build().toString();

        request = new Request.Builder()
                .url(url)
             //   .addHeader("Authorization",tokan)
                .post(body)
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (pDialog.isShowing())
                    pDialog.dismiss();
                Log.e("error","urlConnectionError");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (pDialog.isShowing())
                    pDialog.dismiss();

                try {
                    Log.e("sending","urlConnectionError");
                    serviceListener.onResponse(response.body().string());

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void getRuleList(String userid,String doctypeid,String tokan){
        pDialog.show();
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        HttpUrl.Builder urlBuilder = HttpUrl.parse(GetDocumentTypeRules).newBuilder();
        Map<String, String> params = new HashMap<String, String>();
        params.put("UserId", userid);
        params.put("DocumentTypeID",doctypeid);
       // Log.e("GETDOCUMENTTYPERULES",userid+" "+doctypeid);
        JSONObject jsonObject = new JSONObject(params);
        RequestBody body = RequestBody.create(JSON, jsonObject.toString());
        String url = urlBuilder.build().toString();

        request = new Request.Builder()
                .url(url)
                .addHeader("Authorization",tokan)
                .post(body)
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (pDialog.isShowing())
                    pDialog.dismiss();
                Log.e("error","urlConnectionError");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (pDialog.isShowing())
                    pDialog.dismiss();

                try {
                    Log.e("sending","urlConnectionError");
                    serviceListener.onResponse(response.body().string());

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void getlistdocument(String UserId , String Rulemapid , String rule ,String Tokan){
        //  Log.e("ruleIDS",UserId+"\n"+Rulemapid+"\n"+rule);
        //  Log.e("urls",GetDocumentDetailByRule);
        //  Log.e("tokan",Tokan);
        pDialog.show();
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        HttpUrl.Builder urlBuilder = HttpUrl.parse(GetDocumentDetailByRule).newBuilder();
        Map<String, String> params = new HashMap<String, String>();
        params.put("UserId", UserId);
        params.put("RuleMapperId",Rulemapid);
        params.put("Type",rule);
       // Log.e("parms", String.valueOf(params));
        JSONObject jsonObject = new JSONObject(params);
        RequestBody body = RequestBody.create(JSON, jsonObject.toString());
        String url = urlBuilder.build().toString();

        request = new Request.Builder()
                .url(url)
                .addHeader("Authorization",Tokan)
                .post(body)
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (pDialog.isShowing())
                    pDialog.dismiss();
                Log.e("error","urlConnectionError");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (pDialog.isShowing())
                    pDialog.dismiss();

                try {
                    Log.e("sending","urlConnectionError");
                    serviceListener.onResponse(response.body().string());

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }


        });
    }

    public void viewdocument(String UserId , String Rulemapid , String documentid ,String Tokan){
        //     Log.e("DocViewid",documentid);
        pDialog.show();
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        HttpUrl.Builder urlBuilder = HttpUrl.parse(ViewDocumentByRuleId).newBuilder();
        Map<String, String> params = new HashMap<String, String>();
        params.put("UserId", UserId);
        params.put("RuleMapperId",Rulemapid);
        params.put("DocumentId",documentid);
        JSONObject jsonObject = new JSONObject(params);
        RequestBody body = RequestBody.create(JSON, jsonObject.toString());
        String url = urlBuilder.build().toString();

        request = new Request.Builder()
                .url(url)
                .addHeader("Authorization",Tokan)

                .post(body)
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (pDialog.isShowing())
                    pDialog.dismiss();
                Log.e("error","urlConnectionError");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (pDialog.isShowing())
                    pDialog.dismiss();

                try {
                    Log.e("sending","urlConnectionError");
                    serviceListener.onResponse(response.body().string());

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void downloaddocument(String rulemapperid , String documentid , String Type,String Tokan){
        //    Log.e("IDS",rulemapperid+" "+documentid+" "+Type);
        pDialog.show();
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        HttpUrl.Builder urlBuilder = HttpUrl.parse(DownloadDocument).newBuilder();
        Map<String, String> params = new HashMap<String, String>();
        params.put("RuleMapperId", rulemapperid);
        params.put("DocumentId",documentid);
        params.put("Type",Type);
      //  Log.e("DOWNLOAD_DOCUMENT",rulemapperid+" " +documentid+ " "+ Type);
        JSONObject jsonObject = new JSONObject(params);
        RequestBody body = RequestBody.create(JSON, jsonObject.toString());
        String url = urlBuilder.build().toString();
        request = new Request.Builder()
                .url(url)
                .addHeader("Authorization",Tokan)
                .post(body)
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (pDialog.isShowing())
                    pDialog.dismiss();
                Log.e("error","urlConnectionError");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (pDialog.isShowing())
                    pDialog.dismiss();

                try {
                    Log.e("sending","urlConnectionError");
                    serviceListener.onResponse(response.body().string());

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void getsearchRole(String vendorID,String Tokan){
        //     Log.e("Roles",vendorID+" "+Tokan);
        pDialog.show();
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        HttpUrl.Builder urlBuilder = HttpUrl.parse(GetVendorRules).newBuilder();
        Map<String, String> params = new HashMap<String, String>();
        params.put("VENDORID",vendorID);

        JSONObject jsonObject = new JSONObject(params);
        RequestBody body = RequestBody.create(JSON, jsonObject.toString());
        String url = urlBuilder.build().toString();

        request = new Request.Builder()
                .url(url)
                .addHeader("Authorization",Tokan)
                .post(body)
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (pDialog.isShowing())
                    pDialog.dismiss();
                Log.e("error","urlConnectionError");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (pDialog.isShowing())
                    pDialog.dismiss();

                try {
                    Log.e("sending","urlConnectionError");
                    serviceListener.onResponse(response.body().string());

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void checkonevolte(String venderId,Integer docId,String documentnum,String tokan,String casts){
      //  Log.e("e-volte",venderId+" "+ruleID+" "+documentnum+" "+tokan);
        pDialog.show();
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        HttpUrl.Builder urlBuilder = HttpUrl.parse(GetEvaultData).newBuilder();
        Map<String, String> params = new HashMap<String, String>();
//        params.put("ruleID",ruleID);
        params.put("DocTypeId",String.valueOf(docId));
        params.put("DocumentNumber",documentnum);
        params.put("Token",tokan);
     //   Log.e("checkonevolte",docId +" "+ documentnum+" " + tokan);
   //     Log.e("casts",casts);
        if(casts.equals("null")){

        }
        else { params.put("CasteType",casts);

        }
        JSONObject jsonObject = new JSONObject(params);
        RequestBody body = RequestBody.create(JSON, jsonObject.toString());

    //    Log.e("evaultVerification",jsonObject.toString());
        String url = urlBuilder.build().toString();
        CacheControl cacheControl = new CacheControl.Builder()
                .maxAge(15, TimeUnit.NANOSECONDS)
                .build();

        request = new Request.Builder()
                .url(url)
                .cacheControl(cacheControl)
//                .addHeader("Authorization",tokan)
                .post(body)
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                    pDialog.dismiss();
                Log.e("error","urlConnectionError");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                    pDialog.dismiss();

                try {

                   // Log.e("sending", "urlConnectionError");
                    serviceListener.onResponse(response.body().string());

                } catch (JSONException e) {
                    e.printStackTrace();
                }

//                if(response.code()==200){
//
//                }else
//                    ((Activity)context).runOnUiThread(new Runnable() {
//                        public void run() {
//                            Toast.makeText(context, "Please try again......", Toast.LENGTH_LONG).show();
//                        }
//                    });
            }
        });
    }
    public void checkAadhaarApis(String AadhaarNo,String rdsId,String rdsVer,String dpId,String dc,String mi
                                 ,String mc,String ts,String fType,String iCount,String pCount,
                                 String errCode,String errInfo,String fCount,String nmPoints,String qScore,
                                 String srno,String deviceError,String ci,String SkeyText,
                                 String Hmac,String PIDDataText,String PIDDatatype,String Venderid,String userid,String RuleId ,String Mode,String SSOID){
        pDialog.show();
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        HttpUrl.Builder urlBuilder = HttpUrl.parse(checkAadhaarApis).newBuilder();
        Map<String, String> params = new HashMap<String, String>();
        params.put("AadhaarNo",AadhaarNo);
        params.put("rdsId",rdsId);
        params.put("rdsVer",rdsVer);
        params.put("dpId",dpId);

        params.put("dc",dc);
        params.put("mi",mi);
        params.put("mc",mc);
        params.put("ts",ts);
        params.put("fType",fType);
        params.put("iCount",iCount);
        params.put("pCount",pCount);
        params.put("errCode",errCode);

        params.put("errInfo",errInfo);
        params.put("fCount",fCount);
        params.put("nmPoints",nmPoints);
        params.put("qScore",qScore);
        params.put("srno",srno);
        params.put("deviceError",deviceError);
        params.put("ci",ci);
        params.put("SkeyText",SkeyText);

        params.put("Hmac",Hmac);
        params.put("PIDDataText",PIDDataText);
        params.put("PIDDatatype",PIDDatatype);
        params.put("VendorID",Venderid);
        params.put("UserId",userid);
        params.put("RuleId",RuleId);
        params.put("Mode",Mode );
        params.put("SSOID",SSOID);


        JSONObject jsonObject = new JSONObject(params);
        String param = jsonObject.toString();
       // Log.e("paramssss", param);
        RequestBody body = RequestBody.create(JSON, jsonObject.toString());
        String url = urlBuilder.build().toString();
        CacheControl cacheControl = new CacheControl.Builder()
                .maxAge(15, TimeUnit.NANOSECONDS)
                .build();

        request = new Request.Builder()
                .url(url)
                .cacheControl(cacheControl)
//                .cacheControl(CacheControl.FORCE_CACHE)
//                .addHeader("Authorization",tokan)

                .post(body)
                .build();

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                pDialog.dismiss();
                try {
                    Log.e("error","urlConnectionError");
                    serviceListener.onFailer("urlConnectionError");

                }catch (JSONException er){
                    er.printStackTrace();
                }

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                pDialog.dismiss();
                try {
                 //   Log.e("sending","urlConnectionError");
                    serviceListener.onResponse(response.body().string());

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

//    public void checkOnDatavolte(String venderId,String ruleID,String documentnum,
//                                 String tokan,String casts ,String seldate,String examType,
//                                 String resultType,Integer getDocTypeID,int aadhaarType,String Venderid,String UserId,String RuleId,String Appname,String SSOIDSS,String ExamTypenew){
//      //  Log.e("data-volte",venderId+" "+ruleID+" "+documentnum);
//        pDialog.show();
//        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
//        HttpUrl.Builder urlBuilder = HttpUrl.parse(GetDigitalDataForApi).newBuilder();
//        Map<String, String> params = new HashMap<String, String>();
////        params.put("RuleId",ruleID);
//        params.put("DocTypeId",String.valueOf(getDocTypeID));
//        params.put("DocumentNumber",documentnum);
//        params.put("Token",tokan);
//        params.put("VendorID",Venderid);
//        params.put("UserId",UserId);
//        params.put("RuleId",RuleId);
//        params.put("Mode",Appname );
//        params.put("SSOID",SSOIDSS);
//       // Log.e("GetDigitalDataForApi", String.valueOf(params));
//
//
//        if(getDocTypeID.equals(ConstantDocumentID.BIRTH_CERTIFICATE)){
//            params.put("Date",seldate);
//          //  Log.e("BIRTH_CERTIFICATE",ruleID+" " +documentnum+" "+ seldate);
//        }
//        else if(getDocTypeID.equals(ConstantDocumentID.MARRIAGE_CERTIFICATE)){
//            params.put("Date",seldate);
//
//        }
//        else if(getDocTypeID.equals(ConstantDocumentID.AADHAR_CARD)){
//            if(aadhaarType==1){
//                params.put("FullName",examType);
//                params.put("Gender",resultType);
//                params.put("Date",seldate);
//                params.put("AadhaarType",String.valueOf(aadhaarType));
//               // Log.e("AADHAR_CARD",ruleID+" " +documentnum+" "+ seldate+" "+ examType+" "+resultType +" "+aadhaarType);
//            }
//            else {
//                params.put("AadhaarType",String.valueOf(aadhaarType));
//              //  Log.e("AADHAR_CARD",ruleID+" " +documentnum+" "+aadhaarType);
//            }
//
//        }
//        else if(getDocTypeID.equals(ConstantDocumentID.DEATH_CERTIFICATE)){
//            params.put("Date",seldate);
//           // Log.e("DEATH_CERTIFICATE",ruleID+" " +documentnum+" "+ seldate);
//
//        }
//        else if(getDocTypeID.equals(ConstantDocumentID.CASTE_CERTIFICATE)){
//            params.put("DocTypeId",seldate);
//            //params.put("CasteType",seldate);
//           // Log.e("CASTE_CERTIFICATE",ruleID+" " +documentnum+" "+ seldate );
//
//        }
//        else if(getDocTypeID.equals(ConstantDocumentID.ARMS_LICENSE)){
//            params.put("DeviceNumber",seldate);
//          //  Log.e("ARMS_LICENSE",ruleID+" " +documentnum+" "+ seldate);
//
//
//        }
//        else if(getDocTypeID.equals(ConstantDocumentID.POLICE_CHARACTER_CERTIFICATE)){
//            params.put("FirstName",seldate);
//            params.put("LastName",examType);
//            params.put("FatherName",resultType);
//           // Log.e("POLICE_CHARACTER",ruleID+" " +documentnum+" "+ seldate+" "+ examType+" "+resultType );
//        }
//        else if(getDocTypeID.equals(ConstantDocumentID.DL_CERTIFICATE)){
//            params.put("Date",seldate);
//           // Log.e("DL_CERTIFICATE",ruleID+" " +documentnum+" "+ seldate );
//        }
//        else if(getDocTypeID.equals(ConstantDocumentID.EDUCATION_MARKSHEET)){
//            params.put("DocTypeId",examType);
//            params.put("Date",seldate);
//            params.put("ExamType",ExamTypenew);
//            params.put("ResultType",resultType);
//            if(examType.equals("29")){
//                params.put("IsPdf","false");
//            }
//            else if(examType.equals("30")){
//                params.put("IsPdf","false");
//            }
//            else {
//                params.put("IsPdf","true");
//            }
//
//           // Log.e("EDUCATION_MARKSHEET",ruleID+" " +documentnum+" "+ seldate+" "+ examType+" "+resultType+" "+ExamTypenew );
//        }
//        else if(getDocTypeID.equals(ConstantDocumentID.MEDICAL_DIARY)){
//            params.put("FirstName",seldate);
//            params.put("LastName",examType);
//          //  Log.e("MEDICAL_DIARY",ruleID+" " +documentnum+" "+ seldate+" "+ examType+" "+resultType );
//        }
//        else {
//            params.put("Date",seldate);
////            params.put("ExamType",examType);
////            params.put("ResultType",resultType);
////            params.put("CasteType",seldate);
//
//        }
//
//        JSONObject jsonObject = new JSONObject(params);
//        String param = jsonObject.toString();
//       // Log.e("param", param);
//        RequestBody body = RequestBody.create(JSON, jsonObject.toString());
//        String url = urlBuilder.build().toString();
//        CacheControl cacheControl = new CacheControl.Builder()
//                .maxAge(15, TimeUnit.NANOSECONDS)
//                .build();
//
//        request = new Request.Builder()
//                .url(url)
//                .cacheControl(cacheControl)
////                .cacheControl(CacheControl.FORCE_CACHE)
////                .addHeader("Authorization",tokan)
//
//                .post(body)
//                .build();
//
//        okHttpClient.newCall(request).enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                pDialog.dismiss();
//                try {
//                  //  Log.e("error","urlConnectionError");
//                    serviceListener.onFailer("urlConnectionError");
//
//                }catch (JSONException er){
//                    er.printStackTrace();
//                }
//
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                    pDialog.dismiss();
//                try {
//                  //  Log.e("sending","urlConnectionError");
//                    serviceListener.onResponse(response.body().string());
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//    }

    private Properties loadPreferences(String prefsFile) {
        InputStream is = null;
        Properties prefs = null;
        try {
            is = context.getAssets().open(prefsFile);
            if (is != null) {
                prefs = new Properties();
                prefs.load(is);
            }
        } catch (IOException ex) {
          //  Log.d(this.toString(), "Error accessing preferences file");
            return null;
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
            } catch (IOException ex) {
             //   Log.d(this.toString(), "Error closing preferences file");
            }
        }
        return prefs;
    }
}

//      Log.e("casts",casts);
//        if(casts.equals("null")){
//        }
//        else { params.put("CasteType",casts); }
//        HttpUrl.Builder urlBuilder = HttpUrl.parse("http://10.70.230.128/DataValidationWEBAPI/MobileApi/GetDigitalDataForApi").newBuilder();
//        Map<String, String> params = new HashMap<String, String>();
//        params.put("RuleId",ruleID);
//        params.put("DocumentNumber",documentnum);
//        params.put("Token",tokan);
//        Log.e("casts",casts);
//        if(casts.equals("null")){
//        }
//        else { params.put("CasteType",casts); }

// Log.e("paem", String.valueOf(params));


