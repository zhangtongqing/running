package com.peipao.framework.util;


import com.peipao.framework.constant.ResultMsg;
import com.peipao.framework.exception.BusinessException;
import com.peipao.qdl.appeal.model.QueryTypeEnum;
import com.peipao.qdl.discover.model.SortEnum;
import com.peipao.qdl.statistics.model.SortTypeEnum;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SuppressWarnings("rawtypes")
public class ValidateUtil {

	public final static boolean isNull(Collection collection) {
		return collection == null || collection.size() == 0;
	}

	public final static boolean isNull(Map map) {
		return map == null || map.size() == 0;
	}

	/**
	 * 匹配URL地址
	 * 
	 * @param str
	 */
	public final static boolean isUrl(String str) {
		return match(str, "^http://([\\w-]+\\.)+[\\w-]+(/[\\w-./?%&=]*)?$");
	}

	/**
	 * 匹配密码，以字母开头，长度在6-12之间，只能包含字符、数字和下划线。
	 * 
	 * @param str
	 * 
	 * 
	 */
	public final static boolean isPwd(String str) {
		return match(str, "^[a-zA-Z]\\w{6,12}$");
	}

	/**
	 * 验证字符，只能包含中文、英文、数字、下划线等字符。
	 * 
	 * @param str
	 * 
	 * 
	 */
	public final static boolean stringCheck(String str) {
		return match(str, "^[a-zA-Z0-9\u4e00-\u9fa5-_]+$");
	}

	/**
	 * 匹配Email地址
	 * 
	 * @param str
	 * 
	 * 
	 */
	public final static boolean isEmail(String str) {
		return match(str, "^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$");
	}

	/**
	 * 匹配非负整数（正整数+0）
	 * 
	 * @param str
	 * 
	 * 
	 */
	public final static boolean isInteger(String str) {
		return match(str, "^[+]?\\d+$");
	}

	/**
	 * 判断数值类型，包括整数和浮点数
	 * 
	 * @param str
	 * 
	 * 
	 */
	public final static boolean isNumeric(String str) {
		return isFloat(str) || isInteger(str);
	}

	/**
	 * 只能输入数字
	 * 
	 * @param str
	 * 
	 * 
	 */
	public final static boolean isDigits(String str) {
		return match(str, "^[0-9]*$");
	}

	/**
	 * 匹配正浮点数
	 * 
	 * @param str
	 * 
	 * 
	 */
	public final static boolean isFloat(String str) {
		return match(str, "^[-\\+]?\\d+(\\.\\d+)?$");
	}

	/**
	 * 联系电话(手机/电话皆可)验证
	 * 
	 * @param text
	 * 
	 * 
	 */
	public final static boolean isTel(String text) {
		return isMobile(text) || isPhone(text);
	}

	/**
	 * 电话号码验证
	 * 
	 * @param text
	 * 
	 * 
	 */
	public final static boolean isPhone(String text) {
		return match(text, "^(\\d{3,4}-?)?\\d{7,9}$");
	}

	/**
	 * 手机号码验证
	 * 
	 * @param text
	 * 
	 * 
	 */
	public final static boolean isMobile(String text) {
		return match(text, "^1[3-9]\\d{9}$");
	}

	/**
	 * 身份证号码验证
	 * 
	 * @param text
	 * 
	 * 
	 */
	public final static boolean isIdCardNo(String text) {
		return match(text, "^(\\d{6})()?(\\d{4})(\\d{2})(\\d{2})(\\d{3})(\\w)$");
	}

	/**
	 * 邮政编码验证
	 * 
	 * @param text
	 * 
	 * 
	 */
	public final static boolean isZipCode(String text) {
		return match(text, "^[0-9]{6}$");
	}

	/**
	 * 判断整数num是否等于0
	 * 
	 * @param num
	 * 
	 * 
	 */
	public final static boolean isIntEqZero(int num) {
		return num == 0;
	}

	/**
	 * 判断整数num是否大于0
	 * 
	 * @param num
	 * 
	 * 
	 */
	public final static boolean isIntGtZero(int num) {
		return num > 0;
	}

	/**
	 * 判断整数num是否大于或等于0
	 * 
	 * @param num
	 * 
	 * 
	 */
	public final static boolean isIntGteZero(int num) {
		return num >= 0;
	}

	/**
	 * 判断浮点数num是否等于0
	 * 
	 * @param num
	 *            浮点数
	 * 
	 * 
	 */
	public final static boolean isFloatEqZero(float num) {
		return num == 0f;
	}

	/**
	 * 判断浮点数num是否大于0
	 * 
	 * @param num
	 *            浮点数
	 * 
	 * 
	 */
	public final static boolean isFloatGtZero(float num) {
		return num > 0f;
	}

	/**
	 * 判断浮点数num是否大于或等于0
	 * 
	 * @param num
	 *            浮点数
	 * 
	 * 
	 */
	public final static boolean isFloatGteZero(float num) {
		return num >= 0f;
	}

	/**
	 * 判断是否为合法字符(a-zA-Z0-9-_)
	 * 
	 * @param text
	 * 
	 * 
	 */
	public final static boolean isRightfulString(String text) {
		return match(text, "^[A-Za-z0-9_-]+$");
	}

	/**
	 * 判断英文字符(a-zA-Z)
	 * 
	 * @param text
	 * 
	 * 
	 */
	public final static boolean isEnglish(String text) {
		return match(text, "^[A-Za-z]+$");
	}

	/**
	 * 判断中文字符(包括汉字和符号)
	 * 
	 * @param text
	 * 
	 * 
	 */
	public final static boolean isChineseChar(String text) {
		return match(text, "^[\u0391-\uFFE5]+$");
	}

	/**
	 * 匹配汉字
	 * 
	 * @param text
	 * 
	 * 
	 */
	public final static boolean isChinese(String text) {
		return match(text, "^[\u4e00-\u9fa5]+$");
	}

	/**
	 * 是否包含中英文特殊字符，除英文"-_"字符外
	 * 
	 * @param text
	 * 
	 */
	public static boolean isContainsSpecialChar(String text) {
		if (StringUtils.isBlank(text))
			return false;
		String[] chars = { "[", "`", "~", "!", "@", "#", "$", "%", "^", "&", "*", "(", ")", "+", "=", "|", "{", "}",
				"'", ":", ";", "'", ",", "[", "]", ".", "<", ">", "/", "?", "~", "！", "@", "#", "￥", "%", "…", "&", "*",
				"（", "）", "—", "+", "|", "{", "}", "【", "】", "‘", "；", "：", "”", "“", "’", "。", "，", "、", "？", "]" };
		for (String ch : chars) {
			if (text.contains(ch))
				return true;
		}
		return false;
	}

	/**
	 * 过滤中英文特殊字符，除英文"-_"字符外
	 * 
	 * @param text
	 * 
	 */
	public static String stringFilter(String text) {
		String regExpr = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
		Pattern p = Pattern.compile(regExpr);
		Matcher m = p.matcher(text);
		return m.replaceAll("").trim();
	}

	/**
	 * 过滤html代码
	 * 
	 * @param inputString
	 *            含html标签的字符串
	 * 
	 */
	public static String htmlFilter(String inputString) {
		String htmlStr = inputString; // 含html标签的字符串
		String textStr = "";
		Pattern p_script;
		Matcher m_script;
		Pattern p_style;
		Matcher m_style;
		Pattern p_html;
		Matcher m_html;
		Pattern p_ba;
		Matcher m_ba;

		try {
			String regEx_script = "<[\\s]*?script[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?script[\\s]*?>"; // 定义script的正则表达式{或<script[^>]*?>[\\s\\S]*?<\\/script>
			// }
			String regEx_style = "<[\\s]*?style[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?style[\\s]*?>"; // 定义style的正则表达式{或<style[^>]*?>[\\s\\S]*?<\\/style>
			// }
			String regEx_html = "<[^>]+>"; // 定义HTML标签的正则表达式
			String patternStr = "\\s+";

			p_script = Pattern.compile(regEx_script, Pattern.CASE_INSENSITIVE);
			m_script = p_script.matcher(htmlStr);
			htmlStr = m_script.replaceAll(""); // 过滤script标签

			p_style = Pattern.compile(regEx_style, Pattern.CASE_INSENSITIVE);
			m_style = p_style.matcher(htmlStr);
			htmlStr = m_style.replaceAll(""); // 过滤style标签

			p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
			m_html = p_html.matcher(htmlStr);
			htmlStr = m_html.replaceAll(""); // 过滤html标签

			p_ba = Pattern.compile(patternStr, Pattern.CASE_INSENSITIVE);
			m_ba = p_ba.matcher(htmlStr);
			htmlStr = m_ba.replaceAll(""); // 过滤空格

			textStr = htmlStr;

		} catch (Exception e) {
			System.err.println("Html2Text: " + e.getMessage());
		}
		return textStr;// 返回文本字符串
	}

	/**
	 * 正则表达式匹配
	 * 
	 * @param text
	 *            待匹配的文本
	 * @param reg
	 *            正则表达式
	 * 
	 * 
	 */
	private final static boolean match(String text, String reg) {
		if (StringUtils.isBlank(text) || StringUtils.isBlank(reg))
			return false;
		return Pattern.compile(reg).matcher(text).matches();
	}

	public static <T> Predicate<T> distinctByKey(Function<? super T,Object> keyExtractor) {
		Map<Object,Boolean> seen = new ConcurrentHashMap<>();
		return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
	}

	/**
	 * @param jsonObject
	 * @Description 简单json格式校验指定key非空
	 */
	public static boolean jsonWithNotEmptyKeys(JSONObject jsonObject, String[] notEmptyKeys) {
		if(jsonObject == null || jsonObject.size() == 0) {
			return false;
		}
		int length = notEmptyKeys.length;
		for (int i = 0; i < notEmptyKeys.length; i++) {
			String key = notEmptyKeys[i];
			if(jsonObject.containsKey(key) && null != jsonObject.get(key) && StringUtil.isNotEmpty(jsonObject.getString(key))) {
				length--;
			}
		}
		return length <= 0;
	}

	public static boolean jsonValidateWithKey(JSONObject jsonObject, String key) {
		boolean flag = false;
		if(jsonObject != null && jsonObject.size() > 0) {
			if(jsonObject.containsKey(key) && null != jsonObject.get(key) && StringUtil.isNotEmpty(jsonObject.getString(key))) {
				flag = true;
			}
		}
		return flag;
	}

	/**
	 * @Description 检查开始时间startTime和结束时间endTime
	 * @param json
	 * @return
	 */
	public static JSONObject checkStartEndTime(JSONObject json) {
		String currDay = DateUtil.getDateString(new Date(), "YYYY-MM-dd");
		if(!ValidateUtil.jsonWithNotEmptyKeys(json, new String[]{"startTime"})
				&& !ValidateUtil.jsonWithNotEmptyKeys(json, new String[]{"endTime"})) {
			json.accumulate("startTime", currDay);
			json.accumulate("endTime", currDay);
		} else if(ValidateUtil.jsonWithNotEmptyKeys(json, new String[]{"startTime"})
				&& !ValidateUtil.jsonWithNotEmptyKeys(json, new String[]{"endTime"})) {
			json.accumulate("endTime", currDay);
		} else if(!ValidateUtil.jsonWithNotEmptyKeys(json, new String[]{"startTime"})
				&& ValidateUtil.jsonWithNotEmptyKeys(json, new String[]{"endTime"})) {
			//如果设置了结束时间，那么开始时间非空, 此处进行参数错误提示
			throw new BusinessException(ResultMsg.START_TIME_IS_NULL);//请设置查询条件：开始时间
		}
		json.put("startTime", json.getString("startTime").replace("/", "-").concat(" 00:00:00"));
		json.put("endTime", json.getString("endTime").replace("/", "-").concat(" 23:59:59"));
		return json;
	}

    public static void createQueryTypeParams(JSONObject json) {
        if(ValidateUtil.jsonValidateWithKey(json, "queryType") && ValidateUtil.jsonValidateWithKey(json, "queryString"))
        {
            int queryType = json.getInt("queryType");
            if(queryType == QueryTypeEnum.BY_USERNAME.getCode()) {
                json.put("queryType", QueryTypeEnum.BY_USERNAME.getName());
            } else if(queryType == QueryTypeEnum.BY_STUDENT_NO.getCode()) {
                json.put("queryType", QueryTypeEnum.BY_STUDENT_NO.getName());
            } else if(queryType == QueryTypeEnum.BY_MOBILE.getCode()) {
                json.put("queryType", QueryTypeEnum.BY_MOBILE.getName());
            } else {
                json.remove("queryType");
            }
        }
    }

	public static void createSortParams(JSONObject json) {
		if(jsonValidateWithKey(json, "sortName") && jsonValidateWithKey(json, "sortType"))
		{
			String sortName = json.getString("sortName");
            if(sortName.equals(SortTypeEnum.studentNo.name())) {
                json.put("sortName", sortName);
            } else if(sortName.equals(SortTypeEnum.morningRunningCount.name())) {
				json.put("sortName", sortName);
			} else if(sortName.equals(SortTypeEnum.runningLength.name())) {
				json.put("sortName", sortName);
			} else if(sortName.equals(SortTypeEnum.score.name())) {
				json.put("sortName", sortName);
			} else if(sortName.equals(SortTypeEnum.mobile.name())) {
                json.put("sortName", sortName);
            } else if(sortName.equals(SortTypeEnum.classname.name())) {
                json.put("sortName", sortName);
            } else if(sortName.equals(SortTypeEnum.courseName.name())) {
                json.put("sortName", sortName);
            } else if(sortName.equals(SortTypeEnum.freeRunningLength.name())) {
                json.put("sortName", sortName);
            } else if(sortName.equals(SortTypeEnum.schoolActivityScore.name())) {
                json.put("sortName", sortName);
            } else {
				json.put("sortName", SortTypeEnum.studentNo.name());
			}

			String sortType = json.getString("sortType");
			if(sortType.equals(SortEnum.ASC.name())) {
				json.put("sortType", sortType);
			} else if(sortType.equals(SortEnum.DESC.name())) {
				json.put("sortType", sortType);
			} else {
				json.put("sortType", SortEnum.DESC.name());
			}
		} else {
			json.put("sortName", SortTypeEnum.studentNo.name());
			json.put("sortType", SortEnum.ASC.name());
		}
	}

	/**
	 * 字母数字组合
	 */
	public static boolean isDigitalOrLetter(String text) {
		return match(text, "^[A-Za-z0-9]+$");
	}

	/**
	 * 汉字字母数字组合
	 */
	public static boolean isChineseOrDigitalOrLetter(String text) {
		return match(text, "^[A-Za-z0-9\u4e00-\u9fa5]+$");
	}


	// 判断一个字符串是否含有数字
	public static boolean hasDigit(String content) {
		boolean flag = false;
		Pattern p = Pattern.compile(".*\\d+.*");
		Matcher m = p.matcher(content);
		if (m.matches()) {
			flag = true;
		}
		return flag;
	}

	/**学号检查 字母或数字
	 * @param str
	 * @return
	 */
	public final static boolean checkStudentNO(String str) {
		return match(str, "^[A-Za-z0-9]+$");
	}

	/**
	 * 姓名验证  可包含数字字母中文和"."号
	 * @param str
	 * @return
	 */
	public final static boolean checkUserName(String str) {
		return match(str, "^[A-Za-z0-9\u4e00-\u9fa5\\.]+$");
	}

	/**
	 * 班级验证  可包含数字中文（）和 - 符号
	 * @param str
	 * @return
	 */
	public final static boolean checkClassName(String str) {
		return match(str, "^[A-Za-z0-9\u4e00-\u9fa5\\(\\)\\-]+$");
	}
}
