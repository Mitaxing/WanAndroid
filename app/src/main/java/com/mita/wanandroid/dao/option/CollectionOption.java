package com.mita.wanandroid.dao.option;

import android.util.Log;

import com.mita.wanandroid.dao.GreenDaoManager;
import com.mita.wanandroid.dao.UserCollection;
import com.mita.wanandroid.dao.UserCollectionDao;

import java.util.List;

/**
 * 用户收藏数据库操作
 *
 * @author Mita
 * @date 2018/5/7 16:01
 **/
public class CollectionOption {

    /**
     * 保存最新收藏
     *
     * @param ids 收藏的id
     */
    public static void saveUserCollectionId(List<Integer> ids) {
        UserCollectionDao dao = getContactDao();
        dao.deleteAll();
        for (Integer id : ids) {
            Log.i("xing", "已经收藏的ID：" + id);
            UserCollection collection = new UserCollection();
            collection.setCollectId(id);
            dao.save(collection);
        }
    }

    /**
     * 判断是否收藏了
     *
     * @param id ID
     * @return boolean
     */
    public static boolean hasCollected(int id) {
        UserCollectionDao dao = getContactDao();
        long count = dao.queryBuilder().where(UserCollectionDao.Properties.CollectId.eq(id)).count();
        return count > 0;
    }

    /**
     * 保存收藏ID
     *
     * @param id ID
     */
    public static void saveCollectionId(int id) {
        UserCollectionDao dao = getContactDao();
        UserCollection collection = new UserCollection();
        collection.setCollectId(id);
        dao.save(collection);
    }

    /**
     * 取消收藏ID
     *
     * @param id ID
     */
    public static void deleteCollectionId(int id) {
        UserCollectionDao dao = getContactDao();
        UserCollection collection = dao.queryBuilder().where(UserCollectionDao.Properties.CollectId.eq(id)).unique();
        if (collection != null) {
            dao.delete(collection);
        }
    }

    private static UserCollectionDao getContactDao() {
        return GreenDaoManager.getInstance().getSession().getUserCollectionDao();
    }
}
