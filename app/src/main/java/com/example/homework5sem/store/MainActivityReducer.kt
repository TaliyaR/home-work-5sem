package com.example.homework5sem.store

import com.example.homework5sem.store.MainActivityAction.*

class MainActivityReducer {
    fun reduce(state: MainActivityState, action: MainActivityAction): MainActivityState {
        return when (action) {
            CalculateStarted -> state.copy(isLoading = true)
            CalculateError -> state.copy(isLoading = false)
            is CalculateSuccess -> state.copy(isLoading = false, values = action.values)
            is CalculateWrote -> state.copy(values = null)
        }
    }
}