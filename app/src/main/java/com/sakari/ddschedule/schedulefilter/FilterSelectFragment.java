package com.sakari.ddschedule.schedulefilter;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
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

import com.sakari.ddschedule.R;
import com.sakari.ddschedule.model.LiverModel;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FilterSelectFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FilterSelectFragment extends Fragment {

    public static final String ARG_PARAM1 = "param1";
    private String mParam1;

    private FilterSelectAdapter mFilterSelectAdapter;
    private List<LiverModel> mLivers = new ArrayList<>();
    private FilterViewModel mFilterViewModel;

    public FilterSelectFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment FilterSelectFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FilterSelectFragment newInstance(String param1) {
        FilterSelectFragment fragment = new FilterSelectFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_filter_view_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager(new GridLayoutManager(context, 2));

            mFilterSelectAdapter = new FilterSelectAdapter(context, mLivers);
            recyclerView.setAdapter(mFilterSelectAdapter);

            //Room
            mFilterViewModel = new ViewModelProvider(this).get(FilterViewModel.class);
            mFilterViewModel.getLivers(mParam1).observe(getViewLifecycleOwner(), liverModels -> {
                mLivers = liverModels;
                mFilterSelectAdapter.setList(mLivers);
            });

            // 设置点击事件
            mFilterSelectAdapter.setOnItemClickListener((adapter, view1, position) -> {
                // cb点击事件
                CheckBox cb = view1.findViewById(R.id.group_checkbox);
                if (cb.isChecked()) {
                    cb.setChecked(false);
                    mLivers.get(position).setBlocked(true);
                }else{
                    cb.setChecked(true);
                    mLivers.get(position).setBlocked(false);
                }
            });
        }
        return view;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.toolbar_menu_liver, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.toolbar_select_all:
                for (LiverModel lm:mLivers)
                    lm.setBlocked(false);
                mFilterSelectAdapter.setList(mLivers);
                break;
            case R.id.toolbar_deselect_all:
                for (LiverModel lm:mLivers)
                    lm.setBlocked(true);
                mFilterSelectAdapter.setList(mLivers);
                break;
            case R.id.toolbar_save:
                mFilterViewModel.insertLivers(mLivers);
                getActivity().onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}