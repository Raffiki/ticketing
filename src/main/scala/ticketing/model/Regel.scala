package ticketing.model

case class Regel(ticket: String, minimumLeeftijd: Int)

trait Warning {
  def bericht: String
}

case class RegelWarning(regel: Regel, account: Account) extends Warning {
  def bericht =
    s"een ${regel.ticket} ticket vereist een minimumleeftijd van ${regel.minimumLeeftijd} jaar. De koper is ${account.leeftijd} jaar."
}

case class BudgetWarning(account: Account) extends Warning {
  def bericht =
    s"Account ${account.naam} heeft onvoldoende budget om deze bestelling te plaatsen."
}

case class TicketWarning(naam: String) extends Warning {
  def bericht =
    s"Account $naam tracht meerdere tickets te bestellen."
}
