import audio.AudioPlayer
import audio.Synthesizer

object Entrypoint {

    @JvmStatic
    fun main(args: Array<String>) {

        AudioPlayer(Config.load()).play()

        Thread.sleep(60_000)
        Synthesizer.cleanup() // Needs this call if running
    }
}