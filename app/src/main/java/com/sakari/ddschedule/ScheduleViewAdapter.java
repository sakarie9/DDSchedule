package com.sakari.ddschedule;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.preference.Preference;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.transition.DrawableCrossFadeFactory;
import com.chad.library.adapter.base.BaseSectionQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.sakari.ddschedule.model.ScheduleHeader;
import com.sakari.ddschedule.model.ScheduleModel;
import com.sakari.ddschedule.util.DateUtil;
import com.sakari.ddschedule.util.SettingsUtil;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ScheduleViewAdapter extends BaseSectionQuickAdapter<ScheduleHeader, BaseViewHolder> {

    private Context mContext;
    private Boolean isPicturesEnabled;

    public ScheduleViewAdapter(Context context, List<ScheduleHeader> scheduleHeader) {
        super(R.layout.fragment_item_header, scheduleHeader);
        setNormalLayout(R.layout.fragment_item);
        mContext = context;
        isPicturesEnabled = SettingsUtil.getBoolean(context,"switch_pictures",true);
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
        RequestOptions options = new RequestOptions()
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .priority(Priority.HIGH);
        // Settings
        if(isPicturesEnabled) {
            Glide.with(mContext)
                    .load(s.getThumbnail_url())
                    .placeholder(R.drawable.no_thumbnail)
                    .error(R.drawable.no_thumbnail)
                    .fallback(R.drawable.no_thumbnail)
                    .apply(options)
                    .centerCrop()
                    .transition(DrawableTransitionOptions.with(drawableCrossFadeFactory))
                    .into((ImageView) helper.getView(R.id.thumbnail));
        } else {
            helper.getView(R.id.thumbnail).setVisibility(View.GONE);
        }
        helper.setText(R.id.groups_name, s.getGroups_name());
        helper.setText(R.id.streamer_name, s.getStreamer_name());
        helper.setText(R.id.title, s.getTitle());
        if (s.getCh_type() == 2) { //Bili icon
            helper.setImageResource(R.id.source_icon, R.drawable.ic_bilibili);
        } else {
            helper.setImageResource(R.id.source_icon, R.drawable.ic_youtube);
        }
    }
}
