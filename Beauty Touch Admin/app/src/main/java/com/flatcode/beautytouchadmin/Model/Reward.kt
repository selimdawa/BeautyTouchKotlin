package com.flatcode.beautytouchadmin.Model

class Reward {
    var range: String? = null
    var reward: String? = null
    var reward2: String? = null
    var reward3: String? = null
    var reward4: String? = null
    var reward5: String? = null
    var reward6: String? = null

    constructor()

    constructor(
        range: String?, reward: String?, reward2: String?, reward3: String?,
        reward4: String?, reward5: String?, reward6: String?
    ) {
        this.range = range
        this.reward = reward
        this.reward2 = reward2
        this.reward3 = reward3
        this.reward4 = reward4
        this.reward5 = reward5
        this.reward6 = reward6
    }
}