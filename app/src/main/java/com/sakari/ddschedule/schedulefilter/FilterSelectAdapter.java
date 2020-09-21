package com.sakari.ddschedule.schedulefilter;

import android.content.Context;
import android.widget.CheckBox;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.sakari.ddschedule.R;
import com.sakari.ddschedule.model.GroupModel;
import com.sakari.ddschedule.model.LiverModel;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class FilterSelectAdapter extends BaseQuickAdapter<LiverModel, BaseViewHolder> {

    private List<LiverModel> mValues;
    private Context mContext;

    public FilterSelectAdapter(Context context, @Nullable List<LiverModel> data) {
        super(R.layout.fragment_group_view, data);
        mContext = context;
        mValues = data;
    }

    @Override
    protected void convert(@NotNull BaseViewHolder helper, LiverModel item) {
        String url = item.getThumbnail_url();
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

        helper.setText(R.id.group_name, item.getName());
        ((CheckBox)helper.getView(R.id.group_checkbox)).setChecked(!item.isBlocked());
    }
}
