package com.store.clientservercomponent.data.managers.publisher

import com.store.clientservercomponent.Carry
import com.store.clientservercomponent.models.developer.Developer
import com.store.clientservercomponent.models.developer.Developers
import com.store.clientservercomponent.models.publisher.Publisher
import com.store.clientservercomponent.models.publisher.Publishers

interface PublisherManager {

    // Получение списка издателей
    // limit - Количество элементов в выдаче
    // offset - Смещение элементов для пагинации
    fun getPublishers(limit: Int, offset: Int, carry: Carry<Publishers>)

    // Получение данных об издателе
    fun getPublisher(id: Long, carry: Carry<Publisher>)

    // Получение списка разработчиков
    fun getDevelopers(limit: Int, offset: Int, carry: Carry<Developers>)

    // Получение данных о разработчике
    fun getDeveloper(id: Long, carry: Carry<Developer>)
}