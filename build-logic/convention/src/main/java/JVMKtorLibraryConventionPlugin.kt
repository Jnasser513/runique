import com.android.build.api.dsl.LibraryExtension
import com.jnasser.convention.ExtensionType
import com.jnasser.convention.configureBuildTypes
import com.jnasser.convention.configureKotlinAndroid
import com.jnasser.convention.configureKotlinJvm
import com.jnasser.convention.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.kotlin

class JVMKtorLibraryConventionPlugin: Plugin<Project> {
    override fun apply(target: Project) {
        target.run {
            pluginManager.apply("org.jetbrains.kotlin.plugin.serialization")

            dependencies {
                "implementation"(libs.findBundle("ktor").get())
            }
        }
    }
}