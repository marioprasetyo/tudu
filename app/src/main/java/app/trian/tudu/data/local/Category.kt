package app.trian.tudu.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entity Category
 * author Trian Damai
 * created_at 28/01/22 - 10.28
 * site https://trian.app
 */
@Entity(tableName = "tb_category")
data class Category(
    @PrimaryKey
    @ColumnInfo(name = "categoryId")
    var categoryId:String="",
    @ColumnInfo(name = "name")
    var name:String,
    @ColumnInfo(name="color")
    var color:String,
    @ColumnInfo(name="used_count")
    var usedCount:Int=0,
    @ColumnInfo(name = "created_at")
    var created_at:Long,
    @ColumnInfo(name = "updated_at")
    var updated_at:Long,
)
