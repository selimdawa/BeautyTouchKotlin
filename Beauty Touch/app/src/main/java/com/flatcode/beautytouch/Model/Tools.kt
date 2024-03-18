package com.flatcode.beautytouch.Model

class Tools {
    var imageLogo: String? = null
    var oldImageLogo: String? = null
    var imageSession: String? = null
    var oldImageSession: String? = null
    var session: String? = null
    var oldSession: String? = null
    var sessionNumber: String? = null
    var oldSessionNumber: String? = null
    var year: String? = null
    var oldYear: String? = null
    var aboutMe: String? = null
    var imageMe: String? = null

    constructor()

    constructor(
        imageLogo: String?, oldImageLogo: String?, imageSession: String?, oldImageSession: String?,
        session: String?, oldSession: String?, sessionNumber: String?, oldSessionNumber: String?,
        year: String?, oldYear: String?, aboutMe: String?, imageMe: String?
    ) {
        this.imageLogo = imageLogo
        this.oldImageLogo = oldImageLogo
        this.imageSession = imageSession
        this.oldImageSession = oldImageSession
        this.session = session
        this.oldSession = oldSession
        this.sessionNumber = sessionNumber
        this.oldSessionNumber = oldSessionNumber
        this.year = year
        this.oldYear = oldYear
        this.aboutMe = aboutMe
        this.imageMe = imageMe
    }
}