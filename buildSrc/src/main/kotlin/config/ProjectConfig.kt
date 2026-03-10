package config

object ProjectConfig {

    object EnvVariables {
        private fun env(key: String, isRequired: Boolean = true): String {
            val value = System.getenv(key) ?: ""
            if (!isRequired && value.isEmpty()) {
                println("Optional env var: $key does not have a value. Empty value will be used instead.")
                return value
            }
            if (value.isNotEmpty()) return "\"$value\""
            println("Required env var: $key value must be set.")
            error("Required env var: $key value must be set.")
        }

        val PANDASCORE_DEMO_API_TOKEN: String = env(key = "PANDASCORE_DEMO_API_TOKEN", isRequired = true)
        val RELEASE_SIGN_KEY_ALIAS: String = env(key = "RELEASE_SIGN_KEY_ALIAS", isRequired = false)
        val RELEASE_SIGN_KEY_PASSWORD: String = env(key = "RELEASE_SIGN_KEY_PASSWORD", isRequired = false)
        val RELEASE_SIGN_STORE_PASSWORD: String = env(key = "RELEASE_SIGN_STORE_PASSWORD", isRequired = false)
        val RELEASE_SIGN_JKS_PATH: String = env(key = "RELEASE_SIGN_JKS_PATH", isRequired = false)
    }
}
