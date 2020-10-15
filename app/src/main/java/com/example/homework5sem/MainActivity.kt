package com.example.homework5sem

import android.os.Bundle
import android.widget.Toast
import android.widget.Toast.LENGTH_LONG
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import com.example.homework5sem.store.MainActivityAction.CalculateWrote
import com.example.homework5sem.store.MainActivityNews
import com.example.homework5sem.store.MainActivityState
import com.example.homework5sem.store.MainActivityStore
import com.example.homework5sem.store.side_effects.CalculateSideEffect
import com.jakewharton.rxrelay2.PublishRelay
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {


    private val store = MainActivityStore(listOf(CalculateSideEffect(CalculatorApi(), PublishRelay.create())), PublishRelay.create())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        store.state
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(::render)
        store.newsRelay
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(::renderNews)

        ed_first.addTextChangedListener{store.actionRelay.onNext(CalculateWrote(Pair("First", it.toString())))}
        ed_second.addTextChangedListener{store.actionRelay.onNext(CalculateWrote(Pair("Second", it.toString())))}
        ed_third.addTextChangedListener{store.actionRelay.onNext(CalculateWrote(Pair("Third", it.toString())))}
    }

    private fun render(state: MainActivityState) {
        state.values?.let {
            if (ed_first.text.toString() != it.first) {
                ed_first.setText(it.first)
            }
            if (ed_second.text.toString() != it.second) {
                ed_second.setText(it.second)
            }
            if (ed_third.text.toString() != it.third) {
                ed_third.setText(it.third)
            }
        }
        progressBar.isVisible = state.showLoading
    }

    private fun renderNews(news: MainActivityNews){
        when(news){
            is MainActivityNews.ShowСalculationResult -> Toast.makeText(this, news.result, LENGTH_LONG).show()
        }
    }
}