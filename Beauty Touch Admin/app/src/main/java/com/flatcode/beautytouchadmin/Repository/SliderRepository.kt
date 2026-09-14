package com.flatcode.beautytouchadmin.Repository

import android.net.Uri
import com.flatcode.beautytouchadmin.Unit.DATA
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class SliderRepository @Inject constructor(
    private val database: FirebaseDatabase,
    private val storage: FirebaseStorage
) {

    fun getSliders(): Flow<Map<String, String>> = callbackFlow {
        val reference = database.getReference(DATA.SLIDER_SHOW)
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val map = mutableMapOf<String, String>()
                for (data in snapshot.children) {
                    map[data.key!!] = data.value.toString()
                }
                trySend(map)
            }
            override fun onCancelled(error: DatabaseError) { close(error.toException()) }
        }
        reference.addValueEventListener(listener)
        awaitClose { reference.removeEventListener(listener) }
    }

    suspend fun uploadSliderImage(name: String, imageUri: Uri, extension: String): String {
        val filePathAndName = "Images/ImageLinks/$name.$extension"
        val reference = storage.getReference(filePathAndName)
        val uploadTask = reference.putFile(imageUri).await()
        return uploadTask.storage.downloadUrl.await().toString()
    }

    suspend fun updateSlider(data: Map<String, Any?>) {
        database.getReference(DATA.SLIDER_SHOW).updateChildren(data).await()
    }
}
