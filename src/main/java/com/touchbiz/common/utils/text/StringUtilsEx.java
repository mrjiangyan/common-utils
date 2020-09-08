package com.touchbiz.common.utils.text;

import com.touchbiz.common.utils.web.UrlUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.util.HtmlUtils;

import java.io.CharArrayWriter;
import java.io.PrintWriter;
import java.util.*;
import java.util.regex.Pattern;

/**
 * 扩展common string
 */
public class StringUtilsEx extends StringUtils {

    /**
     * ","
     */
    private static final String DIVIDER = ",";
    private final static Pattern CALLBACK_REGEX = Pattern.compile("^[\\w$]+$");
    private static final char CHAR_INTER = '\n';
    private static final char CHAR_LT = '<';
    private static final char CHAR_GT = '>';
    private static final char CHAR_QUOT = '"';
    private static final char CHAR_AMP = '&';
    private static final char CHAR_BlANK = ' ';
    private static final char CHAR_DIVIDE = '/';
    private static final char QM_DBC = '?';
    private static final char QM_SBC = '？';
    private static final char COLON_DBC = ':';
    private static final char COLON_SBC = '：';
    private static final char EQUAL_DBC = '=';
    private static final char EQUAL_SBC = '＝';
    private static final char AMP_DBC = '&';
    private static final char AMP_SBC = '＆';
    private static final char SQM_DBC = '\'';
    private static final char SQM_SBC = '‘';
    private static final char DQM_DBC = '"';
    private static final char DQM_SBC = '”';

    /**
     * <pre>
     * http://www.aaa.com?returnURL=www.g.cn&amp;p1=a&amp;p2=c
     * 解析为
     *   returnURL,www.g.cn
     *   p1,a
     *   p2,c
     * </pre>
     *
     * @param query
     * @return
     */
    public static Map<String, String> uriQueryToMap(String query) {
        return uriQueryToMap(query, false, null);
    }

    public static Map<String, String> uriQueryToMapWithDecode(String query) {
        return uriQueryToMap(query, "UTF-8");
    }

    public static Map<String, String> uriQueryToMap(String query, String charset) {
        return uriQueryToMap(query, true, charset);
    }

    public static Map<String, String> uriQueryToMap(String query,
                                                    boolean encode, String charset) {
        return uriQueryToMap(query, encode, charset, false);
    }

    public static Map<String, String> uriQueryToMap(String query,
                                                    boolean encode, String charset, boolean keepOrder) {
        Map<String, String> parameterMap;
        if (keepOrder) {
            parameterMap = new LinkedHashMap<>();
        } else {
            parameterMap = new HashMap<>();
        }
        if (null == query) {
            return parameterMap;
        }
        String paramString = query;
        int p = query.indexOf("?");
        if (p > -1) {
            paramString = query.substring(p + 1);
        }
        if (!StringUtils.isEmpty(paramString)) {
            String[] params = StringUtils.split(paramString, "&");
            String key;
            String value;
            for (String param : params) {
                if (!StringUtils.isEmpty(param)) {
                    p = param.indexOf("=");
                    if (p > 0) {
                        key = param.substring(0, p);
                        value = param.substring(p + 1);
                        if (encode) {
                            parameterMap.put(key, UrlUtils.decodeUrl(value, charset));
                        } else {
                            parameterMap.put(key, value);
                        }
                    }
                }
            }
        }
        return parameterMap;
    }

    /**
     * 只进行一次分割，例如： A=B=C时，只分割第一个=
     *
     * @param query
     * @param separator
     * @return
     */
    public static String[] splitOnce(String query, String separator) {
        int p = query.indexOf(separator);
        if (p > 0) {
            String key = query.substring(0, p);
            String value = query.substring(p + 1);
            return new String[]{key, value};
        } else {
            return new String[]{query};
        }
    }

    /**
     * 一个针对URI query的反向操作，不进行URL Encode
     *
     * @param map
     * @return
     */
    public static String mapToUriQuery(Map<String, String> map) {
        return mapToUriQuery(map, false, null);
    }

    public static String mapToUriQueryWithEncode(Map<String, String> map) {
        return mapToUriQuery(map, "UTF-8");
    }

    public static String mapToUriQuery(Map<String, String> map, String charset) {
        return mapToUriQuery(map, true, charset);
    }

    public static String mapToUriQuery(Map<String, String> map, boolean encode,
                                       String charset) {
        if (null == map) {
            return "";
        }
        StringBuilder buff = new StringBuilder();
        Iterator<String> keys = map.keySet().iterator();
        String key;
        String value;
        while (keys.hasNext()) {
            key = keys.next();
            value = map.get(key);
            if (buff.length() > 0) {
                buff.append("&");
            }
            if (encode) {
                buff.append(key).append("=").append(
                        UrlUtils.encodeUrl(value, charset));
            } else {
                buff.append(key).append("=").append(value);
            }
        }
        return buff.toString();
    }

    /**
     * 对一个数组内地值进行trim
     *
     * @param values
     * @return
     */
    public static String[] trim(String[] values) {
        String[] tv = new String[values.length];
        int i = 0;
        for (String vv : values) {
            tv[i++] = StringUtils.trimWhitespace(vv);
        }
        return tv;
    }

    /**
     * 字符串比较
     *
     * @param var1
     * @param var2
     * @return
     */
    @java.lang.SuppressWarnings("squid:S1201")
    public static boolean equals(String var1, String var2) {
        return var1 != null && var1.equals(var2);
    }

    /**
     * 当一个值与一组值进行比较时，只要有一个target与me相等，则返回true
     *
     * @param me
     * @param targets
     * @return
     */
    public static boolean equalsOr(String me, String[] targets) {
        for (String target : targets) {
            if (Objects.equals(me, target)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 当一个值与一组值进行比较时，只要有一个target与me相等，则返回true
     *
     * @param me
     * @param targets
     * @return
     */
    public static boolean equalsOr(String me, List<String> targets) {
        for (String target : targets) {
            if (Objects.equals(me, target)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 当一个值与一组值进行比较时，只要有一个target与me相等，则返回true
     *
     * @param me
     * @param targets 以','进行分割
     * @return
     */
    public static boolean equalsOr(String me, String targets) {
        return !StringUtils.isEmpty(targets) && equalsOr(me, split(targets, ","));
    }

    /**
     * 当一个值与一组值进行比较时，必须所有target与me相等，才返回true
     *
     * @param me
     * @param targets
     * @return
     */
    public static boolean equalsAnd(String me, String[] targets) {
        for (String target : targets) {
            if (!Objects.equals(me, target)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 如果指定的target为NULL，使用默认值
     *
     * @param target
     * @param defaultValue
     * @return
     */
    public static String trim(String target, String defaultValue) {
        if (StringUtils.isEmpty(target)) {
            return defaultValue.trim();
        }
        return target.trim();
    }

    /**
     * 方法 toHtml 可以把源字符串中的不能在网页中正确显示的 字符替换为可以显示的相应字符串。
     *
     * @param strSource 替换前的字符串
     * @return 替换后的字符串
     */

    public static String toHtml(String strSource) {
        if (StringUtils.isEmpty(strSource)) {
            return "";
        }
        StringBuilder strBufReturn = new StringBuilder();
        for (int i = 0; i < strSource.length(); i++) {
            if (strSource.charAt(i) == CHAR_INTER) {
                strBufReturn.append("<BR>");
            } else if (strSource.charAt(i) == CHAR_LT) {
                strBufReturn.append("<");
            } else if (strSource.charAt(i) == CHAR_GT) {
                strBufReturn.append(">");
            } else if (strSource.charAt(i) == CHAR_QUOT) {
                strBufReturn.append("\"");
            } else if (strSource.charAt(i) == CHAR_AMP) {
                strBufReturn.append("&");
            } else {
                strBufReturn.append(strSource.charAt(i));
            }
        }
        return strBufReturn.toString();
    }

    /**
     * 对空格、回车进行正确替换，其它HTML标记，直接转移为可显示字符串
     *
     * @param string
     * @return
     */
    public static String stringToHTMLString(String string) {
        if (StringUtils.isEmpty(string)) {
            return "";
        }
        StringBuilder sb = new StringBuilder(string.length());
        // true if last char was blank
        boolean lastWasBlankChar = false;
        int len = string.length();
        char c;
        boolean inHtml = false;
        for (int i = 0; i < len; i++) {
            c = string.charAt(i);
            if (c == ' ') {
                // blank gets extra work,
                // this solves the problem you get if you replace all
                // blanks with &nbsp;, if you do that you loss
                // word breaking
                if (lastWasBlankChar && !inHtml) {
                    lastWasBlankChar = false;
                    sb.append("&nbsp;");
                } else {
                    lastWasBlankChar = true;
                    sb.append(' ');
                }
            } else {
                lastWasBlankChar = false;
                //
                // HTML Special Chars
                if (c == CHAR_LT) {
                    inHtml = true;
                    sb.append(c);
                } else if (c == CHAR_GT) {
                    inHtml = false;
                    sb.append(c);
                } else if (c == '&' && (i + 4) < len
                        && (string.charAt(i + 1) != 'n')
                        && (string.charAt(i + 2) != 'b')
                        && (string.charAt(i + 3) != 's')
                        && (string.charAt(i + 4) != 'p')) {
                    sb.append("&amp;");
                } else if (c == '\n')
                // Handle Newline
                {
                    sb.append("<br>");
                } else {
                    int ci = 0xffff & c;
                    if (ci < 160)
                    // nothing special only 7 Bit
                    {
                        sb.append(c);
                    } else {
                        // Not 7 Bit use the unicode system
                        sb.append("&#");
                        sb.append(ci);
                        sb.append(';');
                    }
                }
            }
        }
        return sb.toString();
    }

    /**
     * 将不定数目的字符串连接起来
     *
     * @param args
     * @return
     */
    public static String join(String... args) {
        return String.join("",args);
    }

    /**
     * 将不定数目的字符串连接起来，加分隔符_s:
     *
     * @param args
     * @return
     */
    public static String joinWithSeparator(String... args) {
        return String.join("_s:",args);
    }

    /**
     * [" " , /]过滤掉 [: , ? , & , = , ' , "]转变为全角
     *
     * @param title
     * @return
     */
    public static String escapeTitleForSEO(String title) {
        title = title.trim();
        if (!StringUtils.isEmpty(title)) {
            StringBuilder sb = new StringBuilder(title.length());
            char[] chars = title.toCharArray();
            for (char c : chars) {
                if (c != CHAR_BlANK && c != CHAR_DIVIDE) {
                    if (c == COLON_DBC) {
                        sb.append(COLON_SBC);
                    } else if (c == QM_DBC) {
                        sb.append(QM_SBC);
                    } else if (c == AMP_DBC) {
                        sb.append(AMP_SBC);
                    } else if (c == EQUAL_DBC) {
                        sb.append(EQUAL_SBC);
                    } else if (c == SQM_DBC) {
                        sb.append(SQM_SBC);
                    } else if (c == DQM_DBC) {
                        sb.append(DQM_SBC);
                    } else {
                        sb.append(c);
                    }
                }
            }
            title = sb.toString();
        }
        return title;
    }

    public static String htmlEscape(String input) {
        return HtmlUtils.htmlEscape(input);
    }

    public static boolean isCharacter(String str) {
        if (null == str) {
            return false;
        }
        for (int i = 0; i < str.length(); i++) {
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * JDK默认的split方法碰到长度为0的会不返回，有问题
     *
     * @param str
     * @param token
     * @return List
     */
    public static List<String> str2ListWithNull(String str, String token) {
        List<String> list = new LinkedList<>();
        if (StringUtils.isEmpty(str)) {
            return list;
        }
        int posBegin = 0, posEnd;
        for (; -1 != (posEnd = str.indexOf(token, posBegin)); posBegin = posEnd + token.length()) {
            list.add(str.substring(posBegin, posEnd));
        }
        list.add(str.substring(posBegin));
        return list;
    }

    public static String[] str2ArrayWithNull(String str, String token) {
        List<String> list = str2ListWithNull(str, token);
        if (null == list) {
            return null;
        }
        String[] arrays = new String[list.size()];
        int z = 0;
        for (String temp : list) {
            arrays[z++] = temp;
        }
        return arrays;
    }

    /**
     * 转换出错信息，把异常的堆栈转换成String类型
     *
     * @param ex 异常对象
     * @return String
     */
    @java.lang.SuppressWarnings("squid:S1148")
    public static String ex2Str(Throwable ex) {
        CharArrayWriter caw = new CharArrayWriter();
        ex.printStackTrace(new PrintWriter(caw, true));
        return caw.toString();
    }

    /**
     * 根据指定数字，返回此数字的文本，根据需要的长度，在返回的文本前加零
     * example:addZeroBefore(34, 4);返回"0034"
     *
     * @param num
     * @param needLength
     * @return
     */
    public static String addZeroBefore(int num, int needLength) {
        return addZeroBefore("" + num, needLength);
    }

    public static String addZeroBefore(String str, int needLength) {
        StringBuilder buf = new StringBuilder(str);
        while (buf.length() < needLength) {
            buf.insert(0, "0");
        }
        return buf.toString();
    }

    /**
     * 按照分隔符“,”把字符串转换到列表
     *
     * @param str 要转换的字符串
     * @return List
     */
    public static List<String> str2List(String str) {
        return str2List(str, DIVIDER);
    }

    /**
     * 按照制定分隔符把字符串转换到列表
     *
     * @param str   要转换的字符串
     * @param token 分隔符
     * @return List
     */
    public static List<String> str2List(String str, String token) {
        return Arrays.asList(str.split(token));
    }

    /**
     * 按照分隔符“,”把列表转换到字符串
     *
     * @param collect 要转换的列表
     * @return String
     */
    public static String list2Str(Collection<String> collect) {
        return list2Str(collect, DIVIDER);
    }

    /**
     * 按照制定分隔符把列表转换到字符串
     *
     * @param collect 要转换的列表
     * @param token   分隔符
     * @return String
     */
    public static String list2Str(Collection<String> collect, String token) {
        if (StringUtils.isEmpty(token)) {
            throw new IllegalArgumentException("invalid token:" + token);
        }
        StringBuilder buf = new StringBuilder(collect.size() * 5);
        for (String temp : collect) {
            buf.append(token).append(temp);
        }
        return buf.delete(0, token.length()).toString();
    }

    /**
     * callback方法名的XSS过滤
     *
     * @param name
     * @return
     */
    public static boolean isCallbackName(String name) {
        if (name == null) {
            return false;
        }
        return CALLBACK_REGEX.matcher(name).find();
    }


    public static String deleteLast(StringBuilder builder) {
        String s = builder.toString();
        if (s.contains(",")) {
            int index = s.lastIndexOf(',');
            builder.deleteCharAt(index);
        }
        return builder.toString();
    }
}
