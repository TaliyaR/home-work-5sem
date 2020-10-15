package com.example.homework5sem.store.side_effects

import com.example.homework5sem.CalculatorApi
import com.example.homework5sem.store.MainActivityAction
import com.example.homework5sem.store.MainActivityAction.*
import com.example.homework5sem.store.MainActivityNews
import com.example.homework5sem.store.MainActivityState
import com.freeletics.rxredux.StateAccessor
import com.jakewharton.rxrelay2.Relay
import io.reactivex.Observable
import io.reactivex.rxkotlin.ofType

class CalculateSideEffect(
    private val calculatorApi: CalculatorApi,
    private val newsRelay: Relay<MainActivityNews>
) : MainActivitySideEffect {

    override fun invoke(
        actions: Observable<MainActivityAction>,
        state: StateAccessor<MainActivityState>
    ): Observable<out MainActivityAction> {
        return actions.ofType<CalculateWrote>()
            .switchMap { action ->

                calculatorApi.calculate(action.new)
                    .map<MainActivityAction> {
                        CalculateSuccess(
                            Triple(
                                it.first.toString(),
                                it.second.toString(),
                                it.third.toString()
                            )
                        )
                    }
                    .onErrorReturnItem(CalculateError)
                    .toObservable()
                    .startWith(CalculateStarted)
            }
    }
}
