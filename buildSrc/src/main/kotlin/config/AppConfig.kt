package config

object AppConfig {
    const val ID = "com.denisvieiradev.cstv"

    val version = AppVersion(major = 1, minor = 0, patch = 0)

    object Sdk {
        const val MIN = 30
        const val COMPILE = 36
        const val TARGET = 36
    }
}
