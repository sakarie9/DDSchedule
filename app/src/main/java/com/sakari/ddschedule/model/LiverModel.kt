package com.sakari.ddschedule.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.text.Collator
import java.util.*

@Entity(tableName = "liver_table")
data class LiverModel(
    var groups: String,
    var name: String,
    @PrimaryKey
    var streamer_id: String,
    var thumbnail_url: String?,
    var isSelected: Boolean // 默认白名单
) : Comparator<LiverModel> {
    constructor(): this("","","","",false)

    override fun compare(o1: LiverModel, o2: LiverModel): Int {
        val num = o2.isSelected.compareTo(o1.isSelected)
        return if (num == 0) {
            Collator.getInstance(Locale.JAPANESE).compare(o1.name, o2.name)
        } else num
    }
}