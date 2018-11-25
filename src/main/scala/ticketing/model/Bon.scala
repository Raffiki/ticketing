package ticketing.model

trait Bon extends Bestelbaar {
}

case object Eetbon extends Bon {
    override val prijs = 5
}

case object Drankbon extends Bon {
    override val prijs = 2
}