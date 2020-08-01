package com.example.ddschedule;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.example.ddschedule.model.ScheduleHeader;
import com.example.ddschedule.model.ScheduleModel;
import com.example.ddschedule.network.NetworkRequest;
import com.example.ddschedule.util.DateUtil;
import com.example.ddschedule.util.HeaderUtil;
import com.example.ddschedule.util.ListDataUtil;
import com.example.ddschedule.util.SharedPreferencesUtil;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A fragment representing a list of Items.
 */
public class ScheduleViewFragment extends Fragment implements NetworkRequest.NetDataCallback {

    private SwipeRefreshLayout swipeRefreshLayout;

    private static final int SPAN_COUNT = 2;

    private ScheduleViewAdapter mScheduleViewAdapter;

    private List<ScheduleModel> mModels = new ArrayList<>();
    private List<ScheduleHeader> mHeaders = new ArrayList<>();
    private List<String> mSelectedGroupIDs = new ArrayList<>();

    private ScheduleViewModel mScheduleViewModel;

    //创建 Handler对象，并关联主线程消息队列
    public Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(@NotNull Message msg) {
            super.handleMessage(msg);
            if (msg.what==1){
                //List<ScheduleModel> sm = (List<ScheduleModel>)msg.obj;
                //mScheduleViewAdapter.setList(mList);
                mScheduleViewModel.insertSchedules((List<ScheduleModel>)msg.obj);
            }
        }
    };

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ScheduleViewFragment() {
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

        swipeRefreshLayout = view.findViewById(R.id.schedule_swipe_refresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(!swipeRefreshLayout.isRefreshing()){
                    swipeRefreshLayout.setRefreshing(true);
                }
                requestData(mSelectedGroupIDs);
            }
        });

        // Set the adapter
        if (view instanceof SwipeRefreshLayout) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.list);
            requestData(mSelectedGroupIDs);
            recyclerView.setLayoutManager(new GridLayoutManager(context, SPAN_COUNT));
            mScheduleViewAdapter = new ScheduleViewAdapter(context, mHeaders);
            recyclerView.setAdapter(mScheduleViewAdapter);

            // 设置点击事件
            mScheduleViewAdapter.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(@NonNull BaseQuickAdapter adapter, @NonNull View view, int position) {
                    //Toast.makeText(getContext(), "Clicked "+position, Toast.LENGTH_SHORT).show();
                    if (!mHeaders.get(position).isHeader()){
                        Uri uri = Uri.parse("https://www.youtube.com/watch?v="+((ScheduleModel)mHeaders.get(position).getObject()).getVideo_id());
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        startActivity(intent);
                    }
                }
            });

            mScheduleViewModel = new ViewModelProvider(this).get(ScheduleViewModel.class);

            mScheduleViewModel.getSelectedGroupIDs().observe(getViewLifecycleOwner(), new Observer<List<String>>() {
                @Override
                public void onChanged(List<String> strings) {
                    mSelectedGroupIDs = strings;
                    requestData(mSelectedGroupIDs);

                    //Low performance, get nonnull mSelectedGroupIDs
                    mScheduleViewModel.getSchedules(mSelectedGroupIDs).observe(getViewLifecycleOwner(), new Observer<List<ScheduleModel>>() {
                        @Override
                        public void onChanged(List<ScheduleModel> scheduleModels) {
                            mModels = scheduleModels;
                            mHeaders = HeaderUtil.addHeader(mModels);
                            mScheduleViewAdapter.setNewInstance(mHeaders);
                        }
                    });
                }
            });


        }
        return view;
    }
    private void requestData(List<String> groups) {
        //ListDataUtil listDataUtil = new ListDataUtil(getContext());
        //NetworkRequest http=new NetworkRequest(listDataUtil.getDataList());
        NetworkRequest http=new NetworkRequest(groups);
        http.postData(this);
        swipeRefreshLayout.setRefreshing(false);
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
            requestData(mSelectedGroupIDs);
            //Toast.makeText(getContext(), "Refresh Complete", Toast.LENGTH_SHORT).show();
        } else if (item.getItemId() == R.id.toolbar_edit) {
            Intent intent = new Intent(getContext(),GroupSelectActivity.class);
            getContext().startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

//    @Override
//    public void onResume() {
//        super.onResume();
//        Boolean isRefresh = (Boolean) SharedPreferencesUtil.getParam(getContext(), "group_refresh", false);
//        Log.d("TAG", "onResume: "+isRefresh);
//        if (isRefresh) {
//            requestData();
//            SharedPreferencesUtil.setParam(getContext(), "group_refresh", false);
//        }
//    }
}