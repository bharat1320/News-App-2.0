package com.project.news.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bookmarks")
data class Bookmark(
    @PrimaryKey val url: String,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "urlToImage") val urlToImage: String
)

fun Bookmark.newsToBookmark(item :News) : Bookmark{
    return Bookmark(item.url, item.title, item.urlToImage)
}
