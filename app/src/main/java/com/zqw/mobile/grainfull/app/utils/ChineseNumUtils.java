package com.zqw.mobile.grainfull.app.utils;

/**
 * @ProjectName: GrainFullAndroid
 * @Package: com.zqw.mobile.grainfull.app.utils
 * @ClassName: ChineseNumUtils
 * @Description:
 * @Author: WLY
 * @CreateDate: 2023/1/10 17:52
 */
public class ChineseNumUtils {
    /**
     * 中文数字转阿拉伯数字
     * (长度不能超过long最大值)
     *
     * @param chNum 中文数字
     * @return 阿拉伯数字
     * @author Jarry Leo
     */
    public static long ch2Num(String chNum) {
        // 对应下面单位后面多少个零
        int[] numLen = {16, 8, 4, 3, 2, 1};
        // 中文单位
        String[] dw = {"兆", "亿", "万", "千", "百", "十"};
        // 中文单位另一版
        String[] dw1 = {"兆", "亿", "萬", "仟", "佰", "拾"};
        // 中文数字
        String[] sz = {"零", "一", "二", "三", "四", "五", "六", "七", "八", "九", "十"};
        // 中文数字另一版
        String[] sz1 = {"〇", "壹", "贰", "叁", "肆", "伍", "陆", "柒", "捌", "玖", "拾"};

        if (chNum == null) return 0;// 空对象返回0
        for (int i = 0; i < sz.length; i++) { //统一文字版本
            if (i < dw.length)
                chNum = chNum.replaceAll(dw1[i], dw[i]);
            chNum = chNum.replaceAll(sz1[i], sz[i]);
        }

        chNum = chNum.replaceAll("(百.)\\b", "$1十"); //正则替换为了匹配中文类似二百五这样的词
        if (chNum.length() == 1) {
            for (int i = 0; i < sz.length; i++) {
                if (chNum.equals(sz[i])) return i;
            }
            return 0; //中文数字没有这个字
        }
        chNum = strReverse(chNum); //调转输入的字符串

        for (int i = 0; i < dw.length; i++) {
            if (chNum.contains(dw[i])) {
                String part[] = chNum.split(dw[i], 2); //把字符串分割2部分
                long num1 = ch2Num(strReverse(part[1]));
                long num2 = ch2Num(strReverse(part[0]));
                return (long) ((num1 == 0 ? 1 : num1) * Math.pow(10, numLen[i]) + num2);
            }
        }

        char[] c = chNum.toCharArray();
        long sum = 0;
        for (int i = 0; i < c.length; i++) { //一个个解析数字
            String tem = String.valueOf(c[i]); //根据索引转成对应数字
            sum += ch2Num(tem) * Math.pow(10, i);//根据位置给定数字
        }

        return sum;
    }

    /**
     * 字符串掉转
     */
    private static String strReverse(String str) {
        return new StringBuilder(str).reverse().toString();
    }

    /**
     * @param num 人民币阿拉伯数字
     * @return 中文大写人民币
     * @author Jarry Leo
     */
    public static String upperRMB(String num) {
        // 单位
        char dw[] = {'圆', '拾', '佰', '仟', '萬', '拾', '佰', '仟', '亿', '拾', '佰',
                '仟', '萬', '拾', '佰', '仟', '兆', '拾', '佰', '仟', '萬', '拾', '佰',
                '仟', '亿', '拾', '佰', '仟', '萬', '拾', '佰', '仟'};

        // 中文数字
        char sz[] = {'零', '壹', '贰', '叁', '肆', '伍', '陆', '柒', '捌', '玖'};

        // 数字转成字符数组
        char s[] = num.toCharArray();

        // 创建字符串生成器
        StringBuilder sb = new StringBuilder();
        int index = 0;
        for (int i = s.length - 1; i >= 0; i--) {
            // 倒着插入中文数字和单位
            sb.append("" + dw[index++] + sz[s[i] - 48]);

        }
        // 字符串反转
        String str = sb.reverse().toString();
        String lastStr;

        do {
            // 语法调整
            lastStr = str;
            str = str.replaceAll("零[零拾佰仟]", "零");
            str = str.replaceAll("零([萬亿兆])", "$1零");
            str = str.replaceAll("亿萬", "亿");
            str = str.replaceAll("兆[萬万]", "兆");
            str = str.replaceAll("零圆", "圆");
        } while (!lastStr.equals(str));
        return str;
    }
}
