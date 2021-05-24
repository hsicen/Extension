package com.hsicen.extension

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.hsicen.extension.databinding.ActivityMainBinding
import com.hsicen.library.extensions.viewBinding

/**
 * 作者：hsicen  5/24/21 23:11
 * 邮箱：codinghuang@163.com
 * 作用：
 * 描述：Extension
 */
class NumberFragment : Fragment() {
    private val binding by viewBinding(ActivityMainBinding::inflate)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return binding.root
    }

}