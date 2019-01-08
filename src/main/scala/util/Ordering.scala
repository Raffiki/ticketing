package util

trait MyOrdering[-A] {
  def compare(a: A, b: A): Int
}
