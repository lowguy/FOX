package hao.fox;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import hao.fox.adapter.GoodsAdapter;
import hao.fox.bean.GoodsBean;
import hao.fox.view.LoadListView;
import hao.fox.view.LoadListView.ILoadListener ;

/**
 * mall
 */
public class MainActivity extends AppCompatActivity implements ILoadListener {
    private LoadListView mListView;
    private GoodsAdapter adapter;
    private static String URL = "http://101.200.204.155/customer/mall/index?id=11&category_id=2";//接口 返回json数据

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mListView = (LoadListView) findViewById(R.id.lv_main);
        mListView.setInterface(this);
        new NewAsyncTask().execute(URL);
    }

    /**
     * 将url对应的json数据转化为我们所封装的NewsBean
     *
     * @param url
     * @return
     */
    private List<GoodsBean> getJsonData(String url) {
        //实例化数据模型 List<GoodsBean>
        List<GoodsBean> goodsBeanList = new ArrayList<GoodsBean>();
        try {
            String jsonString = readStream(new URL(url).openStream());
            JSONObject jsonObject;
            GoodsBean goodsBean;
            try {
                jsonObject = new JSONObject(jsonString).getJSONObject("data");
                Log.i("TAG", "getJsonData: "+jsonObject);
                JSONArray jsonArray = jsonObject.getJSONArray("goods");
                for (int i = 0; i < jsonArray.length(); i++) {
                    ArrayList<String> lisetta = new ArrayList<String>();
                    JSONArray jArray = (JSONArray) jsonArray.get(i);
                    if (jArray != null) {
                        for (int k = 0; k < jArray.length(); k++) {
                            lisetta.add(jArray.get(k).toString());
                        }
                    }
                    String pic = "http://static.xxj365.com/upload/product/" + lisetta.get(0) + ".JPG";
                    String title = lisetta.get(8);
                    String description = lisetta.get(9);
                    String price = lisetta.get(6);
                    goodsBean = new GoodsBean();
                    goodsBean.newsIconUrl = pic;
                    goodsBean.newsTitle = title;
                    goodsBean.newsContent = description;
                    goodsBean.newsPrice = "￥" + price;
                    goodsBeanList.add(goodsBean);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return goodsBeanList;
    }

    /**
     * 通过is解析网页返回的数据
     *
     * @param is
     * @return
     */
    private String readStream(InputStream is) {

        InputStreamReader isr;
        String result = "";
        try {
            String line = "";
            isr = new InputStreamReader(is, "utf-8");
            BufferedReader br = new BufferedReader(isr);
            while ((line = br.readLine()) != null) {
                result += line;
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    @Override
    public void onLoad() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                String Url2 = "http://101.200.204.155/customer/mall/index?id=11&category_id=1";//接口 返回json数据
                new NewAsyncTask().execute(Url2);
                mListView.loadComplete();
            }
        }, 2000);

    }

    /**
     * public abstract class AsyncTask extends Object
     * AsyncTask enables proper and easy use of the UI thread.
     * An asynchronous task is defined by 3 generic types, called Params, Progress and Result,
     * and 4 steps, called onPreExecute, doInBackground, onProgressUpdate and onPostExecute.
     * 实现网络的异步访问
     */
    class NewAsyncTask extends AsyncTask<String, Void, List<GoodsBean>> {

        @Override
        protected List<GoodsBean> doInBackground(String... params) {
            return getJsonData(params[0]);
        }

        @Override
        protected void onPostExecute(List<GoodsBean> goodsBeen) {
            super.onPostExecute(goodsBeen);
            if(adapter == null){
                adapter = new GoodsAdapter(MainActivity.this, goodsBeen, mListView);
                mListView.setAdapter(adapter);
            }else{
                adapter.onDataChange(goodsBeen);
            }

        }
    }
}
