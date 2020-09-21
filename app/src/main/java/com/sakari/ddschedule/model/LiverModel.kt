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
    var isBlocked: Boolean // 默认黑名单
) : Comparator<LiverModel> {
    constructor(): this("","","","",false)

    override fun compare(o1: LiverModel, o2: LiverModel): Int {
        val num = o2.isBlocked.compareTo(o1.isBlocked)
        return if (num == 0) {
            Collator.getInstance(Locale.JAPANESE).compare(o1.name, o2.name)
        } else num
    }
}