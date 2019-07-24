package com.hsicen.extension

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnBread.setOnClickListener {
            startActivity(Intent(this, BreadActivity::class.java))
        }

        btnClear.setOnClickListener {
            startActivity(Intent(this, CleanEditTextActivity::class.java))
        }
    }
}
