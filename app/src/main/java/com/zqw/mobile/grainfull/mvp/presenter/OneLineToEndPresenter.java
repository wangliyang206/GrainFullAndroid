package com.zqw.mobile.grainfull.mvp.presenter;

import androidx.annotation.Nullable;

import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.mvp.BasePresenter;
import com.zqw.mobile.grainfull.app.utils.ThreadUtil;
import com.zqw.mobile.grainfull.mvp.contract.OneLineToEndContract;
import com.zqw.mobile.grainfull.mvp.model.entity.RoadOnePen;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import javax.inject.Inject;

import me.jessyan.rxerrorhandler.core.RxErrorHandler;

/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 2023/02/01 14:15
 * ================================================
 */
@ActivityScope
public class OneLineToEndPresenter extends BasePresenter<OneLineToEndContract.Model, OneLineToEndContract.View> {
    @Inject
    RxErrorHandler mErrorHandler;

    // 用来开始寻路
    public boolean startToFindRoad = false;
    // 用来判断是否获取了一张指定图的指定数量的路径
    public boolean findingRoad = false;
    // 行数、列数、障碍物
    private int rows, columns, difficulties;

    private Random random = new Random();
    private Comparator<Integer> integerComparator;

    public interface checkBooleanInterface<t> {
        boolean checkBoolean(t obj);
    }

    @Inject
    public OneLineToEndPresenter(OneLineToEndContract.Model model, OneLineToEndContract.View rootView) {
        super(model, rootView);
    }

    /**
     * 初始化
     */
    public void initGirdRoad(final int initRows, final int initColums, final int initDifficulties) {
        this.rows = initRows;
        this.columns = initColums;
        this.difficulties = initDifficulties;

        ThreadUtil.getInstance().addRunableToSingleThead("initGirdRoad", () -> {
            RoadOnePen aRoad;
            // 先从数据库中找，获取数据库中第一条未通关的关卡
            aRoad = mModel.getSavedYibi(initRows, initColums, initDifficulties);

            // 找不到再直接生成
            if (aRoad == null) {
                aRoad = getAppointedRoad(false);
            }

            final RoadOnePen road = aRoad;

            // 是否通关
            final boolean isPassed;
            if (road != null) {
                // 检查是否通关
                isPassed = mModel.checkPassedYibi(road);
            } else {
                isPassed = false;
            }

            // 已通关
            if (isPassed) {
                // 如果已通关，则重新查询或生成数据。
                initGirdRoad(initRows, initColums, initDifficulties);
                return;
            }

            mRootView.getActivity().runOnUiThread(() -> {
                if (road != null) {
                    // 显示关卡
                    int num = mModel.getPassedCount(rows, columns, difficulties);
                    num = num + 1;
                    mRootView.showLevel(String.valueOf(num));
                    // 加载游戏
                    mRootView.loadGame(road);
                } else {
                    mRootView.showMessage("取消构图");
                }
            });
        });
    }

    /**
     * 根据行列数和难度获取一条指定路径，有可能很长时间都得不到路径，所以最好放在子线程进行
     */
    public RoadOnePen getAppointedRoad(boolean passPassed) {
        startToFindRoad = true;
        RoadOnePen road = null;
        while (startToFindRoad) {
            // 随机产生 “障碍格” 下标数
            final int sp = random.nextInt(rows * columns);
            // 行 * 列 = 数中扣除 障碍下标，得出布局。
            List<Integer> pList = new ArrayList<>();
            for (int i = 0; i < rows * columns; i++) {
                if (i != sp) pList.add(i);
            }
            // 随机打乱原来的顺序
            Collections.shuffle(pList);
            List<List<Integer>> comForbiddensList = comNumLists(null, 20, pList.toArray(new Integer[0]), difficulties, obj -> {
                return !mModel.checkErrorYibi(rows, columns, obj, sp);
            });
            for (List<Integer> forbiddenPositionList : comForbiddensList) {
                if (!startToFindRoad) break;
                if (mModel.checkErrorYibi(rows, columns, getListString(forbiddenPositionList), sp))
                    continue;
                road = getARoad(rows, columns, sp, forbiddenPositionList, passPassed);
            }
        }
        return road;
    }

    /**
     * 根据行列数和起始点以及禁止点尝试获取一条可行路径
     */
    public RoadOnePen getARoad(int rows, int columns, int startPosition, List<Integer> forbiddenPositionList, boolean passPassed) {
        List<RoadOnePen> roads = new ArrayList<>();
        RoadOnePen road = null;
        findingRoad = true;
        findRoads(rows, columns, startPosition, null, forbiddenPositionList, roads, null);
        if (roads.size() > 0 && (road = roads.get(0)) != null) {
            if (passPassed && mModel.checkPassedYibi(road)) {
                return null;
            }
            startToFindRoad = false;
        }
        return road;
    }

    public void findRoads(final int row, final int column, int curPosition, @Nullable List<Integer> choosedPositions, final List<Integer> forbiddenList, @Nullable List<RoadOnePen> roads, int[] nos) {
        if (!startToFindRoad || !findingRoad) return;

        if (forbiddenList.indexOf(curPosition) != -1) {
            return;
        }

        if (choosedPositions == null) {
            choosedPositions = new ArrayList<>();
        }
        if (choosedPositions.isEmpty()) {
            nos = new int[]{0};
            choosedPositions.add(curPosition);
        }

        if (nos == null || nos.length != 1) {
            nos = new int[]{0};
        }

        // 当路线完成时，退出方法，nos的数不会减
        if ((choosedPositions.size() + forbiddenList.size()) == row * column) {
            //得到的路径只要不重复全扔到数据库，以备后用
            final List<Integer> cps = new ArrayList<>(choosedPositions);
            ThreadUtil.getInstance().runOnChildThread(() -> mModel.insertSavedYibi(new RoadOnePen(row, column, cps)));

            if (roads != null) {
                roads.add(new RoadOnePen(rows, columns, choosedPositions));
            }

            findingRoad = false;
            return;
        }


        //四方寻路，且路线互不影响
        if (findUpRoad(curPosition - column, choosedPositions, forbiddenList)) {
            nos[0] = nos[0] + 1;
            List<Integer> nextChoosedPositions = new ArrayList<>(choosedPositions);
            nextChoosedPositions.add(curPosition - column);
            findRoads(row, column, curPosition - column, nextChoosedPositions, forbiddenList, roads, nos);
        }
        if (findLeftRoad(curPosition - 1, column, choosedPositions, forbiddenList)) {
            nos[0] = nos[0] + 1;
            List<Integer> nextChoosedPositions = new ArrayList<>(choosedPositions);
            nextChoosedPositions.add(curPosition - 1);
            findRoads(row, column, curPosition - 1, nextChoosedPositions, forbiddenList, roads, nos);
        }
        if (findDownRoad(curPosition + column, row * column, choosedPositions, forbiddenList)) {
            nos[0] = nos[0] + 1;
            List<Integer> nextChoosedPositions = new ArrayList<>(choosedPositions);
            nextChoosedPositions.add(curPosition + column);
            findRoads(row, column, curPosition + column, nextChoosedPositions, forbiddenList, roads, nos);
        }
        if (findRightRoad(curPosition + 1, row * column, column, choosedPositions, forbiddenList)) {
            nos[0] = nos[0] + 1;
            List<Integer> nextChoosedPositions = new ArrayList<>(choosedPositions);
            nextChoosedPositions.add(curPosition + 1);
            findRoads(row, column, curPosition + 1, nextChoosedPositions, forbiddenList, roads, nos);
        }

        //当前路线未完成才减1
        nos[0] = nos[0] - 1;

        //nos的数≤0时，即所有路线都走不通，记录错误图，一张图由起点和障碍位置确定
        if (nos[0] <= 0) {
            final int sp;
            if (choosedPositions.size() > 0) sp = choosedPositions.get(0);
            else sp = curPosition;
            //记录错误图，减少下次寻路时间
            ThreadUtil.getInstance().runOnChildThread(() -> mModel.insertErrorYibi(row, column, getListString(forbiddenList), sp));
        }

    }

    /**
     * 向上寻路
     */
    private boolean findUpRoad(int upPosition, List<Integer> choosedPositions, List<Integer> forbiddenCount) {
        return upPosition >= 0
                && choosedPositions.lastIndexOf(upPosition) == -1
                && forbiddenCount.indexOf(upPosition) == -1;
    }

    /**
     * 向左寻路
     */
    private boolean findLeftRoad(int leftPosition, int column, List<Integer> choosedPositions, List<Integer> forbiddenCount) {
        return leftPosition >= 0
                && choosedPositions.lastIndexOf(leftPosition) == -1
                && forbiddenCount.indexOf(leftPosition) == -1
                && (leftPosition + 1) % column != 0;
    }

    /**
     * 向下寻路
     */
    private boolean findDownRoad(int downPosition, int size, List<Integer> choosedPositions, List<Integer> forbiddenCount) {
        return downPosition < size
                && choosedPositions.lastIndexOf(downPosition) == -1
                && forbiddenCount.indexOf(downPosition) == -1;
    }

    /**
     * 向右寻路
     */
    private boolean findRightRoad(int rightPosition, int size, int column, List<Integer> choosedPositions, List<Integer> forbiddenCount) {
        return rightPosition < size
                && choosedPositions.lastIndexOf(rightPosition) == -1
                && forbiddenCount.indexOf(rightPosition) == -1
                && rightPosition % column != 0;
    }

    /**
     * 求组合
     *
     * @param result           最终结果
     * @param maxSize          最大数
     * @param nums             布局数
     * @param maxSelect        障碍数
     * @param booleanInterface
     */
    public List<List<Integer>> comNumLists(List<List<Integer>> result, int maxSize, Integer[] nums, int maxSelect, checkBooleanInterface<String> booleanInterface) {
        nums = removeRepeatNum(nums);

        if (result == null) result = new ArrayList<>();
        int n = nums.length;
        if (maxSelect > n) {
            maxSelect = n;
        }
        if (maxSelect <= 0) maxSelect = Math.min(1, n);

        final int resultSize = (int) getNumberComSum(nums.length, maxSelect);
        int startSum = resultSize > 0 ? random.nextInt(resultSize) : 0;
        if (startSum + maxSize > resultSize) startSum = resultSize - maxSize;
        if (startSum < 0) startSum = 0;


        //标识
        Integer[] bs = new Integer[n];
        for (int i = 0; i < n; i++) {
            bs[i] = 0;
        }
        // 初始化
        for (int i = 0; i < maxSelect; i++) {
            bs[i] = 1;
        }
        boolean flag = nums.length > 0;

        int opAddSum = 0;
        while (flag) {
            opAddSum++;
            if (result.size() >= maxSize) return result;
            boolean tempFlag = true;
            int pos = 0;
            int sum = 0;
            if (opAddSum >= startSum) {
                final List<Integer> r = getComNumList(bs, nums, maxSelect);
                sortIntegerList(r);
                if (booleanInterface != null) {
                    if (booleanInterface.checkBoolean(getListString(r))) {
                        result.add(r);
                    }
                } else {
                    result.add(r);
                }
            }

            // 首先找到第一个10组合，然后变成01
            for (int i = 0; i < n - 1; i++) {
                if (bs[i] == 1 && bs[i + 1] == 0) {
                    bs[i] = 0;
                    bs[i + 1] = 1;
                    pos = i;
                    break;
                }
            }

            // 将左边的1全部移动到数组的最左边
            for (int i = 0; i < pos; i++) {
                if (bs[i] == 1) {
                    sum++;
                }
            }
            for (int i = 0; i < pos; i++) {
                if (i < sum) {
                    bs[i] = 1;
                } else {
                    bs[i] = 0;
                }
            }

            // 检查是否所有的1都移动到了最右边
            for (int i = n - maxSelect; i < n; i++) {
                if (bs[i] == 0) {
                    tempFlag = false;
                    break;
                }
            }
            if (!tempFlag) {
                flag = true;
            } else {
                flag = false;
                if (maxSelect != n) {//两者相等时上面的位移操作不会有效，即数字没变，所以不添加
                    final List<Integer> r = getComNumList(bs, nums, maxSelect);
                    sortIntegerList(r);
                    if (booleanInterface != null) {
                        if (booleanInterface.checkBoolean(getListString(r))) {
                            result.add(r);
                        }
                    } else {
                        result.add(r);
                    }
                }
            }

        }
        return result;
    }

    /**
     * 删除重复编号
     */
    public Integer[] removeRepeatNum(Integer... ints) {
        if (ints == null) return new Integer[0];
        if (ints.length == 0) return ints;
        List<Integer> list = new ArrayList<>();
        for (Integer anInt : ints) {
            if (!list.contains(anInt)) {
                list.add(anInt);
            }
        }
        return list.toArray(new Integer[0]);
    }

    /**
     * 获取编号Com Sum
     */
    public long getNumberComSum(int down, int up) {
        if (down < up) return 0;
        if (down == up) return 1;
        final long o1 = getNumberQueueSum(down), o2 = getNumberQueueSum(up), o3 = getNumberQueueSum(down - up);
        if (o2 * o3 == 0) return 0;
        return o1 / (o2 * o3);
    }

    /**
     * 获取队列总数
     */
    public long getNumberQueueSum(int i) {
        if (i <= 0) return 0;
        long sum = 1L;
        while (i > 0) {
            sum = i * sum;
            i--;
        }
        return sum;
    }

    /**
     * 获取通讯号码列表
     */
    private List<Integer> getComNumList(Integer[] bs, Integer[] a, int m) {
        Integer[] result = new Integer[m];
        int pos = 0;
        for (int i = 0; i < bs.length && pos < m; i++) {
            if (bs[i] == 1) {
                result[pos] = a[i];
                pos++;
            }
        }
        return Arrays.asList(result);
    }

    /**
     * 排序整数列表
     */
    public void sortIntegerList(List<Integer> list) {
        if (isListEmpty(list)) return;
        if (integerComparator == null) {
            synchronized (OneLineToEndPresenter.class) {
                integerComparator = (o1, o2) -> {
                    if (o1 == null && o2 == null) return 0;
                    if (o1 == null) return -1;
                    if (o2 == null) return 1;
                    return o1.compareTo(o2);
                };
            }
        }
        Collections.sort(list, integerComparator);
    }

    /**
     * 判断List是否为空
     */
    public boolean isListEmpty(List... lists) {
        if (lists == null) return true;
        for (List l : lists) {
            if (l != null && l.size() != 0) {
                return false;
            }
        }
        return true;
    }

    /**
     * 获取列表字符串
     */
    public String getListString(List list) {
        StringBuilder roadString = new StringBuilder();
        if (list != null) {
            for (int p = 0; p < list.size(); p++) {
                roadString.append(list.get(p));
                if (p != list.size() - 1) {
                    roadString.append(",");
                }
            }
        }
        return roadString.toString();
    }

    /**
     * 成功通关，保存已通关内容
     */
    public void insertPassedYibi(RoadOnePen road) {
        mModel.insertPassedYibi(road);
    }

    /**
     * 收集统计信息
     */
    public void getCollection() {
        int num = mModel.getPassedCount(rows, columns, difficulties);
        mRootView.showCollection(rows, columns, difficulties, num);
    }

    /**
     * 清空关卡数据
     */
    public void clearPassedData() {
        // 清理已通过的关卡
        mModel.clearPassedYibi();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mErrorHandler = null;
        this.random = null;
        this.integerComparator = null;
    }
}