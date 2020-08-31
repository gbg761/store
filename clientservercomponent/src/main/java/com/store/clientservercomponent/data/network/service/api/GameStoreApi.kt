package com.store.clientservercomponent.data.network.service.api

import com.store.clientservercomponent.models.ServerHealth
import com.store.clientservercomponent.models.bundle.Bundle
import com.store.clientservercomponent.models.bundle.Bundles
import com.store.clientservercomponent.models.collections.Collection
import com.store.clientservercomponent.models.collections.Collections
import com.store.clientservercomponent.models.developer.Developer
import com.store.clientservercomponent.models.developer.Developers
import com.store.clientservercomponent.models.product.Build
import com.store.clientservercomponent.models.product.Product
import com.store.clientservercomponent.models.product.Products
import com.store.clientservercomponent.models.publisher.Publisher
import com.store.clientservercomponent.models.publisher.Publishers
import com.store.clientservercomponent.models.tokens.AccessToken
import com.store.clientservercomponent.models.tokens.RefreshToken
import com.store.clientservercomponent.models.user.authorization.AuthorizationResponse
import com.store.clientservercomponent.models.user.authorization.UserLogin
import com.store.clientservercomponent.models.user.library.UserLibrary
import com.store.clientservercomponent.models.user.registraion.UserRegistration
import com.store.clientservercomponent.models.user.subscription.Subscriptions
import com.store.clientservercomponent.models.user.transaction.Transaction
import com.store.clientservercomponent.models.user.transaction.TransactionRequest
import com.store.clientservercomponent.models.user.transaction.Transactions
import com.store.clientservercomponent.models.user.user.User
import com.store.clientservercomponent.models.user.user.Users
import com.store.clientservercomponent.models.user.wishlist.Wishlist
import com.store.clientservercomponent.models.user.wishlist.WishlistItem
import com.store.clientservercomponent.models.user.wishlist.WishlistProduct
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*
import retrofit2.http.Url
import retrofit2.http.GET
import retrofit2.http.Streaming



interface GameStoreApi {

    //////////////////////////////////////////////////////////////////////////////////////
    // Получение списка издателей
    // limit - Количество элементов в выдаче
    // offset - Смещение элементов для пагинации
    @GET("api/v1/publisher/")
    fun getPublishers(@Query("limit") limit: Int, @Query("offset") offset: Int): Single<Publishers>

    // Получение данных об издателе
    @GET("api/v1/publisher/{id}/")
    fun getPublisher(@Path("id") id: Long): Single<Publisher>

    //////////////////////////////////////////////////////////////////////////////////////

    // Получение списка о разработчиков
    @GET("api/v1/developer/")
    fun getDevelopers(@Query("limit") limit: Int, @Query("offset") offset: Int): Single<Developers>

    // Получение данных о разработчике
    @GET("api/v1/developer/{id}/")
    fun getDeveloper(@Path("id") id: Long): Single<Developer>

    //////////////////////////////////////////////////////////////////////////////////////

    // Получение списка продуктов
    // locale - Код локали пользователя
    // platform - Код платформы билда
    @GET("api/v1/product/")
    fun getProducts(
        @Header("Locale") locale: String, @Query("limit") limit: Int, @Query(
            "offset"
        ) offset: Int
    ): Single<Products>

    // Получение продукта
    // developer - ID для фильтрации по разработчику
    @GET("api/v1/product/{id}/")
    fun getProduct(
        @Header("Locale") locale: String, @Path("id") productId: Long
    ): Single<Product>

    // Получение актуального билда продукта
    // platform - Код платформы билда
    @GET("api/v1/product/{id}/build/")
    fun getCurrentBuild(
        @Header("Locale") locale: String, @Path("id") productId: Long, @Query(
            "platform"
        ) platform: String
    ): Single<Build>

    // Получение списка бандлов
    @GET("api/v1/bundle/")
    fun getBundles(
        @Header("Locale") locale: String, @Query("limit") limit: Int, @Query(
            "offset"
        ) offset: Int
    ): Single<Bundles>

    //Получение бандла
    @GET("api/v1/bundle/{id}/")
    fun getBundle(@Header("Locale") locale: String, @Path("id") bundleId: Long): Single<Bundle>

    //////////////////////////////////////////////////////////////////////////////////////

    //Получение списка подборок
    @GET("api/v1/collection/")
    fun getCollections(
        @Header("Locale") locale: String, @Query("limit") limit: Int, @Query("offset") offset: Int
    ): Single<Collections>

    //Получение подборки
    @GET("api/v1/collection/{id}/")
    fun getCollection(@Header("Locale") locale: String, @Path("id") collectionId: Long): Single<Collection>

    //////////////////////////////////////////////////////////////////////////////////////

    //Создание транзакции. Покупка продукта/бандла/подписки происходит через этот метод.
    @POST("api/v1/user/{id}/transaction/")
    fun buy(
        @Header("Authorization") token: String?, @Header("Locale") locale: String,
        @Body createTransactionRequest: TransactionRequest, @Path("id") userId: Long?
    ): Single<Transaction>

    //////////////////////////////////////////////////////////////////////////////////////

    // регистрация нового пользователя
    @POST("api/v1/user/register/")
    fun registerUser(@Body userRegistration: UserRegistration): Completable

    //Авторизация пользователя
    @POST("api/v1/user/login/")
    fun authorizeUser(@Body userLogin: UserLogin): Single<AuthorizationResponse>

    // Обновление refresh-токена
    @POST("api/v1/user/refresh-token/")
    fun refreshToken(@Body refreshToken: RefreshToken): Single<AccessToken>

    //////////////////////////////////////////////////////////////////////////////////////
    //@Header("Authorization") token: String?,
    //Получение списка пользователей
    @GET("api/v1/user/")
    fun getUsers(@Header("Authorization") token: String?, @Query("limit") limit: Int, @Query("offset") offset: Int): Single<Users>

    // Получение пользователя
    @GET("api/v1/user/{id}/")
    fun getUser(@Header("Authorization") token: String?, @Path("id") userId: Long?): Single<User>

    // Получение истории транзакций пользователя
    @GET("api/v1/user/{id}/transaction/")
    fun getUserTransactions(
        @Header("Authorization") token: String?, @Path("id") userId: Long?, @Query("limit") limit: Int, @Query(
            "offset"
        ) offset: Int
    ): Single<Transactions>

    // Получение всех транзакций пользователя
    @GET("api/v1/user/{id}/transaction/")
    fun getAllUserTransactions(@Header("Authorization") token: String?, @Path("id") userId: Long?): Single<Transactions>

    // Получение транзакции
    @GET("api/v1/user/{id}/transaction/{id}/")
    fun getUserTransaction(
        @Header("Authorization") token: String?, @Path("id") userId: Long?, @Path(
            "id"
        ) transactionId: Long
    ): Single<Transaction>

    // Получение истории подписок пользователя
    @GET("api/v1/user/{id}/subscription/")
    fun getUserSubscriptions(
        @Header("Authorization") token: String?, @Path("id") userId: Long?, @Query("limit") limit: Int, @Query(
            "offset"
        ) offset: Int
    ): Single<Subscriptions>


    //Получение библиотеки пользователя
    @GET("api/v1/user/{id}/library/")
    fun getUserLibrary(
        @Header("Authorization") token: String?, @Header("Locale") locale: String, @Path("id") userId: Long?, @Query(
            "limit"
        ) limit: Int, @Query("offset") offset: Int
    ): Single<List<UserLibrary>>

    ///api/v1/user/{id}/wishlist/
    //Создание записи в список желаемых продуктов пользователя
    @POST("api/v1/user/{id}/wishlist/")
    fun addInUserWishlist(
        @Header("Authorization") token: String?, @Header("Locale") locale: String, @Path(
            "id"
        ) userId: Long?, @Body wishlistProduct: WishlistProduct
    ): Single<WishlistItem>

    //Получение списка желаемых продуктов пользователя
    @GET("api/v1/user/{id}/wishlist/")
    fun getUserWishlist(
        @Header("Authorization") token: String?, @Header("Locale") locale: String, @Path("id") userId: Long?, @Query(
            "limit"
        ) limit: Int, @Query(
            "offset"
        ) offset: Int
    ): Single<Wishlist>

    // Удаление записи из списка желаемых продуктов пользователя
    @DELETE("api/v1/user/{id}/wishlist/{product_id}/")
    fun deleteFromUserWishlist(
        @Header("Authorization") token: String?, @Header("Locale") locale: String, @Path("id") userId: Long?, @Path(
            "product_id"
        ) productId: Long
    ): Completable

    // скачиваем apk файл с сервера
    @Streaming
    @GET
    fun downloadApkFile(@Url url: String): Observable<Response<ResponseBody>>

    @Streaming
    @GET
    fun downloadFileWithDynamicUrlAsync(@Url fileUrl: String): Call<ResponseBody>

    // Проверка доступности сервера
    @GET("api/healthcheck/")
    fun checkServerHealth(): Single<ServerHealth>
}