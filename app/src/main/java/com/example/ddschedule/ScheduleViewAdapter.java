package com.example.ddschedule;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.transition.DrawableCrossFadeFactory;
import com.chad.library.adapter.base.BaseSectionQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.example.ddschedule.model.ScheduleHeader;
import com.example.ddschedule.model.ScheduleModel;
import com.example.ddschedule.util.DateUtil;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ScheduleViewAdapter extends BaseSectionQuickAdapter<ScheduleHeader, BaseViewHolder> {

    private Context mContext;

    public ScheduleViewAdapter(Context context, List<ScheduleHeader> scheduleHeader) {
        super(R.layout.fragment_item_header, scheduleHeader);
        setNormalLayout(R.layout.fragment_item);
        mContext = context;
    }

    @Override
    protected void convertHeader(@NotNull BaseViewHolder helper, @NotNull ScheduleHeader s) {
        if (s.getObject() instanceof String) {
            helper.setText(R.id.item_header, (String) s.getObject());
        }
    }

    @Override
    protected void convert(@NotNull BaseViewHolder helper, ScheduleHeader sh) {
        ScheduleModel s = (ScheduleModel) sh.getObject();
        DrawableCrossFadeFactory drawableCrossFadeFactory = new DrawableCrossFadeFactory.Builder(300).setCrossFadeEnabled(true).build();
        Glide.with(mContext)
                .load(s.getThumbnail_url())
                .centerCrop()
                .transition(DrawableTransitionOptions.with(drawableCrossFadeFactory))
                .into((ImageView) helper.getView(R.id.thumbnail));
        helper.setText(R.id.groups_name, s.getGroups_name());
        String dateStr = DateUtil.getDateToString(
                s.getScheduled_start_time(), "MM-dd HH:mm");
        helper.setText(R.id.scheduled_start_time, dateStr);
        helper.setText(R.id.streamer_name, s.getStreamer_name());
        helper.setText(R.id.title, s.getTitle());
    }
}
