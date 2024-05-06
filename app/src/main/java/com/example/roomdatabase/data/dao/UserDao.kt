package com.example.roomdatabase.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.roomdatabase.data.entity.User


@Dao
interface UserDao {
    @Query("SELECT * FROM user")
    fun getAll(): List<User>

    @Insert
    fun insertAll(user: User)

    @Query("SELECT * FROM user WHERE uid = :userId")
    fun getUserById(userId: Int): User

    @Update
    fun update(user: User)

    @Query("DELETE FROM user")
    fun deleteAll()

    @Delete
    fun delete(user: User)
}