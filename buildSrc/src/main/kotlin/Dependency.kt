/**
 * 作者：hsicen  3/20/21 2:47 PM
 * 邮箱：codinghuang@163.com
 * 功能：
 * 描述：第三方SDK和版本号管理
 */

object DepMaven {
    const val jitpack = "https://www.jitpack.io"
    const val aliGoogle = "https://maven.aliyun.com/repository/google"
    const val aliJcenter = "https://maven.aliyun.com/repository/public/"
    const val huawei = "http://developer.huawei.com/repo/"
}


object DepVersion {
    const val minSdk = 23
    const val compileSdk = 30
    const val targetSdk = 30

    const val versionCode = 1
    const val versionName = "1.0"
    const val kotlin = "1.4.30"
}

object Deps {
    val fileMap = mapOf("dir" to "libs", "include" to listOf("*.jar"))
    const val kotlinStb = "org.jetbrains.kotlin:kotlin-stdlib:${DepVersion.kotlin}"
    const val appCompat = "androidx.appcompat:appcompat:1.2.0"
    const val constrainLayout = "androidx.constraintlayout:constraintlayout:2.0.4"
    const val material = "com.google.android.material:material:1.3.0"
    const val logger = "com.orhanobut:logger:2.2.0"

    const val coreKtx = "androidx.core:core-ktx:1.3.2"
    const val activityKtx = "androidx.activity:activity-ktx:1.2.1"
    const val collectionKtx = "androidx.collection:collection-ktx:1.1.0"
    const val fragmentKtx = "androidx.fragment:fragment-ktx:1.3.1"

    const val lifecycleLiveDataKtx = "androidx.lifecycle:lifecycle-livedata-ktx:2.3.0"
    const val lifecycleLiveDataCoreKtx = "androidx.lifecycle:lifecycle-livedata-core-ktx:2.3.0"
    const val lifecycleStreamsKtx = "androidx.lifecycle:lifecycle-reactivestreams-ktx:2.3.0"
    const val lifecycleRuntimeKtx = "androidx.lifecycle:lifecycle-runtime-ktx:2.3.0"
    const val lifecycleViewModelKtx = "androidx.lifecycle:lifecycle-viewmodel-ktx:2.3.0"

    const val navigationRuntimeKtx = "androidx.navigation:navigation-runtime-ktx:2.3.4"
    const val navigationFragmentKtx = "androidx.navigation:navigation-fragment-ktx:2.3.4"
    const val navigationUiKtx = "androidx.navigation:navigation-ui-ktx:2.3.4"

    const val pagingCommonKtx = "androidx.paging:paging-common-ktx:2.1.2"
    const val pagingRuntimeKtx = "androidx.paging:paging-runtime-ktx:2.1.2"
    const val pagingRxjavaKtx = "androidx.paging:paging-rxjava2-ktx:2.1.2"

    const val preferenceKtx = "androidx.preference:preference-ktx:1.1.1"
    const val playKtx = "com.google.android.play:core-ktx:1.8.1"
    const val roomKtx = "androidx.room:room-ktx:2.2.6"
    const val coroutines = "org.jetbrains.kotlinx:kotlinx-coroutines-android:1.4.2"
}