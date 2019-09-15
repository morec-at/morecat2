package morecat.usecase

import cats.data.ContT

object UseCaseCont {

  def apply[F[_], A](f: (A => F[UseCaseResult]) => F[UseCaseResult]): UseCaseCont[F, A] =
    ContT(f)

}
