package com.example.digileaf.database

import android.content.Context
import android.util.Log
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.digileaf.R
import com.example.digileaf.entities.Plant
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONArray

class PrepopulateData(private val context: Context) : RoomDatabase.Callback() {

    override fun onCreate(db: SupportSQLiteDatabase) {
        super.onCreate(db)

        CoroutineScope(Dispatchers.IO).launch {
            prepopulatePlants(context)
        }
    }
    suspend fun prepopulatePlants(context: Context) {
        try {
            val plantDao = AppDatabase.getInstance(context).plantDao()

            val plantList: JSONArray =
                context.resources.openRawResource(R.raw.plants).bufferedReader().use {
                    JSONArray(it.readText())
                }

            plantList.takeIf { it.length() > 0 }?.let { list ->
                for (index in 0 until list.length()) {
                    val plantObj = list.getJSONObject(index)
                    Log.e("Digileaf", plantObj.toString())
                    val plantEntity = Plant(
                        plantObj.getString("name"), plantObj.getString("species"),
                        plantObj.getString("description"), plantObj.getString("imagePath")
                    )
                    plantDao.insertPlant(
                        plantEntity
                    )

                }
                Log.e("Digileaf", "successfully pre-populated plants into database")
            }
        } catch (exception: Exception) {
            Log.e(
                "Digileaf",
                exception.localizedMessage ?: "failed to pre-populate plants into database"
            )
        }
    }
}