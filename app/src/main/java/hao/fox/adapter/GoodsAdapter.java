package hao.fox.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import hao.fox.R;
import hao.fox.bean.GoodsBean;
import hao.fox.loader.ImageLoader;

/**
 * Created by Administrator on 2016/6/7 0007.
 */
public class GoodsAdapter extends BaseAdapter implements OnScrollListener{
    private List<GoodsBean> mList;
    private LayoutInflater mInflater;
    private ImageLoader mImageLoader = null;
    private int mStart,mEnd;
    public static String[] URLS;
    private Boolean mFirstIn;

    public GoodsAdapter(Context context, List<GoodsBean> mList) {
        this.mInflater = LayoutInflater.from(context);
        this.mList = mList;
        mImageLoader = new ImageLoader();
        URLS = new String[mList.size()];
        for(int i=0;i<mList.size();i++){
            URLS[i] = mList.get(i).newsIconUrl;
        }
    }
    public GoodsAdapter(Context context, List<GoodsBean> mList, ListView listView) {
        this.mInflater = LayoutInflater.from(context);
        this.mList = mList;
        mImageLoader = new ImageLoader(listView);
        URLS = new String[mList.size()];
        for(int i=0;i<mList.size();i++){
            URLS[i] = mList.get(i).newsIconUrl;
        }
        mFirstIn = true;
        listView.setOnScrollListener(this);
    }

    public void onDataChange(List<GoodsBean> goodsBeen){
        this.mList = goodsBeen;
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_layout, null);
            viewHolder = new ViewHolder();
            viewHolder.ivIcon = (ImageView) convertView.findViewById(R.id.iv_icon);
            viewHolder.tvTitle = (TextView) convertView.findViewById(R.id.tv_title);
            viewHolder.tvContent = (TextView) convertView.findViewById(R.id.tv_content);
            viewHolder.tvPrice = (TextView) convertView.findViewById(R.id.tv_price);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        String url = mList.get(position).newsIconUrl;
        viewHolder.ivIcon.setTag(url);
        mImageLoader.showImageByAsyncTask(viewHolder.ivIcon, url);
        viewHolder.tvTitle.setText(mList.get(position).newsTitle);
        viewHolder.tvContent.setText(mList.get(position).newsContent);
        viewHolder.tvPrice.setText(mList.get(position).newsPrice);
        return convertView;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if(scrollState == SCROLL_STATE_IDLE){
            //加载可见项
            mImageLoader.loadImages(mStart,mEnd);
        }else{
            //停止任务
            mImageLoader.cancelAllTasks();
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        mStart = firstVisibleItem;
        mEnd = firstVisibleItem + visibleItemCount;
        if(mFirstIn && visibleItemCount > 0){
            mImageLoader.loadImages(mStart,mEnd);
            mFirstIn = false;
        }
    }

    class ViewHolder {
        public TextView tvTitle, tvContent,tvPrice;
        public ImageView ivIcon;
    }
}
