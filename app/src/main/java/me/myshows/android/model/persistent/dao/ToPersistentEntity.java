package me.myshows.android.model.persistent.dao;

import io.realm.RealmObject;

public interface ToPersistentEntity<T> {

    RealmObject toRealmObject(T entity);
}
