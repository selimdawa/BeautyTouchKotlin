package com.flatcode.beautytouch.Model

class User {
    var category: String? = null
    var id: String? = null
    var imageurl: String? = null
    var password: String? = null
    var phonenumber: String? = null
    var status: String? = null
    var username: String? = null
    var city: String? = null
    var typingTo: String? = null
    var mversion: String? = null

    constructor()

    constructor(
        category: String?, id: String?, imageurl: String?, location: String?, location2: String?,
        password: String?, phonenumber: String?, status: String?, username: String?, city: String?,
        typingTo: String?, mversion: String?
    ) {
        this.category = category
        this.id = id
        this.imageurl = imageurl
        this.password = password
        this.phonenumber = phonenumber
        this.status = status
        this.username = username
        this.city = city
        this.typingTo = typingTo
        this.mversion = mversion
    }
}