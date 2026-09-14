package com.flatcode.beautytouch.Repository

import com.flatcode.beautytouch.Model.Post
import com.flatcode.beautytouch.Model.ShoppingCenter
import com.flatcode.beautytouch.Unit.DATA
import com.flatcode.beautytouch.Unit.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PostRepository @Inject constructor(
    private val database: FirebaseDatabase,
    private val auth: FirebaseAuth
) {

    fun getCategoryCount(category: String, publisher: String, aname: String): Flow<Resource<Int>> = callbackFlow {
        trySend(Resource.Loading)
        val reference = database.getReference(DATA.POSTS)
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var count = 0
                for (child in snapshot.children) {
                    val post = child.getValue(Post::class.java)
                    if (post?.category == category && post.publisher == publisher && post.aname == aname) {
                        count++
                    }
                }
                trySend(Resource.Success(count))
            }

            override fun onCancelled(error: DatabaseError) {
                trySend(Resource.Error(error.message))
            }
        }
        reference.addValueEventListener(listener)
        awaitClose { reference.removeEventListener(listener) }
    }

    fun getHotProducts(publisher: String, aname: String): Flow<Resource<List<Post>>> = callbackFlow {
        trySend(Resource.Loading)
        val reference = database.getReference(DATA.POSTS)
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val list = mutableListOf<Post>()
                for (child in snapshot.children) {
                    val post = child.getValue(Post::class.java)
                    if (post != null && post.publisher == publisher && post.aname == aname) {
                        list.add(post)
                    }
                }
                // Sort by views or something to define "hot" if needed, 
                // for now just returning the list as per previous logic.
                trySend(Resource.Success(list))
            }

            override fun onCancelled(error: DatabaseError) {
                trySend(Resource.Error(error.message))
            }
        }
        reference.addValueEventListener(listener)
        awaitClose { reference.removeEventListener(listener) }
    }

    fun getFavoritePosts(publisher: String, aname: String): Flow<Resource<List<Post>>> = callbackFlow {
        trySend(Resource.Loading)
        val uid = auth.currentUser?.uid
        if (uid == null) {
            trySend(Resource.Error("User not logged in"))
            close()
            return@callbackFlow
        }
        val savesRef = database.getReference(DATA.SAVES).child(uid)
        val listener = object : ValueEventListener {
            override fun onDataChange(savesSnapshot: DataSnapshot) {
                val saveIds = savesSnapshot.children.mapNotNull { it.key }
                val postsRef = database.getReference(DATA.POSTS)
                postsRef.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(postsSnapshot: DataSnapshot) {
                        val posts = mutableListOf<Post>()
                        for (child in postsSnapshot.children) {
                            val post = child.getValue(Post::class.java)
                            if (post != null && post.publisher == publisher && post.aname == aname && post.postid in saveIds) {
                                posts.add(post)
                            }
                        }
                        trySend(Resource.Success(posts))
                    }

                    override fun onCancelled(error: DatabaseError) {
                        trySend(Resource.Error(error.message))
                    }
                })
            }

            override fun onCancelled(error: DatabaseError) {
                trySend(Resource.Error(error.message))
            }
        }
        savesRef.addValueEventListener(listener)
        awaitClose { savesRef.removeEventListener(listener) }
    }

    fun getTopVotedPosts(publisher: String, aname: String): Flow<Resource<List<Post>>> = callbackFlow {
        trySend(Resource.Loading)
        val reference = database.getReference(DATA.POSTS).orderByChild(DATA.VIEWS_COUNT)
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val list = mutableListOf<Post>()
                for (child in snapshot.children) {
                    val post = child.getValue(Post::class.java)
                    if (post != null && post.publisher == publisher && post.aname == aname) {
                        list.add(post)
                    }
                }
                trySend(Resource.Success(list.reversed()))
            }

            override fun onCancelled(error: DatabaseError) {
                trySend(Resource.Error(error.message))
            }
        }
        reference.addValueEventListener(listener)
        awaitClose { reference.removeEventListener(listener) }
    }

    fun getPostsByCategory(category: String, publisher: String, aname: String): Flow<Resource<List<Post>>> = callbackFlow {
        trySend(Resource.Loading)
        val reference = database.getReference(DATA.POSTS)
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val list = mutableListOf<Post>()
                for (child in snapshot.children) {
                    val post = child.getValue(Post::class.java)
                    if (post?.category == category && post.publisher == publisher && post.aname == aname) {
                        list.add(post)
                    }
                }
                trySend(Resource.Success(list))
            }

            override fun onCancelled(error: DatabaseError) {
                trySend(Resource.Error(error.message))
            }
        }
        reference.addValueEventListener(listener)
        awaitClose { reference.removeEventListener(listener) }
    }

    fun getPostsBySearch(query: String, publisher: String, aname: String): Flow<Resource<List<Post>>> = callbackFlow {
        trySend(Resource.Loading)
        val reference = database.getReference(DATA.POSTS)
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val list = mutableListOf<Post>()
                for (child in snapshot.children) {
                    val post = child.getValue(Post::class.java)
                    if (post != null && post.publisher == publisher && post.aname == aname &&
                        (post.name?.contains(query, ignoreCase = true) == true)) {
                        list.add(post)
                    }
                }
                trySend(Resource.Success(list))
            }

            override fun onCancelled(error: DatabaseError) {
                trySend(Resource.Error(error.message))
            }
        }
        reference.addValueEventListener(listener)
        awaitClose { reference.removeEventListener(listener) }
    }

    fun getPostDetails(postId: String): Flow<Resource<Post>> = callbackFlow {
        trySend(Resource.Loading)
        val reference = database.getReference(DATA.POSTS).child(postId)
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val post = snapshot.getValue(Post::class.java)
                if (post != null) {
                    trySend(Resource.Success(post))
                } else {
                    trySend(Resource.Error("Post not found"))
                }
            }

            override fun onCancelled(error: DatabaseError) {
                trySend(Resource.Error(error.message))
            }
        }
        reference.addValueEventListener(listener)
        awaitClose { reference.removeEventListener(listener) }
    }

    fun getShoppingCenters(publisher: String, aname: String): Flow<Resource<List<ShoppingCenter>>> = callbackFlow {
        trySend(Resource.Loading)
        val reference = database.getReference(DATA.SHOPPING_CENTERS)
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val list = mutableListOf<ShoppingCenter>()
                for (child in snapshot.children) {
                    val center = child.getValue(ShoppingCenter::class.java)
                    if (center != null && center.publisher == publisher && center.aname == aname) {
                        list.add(center)
                    }
                }
                trySend(Resource.Success(list))
            }

            override fun onCancelled(error: DatabaseError) {
                trySend(Resource.Error(error.message))
            }
        }
        reference.addValueEventListener(listener)
        awaitClose { reference.removeEventListener(listener) }
    }
}