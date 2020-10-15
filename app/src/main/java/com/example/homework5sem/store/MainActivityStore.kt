package com.example.homework5sem.store

import com.example.homework5sem.store.side_effects.MainActivitySideEffect
import com.freeletics.rxredux.reduxStore
import com.jakewharton.rxrelay2.Relay
import io.reactivex.subjects.PublishSubject

class MainActivityStore(
    sideEffects: List<MainActivitySideEffect>,
    val newsRelay: Relay<MainActivityNews>
) {
    val actionRelay = PublishSubject.create<MainActivityAction>()

    val state = actionRelay.reduxStore(
        MainActivityState(),
        sideEffects,
        MainActivityReducer()::reduce
    )
}
