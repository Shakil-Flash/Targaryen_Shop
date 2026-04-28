package com.flash.targaryen.data.model


import com.google.gson.annotations.SerializedName

// ── JSONPlaceholder Post ─────────────────────────────────────────────────────
data class Post(
    @SerializedName("id")     val id: Int,
    @SerializedName("userId") val userId: Int,
    @SerializedName("title")  val title: String,
    @SerializedName("body")   val body: String
)

// ── JSONPlaceholder User ─────────────────────────────────────────────────────
data class User(
    @SerializedName("id")       val id: Int,
    @SerializedName("name")     val name: String,
    @SerializedName("username") val username: String,
    @SerializedName("email")    val email: String,
    @SerializedName("phone")    val phone: String,
    @SerializedName("website")  val website: String,
    @SerializedName("company")  val company: Company,
    @SerializedName("address")  val address: Address
)

data class Company(
    @SerializedName("name")        val name: String,
    @SerializedName("catchPhrase") val catchPhrase: String
)

data class Address(
    @SerializedName("street") val street: String,
    @SerializedName("city")   val city: String,
    @SerializedName("zipcode") val zipcode: String
)

// ── JSONPlaceholder Photo ────────────────────────────────────────────────────
data class Photo(
    @SerializedName("id")           val id: Int,
    @SerializedName("albumId")      val albumId: Int,
    @SerializedName("title")        val title: String,
    @SerializedName("url")          val url: String,
    @SerializedName("thumbnailUrl") val thumbnailUrl: String
)