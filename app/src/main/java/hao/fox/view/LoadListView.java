package hao.fox.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;

import hao.fox.R;

/**
 * Created by Administrator on 2016/6/16 0016.
 */
public class LoadListView extends ListView implements OnScrollListener{
    View footer;//底部布局
    int totalItemCount;// 总数量；
    int lastVisibleItem;// 最后一个可见的item；
    boolean isLoading;// 正在加载；
    ILoadListener iLoadListener;

    public LoadListView(Context context) {
        super(context);
//        initView(context);
    }

    public LoadListView(Context context, AttributeSet attrs) {
        super(context, attrs);
//        initView(context);
    }

    public LoadListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
//        initView(context);
    }

    /**
     * 添加底部加载提示布局到ListView
     *
     * @param context
     */
    private void initView(Context context) {
        Log.i("TAG", "initView: ");
        LayoutInflater inflater = LayoutInflater.from(context);
        footer = inflater.inflate(R.layout.footer_layout, null);
        footer.findViewById(R.id.load_layout).setVisibility(View.GONE);
        this.addFooterView(footer);
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        Log.i("TAG", "onScrollStateChanged: "+scrollState);
        if(totalItemCount == lastVisibleItem && scrollState == SCROLL_STATE_IDLE){
            if(!isLoading){
                isLoading = true;
                //加载更多数据
                footer.findViewById(R.id.load_layout).setVisibility(View.VISIBLE);
                iLoadListener.onLoad();
            }

        }
    }

    /**
     * 加载完毕
     */
    public void loadComplete(){
        isLoading = false;
        footer.findViewById(R.id.load_layout).setVisibility( View.GONE);
    }

    public void setInterface(ILoadListener iLoadListener){
        this.iLoadListener = iLoadListener;
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        Log.i("TAG", "onScroll: ");
        this.lastVisibleItem = firstVisibleItem + visibleItemCount;
        this.totalItemCount = totalItemCount;
    }

    //加载更多数据的回调接口
    public interface ILoadListener{
        public void onLoad();
    }
}
