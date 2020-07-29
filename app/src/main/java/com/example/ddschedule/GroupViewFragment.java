package com.example.ddschedule;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.example.ddschedule.model.GroupModel;
import com.example.ddschedule.util.JsonLocalUtil;

import org.json.JSONException;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

/**
 * A fragment representing a list of Items.
 */
public class GroupViewFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 2;

    GroupViewAdapter mGroupViewAdapter;

    List<GroupModel> mGroups = new ArrayList<>();


    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public GroupViewFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static GroupViewFragment newInstance(int columnCount) {
        GroupViewFragment fragment = new GroupViewFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }

//        for (int i = 0;i<DummyContent.ITEMS.size();i++) {
//            checkState.put(i,false);
//        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_group_view_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }

            //获取社团数据
            try {

                mGroups = JsonLocalUtil.getGroups(context);
                Collator collator = Collator.getInstance(Locale.JAPANESE);
                Collections.sort(mGroups);


            } catch (JSONException e) {
                e.printStackTrace();
            }

            mGroupViewAdapter = new GroupViewAdapter(context, mGroups);
            recyclerView.setAdapter(mGroupViewAdapter);

            // 设置点击事件
            mGroupViewAdapter.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(@NonNull BaseQuickAdapter adapter, @NonNull View view, int position) {
                    // cb点击事件
                    CheckBox cb = view.findViewById(R.id.group_checkbox);
                    if (cb.isChecked()) {
                        cb.setChecked(false);
                        //checkState.put(position, false);
                        mGroups.get(position).setSelected(false);
                    }else{
                        cb.setChecked(true);
                        //checkState.put(position, true);
                        mGroups.get(position).setSelected(true);
                    }
                }
            });
        }
        return view;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.toolbar_save) {
            List<Boolean> boolList = new ArrayList<>();
            boolList.add(true);
            // JDK1.8提供了lambda表达式， 可以从stuList中过滤出符合条件的结果。
            // 定义结果集
            List<GroupModel> result = null;
            result = mGroups.stream()
                    .filter((GroupModel s) -> boolList.contains(s.isSelected()))
                    .collect(Collectors.toList());
            Log.d("TAG", result.toString());
        }
        return super.onOptionsItemSelected(item);
    }
}