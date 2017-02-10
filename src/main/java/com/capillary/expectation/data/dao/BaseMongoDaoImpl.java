package com.capillary.expectation.data.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.CriteriaDefinition;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.capillary.expectation.data.entity.BaseMongoEntity;
import com.capillary.expectation.data.mongo.MongoDataSourceManager;
import com.capillary.expectation.exception.ExpectationException;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

/**
 * 
 * @author aditya
 * @param <T>
 *
 * @param <T> Entity
 * @param <ID> Id type
 */
public abstract class BaseMongoDaoImpl<T extends BaseMongoEntity> implements MongoRepository<T, String> {

    @SuppressWarnings("rawtypes")
    public abstract Class getEntityClass();

    public abstract MongoDataSourceManager getDataSource();

    public MongoTemplate getTemplate() {
        return new MongoTemplate(getDataSource().getMongoClient(), "expectation");
    }

    protected Integer getNextId() {
        String seqName = getSequenceName();
        BasicDBObject query = new BasicDBObject();
        query.put("_id", seqName);
        BasicDBObject update = new BasicDBObject();
        update.put("$inc", new BasicDBObject("seq", 1));
        DBObject result = getTemplate().getCollection("counters").findAndModify(query, update);
        return (Integer) result.get("seq");
    }

    public String getSequenceName() {
        throw new UnsupportedOperationException();
    }

    @Override
    public <S extends T> List<S> save(Iterable<S> entities) {
        for (S s : entities) {
            save(s);
        }
        ArrayList<S> entityList = new ArrayList<>();
        entities.forEach((e) -> entityList.add(e));
        return entityList;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<T> findAll() {
        return (List<T>) getTemplate().findAll(getEntityClass());
    }

    @Override
    public List<T> findAll(Sort sort) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <S extends T> S insert(S entity) {
        getTemplate().insert(entity);
        return entity;
    }

    @Override
    public <S extends T> List<S> insert(Iterable<S> entities) {
        List<S> entityList = (List<S>) entities;
        getTemplate().insert(entityList, getEntityClass());
        return entityList;
    }

    @Override
    public Page<T> findAll(Pageable pageable) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <S extends T> S save(S entity) {
        if (entity.getUid() == null) {
            return insert(entity);
        }

        T findOne = findOne(entity.getUid());
        if (findOne == null) {
            throw new ExpectationException(1, "nothing to update here", null);
        }
        getTemplate().save(entity);

        return entity;
    }

    @Override
    public T findOne(String id) {
        return findByUid(id);
    }

    @Override
    public boolean exists(String id) {
        return findOne(id) == null ? false : true;
    }

    @Override
    public Iterable<T> findAll(Iterable<String> ids) {
        throw new UnsupportedOperationException();
    }

    @Override
    public long count() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void delete(String id) {
        throw new UnsupportedOperationException();

    }

    @Override
    public void delete(T entity) {
        throw new UnsupportedOperationException();

    }

    @Override
    public void delete(Iterable<? extends T> entities) {
        throw new UnsupportedOperationException();
    }

    @SuppressWarnings("unchecked")
    @Override
    public void deleteAll() {
        Query query = Query.query(new Criteria());
        getTemplate().findAllAndRemove(query, getEntityClass());
    }

    @SuppressWarnings("unchecked")
    public T findByUid(String uid) {
        CriteriaDefinition criteria = Criteria.where("uid").is(uid);
        Query query = Query.query(criteria);
        return (T) getTemplate().findOne(query, getEntityClass());
    }

    @Override
    public <S extends T> S findOne(Example<S> example) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <S extends T> Page<S> findAll(Example<S> example, Pageable pageable) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <S extends T> long count(Example<S> example) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <S extends T> boolean exists(Example<S> example) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <S extends T> List<S> findAll(Example<S> example) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <S extends T> List<S> findAll(Example<S> example, Sort sort) {
        throw new UnsupportedOperationException();
    }
}
