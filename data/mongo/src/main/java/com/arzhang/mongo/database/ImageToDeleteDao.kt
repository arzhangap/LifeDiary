package com.arzhang.mongo.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.arzhang.mongo.database.entity.ImageToDelete

@Dao
interface ImageToDeleteDao {

    @Query("SELECT * FROM image_to_delete_table ORDER BY id ASC")
    suspend fun getAllImages() : List<ImageToDelete>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addImageToDelete(imagesToDelete: ImageToDelete)

    @Query("DELETE FROM image_to_delete_table WHERE id=:imageId")
    suspend fun cleanUpImage(imageId: Int)

}