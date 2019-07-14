package morecat.domain

import cats.data.ValidatedNec

package object model {

  type DomainConsistencyResult[A] = ValidatedNec[DomainError, A]

  def isBlank(str: String): Boolean    = str.forall(Character.isWhitespace)
  def isNotBlank(str: String): Boolean = !isBlank(str)

}
