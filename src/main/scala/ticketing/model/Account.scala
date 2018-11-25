package ticketing.model

class Account (val naam: String,
                                   val leeftijd: Int,
                                   val email: String,
                                  startBudget: Int) {

  private var _budget = startBudget
  def budget = _budget

  def betaal(bedrag: Int): Unit = (budget - bedrag) match {
    case x if (0 > x) =>
      throw new TeWeinigBudgetException(
        s"te betalen bedrag ($bedrag) is hoger dan het budget ($budget)")
    case x => _budget = x
  }
  override def toString = s"naam: $naam, leeftijd: $leeftijd, email: $email"

}

final case class TeWeinigBudgetException(private val message: String = "",
                                         private val cause: Throwable =
                                           None.orNull)
    extends Exception(message, cause)
