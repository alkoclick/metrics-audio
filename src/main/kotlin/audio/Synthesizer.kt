package audio

import com.jsyn.JSyn
import com.jsyn.Synthesizer
import com.jsyn.unitgen.LineOut
import com.jsyn.unitgen.PinkNoise
import com.jsyn.unitgen.SineOscillator
import kotlin.concurrent.thread

/**
 * @see http://www.softsynth.com/jsyn/docs/javadocs/
 */
internal object Synthesizer {
    private val synth: Synthesizer = JSyn.createSynthesizer()
    private val lineOut = LineOut()

    init {
        synth.add(lineOut)
        synth.start()
        lineOut.start()

        // Ensure to clean up properly on shutdown
        Runtime.getRuntime().addShutdownHook(thread(start = false) { cleanup() })
    }

    fun generateSound(freq: Double, amp: Double, channel: Int) {
        println("Playing on $freq frequency with $amp volume")
        val sinOsc = SineOscillator(freq, amp)
        // We add a pink noise filter to make the output less monotonous, but keep it on 5% amp to avoid straying too far from the source
        val pinkNoise = PinkNoise()
        pinkNoise.amplitude.set(amp * 0.05)

        synth.add(sinOsc)
        synth.add(pinkNoise)

        sinOsc.output.connect(0, lineOut.input, channel)
        pinkNoise.output.connect(0, lineOut.input, channel)
    }

    fun cleanup() {
        lineOut.stop()
        synth.stop()
    }

}