package com.example.digileaf.database

import android.content.Context
import android.util.Log
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.digileaf.R
import com.example.digileaf.entities.Journal
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
            prepopulateJournals(context)
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
                        plantObj.getString("description"), plantObj.getString("imagePath"),
                        plantObj.getString("lastWatered")
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

    suspend fun prepopulateJournals(context: Context) {
        try {
            val journalDao = AppDatabase.getInstance(context).journalDao()

            val journalList: JSONArray =
                context.resources.openRawResource(R.raw.journals).bufferedReader().use {
                    JSONArray(it.readText())
                }

            journalList.takeIf { it.length() > 0 }?.let { list ->
                for (index in 0 until list.length()) {
                    val journalObj = list.getJSONObject(index)
                    Log.e("Digileaf", journalObj.toString())
                    val journalEntity = Journal(
                        journalObj.getString("date"), journalObj.getString("entry"),
                        journalObj.getString("imagePath"), journalObj.getInt("plantId"),
                        journalObj.getString("plantName")
                    )
                    journalDao.insertJournal(
                        journalEntity
                    )

                }
                Log.e("Digileaf", "successfully pre-populated journals into database")
            }
        } catch (exception: Exception) {
            Log.e(
                "Digileaf",
                exception.localizedMessage ?: "failed to pre-populate journals into database"
            )
        }
    }
}