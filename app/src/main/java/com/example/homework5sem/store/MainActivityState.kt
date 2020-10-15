package com.example.homework5sem.store

data class MainActivityState(
    val isLoading: Boolean = false,
    val values: Triple<String, String, String>? = null,
    val changeFields: Pair<String, String>? = null
) {
    val showLoading = isLoading
}
