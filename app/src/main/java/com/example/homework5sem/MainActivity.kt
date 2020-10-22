package com.example.homework5sem

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var buttonState = State.PRESSED

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button_click.setOnClickListener {
            when (buttonState) {
                State.PRESSED -> showView()
                State.UNPRESSED -> hideView()
            }
        }
    }

    fun showView() {
        buttonState = State.UNPRESSED

        ObjectAnimator.ofFloat(0f, 360f).apply {
            button_rotate.animate().setDuration(500).alpha(1f).scaleX(1f).scaleY(1f)
                .withEndAction {
                    ValueAnimator.ofFloat(0f, 360f).apply {
                        duration = 1000
                        addUpdateListener { animation ->
                            val animatedValue = animation?.animatedValue as Float
                            button_rotate.rotation = animatedValue
                        }
                        start()
                    }
                }
        }
    }

    fun hideView() {
        buttonState = State.PRESSED

        ObjectAnimator.ofFloat(360f, 0f).apply {
            duration = 1000
            addUpdateListener { animation ->
                val animatedValue = animation?.animatedValue as Float
                button_rotate.rotation = animatedValue
                button_rotate.animate().setDuration(500).alpha(1f).scaleX(0f).scaleY(0f)
            }
            start()
        }
    }
}