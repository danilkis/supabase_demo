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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

open class ThingsViewmodel : ViewModel() {
    private val _things = MutableStateFlow<MutableList<Things>>(mutableListOf())

    var things: StateFlow<MutableList<Things>> = _things

    private val _types = MutableStateFlow<MutableList<Type>>(mutableListOf())
    var types: StateFlow<MutableList<Type>> = _types

    private val _boxes = MutableStateFlow<MutableList<Box>>(mutableListOf())

    val boxes: Flow<List<Box>> = flow {
        while (true) {
            delay(500)
            val box = getBoxes()
            emit(box)
            Log.i("BoxFlow1", "REQ")
            deleteComplete.value = false
        }
    }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), emptyList())

    init {
        CoroutineScope(Dispatchers.IO).launch {
            reloadThings()
        }
    }

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

    suspend fun deleteThing(thingId: Int) {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val asyncClient = supaHelper.getAsyncClient()
                asyncClient.postgrest["Things"].delete {
                    eq("id", thingId)
                }
                reloadThings()
            } catch (e: Exception) {
                Log.e("SUPA", e.toString())
            }
        }
    }

    suspend fun insertThing(thing: Things) {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val asyncClient = supaHelper.getAsyncClient()
                val info = asyncClient.postgrest["Things"].select().decodeList<Things>()
                val new_thing =
                    Things(
                        info.last().id + 1,
                        thing.name,
                        thing.store,
                        thing.amount,
                        thing.type,
                        thing.photoUrl,
                        thing.boxId
                    )
                asyncClient.postgrest["Things"].insert(
                    new_thing,
                    returning = Returning.HEADERS_ONLY
                )
                reloadThings()
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
                reloadThings()
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
                return@withContext emptyList()
            }
        } as MutableList<Type>
    }

    suspend fun getBoxes(): MutableList<Box> {
        return withContext(Dispatchers.Main) {
            try {
                var asyncClient = supaHelper.getAsyncClient()
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
                var info = asyncClient.postgrest["Box"].select().decodeList<Box>()
                val new_thing =
                    Box(info.last().id + 1, box.name, box.barcode)
                asyncClient.postgrest["Box"].insert(
                    new_thing,
                    returning = Returning.HEADERS_ONLY
                )
                reloadThings()
            } catch (e: Exception) {
                Log.e("ThingsViewmodel", e.toString())
            }
        }
    }

    suspend fun deleteBox(boxId: Int) {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val asyncClient = supaHelper.getAsyncClient()
                asyncClient.postgrest["Box"].delete {
                    eq("id", boxId)
                }
                reloadThings()
            } catch (e: Exception) {
                Log.e("SUPA", e.toString())
            }
        }
    }

    suspend fun reloadThings() {
        _things.emit(getThings())
        _types.emit(getTypes())
        _boxes.emit(getBoxes())
        deleteComplete.value = false
    }
}