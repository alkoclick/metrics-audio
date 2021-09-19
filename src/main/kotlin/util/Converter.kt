import kotlin.math.absoluteValue


fun nameToAudioFrequency(query: SoundQuery, config: SynthesizerConfig): Double =
    query.name.hashCode().absoluteValue % (config.freqUpper - config.freqLower) + config.freqLower

/**
 * Returned value is in 0..16 because that's the available MIDI channels
 */
fun nameToChannel(soundQuery: SoundQuery) =
    soundQuery.name.hashCode().absoluteValue % 16

/**
 * Returned value is in 0..1
 */
fun resultToPercent(query: SoundQuery, result: Double): Double =
    result.coerceIn(query.min, query.max) / query.max

fun resultToBpm(query: SoundQuery, result: Double, config: MetronomeConfig): Double =
    (resultToPercent(query, result) * config.bpmMax).coerceAtLeast(config.bpmMin)
