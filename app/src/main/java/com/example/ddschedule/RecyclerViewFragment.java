package com.example.ddschedule;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.example.ddschedule.model.ScheduleHeader;
import com.example.ddschedule.model.ScheduleModel;
import com.example.ddschedule.network.NetworkRequest;
import com.example.ddschedule.util.HeaderUtil;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * A fragment representing a list of Items.
 */
public class RecyclerViewFragment extends Fragment implements NetworkRequest.NetDataCallback {

    private static final int SPAN_COUNT = 2;

    private ScheduleViewAdapter mScheduleViewAdapter;

    //private List<ScheduleModel> mList = new ArrayList<>();
    private List<ScheduleHeader> mList = new ArrayList<>();

    //创建 Handler对象，并关联主线程消息队列
    public Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(@NotNull Message msg) {
            super.handleMessage(msg);
            if (msg.what==1){
                // mlist=(ArrayList<NewsData.DataBean>) newsData.getData();
                mList= HeaderUtil.addHeader((List<ScheduleModel>) msg.obj);
                mScheduleViewAdapter.setData(mList);
            }
        }
    };

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public RecyclerViewFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            initData();
            recyclerView.setLayoutManager(new GridLayoutManager(context, SPAN_COUNT));
            mScheduleViewAdapter = new ScheduleViewAdapter(context, mList);
            recyclerView.setAdapter(mScheduleViewAdapter);

            // 设置点击事件
            mScheduleViewAdapter.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(@NonNull BaseQuickAdapter adapter, @NonNull View view, int position) {
                    //Toast.makeText(getContext(), "Clicked "+position, Toast.LENGTH_SHORT).show();
                    if (!mList.get(position).isHeader()){
                        Uri uri = Uri.parse("https://www.youtube.com/watch?v="+((ScheduleModel)mList.get(position).getObject()).getVideo_id());
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        startActivity(intent);
                    }
                }
            });
        }
        return view;
    }
    private void initData() {
        NetworkRequest http=new NetworkRequest();
        http.postData(this);
    }

    @Override
    public void callback(List<ScheduleModel> data) {
        Message msg = Message.obtain();
        msg.what=1;
        msg.obj=data;
        mHandler.sendMessage(msg);
    }

    @Override
    public void err(int code, String s) {

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.toolbar_refresh) {
            initData();
            Toast.makeText(getContext(), "Refresh Complete", Toast.LENGTH_SHORT).show();
            return true;
        } else if (item.getItemId() == R.id.toolbar_edit) {

        }
        return super.onOptionsItemSelected(item);
    }
}