package com.sakari.ddschedule.util;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import com.sakari.ddschedule.model.GroupModel;

//https://github.com/CymChad/BaseRecyclerViewAdapterHelper/blob/master/readme/7-Diff.md
public class DiffCallback extends DiffUtil.ItemCallback<GroupModel> {

    /**
     * 判断是否是同一个item
     *
     * @param oldItem New data
     * @param newItem old Data
     * @return
     */
    @Override
    public boolean areItemsTheSame(@NonNull GroupModel oldItem, @NonNull GroupModel newItem) {
        return oldItem.getGroup_id().equals(newItem.getGroup_id());
    }

    /**
     * 当是同一个item时，再判断内容是否发生改变
     *
     * @param oldItem New data
     * @param newItem old Data
     * @return
     */
    @Override
    public boolean areContentsTheSame(@NonNull GroupModel oldItem, @NonNull GroupModel newItem) {
        return oldItem.getGroup_id().equals(newItem.getGroup_id());
    }

    /**
     * 可选实现
     * 如果需要精确修改某一个view中的内容，请实现此方法。
     * 如果不实现此方法，或者返回null，将会直接刷新整个item。
     *
     * @param oldItem Old data
     * @param newItem New data
     * @return Payload info. if return null, the entire item will be refreshed.
     */
    @Override
    public Object getChangePayload(@NonNull GroupModel oldItem, @NonNull GroupModel newItem) {
        return null;
    }
}