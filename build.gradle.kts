@Suppress("DSL_SCOPE_VIOLATION") // Remove when upgrading to Gradle 8.1+
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.hilt.android) apply false
    alias(libs.plugins.ksp) apply false
}
