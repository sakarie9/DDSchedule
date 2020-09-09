package com.sakari.ddschedule.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import java.text.Collator
import java.util.*

@Entity(tableName = "group_table")
data class GroupModel @Ignore constructor(
        @PrimaryKey
        var group_id: String,
        @ColumnInfo(index = true)
        var name: String,
        var name_jpn: String,
        var twitter_id: String?,
        var twitter_thumbnail_url: String?,
        var count: Int,
        var isSelected: Boolean
) : Comparator<GroupModel> {

    constructor() : this("","","","", "",0,false)

    override fun compare(o1: GroupModel, o2: GroupModel): Int {
        val num = o2.isSelected.compareTo(o1.isSelected)
        return if (num == 0) {
            Collator.getInstance(Locale.JAPANESE).compare(o1.name, o2.name)
        } else num
    }
}