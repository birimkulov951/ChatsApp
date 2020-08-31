package com.example.chatsapp.model

class Users {
    private var uid: String = ""
    private var username: String = ""
    private var profile: String = ""
    private var cover: String = ""
    private var status: String = ""
    private var search: String = ""
    private var instagram: String = ""

    constructor()

    constructor(
        uid: String,
        username: String,
        profile: String,
        cover: String,
        status: String,
        search: String,
        instagram: String
    ) {
        this.uid = uid
        this.username = username
        this.profile = profile
        this.cover = cover
        this.status = status
        this.search = search
        this.instagram = instagram
    }

    fun getUID(): String? {
        return uid
    }
    fun setUID(uid: String){
        this.uid = uid
    }

    fun getUsername(): String? {
        return username
    }
    fun setUsername(username: String){
        this.username = username
    }

    fun getProfile(): String? {
        return profile
    }
    fun setProfile(profile: String){
        this.profile = profile
    }

    fun getCover(): String? {
        return cover
    }
    fun setCover(cover: String){
        this.cover = cover
    }

    fun getStatus(): String? {
        return status
    }
    fun setStatus(status: String){
        this.status = status
    }

    fun getSearch(): String? {
        return search
    }
    fun setSearch(search: String){
        this.search = search
    }

    fun getInstagram(): String? {
        return instagram
    }
    fun setInstagram(instagram: String){
        this.instagram = instagram
    }
}
