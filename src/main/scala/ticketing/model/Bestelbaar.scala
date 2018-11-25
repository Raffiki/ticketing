package ticketing.model

trait Bestelbaar extends Ordered [Bestelbaar] {
    def prijs: Int

    def compare (that: Bestelbaar) = this.prijs.compare(that.prijs)
}
