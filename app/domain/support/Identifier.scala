package domain.support

trait Identifier[A] extends ValueObject[A] {

  override def equals(obj: Any): Boolean = obj match {
    case that: Identifier[_] => value == that.value
    case _                   => false
  }

  override def hashCode: Int = 31 * value.##

}
