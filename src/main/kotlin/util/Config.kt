import audio.AudioType
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.fasterxml.jackson.module.kotlin.KotlinModule
import java.nio.file.Files
import java.nio.file.Paths

data class Config(
    val name: String,
    val version: String = "1.0",
    val synthesizer: SynthesizerConfig,
    val metronome: MetronomeConfig,
    val prometheus: PrometheusConfig = PrometheusConfig(),
    val audioQueries: List<SoundQuery>,
) {

    companion object {

        fun load(filename: String = "config.yaml") : Config {
            val path = Paths.get(filename)
            val mapper = ObjectMapper(YAMLFactory())
            mapper.registerModule(KotlinModule())

            return Files.newBufferedReader(path).use {
                mapper.readValue(it, Config::class.java)
            }
        }
    }
}

data class SynthesizerConfig(
    val freqUpper : Double = 500.0,
    val freqLower : Double = 200.0,
)

data class MetronomeConfig(
    val bpmMin: Double = 0.0,
    val bpmMax: Double = 300.0,
)

data class PrometheusConfig(
    val port: Int = 9090,
    val scheme: String = "http",
    val url: String = "0.0.0.0",
)

data class SoundQuery(
    val name: String,
    val query: String,
    val type: AudioType,
    val min: Double = 0.0,
    val max: Double = 1.0,
)
