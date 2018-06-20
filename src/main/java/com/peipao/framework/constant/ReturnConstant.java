package com.peipao.framework.constant;


public class ReturnConstant {

	public static class SUCCESS {
		public static final int value = 200;
		public static final String desc = "操作成功完成";
	}

	public static class NOT_FOUND {
		public static final int value = 404;
		public static final String desc = "请求的服务不存在";
	}

	public static class INTERNAL_SERVER_ERROR {
		public static final int value = 500;
		public static final String desc = "请求的服务产生异常";
	}

	public static class FAULT_ACCESS_TOKEN {
		public static final int value = 900;
		public static final String desc = "您没有权限访问该接口";
	}

	public static class NO_ACCESS_TOKEN {
		public static final int value = 901;
		public static final String desc = "缺少访问授权参数";
	}

	public static class EXPIRE_ACCESS_TOKEN {
		public static final int value = 902;
		public static final String desc = "您授权已到期";
	}

	public static class KEY_IN_USED {
		public static final int value = 903;
		public static final String desc = "该字典项已经被使用，不允许删除";
	}
	public static class SIGN_ERROR {
		public static final int value = 904;
		public static final String desc = "签名错误";
	}

	public static class BUSINESS_ERROR {
		public static final int value = 10001;
		public static final String desc = "业务逻辑异常";
	}

	public static class RONGYUN_ERROR {
		public static final int value = 10002;
	}

	// Other
	public static class UPLOAD_ERROR {
		public static final int value = 1002;
		public static final String desc = "文件上传异常";
	}

	public static class NULL_POINT_ERROR {
		public static final int value = 2001;
		public static final String desc = "空指针异常";
	}

	public static class SQL_ERROR {
		public static final int value = 2002;
		public static final String desc = "SQL执行异常";
	}

	public static class FILE_NOT_FOUND_ERROR {
		public static final int value = 2003;
		public static final String desc = "文件不存在";
	}

	public static class FILE_TYPE_ERROR {
		public static final int value = 2004;
		public static final String desc = "文件类型错误";
	}

	public static class OTHER_ERROR {
		public static final int value = 9001;
		public static final String desc = "其他异常";
	}

	// common : 10000
	public static class PARAMETER_EMPTY {
		public static final int value = 10003;
		public static final String desc = "参数不能为空";
	}

	public static class PARAMETER_INCORRECT {
		public static final int value = 10004;
		public static final String desc = "参数错误";
	}
	public static class DATA_TOO_LONG {
		public static final int value = 10100;
		public static final String desc = "字符长度超过系统限制";
	}
	
	public static class CONFIG_KEY_REPEAT {
		public static final int value = 10005;
		public static final String desc = "字典项键重复";
	}
	
	public static class CONFIG_DETAIL_REPEAT {
		public static final int value = 10006;
		public static final String desc = "字典项值重复";
	}

	public static class PARAMETER_MISS {
		public static final int value = 10007;
		public static final String desc = "缺少参数";
	}

	public static class DATA_NOT_EXIST{
		public static final int value = 10008;
		public static final String desc = "需要修改的数据不存在";
	}
	public static class PARAMETER_PATTERN_ERROR{
		public static final int value = 10009;
		public static final String desc = "参数格式或类型长度不匹配";
	}
	
	public static class BUSINESS_DISTRICTID_NOT_EXIST {
		public static final int value = 1000401;
		public static final String desc = "districtId不能为空";
	}
	public static class GLOBAL_CONFIG_FANGLE_ERROR {
		public static final int value = 1000402;
		public static final String desc = "校园新鲜事有效编辑时间设置过大，长度最大为9位";
	}
	public static class GLOBAL_CONFIG_USER_BEHAVIOR_VALID_ERROR {
		public static final int value = 1000403;
		public static final String desc = "用户有效操作记录时间不能大于60分钟";
	}
	public static class GLOBAL_CONFIG_USER_BEHAVIOR_INVALID_ERROR {
		public static final int value = 1000404;
		public static final String desc = "用户无效操作记录时间不能小于24小时";
	}
	// Token : 10100
	public static class NO_TOKEN {
		public static final int value = 10101;
		public static final String desc = "缺少参数[token]";
	}

	public static class FAULT_TOKEN {
		public static final int value = 10102;
		public static final String desc = "错误的token";
	}

	public static class EXPIRE_TOKEN {
		public static final int value = 10103;
		public static final String desc = "token已过期，请重新登录";
	}

	// User 10200
	public static class USER_ALREADY_EXIST {
		public static final int value = 10201;
		public static final String desc = "该手机号已注册";
	}

	public static class USER_NOT_EXIST {
		public static final int value = 10202;
		public static final String desc = "该用户不存在";
	}

	public static class USER_VERIFICATION_CODE_INCORRECT {
		public static final int value = 10204;
		public static final String desc = "验证码错误";
	}

	public static class USER_VERIFICATION_CODE_EXPIRED {
		public static final int value = 10205;
		public static final String desc = "验证码过期";
	}

	public static class USER_USERNAME_INCORRECT {
		public static final int value = 10206;
		public static final String desc = "用户名不存在";
	}

	public static class USER_PASSWORD_INCORRECT {
		public static final int value = 10207;
		public static final String desc = "账户或密码错误";
	}
	public static class USER_OLD_PASSWORD_INCORRECT {
		public static final int value = 10207;
		public static final String desc = "原密码错误";
	}

	public static class USER_TOO_MANY_INTEREST {
		public static final int value = 10208;
		public static final String desc = "兴趣爱好超出限制";
	}

	public static class USER_SAME_PASSWORD {
		public static final int value = 10209;
		public static final String desc = "不能与旧密码相同";
	}

	public static class USER_SAME_PHONE_PASSWORD {
		public static final int value = 10210;
		public static final String desc = "密码不能与电话号码相同";
	}

	public static class USER_NO_PHONE {
		public static final int value = 10211;
		public static final String desc = "缺少参数[phone]";
	}

	public static class USER_NO_PASSWORD {
		public static final int value = 10212;
		public static final String desc = "缺少参数[password]";
	}

	public static class USER_FAULT_PHONE {
		public static final int value = 10213;
		public static final String desc = "错误的电话格式";
	}

	public static class USER_COLLEGE_REQUIRED {
		public static final int value = 10214;
		public static final String desc = "请完善用户学校信息";
	}

	public static class USER_FAULT {
		public static final int value = 10215;
		public static final String desc = "错误的用户信息";
	}

	public static class USER_ALREADY_AUTH_ROLE {
		public static final int value = 10216;
		public static final String desc = "该用户已认证该学校该角色，不能再次认证";
	}

	public static class USER_NEED_AUTH {
		public static final int value = 10217;
//		public static final String desc = "学校用户需要认证";
		public static final String desc = "学号不存在，请检查输入的学号或联系学校管理员";
	}

	// add by dong jingzhi
	public static class USER_ALREADY_AUTH {
		public static final int value = 10216;
		public static final String desc = "已经是认证用户";
	}

	public static class USER_LOCK {
		public static final int value = 10217;
		public static final String desc = "密码连续输错5次 用户已经锁住";
	}

	public static class USER_THIRD_PARTY_ALREADY_EXIST {
		public static final int value = 10218;
		public static final String desc = "第三方用户已存在";
	}

	public static class USER_THIRD_PARTY_NOT_EXIST {
		public static final int value = 10219;
		public static final String desc = "第三方用户不存在";
	}

	public static class USER_UNBIND_PERMISSION_REQUIRED {
		public static final int value = 10220;
		public static final String desc = "解除绑定权限不够";
	}

	public static class USER_RELATED_PERMISSION_REQUIRED {
		public static final int value = 10221;
		public static final String desc = "与我相关权限不够";
	}

	public static class USER_UN_AUTH {
		public static final int value = 10222;
		public static final String desc = "用户未认证";
	}

	public static class USER_INVITE_NOT_EXIST {
		public static final int value = 10223;
		public static final String desc = "发送邀请的用户不存在";
	}

	public static class USER_INVITE_ALREADY_EXIST {
		public static final int value = 10224;
		public static final String desc = "您邀请的好友已注册为泊策用户";
	}

	public static class USER_DATE_INPUT {
		public static final int value = 10225;
		public static final String desc = "时间输入有误";
	}

	public static class USER_HEIGHT_INPUT {
		public static final int value = 10226;
		public static final String desc = "身高输入有误";
	}

	public static class USER_WAIT_ACTIVE {
		public static final int value = 10227;
		public static final String desc = "此用户为待激活状态";
	}

	public static class USER_ACTIVE_NOT_PHONE {
		public static final int value = 10228;
		public static final String desc = "不存在该激活电话";
	}

	public static class USER_ACTIVE_FAULT_STATUS {
		public static final int value = 10229;
		public static final String desc = "该手机已经不是待激活状态";
	}

	public static class USER_LOOK_PREFERENCE_NOT_ALLOW {
		public static final int value = 10231;
		public static final String desc = "该用户不是此偏好创建者";
	}

	public static class USER_APPEAL_EXIST {
		public static final int value = 10232;
		public static final String desc = "该用户已经进行过申诉";
	}

    public static class USER_SAME_PHONE {
        public static final int value = 10233;
        public static final String desc = "不能同原手机号相同";
    }

    public static class USER_PASSWORD_EMPTY{
        public static final int value = 10234;
        public static final String desc = "密码不能为空";
    }

    public static class LOGIN_SUCCESS{
        public static final int value = 10235;
        public static final String desc = "登录成功";
    }

    public static class NICKNAME_FAULT_FORMAT{
        public static final int value = 10236;
        public static final String desc = "用户昵称必须字母和数字";
    }

    public static class STUDENT_HAVE_LOAD{
        public static final int value = 10237;
        public static final String desc = "学生用户已经导入";
    }
    public static class LOGIN_TWICE{
        public static final int value = 10238;
        public static final String desc = "异地登录";
    }

	public static class TEACHER_HAVE_BUSINESS{
		public static final int value = 10239;
		public static final String desc = "老师已经有课程或者活动，不能删除";
	}

	public static class CAN_NOT_DELETE_SELF{
		public static final int value = 10240;
		public static final String desc = "不能删除自己";
	}

	public static class MANAGER_CAN_NOT_BE_DELETE{
		public static final int value = 10241;
		public static final String desc = "管理员不能删除";
	}

	public static class STUDENTNO_EXIST {
		public static final int value = 10242;
		public static final String desc = "学号已经注册";
	}

	public static class STUDENTNO_HAVE_AUTH {
		public static final int value = 10243;
		public static final String desc = "学号已经认证";
	}

	public static class USER_NICKNAME_EXIST{
		public static final int value = 10244;
		public static final String desc = "账号名称已经存在";
	}

	public static class USER_USERNAME_ERROR{
		public static final int value = 10245;
		public static final String desc = "姓名与学号不匹配";
	}
	// Role: 12100
	public static class ROLE_NOT_EXIST {
		public static final int value = 12101;
		public static final String desc = "角色不存在";
	}

	public static class ROLE_USER_NOT_EXIST {
		public static final int value = 12102;
		public static final String desc = "角色用户关系不存在";
	}

	public static class ROLE_PERMISSION_REQUIRED {
		public static final int value = 12103;
		public static final String desc = "权限不够";
	}

	public static class ROLE_AUDIT_PERMISSION_REQUIRED {
		public static final int value = 12104;
		public static final String desc = "需要审核权限";
	}

	public static class ROLE_DELETE_PERMISSION_REQUIRED {
		public static final int value = 12105;
		public static final String desc = "需要删除权限";
	}

	public static class ROLE_ADMIN_PERMISSION_REQUIRED {
		public static final int value = 12106;
		public static final String desc = "需要管理员权限";
	}

	public static class ROLE_NAME_REPEAT {
		public static final int value = 12107;
		public static final String desc = "已存在该名称的角色";
	}

	// Lock: 10300
	public static class LOCK_NOT_EXIST {
		public static final int value = 10300;
		public static final String desc = "该地锁不存在";
	}

    public static class LOCK_HAVE_BINDED {
        public static final int value = 10301;
        public static final String desc = "该锁已经被绑定";
    }

    public static class LOCK_NOT_BELONG_USER{
        public static final int value = 10302;
        public static final String desc = "车锁不属于此用户";
    }
    public static class LOCK_HAVE_NOT_BINDED{
        public static final int value = 10303;
        public static final String desc = "该锁未被绑定";
    }


	public static class LOCK_HAVE_AUTHORIZE {
		public static final int value = 10304;
		public static final String desc = "已经授权给此用户";
	}

    //  management :10400
    public static class BUSINESS_NOT_EXIST{
        public static final int value = 10400;
        public static final String desc = "物业不存在";
    }
    public static class BUILDING_NOT_EXIST{
        public static final int value = 10401;
        public static final String desc = "楼宇ID不存在";
    }

    public static class BUILDING_NOT_BELONG_TO_BUSINESS{
        public static final int value = 10402;
        public static final String desc = "楼宇不属于此物业";
    }

    public static class UPDATE_BUSINESS_BIND_PARAMS_SAME{
        public static final int value = 10403;
        public static final String desc = "物业端锁绑定更新，跟现有数据相同错误";
    }

    public static class ADMIN_CAN_NOT_DELETE{
        public static final int value = 10404;
        public static final String desc = "无权删除超级管理员";
    }

    public static class BUILDING_NAME_EXIST{
        public static final int value = 10406;
        public static final String desc = "区位名称已经存在";
    }

    public static class NO_PERMISSION{
        public static final int value = 10407;
        public static final String desc = "无权进行此操作";
    }

    // school 10300
	public static class SCHOOL_NOT_EXIST {
		public static final int value = 10300;
		public static final String desc = "学校不存在";
	}

	// activity 10400
	public static class ACTIVITY_NOT_EXIST {
		public static final int value = 10400;
		public static final String desc = "活动不存在";
	}

	public static class ACTIVITY_PARTICIPATE_NOFIT {
		public static final int value = 10401;
		public static final String desc = "不满足参与条件";
	}

	public static class ACTIVITY_ENROLL_AT_TIME {
		public static final int value = 10402;
		public static final String desc = "请在报名时间内报名";
	}

	public static class ACTIVITY_NOT_ENROLL {
		public static final int value = 10403;
		public static final String desc = "活动没有报名";
	}

	public static class ACTIVITY_SIGN_AT_TIME {
		public static final int value = 10404;
		public static final String desc = "请在签到时间内签到";
	}

	public static class ACTIVITY_SIGN_AT_ADDRESS {
		public static final int value = 10405;
		public static final String desc = "请在签到地址签到";
	}

	public static class ACTIVITY_HAVE_ENROLL {
		public static final int value = 10406;
		public static final String desc = "活动已经报名";
	}

	public static class ACTIVITY_SIGN_LIMIT {
		public static final int value = 10407;
		public static final String desc = "已经超过活动参与次数";
	}

	public static class ACTIVITY_CAN_NOT_DELETE {
		public static final int value = 10408;
		public static final String desc = "活动已经有报名，不能删除";
	}

	public static class ACTIVITY_ENROLL_LIMIT {
		public static final int value = 10409;
		public static final String desc = "活动报名已达上线";
	}

	public static class ACTIVITY_ABDEFDFDF {
		public static final int value = 10410;
		public static final String desc = "对不起，该活动只对指定用户开放报名";
	}

	public static class ACTIVITY_ENROLL_SUCESS {
		public static final int value = 10411;
		public static final String desc = "报名成功";
	}

	// course 10500
	public static class COURSE_NOT_EXIST {
		public static final int value = 10500;
		public static final String desc = "课程不存在";
	}

	public static class COURSE_PARTICIPATE_NOFIT {
		public static final int value = 10501;
		public static final String desc = "不满足参与条件";
	}

	public static class COURSE_ENROLL_AT_TIME {
		public static final int value = 10502;
		public static final String desc = "请在报名时间内报名";
	}

	public static class COURSE_NOT_ENROLL {
		public static final int value = 10503;
		public static final String desc = "课程没有报名";
	}

	public static class COURSE_SIGN_AT_TIME {
		public static final int value = 10504;
		public static final String desc = "请在签到时间内签到";
	}

	public static class COURSE_SIGN_AT_ADDRESS {
		public static final int value = 10505;
		public static final String desc = "请在签到地址签到";
	}

	public static class COURSE_SCHEDULE_NOT_EXIST {
		public static final int value = 10506;
		public static final String desc = "课程表不存在";
	}

	public static class COURSE_HAVE_ENROLL {
		public static final int value = 10508;
		public static final String desc = "课程已经报名";
	}

	public static class COURSE_CHOOSE_COUNT_LIMIT {
		public static final int value = 10509;
		public static final String desc = "请联系老师，在青动力后台变更课程";
	}

	public static class COURSE_HAVE_CHOOSE {
		public static final int value = 10510;
		public static final String desc = "所选课程与上次相同";
	}

	public static class COURSE_SCHEDULE_HAVE_MEMBE {
		public static final int value = 10511;
		public static final String desc = "课程已经有人报名，不能删除";
	}

	public static class COURSE_HAVE_START {
		public static final int value = 10512;
		public static final String desc = "课程已经开始，不能取消";
	}

	public static class COURSE_OVER {
		public static final int value = 10513;
		public static final String desc = "课程已经结束";
	}

	public static class COURSE_SERIAL_HAVE_EXIST {
		public static final int value = 10514;
		public static final String desc = "课程编码已经存在";
	}

	public static class COURSE_HAVE_SIGN {
		public static final int value = 10515;
		public static final String desc = "已经签到";
	}

	public static class COURSE_CAN_NOT_CANCLE_AFTER_SIGN {
		public static final int value = 10517;
		public static final String desc = "已经签到,不能取消报名";
	}

	public static class COURSE_MAX_CAPACITY {
		public static final int value = 10518;
		public static final String desc = "课程报名人数已达上限，请选择其他课程";
	}

	// running 10500
	public static class RUNNING_RECORD_NOT_EXIST {
		public static final int value = 10500;
		public static final String desc = "跑步记录不存在";
	}

	public static class RUNNING_ERROR {
		public static final int value = 10501;
		public static final String desc = "跑步错误";
	}

	public static class RUNNING_RECORD_INCOMPLETE {
		public static final int value = 10502;
		public static final String desc = "跑步记录信息不正确";
	}

	//UserSchool  根据userId获得学校、学期、课程等 10600
    public static class USERSCHOOL_PARAMETER_MISS {
        public static final int value = 10601;
        public static final String desc = "本学期尚未选课";
    }

    //rule  规则 10700
    public static class RUNNING_RULE_EMPTY {
        public static final int value = 10701;
        public static final String desc = "没有查询到跑步规则";
    }
	public static class SCHOOL_RAIL_EMPTY {
		public static final int value = 10702;
		public static final String desc = "校园围栏已启用，围栏节点不能为空";
	}
	public static class RANDOM_NODE_EMPTY {
		public static final int value = 10703;
		public static final String desc = "随机跑随机节点为空";
	}
	public static class RANDOM_NODE_COUNAT_EMPTY {
		public static final int value = 10704;
		public static final String desc = "随机跑必须设置经过随机点";
	}
	public static class  AMUSING_RUNNING_IMG_EMPTY {
		public static final int value = 10705;
		public static final String desc = "趣味跑路径图片为空";
	}
	public static class  RUNNING_LINE_EMPTY {
		public static final int value = 10706;
		public static final String desc = "跑步路线为空";
	}
	public static class  EXAM_ITEM_EMPTY {
		public static final int value = 10707;
		public static final String desc = "本学期考核目标为空";
	}
	public static class  RUNNING_RULE_EXIST {
		public static final int value = 10708;
		public static final String desc = "当前学校学期下规则已存在，不能新增";
	}
	public static class  RUNNING_TYPE_MISS {
		public static final int value = 10709;
		public static final String desc = "缺少跑步类型";
	}
	public static class  RANDOM_NODE_COUNAT_LESS {
		public static final int value = 10710;
		public static final String desc = "随机跑随机点不能小于必须经过的点数量";
	}
	public static class  EXAMITEM_NOT_NONE {
		public static final int value = 10711;
		public static final String desc = "考核项不能为空,至少选一项";
	}
	public static class  EXAMITEM_PASSSCORE_ERROR {
		public static final int value = 10712;
		public static final String desc = "考核项分数及格线必须大于1";
	}
	public static class  RUNNINGRECORD_STATUS_ERROR {
		public static final int value = 10713;
		public static final String desc = "当前跑步记录不符合设为有效状态的要求";
	}
	public static class  MORNINGRUNNING_TIME_ERROR {
		public static final int value = 10715;
		public static final String desc = "当前不在该类型运动的有效时间内";
	}
    public static class  MORNINGRUNNING_COUNT_ERROR {
        public static final int value = 10716;
        public static final String desc = "今日该类型运动已达次数上限，不能再完成";
    }
	public static class  RUNNING_RECORD_EXSIST {
        public static final int value = 10717;
        public static final String desc = "该跑步记录不存在";
    }
    public static class  NODE_STATUS_ERROR {
        public static final int value = 10718;
        public static final String desc = "跑步记录状态错误，请检查";
    }
	public static class  NODE_FILE_QUERY_ERROR {
		public static final int value = 9001;
		public static final String desc = "节点文件查询失败";
	}
	public static class  NODE_QUERY_FAIL {
		public static final int value = 10720;
		public static final String desc = "运动轨迹查询失败";
	}
	public static class  NODE_USER_ERROR {
		public static final int value = 10721;
		public static final String desc = "用户与运动记录不匹配";
	}
	public static class  RUNNING_TYPE_ERROR {
		public static final int value = 10722;
		public static final String desc = "跑步类型错误";
	}
	public static class  TEACHER_NOT_EXIST {
		public static final int value = 10723;
		public static final String desc = "老师帐号不存在";
	}
    public static class  COMPENSATE_NOT_EXIST {
        public static final int value = 10724;
        public static final String desc = "成绩补偿信息不存在";
    }
	public static class DATA_POWER_ERROR {
		public static final int value = 10725;
		public static final String desc = "数据权限错误";
	}
    public static class UNKNOW_ERROR {
        public static final int value = 10726;
        public static final String desc = "数据权限错误";
    }
	public static class APPEAL_STATUS_ERROR {
		public static final int value = 10727;
		public static final String desc = "不符合申诉条件";
	}
	public static class COMPENSATE_PARAMS_TOO_LONG {
		public static final int value = 10728;
		public static final String desc = "补偿成绩参数超过限制";
	}
	public static class RUNNING_START_TIME_ERROR {
		public static final int value = 10729;
		public static final String desc = "运动开始时间异常";
	}
    public static class RUNNING_START_TIME_EMPTY {
        public static final int value = 10730;
        public static final String desc = "运动开始时间异常";
    }
	public static class RUNNING_IS_NOT_EFFECTIVE {
		public static final int value = 10731;
		public static final String desc = "运动记录没有达标";
	}
	public static class  MORNINGRUNNING_DATE_ERROR {
		public static final int value = 10732;
		public static final String desc = "晨跑只能在8:00~13:00开始";
	}
	/*
	 * school user
	 */
	public static class REGISTERED_STUDENT_NO_MODIFY {
		public static final int value = 10801;
		public static final String desc = "账号已经认证，不能修改学号";
	}

	public static class USERNAME_FORMAT_ERROR{
		public static final int value = 10802;
		public static final String desc = "格式错误：姓名可包含 数字、字母、中文和“.”";
	}

	public static class CLASSNAME_FORMAT_ERROR{
		public static final int value = 10803;
		public static final String desc = "格式错误：班级名可包含数字、字母、中文和符号 () . ";
	}

	public static class EFFECTIVE_SIGN_COUNT_EMPTY{
		public static final int value = 10804;
		public static final String desc = "活动可完成次数错误";
	}

	public static class EFFECTIVE_SIGN_COUNT_EXCEED{
		public static final int value = 10805;
		public static final String desc = "活动可完成次数为最大位数4位的正整数";
	}

	public static class MOBILE__NUMBER_NOT_MISMATCH{
		public static final int value = 10806;
		public static final String desc = "请输入正确的原手机号";
	}
	public static class  APP_MAST_UPDATE {
		public static final int value = 20001;
		public static final String desc = "请更新到最新版";
	}

}
