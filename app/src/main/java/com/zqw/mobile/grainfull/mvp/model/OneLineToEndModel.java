package com.zqw.mobile.grainfull.mvp.model;

import android.text.TextUtils;

import com.jess.arms.cj.ApiOperator;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;
import com.zqw.mobile.grainfull.app.greendao.OnePenErrorDao;
import com.zqw.mobile.grainfull.app.greendao.OnePenPassedDao;
import com.zqw.mobile.grainfull.app.greendao.OnePenSavedDao;
import com.zqw.mobile.grainfull.app.utils.DaoManager;
import com.zqw.mobile.grainfull.mvp.contract.OneLineToEndContract;
import com.zqw.mobile.grainfull.mvp.model.entity.OnePenError;
import com.zqw.mobile.grainfull.mvp.model.entity.OnePenPassed;
import com.zqw.mobile.grainfull.mvp.model.entity.OnePenSaved;
import com.zqw.mobile.grainfull.mvp.model.entity.RoadOnePen;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.inject.Inject;

/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 2023/02/01 14:15
 * ================================================
 */
@ActivityScope
public class OneLineToEndModel extends BaseModel implements OneLineToEndContract.Model {
    @Inject
    ApiOperator apiOperator;                                                                        // 数据转换
    @Inject
    DaoManager mDaoManager;

    private Random random = new Random();

    @Inject
    public OneLineToEndModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.apiOperator = null;
        this.mDaoManager = null;
        this.random = null;
    }

    @Override
    public RoadOnePen getSavedYibi(int rows, int columns, int difficulties) {
        List<OnePenSaved> mList = mDaoManager.getDaoSession().getOnePenSavedDao().queryBuilder().where(
                OnePenSavedDao.Properties.Rows.eq(rows),
                OnePenSavedDao.Properties.Columns.eq(columns),
                OnePenSavedDao.Properties.Difficulties.eq(difficulties)
        ).list();

        RoadOnePen mRoadOnePen = null;
        if (mList.size() > 0) {
            OnePenSaved info = mList.get(random.nextInt(mList.size()));
            String[] roadpositions = info.getRoad().split("[,]");
            mRoadOnePen = new RoadOnePen(info.getRows(), info.getColumns(), getIntListFromStrs(roadpositions));
        }
        return mRoadOnePen;
    }

    /**
     * 转变格式
     */
    public List<Integer> getIntListFromStrs(String[] strings) {
        final List<Integer> list = new ArrayList<>();
        for (String p : strings) {
            try {
                list.add(Integer.parseInt(p));
            } catch (Exception e) {
                list.clear();
                break;
            }
        }
        return list;
    }

    @Override
    public void insertErrorYibi(int rows, int columns, String difficultiesStr, int startPosition) {
        if (checkErrorYibi(rows, columns, difficultiesStr, startPosition)) return;
        mDaoManager.getDaoSession().getOnePenErrorDao().insert(new OnePenError(rows, columns, difficultiesStr, startPosition));
    }

    @Override
    public boolean checkErrorYibi(int rows, int columns, String difficultiesStr, int startPosition) {
        if (TextUtils.isEmpty(difficultiesStr)) return true;
        long count = mDaoManager.getDaoSession().getOnePenErrorDao().queryBuilder().where(
                OnePenErrorDao.Properties.Rows.eq(rows),
                OnePenErrorDao.Properties.Columns.eq(columns),
                OnePenErrorDao.Properties.Difficulties.eq(difficultiesStr),
                OnePenErrorDao.Properties.StartPosition.eq(startPosition)
        ).count();
        return count > 0;
    }

    @Override
    public void insertPassedYibi(RoadOnePen road) {
        if (checkPassedYibi(road) || road == null) return;
        mDaoManager.getDaoSession().getOnePenPassedDao().insert(new OnePenPassed(road.getRows(), road.getColumns(), road.getDifficulties(), road.getRoadString()));
    }


    @Override
    public boolean checkPassedYibi(RoadOnePen road) {
        if (road == null) return false;

        long count = mDaoManager.getDaoSession().getOnePenPassedDao().queryBuilder().where(
                OnePenPassedDao.Properties.Rows.eq(road.getRows()),
                OnePenPassedDao.Properties.Columns.eq(road.getColumns()),
                OnePenPassedDao.Properties.Difficulties.eq(road.getDifficulties()),
                OnePenPassedDao.Properties.Road.eq(road.getRoadString())
        ).count();
        return count > 0;
    }

    @Override
    public void insertSavedYibi(RoadOnePen road) {
        if (checkSavedYibi(road)) return;
        mDaoManager.getDaoSession().getOnePenSavedDao().insert(new OnePenSaved(road.getRows(), road.getColumns(), road.getDifficulties(), road.getRoadString()));
    }

    @Override
    public boolean checkSavedYibi(RoadOnePen road) {
        long count = mDaoManager.getDaoSession().getOnePenSavedDao().queryBuilder().where(
                OnePenSavedDao.Properties.Rows.eq(road.getRows()),
                OnePenSavedDao.Properties.Columns.eq(road.getColumns()),
                OnePenSavedDao.Properties.Difficulties.eq(road.getDifficulties()),
                OnePenSavedDao.Properties.Road.eq(road.getRoadString())
        ).count();
        return count > 0;
    }
}