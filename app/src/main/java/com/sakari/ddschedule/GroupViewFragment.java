package com.sakari.ddschedule;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
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

import com.sakari.ddschedule.model.GroupModel;
import com.sakari.ddschedule.util.DiffCallback;

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

    public static List<GroupModel> mGroups = new ArrayList<>();

    //Room Stuff
    private GroupViewModel mGroupViewModel;

    DiffCallback diffCallback = new DiffCallback();


    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public GroupViewFragment() {
    }

//    // TODO: Customize parameter initialization
//    @SuppressWarnings("unused")
//    public static GroupViewFragment newInstance(int columnCount) {
//        GroupViewFragment fragment = new GroupViewFragment();
//        Bundle args = new Bundle();
//        args.putInt(ARG_COLUMN_COUNT, columnCount);
//        fragment.setArguments(args);
//        return fragment;
//    }

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
            mGroupViewModel.getGroups().observe(getViewLifecycleOwner(), groups -> {
                // Update the cached copy of the words in the adapter.
                mGroups = groups;
                mGroupViewAdapter.setList(mGroups);
            });


            // 设置点击事件
            mGroupViewAdapter.setOnItemClickListener((adapter, view1, position) -> {
                // cb点击事件
                CheckBox cb = view1.findViewById(R.id.group_checkbox);
                if (cb.isChecked()) {
                    cb.setChecked(false);
                    //checkState.put(position, false);
                    mGroups.get(position).setSelected(false);
                }else{
                    cb.setChecked(true);
                    //checkState.put(position, true);
                    mGroups.get(position).setSelected(true);
                }
            });
        }
        return view;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.toolbar_save) {
            mGroupViewModel.insertAllGroups(mGroups);
            getActivity().onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.toolbar_menu_group_select, menu);
        SearchView mSearchView = (SearchView) menu.findItem(R.id.toolbar_search).getActionView();
        mSearchView.setMaxWidth(500);
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