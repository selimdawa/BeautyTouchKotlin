package com.flatcode.beautytouchadmin.Model

class Post {
    var category: String? = null
    var name: String? = null
    var postid: String? = null
    var postimage: String? = null
    var postimage2: String? = null
    var postimage3: String? = null
    var postimage4: String? = null
    var postimage5: String? = null
    var postimage6: String? = null
    var postimage7: String? = null
    var postimage8: String? = null
    var postimage9: String? = null
    var postimage10: String? = null
    var price: String? = null
    var indications: String? = null
    var use: String? = null
    var publisher: String? = null
    var aname: String? = null

    constructor()

    constructor(
        category: String?, name: String?, postid: String?, postimage: String?, postimage2: String?,
        postimage3: String?, postimage4: String?, postimage5: String?, postimage6: String?,
        postimage7: String?, postimage8: String?, postimage9: String?, postimage10: String?,
        price: String?, indications: String?, use: String?, publisher: String?, aname: String?
    ) {
        this.category = category
        this.name = name
        this.postid = postid
        this.postimage = postimage
        this.postimage2 = postimage2
        this.postimage3 = postimage3
        this.postimage4 = postimage4
        this.postimage5 = postimage5
        this.postimage6 = postimage6
        this.postimage7 = postimage7
        this.postimage8 = postimage8
        this.postimage9 = postimage9
        this.postimage10 = postimage10
        this.price = price
        this.indications = indications
        this.use = use
        this.publisher = publisher
        this.aname = aname
    }
}