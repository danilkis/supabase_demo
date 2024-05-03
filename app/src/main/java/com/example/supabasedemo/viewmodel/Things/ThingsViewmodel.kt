package com.example.supabasedemo.viewmodel.Things

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.supabasedemo.model.Things.Box
import com.example.supabasedemo.model.Things.Things
import com.example.supabasedemo.model.Things.Type
import com.example.supabasedemo.supabase.supaHelper
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.Returning
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.UUID

open class ThingsViewmodel : ViewModel() {
    val things: Flow<List<Things>> = flow {
        while (true) {
            delay(500)
            val cont = getThings()
            emit(cont)
            Log.i("ThingFlow", "REQ")
            deleteComplete.value = false
        }
    }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(2000L), emptyList())

    val types: Flow<List<Type>> = flow {
        while (true) {
            delay(500)
            val cont = getTypes()
            emit(cont)
            Log.i("TypeFlow", "REQ")
            deleteComplete.value = false
        }
    }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(2000L), emptyList())

    val boxes: Flow<List<Box>> = flow {
        while (true) {
            delay(500)
            val cont = getBoxes()
            emit(cont)
            Log.i("BoxFlow", "REQ")
            deleteComplete.value = false
        }
    }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(2000L), emptyList())

    var deleteComplete = mutableStateOf(false)

    suspend fun getThings(): MutableList<Things> {
        return withContext(Dispatchers.Main) {
            try {
                val asyncClient = supaHelper.getAsyncClient()
                return@withContext asyncClient.postgrest["Things"].select().decodeList<Things>()
            } catch (e: Exception) {
                Log.e("SUPA", e.toString())
                return@withContext emptyList()
            }
        } as MutableList<Things>
    }

    suspend fun deleteThing(thingId: String) {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val asyncClient = supaHelper.getAsyncClient()
                asyncClient.postgrest["Things"].delete {
                    eq("id", thingId)
                }
            } catch (e: Exception) {
                Log.e("SUPA", e.toString())
            }
        }
    }

    suspend fun insertThing(thing: Things) {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val asyncClient = supaHelper.getAsyncClient()
                val new_thing =
                    Things(
                        UUID.randomUUID().toString(),
                        thing.name,
                        thing.store,
                        thing.amount,
                        thing.type,
                        thing.photoUrl,
                        thing.boxId,
                        supaHelper.userUUID
                    )
                asyncClient.postgrest["Things"].insert(
                    new_thing,
                    returning = Returning.HEADERS_ONLY
                )
            } catch (e: Exception) {
                Log.e("SUPA", e.toString())
            }
        }
    }

    suspend fun updateThing(thing: Things) {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val asyncClient = supaHelper.getAsyncClient()

                asyncClient.postgrest["Things"].update(thing)
                {
                    eq("id", thing.id)
                }
            } catch (e: Exception) {
                Log.e("SUPA", e.toString())
            }
        }
    }


    suspend fun getTypes(): MutableList<Type> {
        return withContext(Dispatchers.Main) {
            try {
                var asyncClient = supaHelper.getAsyncClient()
                return@withContext asyncClient.postgrest["Thing_types"].select().decodeList<Type>()
            } catch (e: Exception) {
                Log.e("Types", "Failed to fetch")
                return@withContext emptyList()
            }
        } as MutableList<Type>
    }

    suspend fun getBoxes(): MutableList<Box> {
        return withContext(Dispatchers.Main) {
            try {
                var asyncClient = supaHelper.getAsyncClient()
                Log.e("Boxes", asyncClient.postgrest["Box"].select().decodeList<Box>().toString())
                return@withContext asyncClient.postgrest["Box"].select().decodeList<Box>()
            } catch (e: Exception) {
                return@withContext emptyList()
            }
        } as MutableList<Box>
    }

    suspend fun insertBoxes(box: Box) {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                var asyncClient = supaHelper.getAsyncClient()
                val new_thing =
                    Box(UUID.randomUUID().toString(), box.name, supaHelper.userUUID)
                asyncClient.postgrest["Box"].insert(
                    new_thing,
                    returning = Returning.HEADERS_ONLY
                )
            } catch (e: Exception) {
                Log.e("ThingsViewmodel", e.toString())
            }
        }
    }

    suspend fun deleteBox(boxId: String) {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val asyncClient = supaHelper.getAsyncClient()
                asyncClient.postgrest["Box"].delete {
                    eq("id", boxId)
                }
            } catch (e: Exception) {
                Log.e("SUPA", e.toString())
            }
        }
    }
}