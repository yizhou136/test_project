package com.zy.nut.relayer.server.dao;

import java.util.List;

/**
 * Created by zhougb on 2016/5/9.
 */
public interface GenericDao {

    <G> G       get(long id, Class<G> clazz);
    <G> List<G> getAll(Class<G> clazz);
    <G> long    save (G g, Class<G> clazz);
    <G> void    update(G g, Class<G> clazz);
    <G> void    batchUpdate(List<G> list, Class<G> clazz);
    <G> void    delete(long id, Class<G> clazz);
    <G> void    deleteAll(Class<G> clazz);

}
