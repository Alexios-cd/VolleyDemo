package com.fwj.volleydemo;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.util.LruCache;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageCache;
import com.android.volley.toolbox.ImageLoader.ImageListener;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

/**
 * Demo描述:
 * 1 利用Volley获取JSON数据
 * 2 利用Volley异步加载图片
 * 1 利用NetworkImageView加载网路图片
 * <p/>
 * 参考资料:
 * 1 http://blog.csdn.net/t12x3456/article/details/9221611
 * 2 http://blog.csdn.net/xyz_lmn/article/details/12063561
 * 3 http://blog.csdn.net/lfdfhl/article/details/12221875
 * Thank you very much
 */
public class TwoActivity extends Activity {
    private ImageView mImageView;
    private NetworkImageView mNetworkImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_two);
        init();
    }

    private void init() {
        mImageView = (ImageView) findViewById(R.id.imageView);
        mNetworkImageView = (NetworkImageView) findViewById(R.id.networkImageView);
        getJSONByVolley();
        loadImageByVolley();
        showImageByNetworkImageView();
    }

    /**
     * 利用Volley获取JSON数据
     */
    private void getJSONByVolley() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String JSONDataUrl = "http://pipes.yahooapis.com/pipes/pipe.run?_id=giWz8Vc33BG6rQEQo_NLYQ&_render=json";
        final ProgressDialog progressDialog = ProgressDialog.show(this, "This is title", "...Loading...");

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                JSONDataUrl,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println("response=" + response);
                        if (progressDialog.isShowing() && progressDialog != null) {
                            progressDialog.dismiss();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError arg0) {
                        System.out.println("sorry,Error");
                    }
                });
        requestQueue.add(jsonObjectRequest);
    }


    /**
     * 利用Volley异步加载图片
     * <p/>
     * 注意方法参数:
     * getImageListener(ImageView view, int defaultImageResId, int errorImageResId)
     * 第一个参数:显示图片的ImageView
     * 第二个参数:默认显示的图片资源
     * 第三个参数:加载错误时显示的图片资源
     */
    private void loadImageByVolley() {
        String imageUrl = "http://avatar.csdn.net/6/6/D/1_lfdfhl.jpg";
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        final LruCache<String, Bitmap> lruCache = new LruCache<String, Bitmap>(20);
        ImageCache imageCache = new ImageCache() {
            @Override
            public void putBitmap(String key, Bitmap value) {
                lruCache.put(key, value);
            }

            @Override
            public Bitmap getBitmap(String key) {
                return lruCache.get(key);
            }
        };
        ImageLoader imageLoader = new ImageLoader(requestQueue, imageCache);
        ImageListener listener = ImageLoader.getImageListener(mImageView, R.mipmap.ic_launcher, R.mipmap.ic_launcher);
        imageLoader.get(imageUrl, listener);
    }

    /**
     * 利用NetworkImageView显示网络图片
     */
    private void showImageByNetworkImageView() {
        String imageUrl = "http://avatar.csdn.net/6/6/D/1_lfdfhl.jpg";
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        final LruCache<String, Bitmap> lruCache = new LruCache<String, Bitmap>(20);
        ImageCache imageCache = new ImageCache() {
            @Override
            public void putBitmap(String key, Bitmap value) {
                lruCache.put(key, value);
            }

            @Override
            public Bitmap getBitmap(String key) {
                return lruCache.get(key);
            }
        };
        ImageLoader imageLoader = new ImageLoader(requestQueue, imageCache);
        mNetworkImageView.setTag("url");
        mNetworkImageView.setImageUrl(imageUrl, imageLoader);
    }

}