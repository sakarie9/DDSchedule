package com.example.ddschedule;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
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
        RequestOptions options = new RequestOptions()
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .priority(Priority.HIGH);
        Glide.with(mContext)
                .load(s.getThumbnail_url())
                .error(R.drawable.no_thumbnail)
                .fallback(R.drawable.no_thumbnail)
                .apply(options)
                .centerCrop()
                .transition(DrawableTransitionOptions.with(drawableCrossFadeFactory))
//                .listener(new RequestListener<Drawable>() {
//                    @Override
//                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
//                        //helper.getView(R.id.thumbnail).setVisibility(View.GONE);
//                        return false;
//                    }
//
//                    @Override
//                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
//                        helper.getView(R.id.thumbnail).setVisibility(View.VISIBLE);
//                        return false;
//                    }
//                })
                .into((ImageView) helper.getView(R.id.thumbnail));
        helper.setText(R.id.groups_name, s.getGroups_name());
        String dateStr = DateUtil.getDateToString(
                s.getScheduled_start_time(), "MM-dd HH:mm");
        helper.setText(R.id.streamer_name, s.getStreamer_name());
        helper.setText(R.id.title, s.getTitle());
        if (s.getCh_type() == 2) { //Bili icon
            helper.setImageResource(R.id.source_icon, R.drawable.ic_bilibili);
        } else {
            helper.setImageResource(R.id.source_icon, R.drawable.ic_youtube);
        }
    }
}
