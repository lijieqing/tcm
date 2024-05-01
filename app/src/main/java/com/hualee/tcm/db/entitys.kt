package com.hualee.tcm.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "herbs")
data class HerbEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    /**
     * 名称
     */
    @ColumnInfo(name = "name") val name: String,
    /**
     * 类别
     */
    @ColumnInfo(name = "type") val type: String,
    /**
     * 子类别
     */
    @ColumnInfo(name = "sub_type") val subType: String,
    /**
     * 来源
     */
    @ColumnInfo(name = "source") val source: String,
    /**
     * 图像列表: ["", ""]
     */
    @ColumnInfo(name = "image_list") val imageList: String,
    /**
     * 性味归经
     */
    @ColumnInfo(name = "property") val property: String,
    /**
     * 功效
     */
    @ColumnInfo(name = "effect") val effect: String,
    /**
     * 主治列表: ["", ""]
     */
    @ColumnInfo(name = "indications") val indications: String,
    /**
     * 性能特点
     */
    @ColumnInfo(name = "feature") val feature: String,
    /**
     * 配伍: [ {"herb_name": "", "effect": ""} ]
     */
    @ColumnInfo(name = "combination") val combination: String,
)


@Entity(tableName = "version")
data class VersionEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    @ColumnInfo(name = "version") val version: Long,
)