package com.voidx.github.data.dto

import com.google.gson.annotations.SerializedName

data class GitHubSearchDTO(
    @SerializedName("items")
    val items: List<GitHubRepositoryDTO>?
)