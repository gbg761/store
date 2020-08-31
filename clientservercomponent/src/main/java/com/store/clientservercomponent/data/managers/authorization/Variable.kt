package com.store.clientservercomponent.data.managers.authorization

import io.reactivex.subjects.PublishSubject

class Variable<T>(defValue: T) {

    var value: T = defValue
        set(value) {
            field = value
            observable.onNext(value)
        }

    val observable = PublishSubject.create<T>()
}