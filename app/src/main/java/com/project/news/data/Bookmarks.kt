package com.project.news.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bookmarks")
data class Bookmark(
    @PrimaryKey(autoGenerate = true) val id : Int,
    @ColumnInfo(name = "title") val title : String,
    @ColumnInfo(name = "url") val url : String,
    @ColumnInfo(name = "urlToImage") val urlToImage : String
)

fun Bookmark.newsToBookmark(item :News) : Bookmark{
    return Bookmark(0,item.title,item.url,item.urlToImage)
}
