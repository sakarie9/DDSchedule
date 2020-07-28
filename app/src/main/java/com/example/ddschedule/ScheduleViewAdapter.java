package com.example.ddschedule;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseSectionQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.example.ddschedule.model.ScheduleHeader;
import com.example.ddschedule.model.ScheduleModel;
import com.example.ddschedule.util.DateUtil;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ScheduleViewAdapter extends BaseSectionQuickAdapter<ScheduleHeader, BaseViewHolder> {

    private final List<ScheduleHeader> mValues;
    private Context mContext;

    public ScheduleViewAdapter(Context context, List<ScheduleHeader> scheduleHeader) {
        super(R.layout.fragment_item_header, scheduleHeader);
        setNormalLayout(R.layout.fragment_item);
        mContext = context;
        mValues = scheduleHeader;
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
        Glide.with(mContext)
                .load(s.getThumbnail_url())
                .centerCrop()
                .into((ImageView) helper.getView(R.id.thumbnail));
        helper.setText(R.id.groups_name, s.getGroups_name());
        String dateStr = DateUtil.getDateToString(
                s.getScheduled_start_time(), "MM-dd HH:mm");
        helper.setText(R.id.scheduled_start_time, dateStr);
        helper.setText(R.id.streamer_name, s.getStreamer_name());
        helper.setText(R.id.title, s.getTitle());
    }

    //设置数据的方法
    public void setData(List<ScheduleHeader> list){
        setNewInstance(list);
    }


}
