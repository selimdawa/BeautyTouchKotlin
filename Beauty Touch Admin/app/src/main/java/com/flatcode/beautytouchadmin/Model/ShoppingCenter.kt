package com.flatcode.beautytouchadmin.Model

class ShoppingCenter {
    var id: String? = null
    var name: String? = null
    var imageurl: String? = null
    var imageurl2: String? = null
    var location: String? = null
    var location2: String? = null
    var location3: String? = null
    var numberPhone: String? = null
    var publisher: String? = null
    var aname: String? = null

    constructor()

    constructor(
        id: String?, name: String?, imageurl: String?, imageurl2: String?, location: String?,
        location2: String?, location3: String?, numberPhone: String?, publisher: String?,
        aname: String?
    ) {
        this.id = id
        this.name = name
        this.imageurl = imageurl
        this.imageurl2 = imageurl2
        this.location = location
        this.location2 = location2
        this.location3 = location3
        this.numberPhone = numberPhone
        this.publisher = publisher
        this.aname = aname
    }
}