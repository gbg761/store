package com.store.clientservercomponent.data.managers.user

import com.store.clientservercomponent.Carry
import com.store.clientservercomponent.models.user.library.UserLibrary
import com.store.clientservercomponent.models.user.subscription.Subscriptions
import com.store.clientservercomponent.models.user.transaction.Transaction
import com.store.clientservercomponent.models.user.transaction.Transactions
import com.store.clientservercomponent.models.user.user.User
import com.store.clientservercomponent.models.user.user.Users
import com.store.clientservercomponent.models.user.wishlist.Wishlist
import com.store.clientservercomponent.models.user.wishlist.WishlistItem

// Получение пользователя, списка пользователей
interface UserManager {

    //Получение списка пользователей
    fun getUsers(limit: Int, offset: Int, carry: Carry<Users>)

    // Получение пользователя
    fun getUser(carry: Carry<User>)

    // Получение истории транзакций пользователя
    fun getUserTransactions(limit: Int, offset: Int, carry: Carry<Transactions>)

    // Получение транзакции
    fun getUserTransaction(transactionId: Long, carry: Carry<Transaction>)

    // Получение истории подписок пользователя
    fun getUserSubscriptions(limit: Int, offset: Int, carry: Carry<Subscriptions>)

    //Создание записи в список желаемых продуктов пользователя
    fun addInUserWishlist(productId: Long, carry: Carry<WishlistItem>)

    //Получение списка желаемых продуктов пользователя
    fun getUserWishlist(limit: Int, offset: Int, carry: Carry<Wishlist>)

    fun deleteFromUserWishlist(productId: Long, carry: Carry<Unit>)

    //Получение библиотеки пользователя
    fun getUserLibrary(limit: Int, offset: Int, carry: Carry<List<UserLibrary>>)
}