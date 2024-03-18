package com.flatcode.beautytouchadmin.Model

class User {
    var adLoad = 0
    var adClick = 0
    var id: String? = null
    var imageurl: String? = null
    var mversion: String? = null
    var password: String? = null
    var phonenumber: String? = null
    var started: String? = null
    var username: String? = null

    constructor()

    constructor(
        adLoad: Int, adClick: Int, id: String?, imageurl: String?, mversion: String?,
        password: String?, phonenumber: String?, started: String?, username: String?
    ) {
        this.adLoad = adLoad
        this.adClick = adClick
        this.id = id
        this.imageurl = imageurl
        this.mversion = mversion
        this.password = password
        this.phonenumber = phonenumber
        this.started = started
        this.username = username
    }
}