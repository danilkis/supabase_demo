package com.example.supabasedemo.viewmodel.Things

import com.example.supabasedemo.model.Things.Things
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.withContext

class BoxViewmodel : ThingsViewmodel() { //TODO: При удалении перенести все вещи и Unsorted
    private val _filterThings = MutableStateFlow<MutableList<Things>>(mutableListOf())

    var FilteredThings: StateFlow<MutableList<Things>> = _filterThings
    var boxId = 0
    suspend fun getBoxThings(): MutableList<Things> {
        return withContext(Dispatchers.IO) {
            reloadFilteredDevices()
            return@withContext getThings().filter { thing -> thing.boxId == boxId }
        }.toMutableList()
    }

    suspend fun reloadFilteredDevices() {
        _filterThings.emit(getBoxThings())
    }
}