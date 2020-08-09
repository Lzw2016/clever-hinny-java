package org.clever.hinny.core;

import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

/**
 * 作者：lizw <br/>
 * 创建时间：2020/07/28 22:32 <br/>
 */
public class StringUtils {
    public static final StringUtils Instance = new StringUtils();

    private StringUtils() {
    }

    // Other
    //----------------------------------------------------------------------------------------------------------------------------------------------

    /**
     * 调用对象的toString方法，如果对象为空返回默认值
     *
     * @param object     需要toString的对象
     * @param defaultStr 对象为空时返回的默认值
     * @return 返回对象的toString方法结果
     */
    public String objectToString(Object object, String defaultStr) {
        return org.clever.common.utils.StringUtils.objectToString(object, defaultStr);
    }

    /**
     * 除去html标签
     *
     * @param htmlStr 含有html标签的字符串
     * @return 网页文本内容
     */
    public String delHTMLTag(String htmlStr) {
        return org.clever.common.utils.StringUtils.delHTMLTag(htmlStr);
    }

    // Empty checks
    //----------------------------------------------------------------------------------------------------------------------------------------------

    /**
     * 是否是空字符串(“”或null)
     * <pre>
     *  .isEmpty(null)      = true
     *  .isEmpty("")        = true
     *  .isEmpty(" ")       = false
     *  .isEmpty("bob")     = false
     *  .isEmpty("  bob  ") = false
     * </pre>
     */
    public boolean isEmpty(String cs) {
        return org.clever.common.utils.StringUtils.isEmpty(cs);
    }

    /**
     * 是否是非空字符串(“”或null)
     * <pre>
     *  .isNotEmpty(null)      = false
     *  .isNotEmpty("")        = false
     *  .isNotEmpty(" ")       = true
     *  .isNotEmpty("bob")     = true
     *  .isNotEmpty("  bob  ") = true
     * </pre>
     */
    public boolean isNotEmpty(String cs) {
        return org.clever.common.utils.StringUtils.isNotEmpty(cs);
    }

    /**
     * 是否存在空字符串(“”或null)
     * <pre>
     *  .isAnyEmpty((String) null)    = true
     *  .isAnyEmpty((String[]) null)  = false
     *  .isAnyEmpty(null, "foo")      = true
     *  .isAnyEmpty("", "bar")        = true
     *  .isAnyEmpty("bob", "")        = true
     *  .isAnyEmpty("  bob  ", null)  = true
     *  .isAnyEmpty(" ", "bar")       = false
     *  .isAnyEmpty("foo", "bar")     = false
     *  .isAnyEmpty(new String[]{})   = false
     *  .isAnyEmpty(new String[]{""}) = true
     * </pre>
     */
    public boolean isAnyEmpty(String... css) {
        return org.clever.common.utils.StringUtils.isAnyEmpty(css);
    }

    /**
     * 是否存在非空字符串(“”或null)
     * <pre>
     *  .isNoneEmpty((String) null)    = false
     *  .isNoneEmpty((String[]) null)  = true
     *  .isNoneEmpty(null, "foo")      = false
     *  .isNoneEmpty("", "bar")        = false
     *  .isNoneEmpty("bob", "")        = false
     *  .isNoneEmpty("  bob  ", null)  = false
     *  .isNoneEmpty(new String[] {})  = true
     *  .isNoneEmpty(new String[]{""}) = false
     *  .isNoneEmpty(" ", "bar")       = true
     *  .isNoneEmpty("foo", "bar")     = true
     * </pre>
     */
    public boolean isNoneEmpty(String... css) {
        return org.clever.common.utils.StringUtils.isNoneEmpty(css);
    }

    /**
     * 是否所有字符串都是空字符串(“”或null)
     * <pre>
     *  .isAllEmpty(null)             = true
     *  .isAllEmpty(null, "")         = true
     *  .isAllEmpty(new String[] {})  = true
     *  .isAllEmpty(null, "foo")      = false
     *  .isAllEmpty("", "bar")        = false
     *  .isAllEmpty("bob", "")        = false
     *  .isAllEmpty("  bob  ", null)  = false
     *  .isAllEmpty(" ", "bar")       = false
     *  .isAllEmpty("foo", "bar")     = false
     * </pre>
     */
    public boolean isAllEmpty(String... css) {
        return org.clever.common.utils.StringUtils.isAllEmpty(css);
    }

    /**
     * 是否是空字符串(“”、“ ”或null)
     * <pre>
     *  .isBlank(null)      = true
     *  .isBlank("")        = true
     *  .isBlank(" ")       = true
     *  .isBlank("bob")     = false
     *  .isBlank("  bob  ") = false
     * </pre>
     */
    public boolean isBlank(String cs) {
        return org.clever.common.utils.StringUtils.isBlank(cs);
    }

    /**
     * 是否是非空字符串(“”、“ ”或null)
     * <pre>
     *  .isNotBlank(null)      = false
     *  .isNotBlank("")        = false
     *  .isNotBlank(" ")       = false
     *  .isNotBlank("bob")     = true
     *  .isNotBlank("  bob  ") = true
     * </pre>
     */
    public boolean isNotBlank(String cs) {
        return org.clever.common.utils.StringUtils.isNotBlank(cs);
    }

    /**
     * 是否存在空字符串(“”、“ ”或null)
     * <pre>
     *  .isAnyBlank((String) null)    = true
     *  .isAnyBlank((String[]) null)  = false
     *  .isAnyBlank(null, "foo")      = true
     *  .isAnyBlank(null, null)       = true
     *  .isAnyBlank("", "bar")        = true
     *  .isAnyBlank("bob", "")        = true
     *  .isAnyBlank("  bob  ", null)  = true
     *  .isAnyBlank(" ", "bar")       = true
     *  .isAnyBlank(new String[] {})  = false
     *  .isAnyBlank(new String[]{""}) = true
     *  .isAnyBlank("foo", "bar")     = false
     * </pre>
     */
    public boolean isAnyBlank(String... css) {
        return org.clever.common.utils.StringUtils.isAnyBlank(css);
    }

    /**
     * 是否存在非空字符串(“”、“ ”或null)
     * <pre>
     *  .isNoneBlank((String) null)    = false
     *  .isNoneBlank((String[]) null)  = true
     *  .isNoneBlank(null, "foo")      = false
     *  .isNoneBlank(null, null)       = false
     *  .isNoneBlank("", "bar")        = false
     *  .isNoneBlank("bob", "")        = false
     *  .isNoneBlank("  bob  ", null)  = false
     *  .isNoneBlank(" ", "bar")       = false
     *  .isNoneBlank(new String[] {})  = true
     *  .isNoneBlank(new String[]{""}) = false
     *  .isNoneBlank("foo", "bar")     = true
     * </pre>
     */
    public boolean isNoneBlank(String... css) {
        return org.clever.common.utils.StringUtils.isNoneBlank(css);
    }

    /**
     * 是否所有字符串都是空字符串(“”、“ ”或null)
     * <pre>
     *  .isAllBlank(null)             = true
     *  .isAllBlank(null, "foo")      = false
     *  .isAllBlank(null, null)       = true
     *  .isAllBlank("", "bar")        = false
     *  .isAllBlank("bob", "")        = false
     *  .isAllBlank("  bob  ", null)  = false
     *  .isAllBlank(" ", "bar")       = false
     *  .isAllBlank("foo", "bar")     = false
     *  .isAllBlank(new String[] {})  = true
     * </pre>
     */
    public boolean isAllBlank(String... css) {
        return org.clever.common.utils.StringUtils.isAllBlank(css);
    }

    // Trim
    //----------------------------------------------------------------------------------------------------------------------------------------------

    /**
     * <pre>
     *  .trim(null)          = null
     *  .trim("")            = ""
     *  .trim("     ")       = ""
     *  .trim("abc")         = "abc"
     *  .trim("    abc    ") = "abc"
     * </pre>
     */
    public String trim(String str) {
        return org.clever.common.utils.StringUtils.trim(str);
    }

    /**
     * <pre>
     *  .trimToNull(null)          = null
     *  .trimToNull("")            = null
     *  .trimToNull("     ")       = null
     *  .trimToNull("abc")         = "abc"
     *  .trimToNull("    abc    ") = "abc"
     * </pre>
     */
    public String trimToNull(String str) {
        return org.clever.common.utils.StringUtils.trimToNull(str);
    }

    /**
     * <pre>
     *  .trimToEmpty(null)          = ""
     *  .trimToEmpty("")            = ""
     *  .trimToEmpty("     ")       = ""
     *  .trimToEmpty("abc")         = "abc"
     *  .trimToEmpty("    abc    ") = "abc"
     * </pre>
     */
    public String trimToEmpty(String str) {
        return org.clever.common.utils.StringUtils.trimToEmpty(str);
    }

    /**
     * 截断字符串
     * <pre>
     *  .truncate(null, 0)       = null
     *  .truncate(null, 2)       = null
     *  .truncate("", 4)         = ""
     *  .truncate("abcdefg", 4)  = "abcd"
     *  .truncate("abcdefg", 6)  = "abcdef"
     *  .truncate("abcdefg", 7)  = "abcdefg"
     *  .truncate("abcdefg", 8)  = "abcdefg"
     *  .truncate("abcdefg", -1) = throws an IllegalArgumentException
     * </pre>
     */
    public String truncate(String str, int maxWidth) {
        return org.clever.common.utils.StringUtils.truncate(str, maxWidth);
    }

    /**
     * 截断字符串
     *
     * @param str      被截断的字符串
     * @param offset   起始位置
     * @param maxWidth 结果字符串的最大长度
     */
    public String truncate(String str, int offset, int maxWidth) {
        return org.clever.common.utils.StringUtils.truncate(str, offset, maxWidth);
    }

    // Stripping
    //----------------------------------------------------------------------------------------------------------------------------------------------

    /**
     * 从字符串的开始和结尾删除空白
     * <pre>
     *  .strip(null)     = null
     *  .strip("")       = ""
     *  .strip("   ")    = ""
     *  .strip("abc")    = "abc"
     *  .strip("  abc")  = "abc"
     *  .strip("abc  ")  = "abc"
     *  .strip(" abc ")  = "abc"
     *  .strip(" ab c ") = "ab c"
     * </pre>
     */
    public String strip(String str) {
        return org.clever.common.utils.StringUtils.strip(str);
    }

    /**
     * 从字符串的开始和结尾去除空白，如果字符串在strip之后为空（“”），则返回null
     * <pre>
     *  .stripToNull(null)     = null
     *  .stripToNull("")       = null
     *  .stripToNull("   ")    = null
     *  .stripToNull("abc")    = "abc"
     *  .stripToNull("  abc")  = "abc"
     *  .stripToNull("abc  ")  = "abc"
     *  .stripToNull(" abc ")  = "abc"
     *  .stripToNull(" ab c ") = "ab c"
     * </pre>
     */
    public String stripToNull(String str) {
        return org.clever.common.utils.StringUtils.stripToNull(str);
    }

    /**
     * 从字符串的开始和结尾去除空白，如果字符串在strip之后为null，则返回“”
     * <pre>
     *  .stripToEmpty(null)     = ""
     *  .stripToEmpty("")       = ""
     *  .stripToEmpty("   ")    = ""
     *  .stripToEmpty("abc")    = "abc"
     *  .stripToEmpty("  abc")  = "abc"
     *  .stripToEmpty("abc  ")  = "abc"
     *  .stripToEmpty(" abc ")  = "abc"
     *  .stripToEmpty(" ab c ") = "ab c"
     * </pre>
     */
    public String stripToEmpty(String str) {
        return org.clever.common.utils.StringUtils.stripToEmpty(str);
    }

    /**
     * <pre>
     *  .strip(null, *)          = null
     *  .strip("", *)            = ""
     *  .strip("abc", null)      = "abc"
     *  .strip("  abc", null)    = "abc"
     *  .strip("abc  ", null)    = "abc"
     *  .strip(" abc ", null)    = "abc"
     *  .strip("  abcyx", "xyz") = "  abc"
     * </pre>
     *
     * @param str        源字符串
     * @param stripChars 要删除的字符，null被视为空白
     */
    public String strip(String str, String stripChars) {
        return org.clever.common.utils.StringUtils.strip(str, stripChars);
    }

    /**
     * 删除开始的空白字符
     * <pre>
     *  .stripStart(null, *)          = null
     *  .stripStart("", *)            = ""
     *  .stripStart("abc", "")        = "abc"
     *  .stripStart("abc", null)      = "abc"
     *  .stripStart("  abc", null)    = "abc"
     *  .stripStart("abc  ", null)    = "abc  "
     *  .stripStart(" abc ", null)    = "abc "
     *  .stripStart("yxabc  ", "xyz") = "abc  "
     * </pre>
     *
     * @param str        源字符串
     * @param stripChars 要删除的字符，null被视为空白
     */
    public String stripStart(String str, String stripChars) {
        return org.clever.common.utils.StringUtils.stripStart(str, stripChars);
    }

    /**
     * 删除结束的空白字符
     * <pre>
     *  .stripEnd(null, *)          = null
     *  .stripEnd("", *)            = ""
     *  .stripEnd("abc", "")        = "abc"
     *  .stripEnd("abc", null)      = "abc"
     *  .stripEnd("  abc", null)    = "  abc"
     *  .stripEnd("abc  ", null)    = "abc"
     *  .stripEnd(" abc ", null)    = " abc"
     *  .stripEnd("  abcyx", "xyz") = "  abc"
     *  .stripEnd("120.00", ".0")   = "12"
     * </pre>
     *
     * @param str        源字符串
     * @param stripChars 要删除的字符，null被视为空白
     */
    public String stripEnd(String str, String stripChars) {
        return org.clever.common.utils.StringUtils.stripEnd(str, stripChars);
    }

    // StripAll
    //----------------------------------------------------------------------------------------------------------------------------------------------

    /**
     * <pre>
     *  .stripAll(null)             = null
     *  .stripAll([])               = []
     *  .stripAll(["abc", "  abc"]) = ["abc", "abc"]
     *  .stripAll(["abc  ", null])  = ["abc", null]
     * </pre>
     */
    public String[] stripAll(String... strs) {
        return org.clever.common.utils.StringUtils.stripAll(strs);
    }

    /**
     * <pre>
     *  .stripAll(null, *)                = null
     *  .stripAll([], *)                  = []
     *  .stripAll(["abc", "  abc"], null) = ["abc", "abc"]
     *  .stripAll(["abc  ", null], null)  = ["abc", null]
     *  .stripAll(["abc  ", null], "yz")  = ["abc  ", null]
     *  .stripAll(["yabcz", null], "yz")  = ["abc", null]
     * </pre>
     *
     * @param strs       源字符串数组
     * @param stripChars 要删除的字符，null被视为空白
     */
    public String[] stripAll(String[] strs, String stripChars) {
        return org.clever.common.utils.StringUtils.stripAll(strs, stripChars);
    }

    /**
     * 从字符串中删除音调符号。例如，“à”将被“a”替换。
     * <pre>
     *  .stripAccents(null)         = null
     *  .stripAccents("")           = ""
     *  .stripAccents("control")    = "control"
     *  .stripAccents("éclair")     = "eclair"
     * </pre>
     */
    public String stripAccents(String input) {
        return org.clever.common.utils.StringUtils.stripAccents(input);
    }

    // Equals
    //-----------------------------------------------------------------------

    /**
     * <pre>
     *  .equals(null, null)   = true
     *  .equals(null, "abc")  = false
     *  .equals("abc", null)  = false
     *  .equals("abc", "abc") = true
     *  .equals("abc", "ABC") = false
     * </pre>
     */
    public boolean equals(CharSequence cs1, CharSequence cs2) {
        return org.clever.common.utils.StringUtils.equals(cs1, cs2);
    }

    /**
     * <pre>
     *  .equalsIgnoreCase(null, null)   = true
     *  .equalsIgnoreCase(null, "abc")  = false
     *  .equalsIgnoreCase("abc", null)  = false
     *  .equalsIgnoreCase("abc", "abc") = true
     *  .equalsIgnoreCase("abc", "ABC") = true
     * </pre>
     */
    public boolean equalsIgnoreCase(CharSequence str1, CharSequence str2) {
        return org.clever.common.utils.StringUtils.equalsIgnoreCase(str1, str2);
    }

    // Compare
    //----------------------------------------------------------------------------------------------------------------------------------------------

    /**
     * <pre>
     *  .compare(null, null)   = 0
     *  .compare(null , "a")   < 0
     *  .compare("a", null)    > 0
     *  .compare("abc", "abc") = 0
     *  .compare("a", "b")     < 0
     *  .compare("b", "a")     > 0
     *  .compare("a", "B")     > 0
     *  .compare("ab", "abc")  < 0
     * </pre>
     */
    public int compare(String str1, String str2) {
        return org.clever.common.utils.StringUtils.compare(str1, str2);
    }

    /**
     * <pre>
     *  .compare(null, null, *)     = 0
     *  .compare(null , "a", true)  < 0
     *  .compare(null , "a", false) > 0
     *  .compare("a", null, true)   > 0
     *  .compare("a", null, false)  < 0
     *  .compare("abc", "abc", *)   = 0
     *  .compare("a", "b", *)       < 0
     *  .compare("b", "a", *)       > 0
     *  .compare("a", "B", *)       > 0
     *  .compare("ab", "abc", *)    < 0
     * </pre>
     *
     * @param nullIsLess 是否是空值小于非空值
     */
    public int compare(String str1, String str2, boolean nullIsLess) {
        return org.clever.common.utils.StringUtils.compare(str1, str2, nullIsLess);
    }

    /**
     * <pre>
     *  .compareIgnoreCase(null, null)   = 0
     *  .compareIgnoreCase(null , "a")   < 0
     *  .compareIgnoreCase("a", null)    > 0
     *  .compareIgnoreCase("abc", "abc") = 0
     *  .compareIgnoreCase("abc", "ABC") = 0
     *  .compareIgnoreCase("a", "b")     < 0
     *  .compareIgnoreCase("b", "a")     > 0
     *  .compareIgnoreCase("a", "B")     < 0
     *  .compareIgnoreCase("A", "b")     < 0
     *  .compareIgnoreCase("ab", "ABC")  < 0
     * </pre>
     */
    public int compareIgnoreCase(String str1, String str2) {
        return org.clever.common.utils.StringUtils.compareIgnoreCase(str1, str2);
    }

    /**
     * <pre>
     *  .compareIgnoreCase(null, null, *)     = 0
     *  .compareIgnoreCase(null , "a", true)  < 0
     *  .compareIgnoreCase(null , "a", false) > 0
     *  .compareIgnoreCase("a", null, true)   > 0
     *  .compareIgnoreCase("a", null, false)  < 0
     *  .compareIgnoreCase("abc", "abc", *)   = 0
     *  .compareIgnoreCase("abc", "ABC", *)   = 0
     *  .compareIgnoreCase("a", "b", *)       < 0
     *  .compareIgnoreCase("b", "a", *)       > 0
     *  .compareIgnoreCase("a", "B", *)       < 0
     *  .compareIgnoreCase("A", "b", *)       < 0
     *  .compareIgnoreCase("ab", "abc", *)    < 0
     * </pre>
     *
     * @param nullIsLess 是否是空值小于非空值
     */
    public int compareIgnoreCase(String str1, String str2, boolean nullIsLess) {
        return org.clever.common.utils.StringUtils.compareIgnoreCase(str1, str2, nullIsLess);
    }

    /**
     * 将给定字符串与搜索字符串的CharSequences vararg进行比较，如果字符串等于任何搜索字符串，则返回true
     * <pre>
     *  .equalsAny(null, (CharSequence[]) null) = false
     *  .equalsAny(null, null, null)    = true
     *  .equalsAny(null, "abc", "def")  = false
     *  .equalsAny("abc", null, "def")  = false
     *  .equalsAny("abc", "abc", "def") = true
     *  .equalsAny("abc", "ABC", "DEF") = false
     * </pre>
     */
    public boolean equalsAny(CharSequence string, CharSequence... searchStrings) {
        return org.clever.common.utils.StringUtils.equalsAny(string, searchStrings);
    }

    /**
     * 将给定字符串与搜索字符串的CharSequences vararg进行比较，如果字符串等于任何搜索字符串，则返回true。忽略大小写
     * <pre>
     *  .equalsAnyIgnoreCase(null, (CharSequence[]) null) = false
     *  .equalsAnyIgnoreCase(null, null, null)    = true
     *  .equalsAnyIgnoreCase(null, "abc", "def")  = false
     *  .equalsAnyIgnoreCase("abc", null, "def")  = false
     *  .equalsAnyIgnoreCase("abc", "abc", "def") = true
     *  .equalsAnyIgnoreCase("abc", "ABC", "DEF") = true
     * </pre>
     */
    public boolean equalsAnyIgnoreCase(CharSequence string, CharSequence... searchStrings) {
        return org.clever.common.utils.StringUtils.equalsAnyIgnoreCase(string, searchStrings);
    }

    // IndexOf
    //----------------------------------------------------------------------------------------------------------------------------------------------

    /**
     * <pre>
     *  .indexOf(null, *)         = -1
     *  .indexOf("", *)           = -1
     *  .indexOf("aabaabaa", 'a') = 0
     *  .indexOf("aabaabaa", 'b') = 2
     * </pre>
     */
    public int indexOf(CharSequence seq, int searchChar) {
        return org.clever.common.utils.StringUtils.indexOf(seq, searchChar);
    }

    /**
     * <pre>
     *  .indexOf(null, *, *)          = -1
     *  .indexOf("", *, *)            = -1
     *  .indexOf("aabaabaa", 'b', 0)  = 2
     *  .indexOf("aabaabaa", 'b', 3)  = 5
     *  .indexOf("aabaabaa", 'b', 9)  = -1
     *  .indexOf("aabaabaa", 'b', -1) = 2
     * </pre>
     */
    public int indexOf(CharSequence seq, int searchChar, int startPos) {
        return org.clever.common.utils.StringUtils.indexOf(seq, searchChar, startPos);
    }

    /**
     * <pre>
     *  .indexOf(null, *)          = -1
     *  .indexOf(*, null)          = -1
     *  .indexOf("", "")           = 0
     *  .indexOf("", *)            = -1 (except when * = "")
     *  .indexOf("aabaabaa", "a")  = 0
     *  .indexOf("aabaabaa", "b")  = 2
     *  .indexOf("aabaabaa", "ab") = 1
     *  .indexOf("aabaabaa", "")   = 0
     * </pre>
     */
    public int indexOf(CharSequence seq, CharSequence searchSeq) {
        return org.clever.common.utils.StringUtils.indexOf(seq, searchSeq);
    }

    /**
     * <pre>
     *  .indexOf(null, *, *)          = -1
     *  .indexOf(*, null, *)          = -1
     *  .indexOf("", "", 0)           = 0
     *  .indexOf("", *, 0)            = -1 (except when * = "")
     *  .indexOf("aabaabaa", "a", 0)  = 0
     *  .indexOf("aabaabaa", "b", 0)  = 2
     *  .indexOf("aabaabaa", "ab", 0) = 1
     *  .indexOf("aabaabaa", "b", 3)  = 5
     *  .indexOf("aabaabaa", "b", 9)  = -1
     *  .indexOf("aabaabaa", "b", -1) = 2
     *  .indexOf("aabaabaa", "", 2)   = 2
     *  .indexOf("abc", "", 9)        = 3
     * </pre>
     */
    public int indexOf(CharSequence seq, CharSequence searchSeq, int startPos) {
        return org.clever.common.utils.StringUtils.indexOf(seq, searchSeq, startPos);
    }

    /**
     * <pre>
     *  .ordinalIndexOf(null, *, *)          = -1
     *  .ordinalIndexOf(*, null, *)          = -1
     *  .ordinalIndexOf("", "", *)           = 0
     *  .ordinalIndexOf("aabaabaa", "a", 1)  = 0
     *  .ordinalIndexOf("aabaabaa", "a", 2)  = 1
     *  .ordinalIndexOf("aabaabaa", "b", 1)  = 2
     *  .ordinalIndexOf("aabaabaa", "b", 2)  = 5
     *  .ordinalIndexOf("aabaabaa", "ab", 1) = 1
     *  .ordinalIndexOf("aabaabaa", "ab", 2) = 4
     *  .ordinalIndexOf("aabaabaa", "", 1)   = 0
     *  .ordinalIndexOf("aabaabaa", "", 2)   = 0
     * </pre>
     *
     * @param ordinal 要查找的第n个searchStr
     */
    public int ordinalIndexOf(CharSequence str, CharSequence searchStr, int ordinal) {
        return org.clever.common.utils.StringUtils.ordinalIndexOf(str, searchStr, ordinal);
    }

    /**
     * <pre>
     *  .indexOfIgnoreCase(null, *)          = -1
     *  .indexOfIgnoreCase(*, null)          = -1
     *  .indexOfIgnoreCase("", "")           = 0
     *  .indexOfIgnoreCase("aabaabaa", "a")  = 0
     *  .indexOfIgnoreCase("aabaabaa", "b")  = 2
     *  .indexOfIgnoreCase("aabaabaa", "ab") = 1
     * </pre>
     */
    public int indexOfIgnoreCase(CharSequence str, CharSequence searchStr) {
        return org.clever.common.utils.StringUtils.indexOfIgnoreCase(str, searchStr);
    }

    /**
     * <pre>
     *  .indexOfIgnoreCase(null, *, *)          = -1
     *  .indexOfIgnoreCase(*, null, *)          = -1
     *  .indexOfIgnoreCase("", "", 0)           = 0
     *  .indexOfIgnoreCase("aabaabaa", "A", 0)  = 0
     *  .indexOfIgnoreCase("aabaabaa", "B", 0)  = 2
     *  .indexOfIgnoreCase("aabaabaa", "AB", 0) = 1
     *  .indexOfIgnoreCase("aabaabaa", "B", 3)  = 5
     *  .indexOfIgnoreCase("aabaabaa", "B", 9)  = -1
     *  .indexOfIgnoreCase("aabaabaa", "B", -1) = 2
     *  .indexOfIgnoreCase("aabaabaa", "", 2)   = 2
     *  .indexOfIgnoreCase("abc", "", 9)        = -1
     * </pre>
     */
    public int indexOfIgnoreCase(CharSequence str, CharSequence searchStr, int startPos) {
        return org.clever.common.utils.StringUtils.indexOfIgnoreCase(str, searchStr, startPos);
    }

    // LastIndexOf
    //----------------------------------------------------------------------------------------------------------------------------------------------

    /**
     * <pre>
     *  .lastIndexOf(null, *)         = -1
     *  .lastIndexOf("", *)           = -1
     *  .lastIndexOf("aabaabaa", 'a') = 7
     *  .lastIndexOf("aabaabaa", 'b') = 5
     * </pre>
     */
    public int lastIndexOf(CharSequence seq, int searchChar) {
        return org.clever.common.utils.StringUtils.lastIndexOf(seq, searchChar);
    }

    /**
     * <pre>
     *
     * </pre>
     */
    public int lastIndexOf(CharSequence seq, int searchChar, int startPos) {
        return org.clever.common.utils.StringUtils.lastIndexOf(seq, searchChar);
    }

    /**
     * <pre>
     *  .lastIndexOf(null, *)          = -1
     *  .lastIndexOf(*, null)          = -1
     *  .lastIndexOf("", "")           = 0
     *  .lastIndexOf("aabaabaa", "a")  = 7
     *  .lastIndexOf("aabaabaa", "b")  = 5
     *  .lastIndexOf("aabaabaa", "ab") = 4
     *  .lastIndexOf("aabaabaa", "")   = 8
     * </pre>
     */
    public int lastIndexOf(CharSequence seq, CharSequence searchSeq) {
        return org.clever.common.utils.StringUtils.lastIndexOf(seq, searchSeq);
    }

    /**
     * <pre>
     *  .lastOrdinalIndexOf(null, *, *)          = -1
     *  .lastOrdinalIndexOf(*, null, *)          = -1
     *  .lastOrdinalIndexOf("", "", *)           = 0
     *  .lastOrdinalIndexOf("aabaabaa", "a", 1)  = 7
     *  .lastOrdinalIndexOf("aabaabaa", "a", 2)  = 6
     *  .lastOrdinalIndexOf("aabaabaa", "b", 1)  = 5
     *  .lastOrdinalIndexOf("aabaabaa", "b", 2)  = 2
     *  .lastOrdinalIndexOf("aabaabaa", "ab", 1) = 4
     *  .lastOrdinalIndexOf("aabaabaa", "ab", 2) = 1
     *  .lastOrdinalIndexOf("aabaabaa", "", 1)   = 8
     *  .lastOrdinalIndexOf("aabaabaa", "", 2)   = 8
     * </pre>
     *
     * @param ordinal 要查找的第n个searchStr
     */
    public int lastOrdinalIndexOf(CharSequence str, CharSequence searchStr, int ordinal) {
        return org.clever.common.utils.StringUtils.lastOrdinalIndexOf(str, searchStr, ordinal);
    }

    /**
     * <pre>
     *  .lastIndexOf(null, *, *)          = -1
     *  .lastIndexOf(*, null, *)          = -1
     *  .lastIndexOf("aabaabaa", "a", 8)  = 7
     *  .lastIndexOf("aabaabaa", "b", 8)  = 5
     *  .lastIndexOf("aabaabaa", "ab", 8) = 4
     *  .lastIndexOf("aabaabaa", "b", 9)  = 5
     *  .lastIndexOf("aabaabaa", "b", -1) = -1
     *  .lastIndexOf("aabaabaa", "a", 0)  = 0
     *  .lastIndexOf("aabaabaa", "b", 0)  = -1
     *  .lastIndexOf("aabaabaa", "b", 1)  = -1
     *  .lastIndexOf("aabaabaa", "b", 2)  = 2
     *  .lastIndexOf("aabaabaa", "ba", 2)  = 2
     * </pre>
     */
    public int lastIndexOf(CharSequence seq, CharSequence searchSeq, int startPos) {
        return org.clever.common.utils.StringUtils.lastIndexOf(seq, searchSeq, startPos);
    }

    /**
     * <pre>
     *  .lastIndexOfIgnoreCase(null, *)          = -1
     *  .lastIndexOfIgnoreCase(*, null)          = -1
     *  .lastIndexOfIgnoreCase("aabaabaa", "A")  = 7
     *  .lastIndexOfIgnoreCase("aabaabaa", "B")  = 5
     *  .lastIndexOfIgnoreCase("aabaabaa", "AB") = 4
     * </pre>
     */
    public int lastIndexOfIgnoreCase(CharSequence str, CharSequence searchStr) {
        return org.clever.common.utils.StringUtils.lastIndexOfIgnoreCase(str, searchStr);
    }

    /**
     * <pre>
     *  .lastIndexOfIgnoreCase(null, *, *)          = -1
     *  .lastIndexOfIgnoreCase(*, null, *)          = -1
     *  .lastIndexOfIgnoreCase("aabaabaa", "A", 8)  = 7
     *  .lastIndexOfIgnoreCase("aabaabaa", "B", 8)  = 5
     *  .lastIndexOfIgnoreCase("aabaabaa", "AB", 8) = 4
     *  .lastIndexOfIgnoreCase("aabaabaa", "B", 9)  = 5
     *  .lastIndexOfIgnoreCase("aabaabaa", "B", -1) = -1
     *  .lastIndexOfIgnoreCase("aabaabaa", "A", 0)  = 0
     *  .lastIndexOfIgnoreCase("aabaabaa", "B", 0)  = -1
     * </pre>
     */
    public int lastIndexOfIgnoreCase(CharSequence str, CharSequence searchStr, int startPos) {
        return org.clever.common.utils.StringUtils.lastIndexOfIgnoreCase(str, searchStr, startPos);
    }

    // Contains
    //----------------------------------------------------------------------------------------------------------------------------------------------

    /**
     * <pre>
     *  .contains(null, *)    = false
     *  .contains("", *)      = false
     *  .contains("abc", 'a') = true
     *  .contains("abc", 'z') = false
     * </pre>
     */
    public boolean contains(CharSequence seq, int searchChar) {
        return org.clever.common.utils.StringUtils.contains(seq, searchChar);
    }

    /**
     * <pre>
     *  .contains(null, *)     = false
     *  .contains(*, null)     = false
     *  .contains("", "")      = true
     *  .contains("abc", "")   = true
     *  .contains("abc", "a")  = true
     *  .contains("abc", "z")  = false
     * </pre>
     */
    public boolean contains(CharSequence seq, CharSequence searchSeq) {
        return org.clever.common.utils.StringUtils.contains(seq, searchSeq);
    }

    /**
     * <pre>
     *  .containsIgnoreCase(null, *)    = false
     *  .containsIgnoreCase(*, null)    = false
     *  .containsIgnoreCase("", "")     = true
     *  .containsIgnoreCase("abc", "")  = true
     *  .containsIgnoreCase("abc", "a") = true
     *  .containsIgnoreCase("abc", "z") = false
     *  .containsIgnoreCase("abc", "A") = true
     *  .containsIgnoreCase("abc", "Z") = false
     * </pre>
     */
    public boolean containsIgnoreCase(CharSequence str, CharSequence searchStr) {
        return org.clever.common.utils.StringUtils.containsIgnoreCase(str, searchStr);
    }

    /**
     * 检查给定的CharSequence是否包含任何空白字符
     */
    public boolean containsWhitespace(CharSequence seq) {
        return org.clever.common.utils.StringUtils.containsWhitespace(seq);
    }

    /**
     * <pre>
     *  .indexOfAny(null, *)                = -1
     *  .indexOfAny("", *)                  = -1
     *  .indexOfAny(*, null)                = -1
     *  .indexOfAny(*, [])                  = -1
     *  .indexOfAny("zzabyycdxx",['z','a']) = 0
     *  .indexOfAny("zzabyycdxx",['b','y']) = 3
     *  .indexOfAny("aba", ['z'])           = -1
     * </pre>
     */
    public int indexOfAny(CharSequence cs, char... searchChars) {
        return org.clever.common.utils.StringUtils.indexOfAny(cs, searchChars);
    }

    /**
     * <pre>
     *  .indexOfAny(null, *)            = -1
     *  .indexOfAny("", *)              = -1
     *  .indexOfAny(*, null)            = -1
     *  .indexOfAny(*, "")              = -1
     *  .indexOfAny("zzabyycdxx", "za") = 0
     *  .indexOfAny("zzabyycdxx", "by") = 3
     *  .indexOfAny("aba","z")          = -1
     * </pre>
     */
    public int indexOfAny(CharSequence cs, String searchChars) {
        return org.clever.common.utils.StringUtils.indexOfAny(cs, searchChars);
    }

    /**
     * <pre>
     *  .containsAny(null, *)                = false
     *  .containsAny("", *)                  = false
     *  .containsAny(*, null)                = false
     *  .containsAny(*, [])                  = false
     *  .containsAny("zzabyycdxx",['z','a']) = true
     *  .containsAny("zzabyycdxx",['b','y']) = true
     *  .containsAny("zzabyycdxx",['z','y']) = true
     *  .containsAny("aba", ['z'])           = false
     * </pre>
     */
    public boolean containsAny(CharSequence cs, char... searchChars) {
        return org.clever.common.utils.StringUtils.containsAny(cs, searchChars);
    }

    /**
     * <pre>
     *  .containsAny(null, *)               = false
     *  .containsAny("", *)                 = false
     *  .containsAny(*, null)               = false
     *  .containsAny(*, "")                 = false
     *  .containsAny("zzabyycdxx", "za")    = true
     *  .containsAny("zzabyycdxx", "by")    = true
     *  .containsAny("zzabyycdxx", "zy")    = true
     *  .containsAny("zzabyycdxx", "\tx")   = true
     *  .containsAny("zzabyycdxx", "$.#yF") = true
     *  .containsAny("aba","z")             = false
     * </pre>
     */
    public boolean containsAny(CharSequence cs, CharSequence searchChars) {
        return org.clever.common.utils.StringUtils.containsAny(cs, searchChars);
    }

    /**
     * <pre>
     *  .containsAny(null, *)            = false
     *  .containsAny("", *)              = false
     *  .containsAny(*, null)            = false
     *  .containsAny(*, [])              = false
     *  .containsAny("abcd", "ab", null) = true
     *  .containsAny("abcd", "ab", "cd") = true
     *  .containsAny("abc", "d", "abc")  = true
     * </pre>
     */
    public boolean containsAny(CharSequence cs, CharSequence... searchCharSequences) {
        return org.clever.common.utils.StringUtils.containsAny(cs, searchCharSequences);
    }

    // IndexOfAnyBut chars
    //----------------------------------------------------------------------------------------------------------------------------------------------

    /**
     * 搜索CharSequence以查找不在给定字符集中的任何字符的第一个索引。空的CharSequence将返回-1。空搜索字符串将返回-1。
     * <pre>
     *  .indexOfAnyBut(null, *)                              = -1
     *  .indexOfAnyBut("", *)                                = -1
     *  .indexOfAnyBut(*, null)                              = -1
     *  .indexOfAnyBut(*, [])                                = -1
     *  .indexOfAnyBut("zzabyycdxx", new char[] {'z', 'a'} ) = 3
     *  .indexOfAnyBut("aba", new char[] {'z'} )             = 0
     *  .indexOfAnyBut("aba", new char[] {'a', 'b'} )        = -1
     * </pre>
     */
    public int indexOfAnyBut(CharSequence cs, char... searchChars) {
        return org.clever.common.utils.StringUtils.indexOfAnyBut(cs, searchChars);
    }

    /**
     * 搜索CharSequence以查找不在给定字符集中的任何字符的第一个索引。空的CharSequence将返回-1。空搜索字符串将返回-1。
     * <pre>
     *  .indexOfAnyBut(null, *)            = -1
     *  .indexOfAnyBut("", *)              = -1
     *  .indexOfAnyBut(*, null)            = -1
     *  .indexOfAnyBut(*, "")              = -1
     *  .indexOfAnyBut("zzabyycdxx", "za") = 3
     *  .indexOfAnyBut("zzabyycdxx", "")   = -1
     *  .indexOfAnyBut("aba","ab")         = -1
     * </pre>
     */
    public int indexOfAnyBut(CharSequence seq, CharSequence searchChars) {
        return org.clever.common.utils.StringUtils.indexOfAnyBut(seq, searchChars);
    }

    // ContainsOnly
    //----------------------------------------------------------------------------------------------------------------------------------------------

    /**
     * 检查CharSequence是否只包含某些字符
     * <pre>
     *  .containsOnly(null, *)       = false
     *  .containsOnly(*, null)       = false
     *  .containsOnly("", *)         = true
     *  .containsOnly("ab", '')      = false
     *  .containsOnly("abab", 'abc') = true
     *  .containsOnly("ab1", 'abc')  = false
     *  .containsOnly("abz", 'abc')  = false
     * </pre>
     */
    public boolean containsOnly(CharSequence cs, char... valid) {
        return org.clever.common.utils.StringUtils.containsOnly(cs, valid);
    }

    /**
     * 检查CharSequence是否只包含某些字符
     * <pre>
     *  .containsOnly(null, *)       = false
     *  .containsOnly(*, null)       = false
     *  .containsOnly("", *)         = true
     *  .containsOnly("ab", "")      = false
     *  .containsOnly("abab", "abc") = true
     *  .containsOnly("ab1", "abc")  = false
     *  .containsOnly("abz", "abc")  = false
     * </pre>
     */
    public boolean containsOnly(CharSequence cs, String validChars) {
        return org.clever.common.utils.StringUtils.containsOnly(cs, validChars);
    }

    // ContainsNone
    //----------------------------------------------------------------------------------------------------------------------------------------------

    /**
     * 检查CharSequence是否不包含某些字符
     * <pre>
     *  .containsNone(null, *)       = true
     *  .containsNone(*, null)       = true
     *  .containsNone("", *)         = true
     *  .containsNone("ab", '')      = true
     *  .containsNone("abab", 'xyz') = true
     *  .containsNone("ab1", 'xyz')  = true
     *  .containsNone("abz", 'xyz')  = false
     * </pre>
     */
    public boolean containsNone(CharSequence cs, char... searchChars) {
        return org.clever.common.utils.StringUtils.containsNone(cs, searchChars);
    }

    /**
     * 检查CharSequence是否不包含某些字符
     * <pre>
     *  .containsNone(null, *)       = true
     *  .containsNone(*, null)       = true
     *  .containsNone("", *)         = true
     *  .containsNone("ab", "")      = true
     *  .containsNone("abab", "xyz") = true
     *  .containsNone("ab1", "xyz")  = true
     *  .containsNone("abz", "xyz")  = false
     * </pre>
     */
    public boolean containsNone(CharSequence cs, String invalidChars) {
        return org.clever.common.utils.StringUtils.containsNone(cs, invalidChars);
    }

    // IndexOfAny strings
    //----------------------------------------------------------------------------------------------------------------------------------------------

    /**
     * <pre>
     *  .indexOfAny(null, *)                     = -1
     *  .indexOfAny(*, null)                     = -1
     *  .indexOfAny(*, [])                       = -1
     *  .indexOfAny("zzabyycdxx", ["ab","cd"])   = 2
     *  .indexOfAny("zzabyycdxx", ["cd","ab"])   = 2
     *  .indexOfAny("zzabyycdxx", ["mn","op"])   = -1
     *  .indexOfAny("zzabyycdxx", ["zab","aby"]) = 1
     *  .indexOfAny("zzabyycdxx", [""])          = 0
     *  .indexOfAny("", [""])                    = 0
     *  .indexOfAny("", ["a"])                   = -1
     * </pre>
     */
    public int indexOfAny(CharSequence str, CharSequence... searchStrs) {
        return org.clever.common.utils.StringUtils.indexOfAny(str, searchStrs);
    }

    /**
     * <pre>
     *  .lastIndexOfAny(null, *)                   = -1
     *  .lastIndexOfAny(*, null)                   = -1
     *  .lastIndexOfAny(*, [])                     = -1
     *  .lastIndexOfAny(*, [null])                 = -1
     *  .lastIndexOfAny("zzabyycdxx", ["ab","cd"]) = 6
     *  .lastIndexOfAny("zzabyycdxx", ["cd","ab"]) = 6
     *  .lastIndexOfAny("zzabyycdxx", ["mn","op"]) = -1
     *  .lastIndexOfAny("zzabyycdxx", ["mn","op"]) = -1
     *  .lastIndexOfAny("zzabyycdxx", ["mn",""])   = 10
     * </pre>
     */
    public int lastIndexOfAny(CharSequence str, CharSequence... searchStrs) {
        return org.clever.common.utils.StringUtils.lastIndexOfAny(str, searchStrs);
    }

    // Substring
    //----------------------------------------------------------------------------------------------------------------------------------------------

    /**
     * <pre>
     *  .substring(null, *)   = null
     *  .substring("", *)     = ""
     *  .substring("abc", 0)  = "abc"
     *  .substring("abc", 2)  = "c"
     *  .substring("abc", 4)  = ""
     *  .substring("abc", -2) = "bc"
     *  .substring("abc", -4) = "abc"
     * </pre>
     */
    public String substring(String str, int start) {
        return org.clever.common.utils.StringUtils.substring(str, start);
    }

    /**
     * <pre>
     *  .substring(null, *, *)    = null
     *  .substring("", * ,  *)    = "";
     *  .substring("abc", 0, 2)   = "ab"
     *  .substring("abc", 2, 0)   = ""
     *  .substring("abc", 2, 4)   = "c"
     *  .substring("abc", 4, 6)   = ""
     *  .substring("abc", 2, 2)   = ""
     *  .substring("abc", -2, -1) = "b"
     *  .substring("abc", -4, 2)  = "ab"
     * </pre>
     */
    public String substring(String str, int start, int end) {
        return org.clever.common.utils.StringUtils.substring(str, start, end);
    }

    // Left/Right/Mid
    //----------------------------------------------------------------------------------------------------------------------------------------------

    /**
     * <pre>
     *  .left(null, *)    = null
     *  .left(*, -ve)     = ""
     *  .left("", *)      = ""
     *  .left("abc", 0)   = ""
     *  .left("abc", 2)   = "ab"
     *  .left("abc", 4)   = "abc"
     * </pre>
     */
    public String left(String str, int len) {
        return org.clever.common.utils.StringUtils.left(str, len);
    }

    /**
     * <pre>
     *  .right(null, *)    = null
     *  .right(*, -ve)     = ""
     *  .right("", *)      = ""
     *  .right("abc", 0)   = ""
     *  .right("abc", 2)   = "bc"
     *  .right("abc", 4)   = "abc"
     * </pre>
     */
    public String right(String str, int len) {
        return org.clever.common.utils.StringUtils.right(str, len);
    }

    /**
     * <pre>
     *  .mid(null, *, *)    = null
     *  .mid(*, *, -ve)     = ""
     *  .mid("", 0, *)      = ""
     *  .mid("abc", 0, 2)   = "ab"
     *  .mid("abc", 0, 4)   = "abc"
     *  .mid("abc", 2, 4)   = "c"
     *  .mid("abc", 4, 2)   = ""
     *  .mid("abc", -2, 2)  = "ab"
     * </pre>
     */
    public String mid(String str, int pos, int len) {
        return org.clever.common.utils.StringUtils.mid(str, pos, len);
    }

    // SubStringAfter/SubStringBefore
    //----------------------------------------------------------------------------------------------------------------------------------------------

    /**
     * 获取第一次出现分隔符之前的子字符串
     * <pre>
     *  .substringBefore(null, *)      = null
     *  .substringBefore("", *)        = ""
     *  .substringBefore("abc", "a")   = ""
     *  .substringBefore("abcba", "b") = "a"
     *  .substringBefore("abc", "c")   = "ab"
     *  .substringBefore("abc", "d")   = "abc"
     *  .substringBefore("abc", "")    = ""
     *  .substringBefore("abc", null)  = "abc"
     * </pre>
     */
    public String substringBefore(String str, String separator) {
        return org.clever.common.utils.StringUtils.substringBefore(str, separator);
    }

    /**
     * 获取第一次出现分隔符后的子字符串
     * <pre>
     *  .substringAfter(null, *)      = null
     *  .substringAfter("", *)        = ""
     *  .substringAfter(*, null)      = ""
     *  .substringAfter("abc", "a")   = "bc"
     *  .substringAfter("abcba", "b") = "cba"
     *  .substringAfter("abc", "c")   = ""
     *  .substringAfter("abc", "d")   = ""
     *  .substringAfter("abc", "")    = "abc"
     * </pre>
     */
    public String substringAfter(String str, String separator) {
        return org.clever.common.utils.StringUtils.substringAfter(str, separator);
    }

    /**
     * 获取最后一次出现分隔符之前的子字符串
     * <pre>
     *  .substringBeforeLast(null, *)      = null
     *  .substringBeforeLast("", *)        = ""
     *  .substringBeforeLast("abcba", "b") = "abc"
     *  .substringBeforeLast("abc", "c")   = "ab"
     *  .substringBeforeLast("a", "a")     = ""
     *  .substringBeforeLast("a", "z")     = "a"
     *  .substringBeforeLast("a", null)    = "a"
     *  .substringBeforeLast("a", "")      = "a"
     * </pre>
     */
    public String substringBeforeLast(String str, String separator) {
        return org.clever.common.utils.StringUtils.substringBeforeLast(str, separator);
    }

    /**
     * 获取最后一次出现分隔符后的子字符串
     * <pre>
     *  .substringAfterLast(null, *)      = null
     *  .substringAfterLast("", *)        = ""
     *  .substringAfterLast(*, "")        = ""
     *  .substringAfterLast(*, null)      = ""
     *  .substringAfterLast("abc", "a")   = "bc"
     *  .substringAfterLast("abcba", "b") = "a"
     *  .substringAfterLast("abc", "c")   = ""
     *  .substringAfterLast("a", "a")     = ""
     *  .substringAfterLast("a", "z")     = ""
     * </pre>
     */
    public String substringAfterLast(String str, String separator) {
        return org.clever.common.utils.StringUtils.substringAfterLast(str, separator);
    }

    // Substring between
    //----------------------------------------------------------------------------------------------------------------------------------------------

    /**
     * 获取嵌套在同一字符串的两个实例之间的字符串
     * <pre>
     *  .substringBetween(null, *)            = null
     *  .substringBetween("", "")             = ""
     *  .substringBetween("", "tag")          = null
     *  .substringBetween("tagabctag", null)  = null
     *  .substringBetween("tagabctag", "")    = ""
     *  .substringBetween("tagabctag", "tag") = "abc"
     * </pre>
     */
    public String substringBetween(String str, String tag) {
        return org.clever.common.utils.StringUtils.substringBetween(str, tag);
    }

    /**
     * 获取嵌套在两个字符串之间的字符串。只返回第一个匹配项
     * <pre>
     *  .substringBetween("wx[b]yz", "[", "]") = "b"
     *  .substringBetween(null, *, *)          = null
     *  .substringBetween(*, null, *)          = null
     *  .substringBetween(*, *, null)          = null
     *  .substringBetween("", "", "")          = ""
     *  .substringBetween("", "", "]")         = null
     *  .substringBetween("", "[", "]")        = null
     *  .substringBetween("yabcz", "", "")     = ""
     *  .substringBetween("yabcz", "y", "z")   = "abc"
     *  .substringBetween("yabczyabcz", "y", "z")   = "abc"
     * </pre>
     */
    public String substringBetween(String str, String open, String close) {
        return org.clever.common.utils.StringUtils.substringBetween(str, open, close);
    }

    /**
     * 在字符串中搜索由开始和结束标记分隔的子字符串，返回数组中所有匹配的子字符串
     * <pre>
     *  .substringsBetween("[a][b][c]", "[", "]") = ["a","b","c"]
     *  .substringsBetween(null, *, *)            = null
     *  .substringsBetween(*, null, *)            = null
     *  .substringsBetween(*, *, null)            = null
     *  .substringsBetween("", "[", "]")          = []
     * </pre>
     */
    public String[] substringsBetween(String str, String open, String close) {
        return org.clever.common.utils.StringUtils.substringsBetween(str, open, close);
    }

    // Nested extraction
    //----------------------------------------------------------------------------------------------------------------------------------------------

    // Splitting
    //----------------------------------------------------------------------------------------------------------------------------------------------

    /**
     * 使用空格作为分隔符，将提供的文本拆分为数组
     * <pre>
     *  .split(null)       = null
     *  .split("")         = []
     *  .split("abc def")  = ["abc", "def"]
     *  .split("abc  def") = ["abc", "def"]
     *  .split(" abc ")    = ["abc"]
     * </pre>
     */
    public String[] split(String str) {
        return org.clever.common.utils.StringUtils.split(str);
    }

    /**
     * <pre>
     *  .split(null, *)         = null
     *  .split("", *)           = []
     *  .split("a.b.c", '.')    = ["a", "b", "c"]
     *  .split("a..b.c", '.')   = ["a", "b", "c"]
     *  .split("a:b:c", '.')    = ["a:b:c"]
     *  .split("a b c", ' ')    = ["a", "b", "c"]
     * </pre>
     */
    public String[] split(String str, char separatorChar) {
        return org.clever.common.utils.StringUtils.split(str, separatorChar);
    }

    /**
     * <pre>
     *  .split(null, *)         = null
     *  .split("", *)           = []
     *  .split("abc def", null) = ["abc", "def"]
     *  .split("abc def", " ")  = ["abc", "def"]
     *  .split("abc  def", " ") = ["abc", "def"]
     *  .split("ab:cd:ef", ":") = ["ab", "cd", "ef"]
     * </pre>
     */
    public String[] split(String str, String separatorChars) {
        return org.clever.common.utils.StringUtils.split(str, separatorChars);
    }

    /**
     * <pre>
     *  .split(null, *, *)            = null
     *  .split("", *, *)              = []
     *  .split("ab cd ef", null, 0)   = ["ab", "cd", "ef"]
     *  .split("ab   cd ef", null, 0) = ["ab", "cd", "ef"]
     *  .split("ab:cd:ef", ":", 0)    = ["ab", "cd", "ef"]
     *  .split("ab:cd:ef", ":", 2)    = ["ab", "cd:ef"]
     * </pre>
     */
    public String[] split(String str, String separatorChars, int max) {
        return org.clever.common.utils.StringUtils.split(str, separatorChars, max);
    }

    //----------------------------------------------------------------------------------------------------------------------------------------------

    /**
     * <pre>
     *  .splitByWholeSeparator(null, *)               = null
     *  .splitByWholeSeparator("", *)                 = []
     *  .splitByWholeSeparator("ab de fg", null)      = ["ab", "de", "fg"]
     *  .splitByWholeSeparator("ab   de fg", null)    = ["ab", "de", "fg"]
     *  .splitByWholeSeparator("ab:cd:ef", ":")       = ["ab", "cd", "ef"]
     *  .splitByWholeSeparator("ab-!-cd-!-ef", "-!-") = ["ab", "cd", "ef"]
     * </pre>
     */
    public String[] splitByWholeSeparator(String str, String separator) {
        return org.clever.common.utils.StringUtils.splitByWholeSeparator(str, separator);
    }

    /**
     * <pre>
     *  .splitByWholeSeparator(null, *, *)               = null
     *  .splitByWholeSeparator("", *, *)                 = []
     *  .splitByWholeSeparator("ab de fg", null, 0)      = ["ab", "de", "fg"]
     *  .splitByWholeSeparator("ab   de fg", null, 0)    = ["ab", "de", "fg"]
     *  .splitByWholeSeparator("ab:cd:ef", ":", 2)       = ["ab", "cd:ef"]
     *  .splitByWholeSeparator("ab-!-cd-!-ef", "-!-", 5) = ["ab", "cd", "ef"]
     *  .splitByWholeSeparator("ab-!-cd-!-ef", "-!-", 2) = ["ab", "cd-!-ef"]
     * </pre>
     */
    public String[] splitByWholeSeparator(String str, String separator, int max) {
        return org.clever.common.utils.StringUtils.splitByWholeSeparator(str, separator, max);
    }

    /**
     * <pre>
     *  .splitByWholeSeparatorPreserveAllTokens(null, *)               = null
     *  .splitByWholeSeparatorPreserveAllTokens("", *)                 = []
     *  .splitByWholeSeparatorPreserveAllTokens("ab de fg", null)      = ["ab", "de", "fg"]
     *  .splitByWholeSeparatorPreserveAllTokens("ab   de fg", null)    = ["ab", "", "", "de", "fg"]
     *  .splitByWholeSeparatorPreserveAllTokens("ab:cd:ef", ":")       = ["ab", "cd", "ef"]
     *  .splitByWholeSeparatorPreserveAllTokens("ab-!-cd-!-ef", "-!-") = ["ab", "cd", "ef"]
     * </pre>
     */
    public String[] splitByWholeSeparatorPreserveAllTokens(String str, String separator) {
        return org.clever.common.utils.StringUtils.splitByWholeSeparatorPreserveAllTokens(str, separator);
    }

    /**
     * <pre>
     *  .splitByWholeSeparatorPreserveAllTokens(null, *, *)               = null
     *  .splitByWholeSeparatorPreserveAllTokens("", *, *)                 = []
     *  .splitByWholeSeparatorPreserveAllTokens("ab de fg", null, 0)      = ["ab", "de", "fg"]
     *  .splitByWholeSeparatorPreserveAllTokens("ab   de fg", null, 0)    = ["ab", "", "", "de", "fg"]
     *  .splitByWholeSeparatorPreserveAllTokens("ab:cd:ef", ":", 2)       = ["ab", "cd:ef"]
     *  .splitByWholeSeparatorPreserveAllTokens("ab-!-cd-!-ef", "-!-", 5) = ["ab", "cd", "ef"]
     *  .splitByWholeSeparatorPreserveAllTokens("ab-!-cd-!-ef", "-!-", 2) = ["ab", "cd-!-ef"]
     * </pre>
     */
    public String[] splitByWholeSeparatorPreserveAllTokens(String str, String separator, int max) {
        return org.clever.common.utils.StringUtils.splitByWholeSeparatorPreserveAllTokens(str, separator, max);
    }

    /**
     * <pre>
     *  .splitPreserveAllTokens(null)       = null
     *  .splitPreserveAllTokens("")         = []
     *  .splitPreserveAllTokens("abc def")  = ["abc", "def"]
     *  .splitPreserveAllTokens("abc  def") = ["abc", "", "def"]
     *  .splitPreserveAllTokens(" abc ")    = ["", "abc", ""]
     * </pre>
     */
    public String[] splitPreserveAllTokens(String str) {
        return org.clever.common.utils.StringUtils.splitPreserveAllTokens(str);
    }

    /**
     * <pre>
     *  .splitPreserveAllTokens(null, *)         = null
     *  .splitPreserveAllTokens("", *)           = []
     *  .splitPreserveAllTokens("a.b.c", '.')    = ["a", "b", "c"]
     *  .splitPreserveAllTokens("a..b.c", '.')   = ["a", "", "b", "c"]
     *  .splitPreserveAllTokens("a:b:c", '.')    = ["a:b:c"]
     *  .splitPreserveAllTokens("a\tb\nc", null) = ["a", "b", "c"]
     *  .splitPreserveAllTokens("a b c", ' ')    = ["a", "b", "c"]
     *  .splitPreserveAllTokens("a b c ", ' ')   = ["a", "b", "c", ""]
     *  .splitPreserveAllTokens("a b c  ", ' ')   = ["a", "b", "c", "", ""]
     *  .splitPreserveAllTokens(" a b c", ' ')   = ["", a", "b", "c"]
     *  .splitPreserveAllTokens("  a b c", ' ')  = ["", "", a", "b", "c"]
     *  .splitPreserveAllTokens(" a b c ", ' ')  = ["", a", "b", "c", ""]
     * </pre>
     */
    public String[] splitPreserveAllTokens(String str, char separatorChar) {
        return org.clever.common.utils.StringUtils.splitPreserveAllTokens(str, separatorChar);
    }


    /**
     * <pre>
     *  .splitPreserveAllTokens(null, *)           = null
     *  .splitPreserveAllTokens("", *)             = []
     *  .splitPreserveAllTokens("abc def", null)   = ["abc", "def"]
     *  .splitPreserveAllTokens("abc def", " ")    = ["abc", "def"]
     *  .splitPreserveAllTokens("abc  def", " ")   = ["abc", "", def"]
     *  .splitPreserveAllTokens("ab:cd:ef", ":")   = ["ab", "cd", "ef"]
     *  .splitPreserveAllTokens("ab:cd:ef:", ":")  = ["ab", "cd", "ef", ""]
     *  .splitPreserveAllTokens("ab:cd:ef::", ":") = ["ab", "cd", "ef", "", ""]
     *  .splitPreserveAllTokens("ab::cd:ef", ":")  = ["ab", "", cd", "ef"]
     *  .splitPreserveAllTokens(":cd:ef", ":")     = ["", cd", "ef"]
     *  .splitPreserveAllTokens("::cd:ef", ":")    = ["", "", cd", "ef"]
     *  .splitPreserveAllTokens(":cd:ef:", ":")    = ["", cd", "ef", ""]
     * </pre>
     */
    public String[] splitPreserveAllTokens(String str, String separatorChars) {
        return org.clever.common.utils.StringUtils.splitPreserveAllTokens(str, separatorChars);
    }

    /**
     * <pre>
     *  .splitPreserveAllTokens(null, *, *)            = null
     *  .splitPreserveAllTokens("", *, *)              = []
     *  .splitPreserveAllTokens("ab de fg", null, 0)   = ["ab", "cd", "ef"]
     *  .splitPreserveAllTokens("ab   de fg", null, 0) = ["ab", "cd", "ef"]
     *  .splitPreserveAllTokens("ab:cd:ef", ":", 0)    = ["ab", "cd", "ef"]
     *  .splitPreserveAllTokens("ab:cd:ef", ":", 2)    = ["ab", "cd:ef"]
     *  .splitPreserveAllTokens("ab   de fg", null, 2) = ["ab", "  de fg"]
     *  .splitPreserveAllTokens("ab   de fg", null, 3) = ["ab", "", " de fg"]
     *  .splitPreserveAllTokens("ab   de fg", null, 4) = ["ab", "", "", "de fg"]
     * </pre>
     */
    public String[] splitPreserveAllTokens(String str, String separatorChars, int max) {
        return org.clever.common.utils.StringUtils.splitPreserveAllTokens(str, separatorChars, max);
    }

    /**
     * <pre>
     *  .splitByCharacterType(null)         = null
     *  .splitByCharacterType("")           = []
     *  .splitByCharacterType("ab de fg")   = ["ab", " ", "de", " ", "fg"]
     *  .splitByCharacterType("ab   de fg") = ["ab", "   ", "de", " ", "fg"]
     *  .splitByCharacterType("ab:cd:ef")   = ["ab", ":", "cd", ":", "ef"]
     *  .splitByCharacterType("number5")    = ["number", "5"]
     *  .splitByCharacterType("fooBar")     = ["foo", "B", "ar"]
     *  .splitByCharacterType("foo200Bar")  = ["foo", "200", "B", "ar"]
     *  .splitByCharacterType("ASFRules")   = ["ASFR", "ules"]
     * </pre>
     */
    public String[] splitByCharacterType(String str) {
        return org.clever.common.utils.StringUtils.splitByCharacterType(str);
    }

    /**
     * <pre>
     *  .splitByCharacterTypeCamelCase(null)         = null
     *  .splitByCharacterTypeCamelCase("")           = []
     *  .splitByCharacterTypeCamelCase("ab de fg")   = ["ab", " ", "de", " ", "fg"]
     *  .splitByCharacterTypeCamelCase("ab   de fg") = ["ab", "   ", "de", " ", "fg"]
     *  .splitByCharacterTypeCamelCase("ab:cd:ef")   = ["ab", ":", "cd", ":", "ef"]
     *  .splitByCharacterTypeCamelCase("number5")    = ["number", "5"]
     *  .splitByCharacterTypeCamelCase("fooBar")     = ["foo", "Bar"]
     *  .splitByCharacterTypeCamelCase("foo200Bar")  = ["foo", "200", "Bar"]
     *  .splitByCharacterTypeCamelCase("ASFRules")   = ["ASF", "Rules"]
     * </pre>
     */
    public String[] splitByCharacterTypeCamelCase(String str) {
        return org.clever.common.utils.StringUtils.splitByCharacterTypeCamelCase(str);
    }

    // Joining
    //----------------------------------------------------------------------------------------------------------------------------------------------

    /**
     * <pre>
     *  .join(null)            = null
     *  .join([])              = ""
     *  .join([null])          = ""
     *  .join(["a", "b", "c"]) = "abc"
     *  .join([null, "", "a"]) = "a"
     * </pre>
     */
    public <T> String join(T... elements) {
        return org.clever.common.utils.StringUtils.join(elements);
    }

    /**
     * <pre>
     *  .join(null, *)               = null
     *  .join([], *)                 = ""
     *  .join([null], *)             = ""
     *  .join(["a", "b", "c"], ';')  = "a;b;c"
     *  .join(["a", "b", "c"], null) = "abc"
     *  .join([null, "", "a"], ';')  = ";;a"
     * </pre>
     */
    public String join(Object[] array, char separator) {
        return org.clever.common.utils.StringUtils.join(array, separator);
    }

    /**
     * <pre>
     *  .join(null, *)               = null
     *  .join([], *)                 = ""
     *  .join([null], *)             = ""
     *  .join([1, 2, 3], ';')  = "1;2;3"
     *  .join([1, 2, 3], null) = "123"
     * </pre>
     */
    public String join(long[] array, char separator) {
        return org.clever.common.utils.StringUtils.join(array, separator);
    }

    /**
     * <pre>
     *  .join(null, *)               = null
     *  .join([], *)                 = ""
     *  .join([null], *)             = ""
     *  .join([1, 2, 3], ';')  = "1;2;3"
     *  .join([1, 2, 3], null) = "123"
     * </pre>
     */
    public String join(int[] array, char separator) {
        return org.clever.common.utils.StringUtils.join(array, separator);
    }

    /**
     * <pre>
     *  .join(null, *)               = null
     *  .join([], *)                 = ""
     *  .join([null], *)             = ""
     *  .join([1, 2, 3], ';')  = "1;2;3"
     *  .join([1, 2, 3], null) = "123"
     * </pre>
     */
    public String join(short[] array, char separator) {
        return org.clever.common.utils.StringUtils.join(array, separator);
    }

    /**
     * <pre>
     *  .join(null, *)               = null
     *  .join([], *)                 = ""
     *  .join([null], *)             = ""
     *  .join([1, 2, 3], ';')  = "1;2;3"
     *  .join([1, 2, 3], null) = "123"
     * </pre>
     */
    public String join(byte[] array, char separator) {
        return org.clever.common.utils.StringUtils.join(array, separator);
    }

    /**
     * <pre>
     *  .join(null, *)               = null
     *  .join([], *)                 = ""
     *  .join([null], *)             = ""
     *  .join([1, 2, 3], ';')  = "1;2;3"
     *  .join([1, 2, 3], null) = "123"
     * </pre>
     */
    public String join(char[] array, char separator) {
        return org.clever.common.utils.StringUtils.join(array, separator);
    }

    /**
     * <pre>
     *  .join(null, *)               = null
     *  .join([], *)                 = ""
     *  .join([null], *)             = ""
     *  .join([1, 2, 3], ';')  = "1;2;3"
     *  .join([1, 2, 3], null) = "123"
     * </pre>
     */
    public String join(float[] array, char separator) {
        return org.clever.common.utils.StringUtils.join(array, separator);
    }

    /**
     * <pre>
     *  .join(null, *)               = null
     *  .join([], *)                 = ""
     *  .join([null], *)             = ""
     *  .join([1, 2, 3], ';')  = "1;2;3"
     *  .join([1, 2, 3], null) = "123"
     * </pre>
     */
    public String join(double[] array, char separator) {
        return org.clever.common.utils.StringUtils.join(array, separator);
    }

    /**
     * <pre>
     *  .join(null, *)               = null
     *  .join([], *)                 = ""
     *  .join([null], *)             = ""
     *  .join(["a", "b", "c"], ';')  = "a;b;c"
     *  .join(["a", "b", "c"], null) = "abc"
     *  .join([null, "", "a"], ';')  = ";;a"
     * </pre>
     */
    public String join(Object[] array, char separator, int startIndex, int endIndex) {
        return org.clever.common.utils.StringUtils.join(array, separator, startIndex, endIndex);
    }

    /**
     * <pre>
     *  .join(null, *)               = null
     *  .join([], *)                 = ""
     *  .join([null], *)             = ""
     *  .join([1, 2, 3], ';')  = "1;2;3"
     *  .join([1, 2, 3], null) = "123"
     * </pre>
     */
    public String join(long[] array, char separator, int startIndex, int endIndex) {
        return org.clever.common.utils.StringUtils.join(array, separator, startIndex, endIndex);
    }

    /**
     * <pre>
     *  .join(null, *)               = null
     *  .join([], *)                 = ""
     *  .join([null], *)             = ""
     *  .join([1, 2, 3], ';')  = "1;2;3"
     *  .join([1, 2, 3], null) = "123"
     * </pre>
     */
    public String join(int[] array, char separator, int startIndex, int endIndex) {
        return org.clever.common.utils.StringUtils.join(array, separator, startIndex, endIndex);
    }

    /**
     * <pre>
     *  .join(null, *)               = null
     *  .join([], *)                 = ""
     *  .join([null], *)             = ""
     *  .join([1, 2, 3], ';')  = "1;2;3"
     *  .join([1, 2, 3], null) = "123"
     * </pre>
     */
    public String join(byte[] array, char separator, int startIndex, int endIndex) {
        return org.clever.common.utils.StringUtils.join(array, separator, startIndex, endIndex);
    }

    /**
     * <pre>
     *  .join(null, *)               = null
     *  .join([], *)                 = ""
     *  .join([null], *)             = ""
     *  .join([1, 2, 3], ';')  = "1;2;3"
     *  .join([1, 2, 3], null) = "123"
     * </pre>
     */
    public String join(short[] array, char separator, int startIndex, int endIndex) {
        return org.clever.common.utils.StringUtils.join(array, separator, startIndex, endIndex);
    }

    /**
     * <pre>
     *  .join(null, *)               = null
     *  .join([], *)                 = ""
     *  .join([null], *)             = ""
     *  .join([1, 2, 3], ';')  = "1;2;3"
     *  .join([1, 2, 3], null) = "123"
     * </pre>
     */
    public String join(char[] array, char separator, int startIndex, int endIndex) {
        return org.clever.common.utils.StringUtils.join(array, separator, startIndex, endIndex);
    }

    /**
     * <pre>
     *  .join(null, *)               = null
     *  .join([], *)                 = ""
     *  .join([null], *)             = ""
     *  .join([1, 2, 3], ';')  = "1;2;3"
     *  .join([1, 2, 3], null) = "123"
     * </pre>
     */
    public String join(double[] array, char separator, int startIndex, int endIndex) {
        return org.clever.common.utils.StringUtils.join(array, separator, startIndex, endIndex);
    }

    /**
     * <pre>
     *  .join(null, *)               = null
     *  .join([], *)                 = ""
     *  .join([null], *)             = ""
     *  .join([1, 2, 3], ';')  = "1;2;3"
     *  .join([1, 2, 3], null) = "123"
     * </pre>
     */
    public String join(float[] array, char separator, int startIndex, int endIndex) {
        return org.clever.common.utils.StringUtils.join(array, separator, startIndex, endIndex);
    }

    /**
     * <pre>
     *  .join(null, *)                = null
     *  .join([], *)                  = ""
     *  .join([null], *)              = ""
     *  .join(["a", "b", "c"], "--")  = "a--b--c"
     *  .join(["a", "b", "c"], null)  = "abc"
     *  .join(["a", "b", "c"], "")    = "abc"
     *  .join([null, "", "a"], ',')   = ",,a"
     * </pre>
     */
    public String join(Object[] array, String separator) {
        return org.clever.common.utils.StringUtils.join(array, separator);
    }

    /**
     * <pre>
     *  .join(null, *, *, *)                = null
     *  .join([], *, *, *)                  = ""
     *  .join([null], *, *, *)              = ""
     *  .join(["a", "b", "c"], "--", 0, 3)  = "a--b--c"
     *  .join(["a", "b", "c"], "--", 1, 3)  = "b--c"
     *  .join(["a", "b", "c"], "--", 2, 3)  = "c"
     *  .join(["a", "b", "c"], "--", 2, 2)  = ""
     *  .join(["a", "b", "c"], null, 0, 3)  = "abc"
     *  .join(["a", "b", "c"], "", 0, 3)    = "abc"
     *  .join([null, "", "a"], ',', 0, 3)   = ",,a"
     * </pre>
     */
    public String join(Object[] array, String separator, int startIndex, int endIndex) {
        return org.clever.common.utils.StringUtils.join(array, separator, startIndex, endIndex);
    }

    /**
     * 将所提供迭代器的元素联接到包含所提供元素的单个字符串中
     */
    public String join(Iterator<?> iterator, char separator) {
        return org.clever.common.utils.StringUtils.join(iterator, separator);
    }

    /**
     * 将所提供迭代器的元素联接到包含所提供元素的单个字符串中
     */
    public String join(Iterator<?> iterator, String separator) {
        return org.clever.common.utils.StringUtils.join(iterator, separator);
    }

    /**
     * 将提供的Iterable的元素联接到包含所提供元素的单个字符串中
     */
    public String join(Iterable<?> iterable, char separator) {
        return org.clever.common.utils.StringUtils.join(iterable, separator);
    }

    /**
     * 将提供的Iterable的元素联接到包含所提供元素的单个字符串中
     */
    public String join(Iterable<?> iterable, String separator) {
        return org.clever.common.utils.StringUtils.join(iterable, separator);
    }

    /**
     * <pre>
     *  .join(null, *)               = null
     *  .join([], *)                 = ""
     *  .join([null], *)             = ""
     *  .join(["a", "b", "c"], ';')  = "a;b;c"
     *  .join(["a", "b", "c"], null) = "abc"
     *  .join([null, "", "a"], ';')  = ";;a"
     * </pre>
     */
    public String join(List<?> list, char separator, int startIndex, int endIndex) {
        return org.clever.common.utils.StringUtils.join(list, separator, startIndex, endIndex);
    }

    /**
     * <pre>
     *  .join(null, *)               = null
     *  .join([], *)                 = ""
     *  .join([null], *)             = ""
     *  .join(["a", "b", "c"], ';')  = "a;b;c"
     *  .join(["a", "b", "c"], null) = "abc"
     *  .join([null, "", "a"], ';')  = ";;a"
     * </pre>
     */
    public String join(List<?> list, String separator, int startIndex, int endIndex) {
        return org.clever.common.utils.StringUtils.join(list, separator, startIndex, endIndex);
    }

    /**
     * <pre>
     *  .joinWith(",", {"a", "b"})        = "a,b"
     *  .joinWith(",", {"a", "b",""})     = "a,b,"
     *  .joinWith(",", {"a", null, "b"})  = "a,,b"
     *  .joinWith(null, {"a", "b"})       = "ab"
     * </pre>
     */
    public String joinWith(String separator, Object... objects) {
        return org.clever.common.utils.StringUtils.joinWith(separator, objects);
    }

    // Delete
    //----------------------------------------------------------------------------------------------------------------------------------------------

    /**
     * <pre>
     *  .deleteWhitespace(null)         = null
     *  .deleteWhitespace("")           = ""
     *  .deleteWhitespace("abc")        = "abc"
     *  .deleteWhitespace("   ab  c  ") = "abc"
     * </pre>
     */
    public String deleteWhitespace(String str) {
        return org.clever.common.utils.StringUtils.deleteWhitespace(str);
    }

    // Remove
    //----------------------------------------------------------------------------------------------------------------------------------------------

    /**
     * <pre>
     *  .removeStart(null, *)      = null
     *  .removeStart("", *)        = ""
     *  .removeStart(*, null)      = *
     *  .removeStart("www.domain.com", "www.")   = "domain.com"
     *  .removeStart("domain.com", "www.")       = "domain.com"
     *  .removeStart("www.domain.com", "domain") = "www.domain.com"
     *  .removeStart("abc", "")    = "abc"
     * </pre>
     */
    public String removeStart(String str, String remove) {
        return org.clever.common.utils.StringUtils.removeStart(str, remove);
    }

    /**
     * <pre>
     *  .removeStartIgnoreCase(null, *)      = null
     *  .removeStartIgnoreCase("", *)        = ""
     *  .removeStartIgnoreCase(*, null)      = *
     *  .removeStartIgnoreCase("www.domain.com", "www.")   = "domain.com"
     *  .removeStartIgnoreCase("www.domain.com", "WWW.")   = "domain.com"
     *  .removeStartIgnoreCase("domain.com", "www.")       = "domain.com"
     *  .removeStartIgnoreCase("www.domain.com", "domain") = "www.domain.com"
     *  .removeStartIgnoreCase("abc", "")    = "abc"
     * </pre>
     */
    public String removeStartIgnoreCase(String str, String remove) {
        return org.clever.common.utils.StringUtils.removeStartIgnoreCase(str, remove);
    }

    /**
     * <pre>
     *  .removeEnd(null, *)      = null
     *  .removeEnd("", *)        = ""
     *  .removeEnd(*, null)      = *
     *  .removeEnd("www.domain.com", ".com.")  = "www.domain.com"
     *  .removeEnd("www.domain.com", ".com")   = "www.domain"
     *  .removeEnd("www.domain.com", "domain") = "www.domain.com"
     *  .removeEnd("abc", "")    = "abc"
     * </pre>
     */
    public String removeEnd(String str, String remove) {
        return org.clever.common.utils.StringUtils.removeEnd(str, remove);
    }

    /**
     * <pre>
     *  .removeEndIgnoreCase(null, *)      = null
     *  .removeEndIgnoreCase("", *)        = ""
     *  .removeEndIgnoreCase(*, null)      = *
     *  .removeEndIgnoreCase("www.domain.com", ".com.")  = "www.domain.com"
     *  .removeEndIgnoreCase("www.domain.com", ".com")   = "www.domain"
     *  .removeEndIgnoreCase("www.domain.com", "domain") = "www.domain.com"
     *  .removeEndIgnoreCase("abc", "")    = "abc"
     *  .removeEndIgnoreCase("www.domain.com", ".COM") = "www.domain")
     *  .removeEndIgnoreCase("www.domain.COM", ".com") = "www.domain")
     * </pre>
     */
    public String removeEndIgnoreCase(String str, String remove) {
        return org.clever.common.utils.StringUtils.removeEndIgnoreCase(str, remove);
    }

    /**
     * <pre>
     *  .remove(null, *)        = null
     *  .remove("", *)          = ""
     *  .remove(*, null)        = *
     *  .remove(*, "")          = *
     *  .remove("queued", "ue") = "qd"
     *  .remove("queued", "zz") = "queued"
     * </pre>
     */
    public String remove(String str, String remove) {
        return org.clever.common.utils.StringUtils.remove(str, remove);
    }

    /**
     * <pre>
     *  .removeIgnoreCase(null, *)        = null
     *  .removeIgnoreCase("", *)          = ""
     *  .removeIgnoreCase(*, null)        = *
     *  .removeIgnoreCase(*, "")          = *
     *  .removeIgnoreCase("queued", "ue") = "qd"
     *  .removeIgnoreCase("queued", "zz") = "queued"
     *  .removeIgnoreCase("quEUed", "UE") = "qd"
     *  .removeIgnoreCase("queued", "zZ") = "queued"
     * </pre>
     */
    public String removeIgnoreCase(String str, String remove) {
        return org.clever.common.utils.StringUtils.removeIgnoreCase(str, remove);
    }

    /**
     * <pre>
     *  .remove(null, *)       = null
     *  .remove("", *)         = ""
     *  .remove("queued", 'u') = "qeed"
     *  .remove("queued", 'z') = "queued"
     * </pre>
     */
    public String remove(String str, char remove) {
        return org.clever.common.utils.StringUtils.remove(str, remove);
    }

    // Replacing
    //----------------------------------------------------------------------------------------------------------------------------------------------

    /**
     * <pre>
     *
     * </pre>
     */
    public String replaceOnce(String text, String searchString, String replacement) {
        return org.clever.common.utils.StringUtils.replaceOnce(text, searchString, replacement);
    }

    /**
     * <pre>
     *
     * </pre>
     */
    public String replaceOnceIgnoreCase(String text, String searchString, String replacement) {
        return org.clever.common.utils.StringUtils.replaceOnceIgnoreCase(text, searchString, replacement);
    }

    /**
     * <pre>
     *
     * </pre>
     */
    public String replace(String text, String searchString, String replacement) {
        return org.clever.common.utils.StringUtils.replace(text, searchString, replacement);
    }

    /**
     * <pre>
     *
     * </pre>
     */
    public String replaceIgnoreCase(String text, String searchString, String replacement) {
        return org.clever.common.utils.StringUtils.replaceIgnoreCase(text, searchString, replacement);
    }

    /**
     * <pre>
     *
     * </pre>
     */
    public String replace(String text, String searchString, String replacement, int max) {
        return org.clever.common.utils.StringUtils.replace(text, searchString, replacement, max);
    }

    /**
     * <pre>
     *
     * </pre>
     */
    public String replaceIgnoreCase(String text, String searchString, String replacement, int max) {
        return org.clever.common.utils.StringUtils.replaceIgnoreCase(text, searchString, replacement, max);
    }

    /**
     * <pre>
     *
     * </pre>
     */
    public String replaceEach(String text, String[] searchList, String[] replacementList) {
        return org.clever.common.utils.StringUtils.replaceEach(text, searchList, replacementList);
    }

    /**
     * <pre>
     *
     * </pre>
     */
    public String replaceEachRepeatedly(String text, String[] searchList, String[] replacementList) {
        return org.clever.common.utils.StringUtils.replaceEachRepeatedly(text, searchList, replacementList);
    }

    /**
     * <pre>
     *
     * </pre>
     */
    public String replaceChars(String str, char searchChar, char replaceChar) {
        return org.clever.common.utils.StringUtils.replaceChars(str, searchChar, replaceChar);
    }

    /**
     * <pre>
     *
     * </pre>
     */
    public String replaceChars(String str, String searchChars, String replaceChars) {
        return org.clever.common.utils.StringUtils.replaceChars(str, searchChars, replaceChars);
    }

    /**
     * <pre>
     *
     * </pre>
     */
    public String overlay(String str, String overlay, int start, int end) {
        return org.clever.common.utils.StringUtils.overlay(str, overlay, start, end);
    }

    /**
     * <pre>
     *
     * </pre>
     */
    public String chomp(String str) {
        return org.clever.common.utils.StringUtils.chomp(str);
    }

    /**
     * <pre>
     *
     * </pre>
     */
    public String chop(String str) {
        return org.clever.common.utils.StringUtils.chop(str);
    }

    /**
     * <pre>
     *
     * </pre>
     */
    public String repeat(String str, int repeat) {
        return org.clever.common.utils.StringUtils.repeat(str, repeat);
    }

    /**
     * <pre>
     *
     * </pre>
     */
    public String repeat(String str, String separator, int repeat) {
        return org.clever.common.utils.StringUtils.repeat(str, separator, repeat);
    }

    /**
     * <pre>
     *
     * </pre>
     */
    public String repeat(char ch, int repeat) {
        return org.clever.common.utils.StringUtils.repeat(ch, repeat);
    }

    /**
     * <pre>
     *
     * </pre>
     */
    public String rightPad(String str, int size) {
        return org.clever.common.utils.StringUtils.rightPad(str, size);
    }

    /**
     * <pre>
     *
     * </pre>
     */
    public String rightPad(String str, int size, char padChar) {
        return org.clever.common.utils.StringUtils.rightPad(str, size, padChar);
    }

    /**
     * <pre>
     *
     * </pre>
     */
    public String rightPad(String str, int size, String padStr) {
        return org.clever.common.utils.StringUtils.rightPad(str, size, padStr);
    }

    /**
     * <pre>
     *
     * </pre>
     */
    public String leftPad(String str, int size) {
        return org.clever.common.utils.StringUtils.leftPad(str, size);
    }

    /**
     * <pre>
     *
     * </pre>
     */
    public String leftPad(String str, int size, char padChar) {
        return org.clever.common.utils.StringUtils.leftPad(str, size, padChar);
    }

    /**
     * <pre>
     *
     * </pre>
     */
    public String leftPad(String str, int size, String padStr) {
        return org.clever.common.utils.StringUtils.leftPad(str, size, padStr);
    }

    /**
     * <pre>
     *
     * </pre>
     */
    public int length(CharSequence cs) {
        return org.clever.common.utils.StringUtils.length(cs);
    }

    /**
     * <pre>
     *
     * </pre>
     */
    public String center(String str, int size) {
        return org.clever.common.utils.StringUtils.center(str, size);
    }

    /**
     * <pre>
     *
     * </pre>
     */
    public String center(String str, int size, char padChar) {
        return org.clever.common.utils.StringUtils.center(str, size, padChar);
    }

    /**
     * <pre>
     *
     * </pre>
     */
    public String center(String str, int size, String padStr) {
        return org.clever.common.utils.StringUtils.center(str, size, padStr);
    }

    /**
     * <pre>
     *
     * </pre>
     */
    public String upperCase(String str) {
        return org.clever.common.utils.StringUtils.upperCase(str);
    }

    /**
     * <pre>
     *
     * </pre>
     */
    public String upperCase(String str, Locale locale) {
        return org.clever.common.utils.StringUtils.upperCase(str, locale);
    }

    /**
     * <pre>
     *
     * </pre>
     */
    public String lowerCase(String str) {
        return org.clever.common.utils.StringUtils.lowerCase(str);
    }

    /**
     * <pre>
     *
     * </pre>
     */
    public String lowerCase(String str, Locale locale) {
        return org.clever.common.utils.StringUtils.lowerCase(str, locale);
    }

    /**
     * <pre>
     *
     * </pre>
     */
    public String capitalize(String str) {
        return org.clever.common.utils.StringUtils.capitalize(str);
    }

    /**
     * <pre>
     *
     * </pre>
     */
    public String uncapitalize(String str) {
        return org.clever.common.utils.StringUtils.uncapitalize(str);
    }

    /**
     * <pre>
     *
     * </pre>
     */
    public String swapCase(String str) {
        return org.clever.common.utils.StringUtils.swapCase(str);
    }

    /**
     * <pre>
     *
     * </pre>
     */
    public int countMatches(CharSequence str, CharSequence sub) {
        return org.clever.common.utils.StringUtils.countMatches(str, sub);
    }

    /**
     * <pre>
     *
     * </pre>
     */
    public int countMatches(CharSequence str, char ch) {
        return org.clever.common.utils.StringUtils.countMatches(str, ch);
    }

    /**
     * <pre>
     *
     * </pre>
     */
    public boolean isAlpha(CharSequence cs) {
        return org.clever.common.utils.StringUtils.isAlpha(cs);
    }

    /**
     * <pre>
     *
     * </pre>
     */
    public boolean isAlphaSpace(CharSequence cs) {
        return org.clever.common.utils.StringUtils.isAlphaSpace(cs);
    }

    /**
     * <pre>
     *
     * </pre>
     */
    public boolean isAlphanumeric(CharSequence cs) {
        return org.clever.common.utils.StringUtils.isAlphanumeric(cs);
    }

    /**
     * <pre>
     *
     * </pre>
     */
    public boolean isAlphanumericSpace(CharSequence cs) {
        return org.clever.common.utils.StringUtils.isAlphanumericSpace(cs);
    }

    /**
     * <pre>
     *
     * </pre>
     */
    public boolean isAsciiPrintable(CharSequence cs) {
        return org.clever.common.utils.StringUtils.isAsciiPrintable(cs);
    }

    /**
     * <pre>
     *
     * </pre>
     */
    public boolean isNumeric(CharSequence cs) {
        return org.clever.common.utils.StringUtils.isNumeric(cs);
    }

    /**
     * <pre>
     *
     * </pre>
     */
    public boolean isNumericSpace(CharSequence cs) {
        return org.clever.common.utils.StringUtils.isNumericSpace(cs);
    }

    /**
     * <pre>
     *
     * </pre>
     */
    public String getDigits(String str) {
        return org.clever.common.utils.StringUtils.getDigits(str);
    }

    /**
     * <pre>
     *
     * </pre>
     */
    public boolean isWhitespace(CharSequence cs) {
        return org.clever.common.utils.StringUtils.isWhitespace(cs);
    }

    /**
     * <pre>
     *
     * </pre>
     */
    public boolean isAllLowerCase(CharSequence cs) {
        return org.clever.common.utils.StringUtils.isAllLowerCase(cs);
    }

    /**
     * <pre>
     *
     * </pre>
     */
    public boolean isAllUpperCase(CharSequence cs) {
        return org.clever.common.utils.StringUtils.isAllUpperCase(cs);
    }

    /**
     * <pre>
     *
     * </pre>
     */
    public boolean isMixedCase(CharSequence cs) {
        return org.clever.common.utils.StringUtils.isMixedCase(cs);
    }

    /**
     * <pre>
     *
     * </pre>
     */
    public String defaultString(String str) {
        return org.clever.common.utils.StringUtils.defaultString(str);
    }

    /**
     * <pre>
     *
     * </pre>
     */
    public String defaultString(String str, String defaultStr) {
        return org.clever.common.utils.StringUtils.defaultString(str, defaultStr);
    }

    /**
     * <pre>
     *
     * </pre>
     */
    public <T extends CharSequence> T firstNonBlank(T... values) {
        return org.clever.common.utils.StringUtils.firstNonBlank(values);
    }

    /**
     * <pre>
     *
     * </pre>
     */
    public <T extends CharSequence> T firstNonEmpty(T... values) {
        return org.clever.common.utils.StringUtils.firstNonEmpty(values);
    }

    /**
     * <pre>
     *
     * </pre>
     */
    public <T extends CharSequence> T defaultIfBlank(T str, T defaultStr) {
        return org.clever.common.utils.StringUtils.defaultIfBlank(str, defaultStr);
    }

    /**
     * <pre>
     *
     * </pre>
     */
    public <T extends CharSequence> T defaultIfEmpty(T str, T defaultStr) {
        return org.clever.common.utils.StringUtils.defaultIfEmpty(str, defaultStr);
    }

    /**
     * <pre>
     *
     * </pre>
     */
    public String rotate(String str, int shift) {
        return org.clever.common.utils.StringUtils.rotate(str, shift);
    }

    /**
     * <pre>
     *
     * </pre>
     */
    public String reverse(String str) {
        return org.clever.common.utils.StringUtils.reverse(str);
    }

    /**
     * <pre>
     *
     * </pre>
     */
    public String reverseDelimited(String str, char separatorChar) {
        return org.clever.common.utils.StringUtils.reverseDelimited(str, separatorChar);
    }

    /**
     * <pre>
     *
     * </pre>
     */
    public String abbreviate(String str, int maxWidth) {
        return org.clever.common.utils.StringUtils.abbreviate(str, maxWidth);
    }

    /**
     * <pre>
     *
     * </pre>
     */
    public String abbreviate(String str, int offset, int maxWidth) {
        return org.clever.common.utils.StringUtils.abbreviate(str, offset, maxWidth);
    }

    /**
     * <pre>
     *
     * </pre>
     */
    public String abbreviate(String str, String abbrevMarker, int maxWidth) {
        return org.clever.common.utils.StringUtils.abbreviate(str, abbrevMarker, maxWidth);
    }

    /**
     * <pre>
     *
     * </pre>
     */
    public String abbreviate(String str, String abbrevMarker, int offset, int maxWidth) {
        return org.clever.common.utils.StringUtils.abbreviate(str, abbrevMarker, maxWidth);
    }

    /**
     * <pre>
     *
     * </pre>
     */
    public String abbreviateMiddle(String str, String middle, int length) {
        return org.clever.common.utils.StringUtils.abbreviateMiddle(str, middle, length);
    }

    /**
     * <pre>
     *
     * </pre>
     */
    public String difference(String str1, String str2) {
        return org.clever.common.utils.StringUtils.difference(str1, str2);
    }

    /**
     * <pre>
     *
     * </pre>
     */
    public int indexOfDifference(CharSequence cs1, CharSequence cs2) {
        return org.clever.common.utils.StringUtils.indexOfDifference(cs1, cs2);
    }

    /**
     * <pre>
     *
     * </pre>
     */
    public int indexOfDifference(CharSequence... css) {
        return org.clever.common.utils.StringUtils.indexOfDifference(css);
    }

    /**
     * <pre>
     *
     * </pre>
     */
    public String getCommonPrefix(String... strs) {
        return org.clever.common.utils.StringUtils.getCommonPrefix(strs);
    }

    /**
     * <pre>
     *
     * </pre>
     */
    public boolean startsWith(CharSequence str, CharSequence prefix) {
        return org.clever.common.utils.StringUtils.startsWith(str, prefix);
    }

    /**
     * <pre>
     *
     * </pre>
     */
    public boolean startsWithIgnoreCase(CharSequence str, CharSequence prefix) {
        return org.clever.common.utils.StringUtils.startsWithIgnoreCase(str, prefix);
    }

    /**
     * <pre>
     *
     * </pre>
     */
    public boolean startsWithAny(CharSequence sequence, CharSequence... searchStrings) {
        return org.clever.common.utils.StringUtils.startsWithAny(sequence, searchStrings);
    }

    /**
     * <pre>
     *
     * </pre>
     */
    public boolean endsWith(CharSequence str, CharSequence suffix) {
        return org.clever.common.utils.StringUtils.endsWith(str, suffix);
    }

    /**
     * <pre>
     *
     * </pre>
     */
    public boolean endsWithIgnoreCase(CharSequence str, CharSequence suffix) {
        return org.clever.common.utils.StringUtils.endsWithIgnoreCase(str, suffix);
    }

    /**
     * <pre>
     *
     * </pre>
     */
    public String normalizeSpace(String str) {
        return org.clever.common.utils.StringUtils.normalizeSpace(str);
    }

    /**
     * <pre>
     *
     * </pre>
     */
    public boolean endsWithAny(CharSequence sequence, CharSequence... searchStrings) {
        return org.clever.common.utils.StringUtils.endsWithAny(sequence, searchStrings);
    }

    /**
     * <pre>
     *
     * </pre>
     */
    public String appendIfMissing(String str, CharSequence suffix, CharSequence... suffixes) {
        return org.clever.common.utils.StringUtils.appendIfMissing(str, suffix, suffixes);
    }

    /**
     * <pre>
     *
     * </pre>
     */
    public String appendIfMissingIgnoreCase(String str, CharSequence suffix, CharSequence... suffixes) {
        return org.clever.common.utils.StringUtils.appendIfMissingIgnoreCase(str, suffix, suffixes);
    }

    /**
     * <pre>
     *
     * </pre>
     */
    public String prependIfMissing(String str, CharSequence prefix, CharSequence... prefixes) {
        return org.clever.common.utils.StringUtils.prependIfMissing(str, prefix, prefixes);
    }

    /**
     * <pre>
     *
     * </pre>
     */
    public String prependIfMissingIgnoreCase(String str, CharSequence prefix, CharSequence... prefixes) {
        return org.clever.common.utils.StringUtils.prependIfMissingIgnoreCase(str, prefix, prefixes);
    }

    /**
     * <pre>
     *
     * </pre>
     */
    public String toEncodedString(byte[] bytes, Charset charset) {
        return org.clever.common.utils.StringUtils.toEncodedString(bytes, charset);
    }

    /**
     * <pre>
     *
     * </pre>
     */
    public String wrap(String str, char wrapWith) {
        return org.clever.common.utils.StringUtils.wrap(str, wrapWith);
    }

    /**
     * <pre>
     *
     * </pre>
     */
    public String wrap(String str, String wrapWith) {
        return org.clever.common.utils.StringUtils.wrap(str, wrapWith);
    }

    /**
     * <pre>
     *
     * </pre>
     */
    public String wrapIfMissing(String str, char wrapWith) {
        return org.clever.common.utils.StringUtils.wrapIfMissing(str, wrapWith);
    }

    /**
     * <pre>
     *
     * </pre>
     */
    public String wrapIfMissing(String str, String wrapWith) {
        return org.clever.common.utils.StringUtils.wrapIfMissing(str, wrapWith);
    }

    /**
     * <pre>
     *
     * </pre>
     */
    public String unwrap(String str, String wrapToken) {
        return org.clever.common.utils.StringUtils.unwrap(str, wrapToken);
    }

    /**
     * <pre>
     *  .unwrap(null, null)         = null
     *  .unwrap(null, '\0')         = null
     *  .unwrap(null, '1')          = null
     *  .unwrap("\'abc\'", '\'')    = "abc"
     *  .unwrap("AABabcBAA", 'A')   = "ABabcBA"
     *  .unwrap("A", '#')           = "A"
     *  .unwrap("#A", '#')          = "#A"
     *  .unwrap("A#", '#')          = "A#"
     * </pre>
     */
    public String unwrap(String str, char wrapChar) {
        return org.clever.common.utils.StringUtils.unwrap(str, wrapChar);
    }

    /**
     * <pre>
     *  .toCodePoints(null)   =  null
     *  .toCodePoints("")     =  []  // empty array
     * </pre>
     */
    public int[] toCodePoints(CharSequence str) {
        return org.clever.common.utils.StringUtils.toCodePoints(str);
    }
}
