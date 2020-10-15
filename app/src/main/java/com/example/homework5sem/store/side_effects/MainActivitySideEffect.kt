package com.example.homework5sem.store.side_effects

import com.example.homework5sem.store.MainActivityAction
import com.example.homework5sem.store.MainActivityState
import com.freeletics.rxredux.SideEffect

typealias MainActivitySideEffect = SideEffect<MainActivityState, MainActivityAction>