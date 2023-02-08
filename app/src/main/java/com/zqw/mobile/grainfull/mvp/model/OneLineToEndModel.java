package com.zqw.mobile.grainfull.mvp.model;

import android.text.TextUtils;

import com.jess.arms.cj.ApiOperator;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;
import com.zqw.mobile.grainfull.app.greendao.OnePenErrorDao;
import com.zqw.mobile.grainfull.app.greendao.OnePenLevelDao;
import com.zqw.mobile.grainfull.app.utils.CommonUtils;
import com.zqw.mobile.grainfull.app.utils.DaoManager;
import com.zqw.mobile.grainfull.mvp.contract.OneLineToEndContract;
import com.zqw.mobile.grainfull.mvp.model.entity.OnePenError;
import com.zqw.mobile.grainfull.mvp.model.entity.OnePenLevel;
import com.zqw.mobile.grainfull.mvp.model.entity.RoadOnePen;

import java.util.ArrayList;
import java.util.List;

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

    @Inject
    public OneLineToEndModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.apiOperator = null;
        this.mDaoManager = null;
    }

    /**
     * 获取数据库中第一条未通关的关卡
     */
    @Override
    public RoadOnePen getSavedYibi(int rows, int columns, int difficulties) {
        List<OnePenLevel> mList = mDaoManager.getDaoSession().getOnePenLevelDao().queryBuilder().where(
                OnePenLevelDao.Properties.Rows.eq(rows),
                OnePenLevelDao.Properties.Columns.eq(columns),
                OnePenLevelDao.Properties.Difficulties.eq(difficulties),
                OnePenLevelDao.Properties.Passed.eq(false)
        ).list();

        RoadOnePen mRoadOnePen = null;
        if (mList.size() > 0) {
            OnePenLevel info = mList.get(0);
            String[] roadPositions = info.getRoad().split("[,]");
            mRoadOnePen = new RoadOnePen(info.get_no(), info.getRows(), info.getColumns(), getIntListFromStrs(roadPositions));
        }
        return mRoadOnePen;
    }

    /**
     * 记录错误图，减少下次寻路时间
     */
    @Override
    public void insertErrorYibi(int rows, int columns, String difficultiesStr, int startPosition) {
        if (checkErrorYibi(rows, columns, difficultiesStr, startPosition)) return;
        mDaoManager.getDaoSession().getOnePenErrorDao().insert(new OnePenError(rows, columns, difficultiesStr, startPosition));
    }

    /**
     * 检查是否是错误路线
     */
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

    /**
     * 保存通关记录
     */
    @Override
    public void insertPassedYibi(RoadOnePen road) {
        // 先查询
        List<OnePenLevel> mList = mDaoManager.getDaoSession().getOnePenLevelDao().queryBuilder().where(
                OnePenLevelDao.Properties.Rows.eq(road.getRows()),
                OnePenLevelDao.Properties.Columns.eq(road.getColumns()),
                OnePenLevelDao.Properties.Difficulties.eq(road.getDifficulties()),
                OnePenLevelDao.Properties.Passed.eq(false)
        ).list();

        if (CommonUtils.isNotEmpty(mList)) {
            // 有数据，则直接修改
            OnePenLevel mOnePenLevel = mList.get(0);
            mOnePenLevel.setPassed(true);
            // 再变更通关状态
            mDaoManager.getDaoSession().getOnePenLevelDao().update(mOnePenLevel);
        } else {
            // 无数据，则直接保存
            mDaoManager.getDaoSession().getOnePenLevelDao().insert(new OnePenLevel(road.getRows(), road.getColumns(), road.getDifficulties(), road.getRoadString(), true));
        }
    }

    /**
     * 检查当前关卡是否通关
     */
    @Override
    public boolean checkPassedYibi(RoadOnePen road) {
        if (road == null) return false;

        long count = mDaoManager.getDaoSession().getOnePenLevelDao().queryBuilder().where(
                OnePenLevelDao.Properties.Rows.eq(road.getRows()),
                OnePenLevelDao.Properties.Columns.eq(road.getColumns()),
                OnePenLevelDao.Properties.Difficulties.eq(road.getDifficulties()),
                OnePenLevelDao.Properties.Road.eq(road.getRoadString()),
                OnePenLevelDao.Properties.Passed.eq(true)
        ).count();
        return count > 0;
    }

    /**
     * 生成关卡，并保存到数据库中
     */
    @Override
    public void insertSavedYibi(RoadOnePen road) {
        if (checkSavedYibi(road)) return;
        mDaoManager.getDaoSession().getOnePenLevelDao().insert(new OnePenLevel(road.getRows(), road.getColumns(), road.getDifficulties(), road.getRoadString(), false));
    }

    /**
     * 检查关卡是否保存重复
     */
    @Override
    public boolean checkSavedYibi(RoadOnePen road) {
        long count = mDaoManager.getDaoSession().getOnePenLevelDao().queryBuilder().where(
                OnePenLevelDao.Properties.Rows.eq(road.getRows()),
                OnePenLevelDao.Properties.Columns.eq(road.getColumns()),
                OnePenLevelDao.Properties.Difficulties.eq(road.getDifficulties()),
                OnePenLevelDao.Properties.Road.eq(road.getRoadString())
        ).count();
        return count > 0;
    }

    /**
     * 清理已生成的关卡数据
     */
    @Override
    public void clearPassedYibi() {
        mDaoManager.getDaoSession().getOnePenLevelDao().deleteAll();
    }

    /**
     * 获取过关总数
     */
    @Override
    public int getPassedCount(int rows, int columns, int difficulties) {
        long count = mDaoManager.getDaoSession().getOnePenLevelDao().queryBuilder().where(
                OnePenLevelDao.Properties.Rows.eq(rows),
                OnePenLevelDao.Properties.Columns.eq(columns),
                OnePenLevelDao.Properties.Difficulties.eq(difficulties),
                OnePenLevelDao.Properties.Passed.eq(true)
        ).count();
        return (int) count;
    }

    /**
     * 转变格式
     */
    private List<Integer> getIntListFromStrs(String[] strings) {
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
}