plugins {
    alias(libs.plugins.runique.android.feature.ui)
}

android {
    namespace = "com.jnasser.analytics.presentation"
}

dependencies {
    implementation(projects.analytics.domain)
}