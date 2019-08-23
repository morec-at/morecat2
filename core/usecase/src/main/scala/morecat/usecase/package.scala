package morecat

import cats.data.ContT

package object usecase {

  type UseCaseCont[F[_], A] = ContT[F, UseCaseResult, A]

}
