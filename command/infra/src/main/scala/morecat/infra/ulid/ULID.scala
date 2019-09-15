package morecat.infra.ulid

import net.petitviolet.ulid4s.{ULID => LibULID}

final case class ULID(private val value: String) {

  val str: String             = value
  val timestamp: Option[Long] = LibULID.timestamp(value)

}

object ULID {

  def apply(): ULID = ULID(LibULID.generate)

}
