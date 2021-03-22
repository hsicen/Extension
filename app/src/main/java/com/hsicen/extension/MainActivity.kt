package com.hsicen.extension

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.hsicen.extension.databinding.ActivityMainBinding
import com.hsicen.library.extensions.clickThrottle
import com.hsicen.library.extensions.dp2px
import com.hsicen.library.extensions.enlargeSingleClickBounds
import com.hsicen.library.toast.info

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.btnExpand.enlargeSingleClickBounds(binding.root, 40.dp2px)

        binding.btnSmall.clickThrottle {
            info("点击Button")
        }

        binding.btnExpand.clickThrottle {
            info("点击Button")
        }
    }
}
