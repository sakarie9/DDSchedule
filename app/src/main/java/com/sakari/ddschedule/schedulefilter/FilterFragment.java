package com.sakari.ddschedule.schedulefilter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sakari.ddschedule.GroupViewAdapter;
import com.sakari.ddschedule.GroupViewModel;
import com.sakari.ddschedule.R;
import com.sakari.ddschedule.db.AppDataBase;
import com.sakari.ddschedule.model.GroupModel;

import java.util.ArrayList;
import java.util.List;

public class FilterFragment extends Fragment {

    private static final String ARG_COLUMN_COUNT = "column-count";
    private int mColumnCount = 2;

    GroupViewAdapter mGroupViewAdapter;

    public static List<GroupModel> mGroups = new ArrayList<>();

    public FilterFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
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

            mGroupViewAdapter = new GroupViewAdapter(context, mGroups, true);
            recyclerView.setAdapter(mGroupViewAdapter);

            //Room
            AppDataBase.getDatabase(getContext())
                    .groupDao().getSelectedGroups().observe(getViewLifecycleOwner(), groups -> {
                // Update the cached copy of the words in the adapter.
                mGroups = groups;
                mGroupViewAdapter.setList(mGroups);
            });

            // 设置点击事件
            mGroupViewAdapter.setOnItemClickListener((adapter, view1, position) -> {

            });
        }

        return view;
    }
}
