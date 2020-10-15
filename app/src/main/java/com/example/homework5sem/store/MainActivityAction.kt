package com.example.homework5sem.store

sealed class MainActivityAction {

    class CalculateWrote(val new: Pair<String, String>): MainActivityAction()

    class CalculateSuccess(val values: Triple<String, String, String>): MainActivityAction()

    object CalculateStarted: MainActivityAction()

    object CalculateError: MainActivityAction()
}
