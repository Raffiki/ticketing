package ticketing.model

trait Ticket extends Bestelbaar {
  val id: Long = Ticket.makeId()
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

object Ticket {
  private var lastUsedId: Long = 0
  private def makeId(): Long = {
    lastUsedId = lastUsedId + 1
    lastUsedId
  }
}

case class EarlyBird private[model] (naam: String) extends Ticket {
  override val prijs = 50
    override def toString = s"Ticket(type: EarlyBird, ${super.toString})"
}

object EarlyBird {
  def apply(naam: String): EarlyBird = new EarlyBird(naam)

}

case class Normal private[model] (naam: String) extends Ticket {
  override val prijs = 100
    override def toString = s"Ticket(type: Normal, ${super.toString})"

}

object Normal {
  def apply(naam: String): Normal = new Normal(naam)
}

case class VIP private[model] (naam: String) extends Ticket {
  override val prijs = 200
    override def toString = s"Ticket(type: VIP, ${super.toString})"

}

object VIP {
  def apply(naam: String): VIP = new VIP( naam)
}
