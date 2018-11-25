package ticketing.model

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

abstract class Logger[A] {
    val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")

    def log(message: A): Unit
}

class ConsoleLogger extends Logger[String] {


    def log(message: String) = println(s"${formatter.format(LocalDateTime.now)}: $message")

}