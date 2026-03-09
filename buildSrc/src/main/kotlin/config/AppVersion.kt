package config

data class AppVersion(val major: Int, val minor: Int, val patch: Int) {
    val code: Int = (10000 * major) + (100 * minor) + patch
    val name: String = "$major.$minor.$patch"
}
