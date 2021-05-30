package com.xyoye.common_component.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.xyoye.data_component.bean.FolderBean
import com.xyoye.data_component.entity.VideoEntity

/**
 * Created by xyoye on 2020/7/29.
 */

@Dao
interface VideoDao {
    @Query("SELECT * FROM video")
    suspend fun getAll(): MutableList<VideoEntity>

    @Query("SELECT * FROM video WHERE folder_path = (:folderPath)")
    fun getVideoInFolder(folderPath: String): LiveData<MutableList<VideoEntity>>

    @Query("SELECT * FROM video WHERE folder_path = (:folderPath)")
    suspend fun getVideoInFolderSuspend(folderPath: String): MutableList<VideoEntity>

    //SELECT old.folder,COUNT(*),new.filter
    //FROM file AS old
    //Left JOIN (SELECT folder,filter FROM file WHERE filter = '1' GROUP BY folder) AS new ON new.folder = old.folder
    //GROUP BY old.folder
    @Query("SELECT video.folder_path,COUNT(*) AS file_count, filter_table.filter " +
            "FROM video " +
            "LEFT JOIN ( SELECT folder_path, filter FROM video WHERE filter = (:isFilter) GROUP BY folder_path) AS filter_table " +
            "ON filter_table.folder_path = video.folder_path " +
            "GROUP BY video.folder_path")
    fun getAllFolder(isFilter: Boolean = true): LiveData<MutableList<FolderBean>>

    @Query("SELECT folder_path,COUNT(*) AS file_count,filter FROM video WHERE filter = (:isFilter) GROUP BY folder_path")
    suspend fun getFolderByFilter(isFilter: Boolean = false): MutableList<FolderBean>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg entities: VideoEntity)

    @Query("DELETE FROM video WHERE file_path = (:filePath)")
    suspend fun deleteByPath(filePath: String)

    @Query("DELETE FROM video WHERE extend = (:isExtend)")
    suspend fun deleteExtend(isExtend: Boolean = true)

    @Query("DELETE FROM video")
    suspend fun deleteAll()

    @Query("UPDATE video SET danmu_path = (:danmuPath), danmu_id = (:danmuId) WHERE file_path = (:filePath)")
    suspend fun updateDanmu(filePath: String, danmuPath: String?, danmuId: Int = 0)

    @Query("UPDATE video SET subtitle_path = (:subtitlePath) WHERE file_path = (:filePath)")
    suspend fun updateSubtitle(filePath: String, subtitlePath: String?)

    @Query("UPDATE video SET filter = (:filter) WHERE folder_path = (:folderPath)")
    suspend fun updateFolderFilter(filter: Boolean, folderPath: String)
}