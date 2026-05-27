// :domain — 순수 Kotlin JVM 모듈. 도메인 모델, Repository 인터페이스, UseCase
// Android 의존성 금지. 외부 모듈을 절대 참조하지 않는다 (Clean Architecture)
plugins {
    alias(libs.plugins.kotlin.jvm)
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

dependencies {
    implementation(libs.hilt.javax.inject)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.androidx.paging.common)
}
