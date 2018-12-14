package morecat.domain

package object article {

  def isBlank(str: String): Boolean    = str.forall(Character.isWhitespace)
  def isNotBlank(str: String): Boolean = !isBlank(str)

}
