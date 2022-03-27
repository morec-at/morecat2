package morecat.api.blog

import morecat.api.{BlogMode, CreateBlogRequest}

object CreateBlogRequestGenerator {

  def gen(
      id: String = "blogId01",
      name: String = "blogName01",
      description: String = "blogDescription",
      mode: BlogMode = BlogMode.PUBLIC
  ): CreateBlogRequest = CreateBlogRequest(
    id = id,
    name = name,
    description = description,
    mode = mode
  )
}
