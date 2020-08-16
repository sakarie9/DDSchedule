package com.example.ddschedule;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.SearchView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.example.ddschedule.model.GroupModel;
import com.example.ddschedule.util.DiffCallback;

import java.util.ArrayList;
import java.util.List;

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

    //Room Stuff
    private GroupViewModel mGroupViewModel;

    DiffCallback diffCallback = new DiffCallback();


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

//        Context context = getContext();
//        listDataUtil = new ListDataUtil(context);
//        // 从GroupSelectUtil获取Groups信息
//        GroupSelectUtil gUtil = new GroupSelectUtil(context, mGroups);
//        List<String> list = listDataUtil.getDataList();
//        //Log.d("TAG get", list.toString());
//        mGroups = gUtil.setSelectedGroupIDs(list);


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

            mGroupViewAdapter = new GroupViewAdapter(context, mGroups);
            mGroupViewAdapter.setDiffCallback(diffCallback);
            recyclerView.setAdapter(mGroupViewAdapter);

            //Room
            mGroupViewModel = new ViewModelProvider(this).get(GroupViewModel.class);
            mGroupViewModel.getGroups().observe(getViewLifecycleOwner(), new Observer<List<GroupModel>>() {
                @Override
                public void onChanged(@Nullable final List<GroupModel> groups) {
                    // Update the cached copy of the words in the adapter.
                    mGroups = groups;
                    mGroupViewAdapter.setList(mGroups);
                }
            });


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
            Context context = getContext();
            //GroupSelectUtil gUtil = new GroupSelectUtil(context, mGroups);

            mGroupViewModel.insertAllGroups(mGroups);

//            //保存
//            listDataUtil.setDataList(gUtil.getSelectedGroupsIDs());
//            //Log.d("TAG set", gUtil.getSelectedGroupsIDs().toString());
//            getActivity().onBackPressed();
//            SharedPreferencesUtil.setParam(context, "group_refresh", true);
            getActivity().onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.toolbar_menu_group_select, menu);
        SearchView mSearchView = (SearchView) menu.findItem(R.id.toolbar_search).getActionView();
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                mGroupViewAdapter.getFilter().filter(s);
                return false;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }
}