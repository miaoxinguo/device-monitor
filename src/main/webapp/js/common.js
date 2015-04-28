/**
 * 去掉字符串前后的空格
 */
function trimAll(str) {
	return str.replace(/(^s*)|(s*$)/g, '');
};

/**
 * 去掉字符串前的空格
 */
function trimLeft(str) {
	return str.replace(/^s*/g, '');
};

/**
 * 去掉字符串后的空格
 */
function trimRight(str) {
	return str.replace(/s*$/, '');
}

/**
 * 判断字符串是否为空
 */
function isEmpty(str) {
	if (str != null && str.length > 0) {
		return true;
	}
	return false;
}

/**
 * 判断两个字符串子否相同
 */
function isEquals(str1, str2) {
	if (str1 == str2) {
		return true;
	}
	return false;
}

/**
 * 忽略大小写判断字符串是否相同
 */
function isEqualsIgnoreCase(str1, str2) {
	if (str1.toUpperCase() == str2.toUpperCase()) {
		return true;
	}
	return false;
}

/**
 * 判断是否是数字
 */
function isNum(value) {
	var reg = /^[0-9]*$/;
	return reg.test(value);
}

/**
 * 判断是否是系统允许输入的合法字符
 */
function isNormalChar(str) {
	var reg = /^[A-Za-z0-9_]+$/;
	return reg.test(str);
}
