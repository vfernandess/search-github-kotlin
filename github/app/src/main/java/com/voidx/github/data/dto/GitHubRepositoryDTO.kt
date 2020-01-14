package com.voidx.github.data.dto

import com.google.gson.annotations.SerializedName
import java.util.*

data class GitHubRepositoryDTO(
    @SerializedName("id")
    val id: Int?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("full_name")
    val fullName: String?,
    @SerializedName("owner")
    val owner: GitHubUserDTO?,
    @SerializedName("html_url")
    val url: String?,
    @SerializedName("description")
    val description: String?,
    @SerializedName("created_at")
    val createdAt: Date?,
    @SerializedName("updated_at")
    val updatedAt: Date?,
    @SerializedName("stargazers_count")
    val starCount: Long?,
    @SerializedName("watchers_count")
    val watchersCount: Long?,
    @SerializedName("forks_count")
    val forksCount: Long?
)