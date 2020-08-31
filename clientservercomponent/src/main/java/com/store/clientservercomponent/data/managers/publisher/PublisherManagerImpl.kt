package com.store.clientservercomponent.data.managers.publisher

import android.content.Context
import com.store.clientservercomponent.Carry
import com.store.clientservercomponent.data.managers.BaseManager
import com.store.clientservercomponent.data.network.module.RestModule
import com.store.clientservercomponent.models.developer.Developer
import com.store.clientservercomponent.models.developer.Developers
import com.store.clientservercomponent.models.publisher.Publisher
import com.store.clientservercomponent.models.publisher.Publishers

class PublisherManagerImpl(
    context: Context,
    private val restModule: RestModule
) :
    BaseManager(context),
    PublisherManager {

    override fun getPublishers(limit: Int, offset: Int, carry: Carry<Publishers>) {

        val d = restModule.getPublishers(limit, offset).subscribe(
            { t: Publishers -> carry.onSuccess(t) },
            { t: Throwable -> carry.onFailure(handleError(t)) }
        )
    }

    override fun getPublisher(id: Long, carry: Carry<Publisher>) {

        val d = restModule.getPublisher(id).subscribe(
            { t: Publisher -> carry.onSuccess(t) },
            { t: Throwable -> carry.onFailure(handleError(t)) }
        )
    }

    override fun getDevelopers(limit: Int, offset: Int, carry: Carry<Developers>) {

        val d = restModule.getDevelopers(limit, offset).subscribe(
            { t: Developers -> carry.onSuccess(t) },
            { t: Throwable -> carry.onFailure(handleError(t)) }
        )
    }

    override fun getDeveloper(id: Long, carry: Carry<Developer>) {

        val d = restModule.getDeveloper(id).subscribe(
            { t: Developer -> carry.onSuccess(t) },
            { t: Throwable -> carry.onFailure(handleError(t)) }
        )
    }
}