package com.arzhang.lifediary.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.arzhang.lifediary.data.database.entity.ImageToUpload

@Dao
interface ImagesToUploadDao {

    @Query("SELECT * FROM images_to_upload_table ORDER BY id ASC")
    suspend fun getAllImages(): List<ImageToUpload>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addImagesToUpload(imageToUpload: ImageToUpload)

    @Query("DELETE FROM images_to_upload_table WHERE id=:imageId")
    suspend fun cleanUpImage(imageId: Int)

}