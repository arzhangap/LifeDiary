package com.arzhang.lifediary.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.arzhang.lifediary.data.database.entity.ImageToUpload

@Database(
    entities = [ImageToUpload::class],
    version = 2,
    exportSchema = false
)
abstract class ImagesDatabase: RoomDatabase() {
    abstract fun imageToUploadDao(): ImageToUploadDao
}