import audio.AudioPlayer

object Entrypoint {

    @JvmStatic
    fun main(args: Array<String>) {

        AudioPlayer(Config.load()).play()

        Thread.sleep(60000)
    }
}