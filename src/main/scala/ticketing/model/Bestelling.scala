package ticketing.model

sealed trait BestelStatus
case object NIET_GEPLAATST extends BestelStatus
case object BEVESTIGD extends BestelStatus
case object GEANNULEERD extends BestelStatus

class Bestelling[+A <: Bestelbaar](account: Account,
                                  items: Seq[A],
                                  regels: Seq[Regel],
                                  logger: Logger[Bestelling[A]]) {

  private var status: BestelStatus = NIET_GEPLAATST

  val totaalBedrag = items.map(_.prijs).sum
  val tickets = items.collect { case t: Ticket => t }

  private def valideerRegels: Seq[Warning] =
    tickets.flatMap(ticket => {

      val toepasbareRegels: Seq[Regel] =
        regels.filter(_.ticket == ticket.getClass.getSimpleName)

      toepasbareRegels.flatMap(regel => {
        logger.log(
          s"validatie bestelling: valideer regel $regel op ticket $ticket")
        regel.minimumLeeftijd >= account.leeftijd match {
          case true  => Some(RegelWarning(regel, account))
          case false => None
        }
      })
    })

  private def valideerMeerdereTickets: Seq[Warning] = {
    logger.log("validatie bestelling: valideer dat een persoon niet meerdere tickets koopt.")

    tickets
      .map(_.naam)
      .groupBy(a => a)
      .collect { case (x, xs) if xs.length > 1 => x }
      .map(TicketWarning(_))
      .toSeq
  }

  private def plaatsBestelling: Seq[A] = {
    account.betaal(totaalBedrag)
    status = BEVESTIGD
    items.sorted[Bestelbaar]
  }

  def bevestig: Either[Seq[Warning], Seq[A]] = status match {
    case NIET_GEPLAATST =>
      valideerRegels ++ valideerMeerdereTickets match {
        case Nil =>
          try {
            Right(plaatsBestelling)
          } catch {
            case e: TeWeinigBudgetException =>
              Left(Seq(BudgetWarning(account)))
          }
        case warnings => Left(warnings)
      }
    case _ =>
      throw new BestellingException(
        s"kan geen bestelling plaatsen in status: $status")
  }

  override def toString =
    s"""Bestelling: account: ${account.toString}, items: ${items
      .map(_.toString)
      .mkString(",")}"""

}

final case class BestellingException(private val message: String = "",
                                     private val cause: Throwable = None.orNull)
    extends Exception(message, cause)
