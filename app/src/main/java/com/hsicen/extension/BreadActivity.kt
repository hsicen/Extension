package com.hsicen.extension

import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_bread.*

/**
 * <p>作者：Hsicen  2019/7/24 10:55
 * <p>邮箱：codinghuang@163.com
 * <p>功能：
 * <p>描述：BreadCrumb view test
 */
class BreadActivity : AppCompatActivity() {

    private var index = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bread)

        bread.setData(arrayListOf("根节点", "父节点"))

        btnAdd.setOnClickListener {
            bread.addData("子节点${++index}")
        }

        bread.addOnItemClick { view, index ->
            Toast.makeText(
                this,
                (view as TextView).text.toString() + "  $index",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}
