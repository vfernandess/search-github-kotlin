package com.voidx.github.data.dto

import com.google.gson.annotations.SerializedName

data class GitHubUserDTO(
    @SerializedName("id")
    val id: Int?,
    @SerializedName("login")
    val login: String?,
    @SerializedName("avatar_url")
    val avatar: String?
)