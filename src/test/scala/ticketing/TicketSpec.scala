import org.scalatest._
import ticketing.model._

class TicketSpec
    extends FunSuite
    with EitherValues
    with BeforeAndAfterEach
    with Matchers {

  var julie: Account = _
  var eric: Account = _
  var frank: Account = _
  var dave: Account = _

  val logger = new ConsoleLogger[Bestelling[Bestelbaar]]()

  override def beforeEach() {
    eric = new Account("Eric", 20, "email", 500)
    julie = new Account("Julie", 17, "email", 500)
    frank = new Account("Frank", 45, "email", 500)
    dave = new Account("Dave", 34, "email", 500)
  }

  test("Een EarlyBird ticket heeft een id en een prijs") {
    val ticket = EarlyBird.apply(eric.naam)
    ticket.prijs should be(50)
    println(ticket.id)

    ticket.id should be(1L)
    ticket.naam should be(eric.naam)
  }

  test(
    "Een betaling door een account verlaagt zijn budget met het bedrag van de betaling") {
    eric.betaal(30)
    eric.budget should be(470)
  }

  test(
    "Bestelbare items worden gesorteerd op prijs, tickets vervolgens op naam van de koper") {

    val tickets = Seq(VIP(eric.naam), EarlyBird(julie.naam), VIP(julie.naam))
    val eetbonnen = Seq(Eetbon, Eetbon)
    val drankbonnen = Seq(Drankbon, Drankbon)

    val teBestellen: Seq[Bestelbaar] = tickets ++ eetbonnen ++ drankbonnen
    val teBestellenSorted = teBestellen.sorted[Bestelbaar]

    teBestellenSorted(0) should equal(Drankbon)
    teBestellenSorted(1) should equal(Drankbon)
    teBestellenSorted(2) should equal(Eetbon)
    teBestellenSorted(3) should equal(Eetbon)
    teBestellenSorted(4).asInstanceOf[EarlyBird].naam shouldBe ("Julie")
    teBestellenSorted(5).asInstanceOf[VIP].naam shouldBe ("Eric")
    teBestellenSorted(6).asInstanceOf[VIP].naam shouldBe ("Julie")
  }

  test(
    "Een bestelling van VIP ticket door een minderjarige faalt met een waarschuwing") {
    val regel = Regel("VIP", 18)
    val ticket = VIP(julie.naam)
    val bestelling = new Bestelling(julie, Seq(ticket), Seq(regel), logger)

    val resultaat = bestelling.bevestig

    resultaat.left.value(0) shouldBe a[RegelWarning]
    resultaat.left
      .value(0)
      .bericht shouldBe "een VIP ticket vereist een minimumleeftijd van 18 jaar. De koper is 17 jaar."
  }

  test("Een bestelling van VIP ticket door een meerderjarige slaagt") {
    val regel = Regel("VIP", 18)
    val ticket = VIP(eric.naam)
    val bestelling = new Bestelling(eric, Seq(ticket), Seq(regel), logger)

    val resultaat = bestelling.bevestig

    resultaat.right.value(0) shouldBe ticket
    eric.budget shouldBe 300
  }

  test(
    "Een bestelling met een waarde groter dan het budget faalt met een warning") {
    val tickets = Seq(VIP(eric.naam), VIP(julie.naam), VIP(frank.naam))
    val bestelling = new Bestelling(eric, tickets, Seq.empty, logger)

    val resultaat = bestelling.bevestig

    resultaat.left.value(0) shouldBe a[BudgetWarning]
    resultaat.left
      .value(0)
      .bericht shouldBe "Account Eric heeft onvoldoende budget om deze bestelling te plaatsen."
    eric.budget shouldBe 500
  }

    test(
    "Een bestelling met meerdere tickets voor dezelfde naam faalt met een warning") {
    val tickets = Seq(VIP(eric.naam), EarlyBird(julie.naam), EarlyBird(eric.naam))
    val bestelling = new Bestelling(eric, tickets, Seq.empty, logger)

    val resultaat = bestelling.bevestig

    resultaat.left.value(0) shouldBe a[TicketWarning]
    resultaat.left
      .value(0)
      .bericht shouldBe "Account Eric tracht meerdere tickets te bestellen."
    eric.budget shouldBe 500
  }

  test("Een geslaagde bestelling geeft de bestelde items geordend terug") {
    val items = Seq(VIP(dave.naam),
                    Normal(julie.naam),
                    EarlyBird(frank.naam),
                    EarlyBird(eric.naam),
                    Drankbon,
                    Eetbon,
                    Drankbon)
    val bestelling = new Bestelling(eric, items, Seq.empty, logger)

    val resultaat = bestelling.bevestig

    resultaat.right.value.length shouldBe items.length

    resultaat.right.value(0) should equal(Drankbon)
    resultaat.right.value(1) should equal(Drankbon)
    resultaat.right.value(2) should equal(Eetbon)
    resultaat.right.value(3).asInstanceOf[EarlyBird].naam shouldBe ("Eric")
    resultaat.right.value(4).asInstanceOf[EarlyBird].naam shouldBe ("Frank")
    resultaat.right.value(5).asInstanceOf[Normal].naam shouldBe ("Julie")
    resultaat.right.value(6).asInstanceOf[VIP].naam shouldBe ("Dave")

    eric.budget shouldBe 91
  }
}
