package com.sakari.ddschedule;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.PreferenceManager;
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
import android.widget.Toast;

import com.sakari.ddschedule.model.ScheduleHeader;
import com.sakari.ddschedule.model.ScheduleModel;
import com.sakari.ddschedule.network.MainRequest;
import com.sakari.ddschedule.setting.SettingsActivity;
import com.sakari.ddschedule.util.HeaderUtil;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A fragment representing a list of Items.
 */
public class ScheduleViewFragment extends Fragment implements MainRequest.NetDataCallback {

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
                //mScheduleViewModel.insertSchedules((List<ScheduleModel>)msg.obj);
                swipeRefreshLayout.setRefreshing(false);
            } else if (msg.what==2){
                Toast.makeText(getContext(), "更新数据失败，请重试！", Toast.LENGTH_SHORT).show();
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
        swipeRefreshLayout.setOnRefreshListener(() -> {
            if(!swipeRefreshLayout.isRefreshing()){
                swipeRefreshLayout.setRefreshing(true);
            }
            requestData(mSelectedGroupIDs);
        });

        // Set the adapter
        if (view instanceof SwipeRefreshLayout) {
            Context context = view.getContext();
            RecyclerView recyclerView = view.findViewById(R.id.list);
            // requestData(mSelectedGroupIDs);
            recyclerView.setLayoutManager(new GridLayoutManager(context, SPAN_COUNT));
            mScheduleViewAdapter = new ScheduleViewAdapter(context, mHeaders);
            recyclerView.setAdapter(mScheduleViewAdapter);

            // 设置点击事件
            mScheduleViewAdapter.setOnItemClickListener((adapter, view1, position) -> {
                //Toast.makeText(getContext(), "Clicked "+position, Toast.LENGTH_SHORT).show();

                if (!mHeaders.get(position).isHeader()){
                    ScheduleModel scheduleModel = ((ScheduleModel)mHeaders.get(position).getObject());
                    if (scheduleModel.getCh_type() == 1){
                        Uri uri = Uri.parse("https://www.youtube.com/watch?v="+scheduleModel.getVideo_id());
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        startActivity(intent);
                    } else if (scheduleModel.getCh_type() == 2){
                        Uri uri = Uri.parse("https://live.bilibili.com/"+scheduleModel.getVideo_id());
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        startActivity(intent);
                    }

                }
            });

            mScheduleViewModel = new ViewModelProvider(this).get(ScheduleViewModel.class);

            mScheduleViewModel.getSelectedGroupIDs().observe(getViewLifecycleOwner(), strings -> {
                mSelectedGroupIDs = strings;
                requestData(mSelectedGroupIDs);

                //setFilter
                mScheduleViewModel.setGroups(mSelectedGroupIDs);
            });

            mScheduleViewModel.getSchedules().observe(getViewLifecycleOwner(), scheduleModels -> {
                mModels = scheduleModels;
                mHeaders = HeaderUtil.addHeader(mModels);
                mScheduleViewAdapter.setNewInstance(mHeaders);
            });
        }

        return view;
    }

    private void requestData(List<String> groups) {
        MainRequest req = new MainRequest(groups, getContext(), this);
        swipeRefreshLayout.setRefreshing(true);
    }

    @Override
    public void NetCallback() {
        Message msg = Message.obtain();
        msg.what=1;
        mHandler.sendMessage(msg);
    }

    @Override
    public void NetErr(int code,String s) {
        Log.d("TAG", "NetErr: " +code + s);
        Message msg = Message.obtain();
        msg.what=2;
        mHandler.sendMessage(msg);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.toolbar_settings) {
            Intent intent = new Intent(getContext(), SettingsActivity.class);
            getContext().startActivity(intent);
        } else if (item.getItemId() == R.id.toolbar_edit) {
            Intent intent = new Intent(getContext(),GroupSelectActivity.class);
            getContext().startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}