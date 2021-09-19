package audio

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.sound.midi.MidiChannel
import javax.sound.midi.MidiSystem
import kotlin.math.roundToInt

internal object MidiPlayer {
    private val mChannels: Array<MidiChannel>

    init {
        val midiSynth = MidiSystem.getSynthesizer()
        midiSynth.open()

        mChannels = midiSynth.channels

        // Load a different instrument on every channel for some variety
        mChannels.forEachIndexed { index, it ->
            val instr = midiSynth.defaultSoundbank.instruments[index]
            assert(midiSynth.loadInstrument(instr)) { "Failed to load instrument" }
            it.programChange(instr.patch.program)
        }
    }

    fun playNote(channel: Int, note: Int, velocity: Double, durationMillis: Long) {
        println("Playing on $velocity velocity over channel $channel for $durationMillis millis")

        GlobalScope.launch {
            mChannels[channel].noteOn(note, velocity.roundToInt())
            delay(durationMillis)
            mChannels[channel].noteOff(note)
        }
    }

    fun repeatNote(channel: Int, note: Int, velocity: Double, periodMillis: Long, totalDurationMillis: Long) {
        GlobalScope.launch {
            val timeUp = System.currentTimeMillis() + totalDurationMillis
            while (System.currentTimeMillis() < timeUp) {
                playNote(channel, note, velocity, periodMillis / 2)
                delay(periodMillis)
            }
        }
    }
}