package ticketing.model

import util.MyOrdering

trait Bestelbaar extends MyOrdering[Bestelbaar] {
    def prijs: Int

    def compare (that: Bestelbaar) = this.prijs.compare(that.prijs)
}
