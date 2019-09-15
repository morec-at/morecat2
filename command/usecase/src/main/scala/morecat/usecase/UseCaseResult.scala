package morecat.usecase

import cats.data.NonEmptyChain
import morecat.domain.model.DomainError

sealed trait UseCaseResult

trait NormalCase extends UseCaseResult

trait AbnormalCase extends UseCaseResult

final case class Fatal(reasons: NonEmptyChain[DomainError]) extends AbnormalCase

final case class InvalidParameters(reasons: NonEmptyChain[DomainError]) extends AbnormalCase
