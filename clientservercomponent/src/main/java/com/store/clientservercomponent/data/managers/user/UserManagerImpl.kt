package com.store.clientservercomponent.data.managers.user

import android.content.Context
import com.store.clientservercomponent.Carry
import com.store.clientservercomponent.data.managers.BaseManager
import com.store.clientservercomponent.data.managers.authorization.InvalidAccessTokenListener
import com.store.clientservercomponent.data.network.module.RestModule
import com.store.clientservercomponent.extensions.models.addHost
import com.store.clientservercomponent.models.user.library.UserLibrary
import com.store.clientservercomponent.models.user.subscription.Subscriptions
import com.store.clientservercomponent.models.user.transaction.Transaction
import com.store.clientservercomponent.models.user.transaction.Transactions
import com.store.clientservercomponent.models.user.user.User
import com.store.clientservercomponent.models.user.user.Users
import com.store.clientservercomponent.models.user.wishlist.Wishlist
import com.store.clientservercomponent.models.user.wishlist.WishlistItem
import com.store.clientservercomponent.models.user.wishlist.WishlistProduct
import retrofit2.HttpException

class UserManagerImpl(
    context: Context,
    private val restModule: RestModule,
    private val invalidAccessTokenListener: InvalidAccessTokenListener
) :
    BaseManager(context),
    UserManager {

    override fun getUsers(limit: Int, offset: Int, carry: Carry<Users>) {

        val d = restModule.getUsers(limit, offset).subscribe(

            { t: Users -> carry.onSuccess(t) },
            { t: Throwable ->

                // проверяем валидность токена
                // если истек срок действия токена,
                // необходимо выполнить запрос на его обновление
                if ((t is HttpException) && isInvalidToken(t)) {
                    invalidAccessTokenListener.refreshInvalidAccessToken()
                } else {
                    carry.onFailure(handleError(t))
                }
            }
        )
    }

    override fun getUser(carry: Carry<User>) {

        val d = restModule.getUser().subscribe(

            { t: User -> carry.onSuccess(t) },
            { t: Throwable ->

                if ((t is HttpException) && isInvalidToken(t)) {
                    invalidAccessTokenListener.refreshInvalidAccessToken()
                } else {
                    carry.onFailure(handleError(t))
                }
            }
        )
    }

    override fun getUserTransactions(limit: Int, offset: Int, carry: Carry<Transactions>) {

        val d = restModule.getUserTransactions(limit, offset).subscribe(

            { t: Transactions -> carry.onSuccess(t)},
            { t: Throwable ->

                if ((t is HttpException) && isInvalidToken(t)) {
                    invalidAccessTokenListener.refreshInvalidAccessToken()
                } else {
                    carry.onFailure(handleError(t))
                }
            }
        )
    }

    override fun getUserTransaction(transactionId: Long, carry: Carry<Transaction>) {

        val d = restModule.getUserTransaction(transactionId).subscribe(

            { t: Transaction -> carry.onSuccess(t) },
            { t: Throwable ->

                if ((t is HttpException) && isInvalidToken(t)) {
                    invalidAccessTokenListener.refreshInvalidAccessToken()
                } else {
                    carry.onFailure(handleError(t))
                }
            }
        )
    }

    override fun getUserSubscriptions(limit: Int, offset: Int, carry: Carry<Subscriptions>) {

        val d = restModule.getUserSubscriptions(limit, offset).subscribe(

            { t: Subscriptions -> carry.onSuccess(t) },
            { t: Throwable ->

                if ((t is HttpException) && isInvalidToken(t)) {
                    invalidAccessTokenListener.refreshInvalidAccessToken()
                } else {
                    carry.onFailure(handleError(t))
                }
            }
        )
    }

    override fun addInUserWishlist(productId: Long, carry: Carry<WishlistItem>) {

        val d = restModule.addInUserWishlist(WishlistProduct(productId)).subscribe(
            { t: WishlistItem -> carry.onSuccess(t) },
            { t: Throwable ->

                if ((t is HttpException) && isInvalidToken(t)) {
                    invalidAccessTokenListener.refreshInvalidAccessToken()
                } else {
                    carry.onFailure(handleError(t))
                }
            }
        )
    }

    override fun getUserWishlist(limit: Int, offset: Int, carry: Carry<Wishlist>) {

        val d = restModule.getUserWishlist(limit, offset).subscribe(

            { t: Wishlist ->
                t.addHost()
                carry.onSuccess(t)
            },
            { t: Throwable ->

                if ((t is HttpException) && isInvalidToken(t)) {
                    invalidAccessTokenListener.refreshInvalidAccessToken()
                } else {
                    carry.onFailure(handleError(t))
                }
            }
        )
    }

    override fun getUserLibrary(limit: Int, offset: Int, carry: Carry<List<UserLibrary>>) {

        val d = restModule.getUserLibrary(limit, offset).subscribe(

            { t: List<UserLibrary> -> carry.onSuccess(t) },
            { t: Throwable ->

                if ((t is HttpException) && isInvalidToken(t)) {
                    invalidAccessTokenListener.refreshInvalidAccessToken()
                } else {
                    carry.onFailure(handleError(t))
                }
            }
        )
    }

    override fun deleteFromUserWishlist(productId: Long, carry: Carry<Unit>) {

        val d = restModule.deleteFromUserWishlist(productId).subscribe(
            { carry.onSuccess(Unit)},
            { t: Throwable ->

                if ((t is HttpException) && isInvalidToken(t)) {
                    invalidAccessTokenListener.refreshInvalidAccessToken()
                } else {
                    carry.onFailure(handleError(t))
                }
            }
        )
    }
}