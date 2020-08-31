package com.store.clientservercomponent.data.network.module

import android.util.Log
import com.store.clientservercomponent.data.network.service.api.GameStoreApi
import com.store.clientservercomponent.extensions.locale.lang
import com.store.clientservercomponent.models.tokens.RefreshToken
import com.store.clientservercomponent.models.user.authorization.UserLogin
import com.store.clientservercomponent.models.user.registraion.UserRegistration
import com.store.clientservercomponent.models.user.transaction.Transaction
import com.store.clientservercomponent.models.user.transaction.TransactionRequest
import com.store.clientservercomponent.models.user.transaction.Transactions
import com.store.clientservercomponent.models.user.user.User
import com.store.clientservercomponent.models.user.wishlist.WishlistProduct
import com.auth0.android.jwt.JWT
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.ResponseBody
import retrofit2.Response
import java.util.*


class RestModuleImpl(
    private val gameStoreApi: GameStoreApi,
    private var token: String//  access необходим для выполнения запросов
) : RestModule {

    // необходима для получения id текущего юзера из токена с помощью JWT
    // JWT.getClaim(CLAIMS_USER_ID)
    private val CLAIMS_USER_ID = "user_id"

    // необходим для формирования access токена, так как
    // содержимое заголовка должно быть в формате "Token <access_token>"
    private val TOKEN = "Token "

    // передается в качестве параметра в запрос получения актуального билда продукта
    private val PLATFORM = "android"

    private var jwt: JWT? = null

    init {
        // с помощью JWT вытаскиваем id текущего юзера из токена
        jwt = if (token.isNotEmpty()) JWT(token) else null
    }

    override fun refreshToken(token: String) {
        this.token = token
        jwt = JWT(token)
    }

    // Получение списка издателей
    // limit - Количество элементов в выдаче
    // offset - Смещение элементов для пагинации
    override fun getPublishers(limit: Int, offset: Int) =
        gameStoreApi.getPublishers(limit, offset)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    // Получение данных об издателе
    override fun getPublisher(id: Long) = gameStoreApi.getPublisher(id)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())

    //////////////////////////////////////////////////////////////////////////////////////

    // Получение списка разработчиков
    override fun getDevelopers(limit: Int, offset: Int) = gameStoreApi.getDevelopers(limit, offset)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())

    // Получение данных о разработчике
    override fun getDeveloper(id: Long) = gameStoreApi.getDeveloper(id)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())

    // Получение списка продуктов
    // locale - Код локали пользователя
    override fun getProducts(limit: Int, offset: Int) =
        gameStoreApi.getProducts(Locale.getDefault().lang, limit, offset)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())


    // Получение продукта
    // developer - ID для фильтрации по разработчику
    override fun getProduct(productId: Long) =
        gameStoreApi.getProduct(Locale.getDefault().lang, productId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())


    // Получение актуального билда продукта
    // platform - Код платформы билда
    override fun getCurrentBuild(productId: Long) =
        gameStoreApi.getCurrentBuild(Locale.getDefault().lang, productId, PLATFORM)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())


    // Получение списка бандлов
    override fun getBundles(limit: Int, offset: Int) =
        gameStoreApi.getBundles(Locale.getDefault().lang, limit, offset)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())


    //Получение бандла
    override fun getBundle(bundleId: Long) =
        gameStoreApi.getBundle(Locale.getDefault().lang, bundleId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())


    //////////////////////////////////////////////////////////////////////////////////////

    //Получение списка подборок
    override fun getCollections(limit: Int, offset: Int) =
        gameStoreApi.getCollections(Locale.getDefault().lang, limit, offset)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())


    //Получение подборки
    override fun getCollection(collectionId: Long) =
        gameStoreApi.getCollection(Locale.getDefault().lang, collectionId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())


    //////////////////////////////////////////////////////////////////////////////////////

    // регистрация нового пользователя
    override fun registerUser(userRegistration: UserRegistration) =
        gameStoreApi.registerUser(userRegistration)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    //Авторизация пользователя
    override fun authorizeUser(userLogin: UserLogin) = gameStoreApi.authorizeUser(userLogin)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())

    // Обновление access-токена
    override fun refreshToken(refreshToken: RefreshToken) =
        gameStoreApi.refreshToken(refreshToken)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    // Создание транзакции. Покупка продукта/бандла/подписки происходит через этот метод
    override fun buy(transactionRequest: TransactionRequest): Single<Transaction> {
        val uid = getUserId()
        return gameStoreApi.buy(
            TOKEN + token,
            Locale.getDefault().lang,
            transactionRequest,
            getUserId()
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }


    //Получение списка пользователей
    override fun getUsers(limit: Int, offset: Int) =
        gameStoreApi.getUsers(TOKEN + token, limit, offset)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    // Получение пользователя
    override fun getUser(): Single<User> = gameStoreApi.getUser(TOKEN + token, getUserId())
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())

    // Получение истории транзакций пользователя
    override fun getUserTransactions(limit: Int, offset: Int): Single<Transactions> {
        val t = TOKEN + token
        val id = getUserId()
        val lim = limit
        return gameStoreApi.getUserTransactions(TOKEN + token, getUserId(), limit, offset)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    // Получение всех транзакций пользователя
    override fun getAllUserTransactions() =
        gameStoreApi.getAllUserTransactions(TOKEN + token, getUserId())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    // Получение транзакции
    override fun getUserTransaction(transactionId: Long) =
        gameStoreApi.getUserTransaction(TOKEN + token, getUserId(), transactionId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    // Получение истории подписок пользователя
    override fun getUserSubscriptions(limit: Int, offset: Int) =
        gameStoreApi.getUserSubscriptions(TOKEN + token, getUserId(), limit, offset)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    //Получение списка желаемых продуктов пользователя
    override fun getUserWishlist(limit: Int, offset: Int) =
        gameStoreApi.getUserWishlist(
            TOKEN + token,
            Locale.getDefault().lang,
            getUserId(),
            limit,
            offset
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    override fun addInUserWishlist(wishlistProduct: WishlistProduct) =
        gameStoreApi.addInUserWishlist(
            TOKEN + token,
            Locale.getDefault().lang,
            getUserId(),
            wishlistProduct
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    //Получение библиотеки пользователя
    override fun getUserLibrary(limit: Int, offset: Int) =
        gameStoreApi.getUserLibrary(
            TOKEN + token,
            Locale.getDefault().lang,
            getUserId(),
            limit,
            offset
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    // Удаление записи из списка желаемых продуктов пользователя
    override fun deleteFromUserWishlist(productId: Long) =
        gameStoreApi.deleteFromUserWishlist(
            TOKEN + token,
            Locale.getDefault().lang,
            getUserId(),
            productId
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    override fun checkServerHealth() = gameStoreApi.checkServerHealth()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())

    // скачиваем apk файл с сервера
    override fun downloadApkFile(url: String): Observable<Response<ResponseBody>> =
        gameStoreApi.downloadApkFile(url)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    override fun downloadFile(fileUrl: String) =
        gameStoreApi.downloadFileWithDynamicUrlAsync(fileUrl)

    // получем id юзера из токена
    //private fun getUserId() = jwt?.getClaim(CLAIMS_USER_ID)?.asLong()

    private fun getUserId(): Long? {
        val jwt1 = jwt
        val claim = jwt?.getClaim(CLAIMS_USER_ID)
        val id = claim?.asLong()
        Log.d("RestModule", "$id")
        return id
    }
}