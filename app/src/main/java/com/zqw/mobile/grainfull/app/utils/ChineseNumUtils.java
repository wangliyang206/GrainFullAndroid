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
    public static String[] chineseCode = {"零", "壹", "贰", "叁", "肆", "伍", "陆", "柒", "捌", "玖"};
    private static String unit[][] = { { "", "万", "亿"}, { "", "拾", "佰", "仟"}};
    private static String[] elseUnit = {"分", "角"};

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
     * 人民币阿拉伯数字
     */
    public static String getRmb(double num) {
        String s = "";
        String numString = String.format("%.2f", num);
        String[] strings = numString.split("\\.");
        if (num == 0) {
            s = "零圆整";
        } else {
            s += getRmbValue(false, strings[0]) + getRmbValue(true, strings[1]);
        }
        return s;
    }

    /**
     * 返回格式化人民币结果
     */
    private static String getRmbValue(boolean isPoint, String snum) {
        String result = "";
        String[] numArray = snum.split("");
        //判断是整数部分还是小数部分
        if (isPoint) {
            //小数部分
            //判断是否是00
            if (snum.equals("00")) {
                // 00加圆整
                result += "圆整";
            } else {
                result += "圆";
                //非00需要加角分单位
                for (int i = 0; i < numArray.length; i++) {
                    int value = Integer.valueOf(numArray[i]) % 10;
                    result += (chineseCode[value] + elseUnit[numArray.length - 1 - i]);
                }
            }
        } else {
            //整数部分
            int integerPart = (int) Math.floor(Double.valueOf(snum));
            for (int i = 0; i < unit[0].length && integerPart > 0; i++) {
                String p = "";
                for (int j = 0; j < unit[1].length; j++) {
                    //每次除以10确定当前大写汉字是什么
                    p = chineseCode[integerPart % 10] + unit[1][j] + p;
                    integerPart = integerPart / 10;
                }
                //使用正则去判断0
                result = p.replaceAll("(零.)*零$", "") + unit[0][i] + result;
            }
        }
        //把多余的零替换掉
        result = result.replaceAll("(零.)+", "零");
        if (result.substring(0, 1).equals("零")) {
            result = result.substring(1, result.length());
        }
        return result;
    }
}
