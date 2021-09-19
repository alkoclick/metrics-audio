package audio

import Config
import PrometheusRestService
import SoundQuery
import nameToAudioFrequency
import nameToChannel
import resultToBpm
import resultToPercent

class AudioPlayer(private val config: Config) {
    private val promService = PrometheusRestService(config.prometheus)

    fun play() {
        config.audioQueries.forEachIndexed { index, soundQuery ->
            val result = promService.query(soundQuery.query).data.result.first().value[1].toDouble()
            println("${soundQuery.name}: `${soundQuery.query}` ---> $result")
            val resultPercentage = resultToPercent(soundQuery, result)

            if (resultPercentage > 0)
                playSound(soundQuery, config, result, index)
        }
    }

    private fun playSound(soundQuery: SoundQuery, config: Config, result: Double, i: Int) {
        when (soundQuery.type) {
            AudioType.SYNTH -> Synthesizer.generateSound(
                nameToAudioFrequency(soundQuery, config.synthesizer),
                resultToPercent(soundQuery, result),
                i % 2
            )
            AudioType.METRO -> {
                val bpm = resultToBpm(soundQuery, result, config.metronome).toLong()
                if (bpm > 0)
                    MidiPlayer.repeatNote(
                        channel = nameToChannel(soundQuery),
                        note = 50,
                        velocity = resultToPercent(soundQuery, result) * 100,
                        periodMillis = 60_000 / bpm,
                        totalDurationMillis = 60_000
                    )
            }
        }
    }
}
