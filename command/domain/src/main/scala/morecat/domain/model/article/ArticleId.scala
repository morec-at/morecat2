package morecat.domain.model.article

import morecat.infra.ulid.ULID

final case class ArticleId(value: ULID = ULID())
