package com.flatcode.beautytouch.Unitimport

import com.flatcode.beautytouch.Activity.*
import com.flatcode.beautytouch.Activity.SplashActivity
import com.flatcode.beautytouch.Auth.ForgetPasswordActivity
import com.flatcode.beautytouch.Auth.LoginActivity
import com.flatcode.beautytouch.Auth.RegisterActivity
import com.flatcode.beautytouch.Auth.AuthActivity
import com.flatcode.beautytouch.Fragment.HomeFragment
import com.flatcode.beautytouch.Fragment.ShoppingCentersFragment
import com.flatcode.beautytouch.Fragment.SkinProductsFragment
import com.flatcode.beautytouch.Fragment.HairProductsFragment

object CLASS {
    var AUTH: Class<*> = AuthActivity::class.java
    var REGISTER: Class<*> = RegisterActivity::class.java
    var LOGIN: Class<*> = LoginActivity::class.java
    var FORGET_PASSWORD: Class<*> = ForgetPasswordActivity::class.java
    var SPLASH: Class<*> = SplashActivity::class.java
    var MAIN: Class<*> = MainActivity::class.java
    var PROFILE: Class<*> = ProfileActivity::class.java
    var FAVORITES: Class<*> = FavoritesActivity::class.java
    var LEADERBOARD: Class<*> = LeaderboardActivity::class.java
    var LEADERBOARD_OLD: Class<*> = LeaderboardOldActivity::class.java
    var POST_DETAILS: Class<*> = PostDetailsActivity::class.java
    var REWARD: Class<*> = RewardActivity::class.java
    var HOME: Class<*> = HomeFragment::class.java
    var HAIR_PRODUCTS: Class<*> = HairProductsFragment::class.java
    var SKIN_PRODUCTS: Class<*> = SkinProductsFragment::class.java
    var SHOPPING_CENTERS: Class<*> = ShoppingCentersFragment::class.java
}