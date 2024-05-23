package com.arzhang.mongo.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.arzhang.mongo.database.entity.ImageToDelete
import com.arzhang.mongo.database.entity.ImageToUpload

@Database(
    entities = [ImageToUpload::class, ImageToDelete::class],
    version = 1,
    exportSchema = false,
)
abstract class ImagesDatabase: RoomDatabase() {
    abstract fun imageToUploadDao(): ImageToUploadDao
    abstract fun imageToDeleteDao(): ImageToDeleteDao
}