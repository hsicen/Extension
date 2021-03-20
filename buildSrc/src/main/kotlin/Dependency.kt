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
    const val kotlinStb = "org.jetbrains.kotlin:kotlin-stdlib:${DepVersion.kotlin}"
    const val appCompat = "androidx.appcompat:appcompat:1.2.0"
    const val corKtx = "androidx.core:core-ktx:1.3.2"
    const val constrainLayout = "androidx.constraintlayout:constraintlayout:2.0.4"
    const val material = "com.google.android.material:material:1.3.0"
    const val logger = "com.orhanobut:logger:2.2.0"
}