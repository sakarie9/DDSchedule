package com.sakari.ddschedule;

import android.content.Context;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.sakari.ddschedule.model.GroupModel;
import com.sakari.ddschedule.util.SettingsUtil;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import static com.sakari.ddschedule.GroupViewFragment.mGroups;


public class GroupViewAdapter extends BaseQuickAdapter<GroupModel, BaseViewHolder> implements Filterable {

    private List<GroupModel> mValues;
    private List<GroupModel> mFilterList = new ArrayList<>();
    private Context mContext;
    private Boolean isPicturesEnabled;
    private Boolean isFilter = false;

    public GroupViewAdapter(Context context,  List<GroupModel> items) {
        super(R.layout.fragment_group_view, items);
        mContext = context;
        mValues = items;
        isPicturesEnabled = SettingsUtil.getBoolean(context,"switch_pictures",true);
    }
    //
    public GroupViewAdapter(Context context,  List<GroupModel> items, Boolean isFilter) {
        super(R.layout.fragment_group_view, items);
        mContext = context;
        mValues = items;
        isPicturesEnabled = SettingsUtil.getBoolean(context,"switch_pictures",true);
        this.isFilter = isFilter;
    }

    @Override
    protected void convert(@NotNull BaseViewHolder helper, GroupModel item) {
        String url = item.getTwitter_thumbnail_url();
        if(isPicturesEnabled) {
            RequestOptions options = new RequestOptions()
                    .centerCrop()
                    .placeholder(R.drawable.default_avatar)
                    .error(R.drawable.default_avatar)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .priority(Priority.HIGH);
            Glide.with(mContext)
                    .load(url)
                    .apply(options)
                    .circleCrop()
                    .into((ImageView) helper.getView(R.id.group_avatar));
        } else {
            helper.getView(R.id.group_avatar).setVisibility(View.GONE);
        }

        if(isFilter) {
            helper.getView(R.id.group_checkbox).setVisibility(View.GONE);
        }

        helper.setText(R.id.group_name, item.getName());
        ((CheckBox)helper.getView(R.id.group_checkbox)).setChecked(item.isSelected());
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String charString = constraint.toString();
                if (charString.isEmpty()) {
                    //没有过滤的内容，则使用源数据
                    mFilterList = mValues;
                } else {
                    List<GroupModel> filteredList = new ArrayList<>();
                    for (GroupModel gm : mValues) {
                        //这里根据需求，添加匹配规则
                        if ((gm.getName().toLowerCase()).contains((charString.toLowerCase()))) {
                            filteredList.add(gm);
                        } else if ((gm.getGroup_id().toLowerCase()).contains((charString.toLowerCase()))){
                            filteredList.add(gm);
                        }
                    }

                    mFilterList = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = mFilterList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                List<GroupModel> list = (List<GroupModel>) results.values;
                setDiffNewData(list);
                mGroups = list;
            }
        };
    }
}