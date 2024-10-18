plugins {
    alias(libs.plugins.jetbrains.kotlin.jvm)
}

dependencies {
    implementation(libs.hilt.javax.inject)
    implementation(libs.jetbrains.kotlinx)
    implementation(libs.androidx.paging.common)

}