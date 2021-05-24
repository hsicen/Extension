package com.hsicen.library.extensions

import android.app.Activity
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.viewbinding.ViewBinding
import java.lang.reflect.ParameterizedType
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

/**
 * 作者：hsicen  5/23/21 22:19
 * 邮箱：codinghuang@163.com
 * 作用：
 * 描述：ViewBinding扩展
 */

//扩展
fun <T : ViewBinding> Activity.inflate(inflater: (LayoutInflater) -> T) = lazy {
    inflater(layoutInflater).apply { setContentView(root) }
}

fun <T : ViewBinding> Dialog.inflate(inflater: (LayoutInflater) -> T) = lazy {
    inflater(layoutInflater).apply { setContentView(root) }
}

inline fun <reified T : ViewBinding> Fragment.inflate() = FragmentViewBindingDelegate(T::class.java)

//基类
abstract class BindingActivity<T : ViewBinding> : AppCompatActivity() {
    protected lateinit var binding: T

    @Suppress("UNCHECKED_CAST")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val type = javaClass.genericSuperclass
        if (type is ParameterizedType) {
            val clazz = type.actualTypeArguments[0] as Class<T>
            val method = clazz.getMethod("inflate", LayoutInflater::class.java)
            binding = method.invoke(null, layoutInflater) as T
            setContentView(binding.root)
        }
    }
}

abstract class BindingFragment<T : ViewBinding> : Fragment() {
    private var _binding: T? = null
    protected val binding get() = _binding!!

    @Suppress("UNCHECKED_CAST")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val type = javaClass.genericSuperclass
        val clazz = (type as ParameterizedType).actualTypeArguments[0] as Class<T>
        val method = clazz.getMethod(
            "inflate",
            LayoutInflater::class.java,
            ViewGroup::class.java,
            Boolean::class.java
        )
        _binding = method.invoke(null, layoutInflater, container, false) as T

        this.viewLifecycleOwner.lifecycle.addObserver(object : LifecycleEventObserver {
            override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
                if (event == Lifecycle.Event.ON_DESTROY) _binding = null
            }
        })
        return binding.root
    }
}

class FragmentViewBindingDelegate<T : ViewBinding>(private val clazz: Class<T>) :
    ReadOnlyProperty<Fragment, T> {
    private var binding: T? = null

    @Suppress("UNCHECKED_CAST")
    override fun getValue(thisRef: Fragment, property: KProperty<*>): T {
        if (binding == null) {
            binding =
                clazz.getMethod("bind", View::class.java).invoke(null, thisRef.requireView()) as T
            thisRef.viewLifecycleOwner.lifecycle.addObserver(object : LifecycleEventObserver {
                override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
                    if (event == Lifecycle.Event.ON_DESTROY) binding = null
                }
            })
        }
        return binding!!
    }
}
