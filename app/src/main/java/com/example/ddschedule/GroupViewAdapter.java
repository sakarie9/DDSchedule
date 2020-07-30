package com.example.ddschedule;

import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.example.ddschedule.model.GroupModel;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class GroupViewAdapter extends BaseQuickAdapter<GroupModel, BaseViewHolder> {

    private final List<GroupModel> mValues;
    private Context mContext;

    public GroupViewAdapter(Context context,  List<GroupModel> items) {
        super(R.layout.fragment_group_view, items);
        mContext = context;
        mValues = items;
    }

    @Override
    protected void convert(@NotNull BaseViewHolder helper, GroupModel item) {
        String url = item.getTwitter_thumbnail_url();
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
        ((CheckBox)helper.getView(R.id.group_checkbox)).setChecked(item.isSelected());
    }

}