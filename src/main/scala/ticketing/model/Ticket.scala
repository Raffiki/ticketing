package ticketing.model

trait Ticket extends Bestelbaar {
  val id: Long
  val naam: String

  override def compare(that: Bestelbaar) = that match {
    case t: Ticket => super.compare(t) match {
      case 0 => this.naam.compare(t.naam)
      case other => other
    }
    case _ => super.compare(that)
  }

  override def toString = s"id: $id, naam: $naam"
}

case class EarlyBird private[model] (id: Long, naam: String) extends Ticket {
  override val prijs = 50
    override def toString = s"Ticket(type: EarlyBird, ${super.toString})"
}

object EarlyBird {
  def apply(naam: String): EarlyBird = EarlyBird(1L, naam)

}

case class Normal private[model] (id: Long, naam: String) extends Ticket {
  override val prijs = 100
    override def toString = s"Ticket(type: Normal, ${super.toString})"

}

object Normal {
  def apply(naam: String): Normal = Normal(1L, naam)
}

case class VIP private[model] (id: Long, naam: String) extends Ticket {
  override val prijs = 200
    override def toString = s"Ticket(type: VIP, ${super.toString})"

}

object VIP {
  def apply(naam: String): VIP = VIP(1L, naam)
}
