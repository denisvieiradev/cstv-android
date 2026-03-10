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

        val PANDASCORE_DEMO_API_TOKEN: String = env(key = "PANDASCORE_DEMO_API_TOKEN", isRequired = false)
    }
}
