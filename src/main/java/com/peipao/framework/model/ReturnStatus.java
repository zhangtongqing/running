package com.peipao.framework.model;

import com.peipao.framework.constant.ReturnConstant;

public enum ReturnStatus {

    SUCCESS(ReturnConstant.SUCCESS.value, ReturnConstant.SUCCESS.desc),
    INTERNAL_SERVER_ERROR(ReturnConstant.INTERNAL_SERVER_ERROR.value, ReturnConstant.INTERNAL_SERVER_ERROR.desc),
    NOT_FOUND(ReturnConstant.NOT_FOUND.value, ReturnConstant.NOT_FOUND.desc),


    FAULT_ACCESS_TOKEN(ReturnConstant.FAULT_ACCESS_TOKEN.value, ReturnConstant.FAULT_ACCESS_TOKEN.desc),
    NO_ACCESS_TOKEN(ReturnConstant.NO_ACCESS_TOKEN.value, ReturnConstant.NO_ACCESS_TOKEN.desc),
    EXPIRE_ACCESS_TOKEN(ReturnConstant.EXPIRE_ACCESS_TOKEN.value, ReturnConstant.EXPIRE_ACCESS_TOKEN.desc),
    KEY_IN_USED(ReturnConstant.KEY_IN_USED.value, ReturnConstant.KEY_IN_USED.desc),
    BUSINESS_ERROR(ReturnConstant.BUSINESS_ERROR.value, ReturnConstant.BUSINESS_ERROR.desc),

    // Common
    PARAMETER_EMPTY(ReturnConstant.PARAMETER_EMPTY.value, ReturnConstant.PARAMETER_EMPTY.desc),
    PARAMETER_INCORRECT(ReturnConstant.PARAMETER_INCORRECT.value, ReturnConstant.PARAMETER_INCORRECT.desc),
    DATA_TOO_LONG(ReturnConstant.DATA_TOO_LONG.value, ReturnConstant.DATA_TOO_LONG.desc),
    CONFIG_KEY_REPEAT(ReturnConstant.CONFIG_KEY_REPEAT.value, ReturnConstant.CONFIG_KEY_REPEAT.desc),
    CONFIG_DETAIL_REPEAT(ReturnConstant.CONFIG_DETAIL_REPEAT.value, ReturnConstant.CONFIG_DETAIL_REPEAT.desc),
    GLOBAL_CONFIG_FANGLE_ERROR(ReturnConstant.GLOBAL_CONFIG_FANGLE_ERROR.value, ReturnConstant.GLOBAL_CONFIG_FANGLE_ERROR.desc),
    GLOBAL_CONFIG_USER_BEHAVIOR_VALID_ERROR(ReturnConstant.GLOBAL_CONFIG_USER_BEHAVIOR_VALID_ERROR.value, ReturnConstant.GLOBAL_CONFIG_USER_BEHAVIOR_VALID_ERROR.desc),
    GLOBAL_CONFIG_USER_BEHAVIOR_INVALID_ERROR(ReturnConstant.GLOBAL_CONFIG_USER_BEHAVIOR_INVALID_ERROR.value, ReturnConstant.GLOBAL_CONFIG_USER_BEHAVIOR_INVALID_ERROR.desc),
    BUSINESS_DISTRICTID_NOT_EXIST(ReturnConstant.BUSINESS_DISTRICTID_NOT_EXIST.value, ReturnConstant.BUSINESS_DISTRICTID_NOT_EXIST.desc),
    PARAMETER_MISS(ReturnConstant.PARAMETER_MISS.value, ReturnConstant.PARAMETER_MISS.desc),
    DATA_NOT_EXIST(ReturnConstant.DATA_NOT_EXIST.value, ReturnConstant.DATA_NOT_EXIST.desc),
    PARAMETER_PATTERN_ERROR(ReturnConstant.PARAMETER_PATTERN_ERROR.value, ReturnConstant.PARAMETER_PATTERN_ERROR.desc),
    SIGN_ERROR(ReturnConstant.SIGN_ERROR.value, ReturnConstant.SIGN_ERROR.desc),


    // Token 10100
    NO_TOKEN(ReturnConstant.NO_TOKEN.value, ReturnConstant.NO_TOKEN.desc),
    FAULT_TOKEN(ReturnConstant.FAULT_TOKEN.value, ReturnConstant.FAULT_TOKEN.desc),
    EXPIRE_TOKEN(ReturnConstant.EXPIRE_TOKEN.value, ReturnConstant.EXPIRE_TOKEN.desc),

    // User : 10200
    USER_ALREADY_EXIST(ReturnConstant.USER_ALREADY_EXIST.value, ReturnConstant.USER_ALREADY_EXIST.desc),
    USER_NOT_EXIST(ReturnConstant.USER_NOT_EXIST.value, ReturnConstant.USER_NOT_EXIST.desc),
    TEACHER_NOT_EXIST(ReturnConstant.TEACHER_NOT_EXIST.value, ReturnConstant.TEACHER_NOT_EXIST.desc),
    USER_VERIFICATION_CODE_INCORRECT(ReturnConstant.USER_VERIFICATION_CODE_INCORRECT.value, ReturnConstant.USER_VERIFICATION_CODE_INCORRECT.desc),
    USER_VERIFICATION_CODE_EXPIRED(ReturnConstant.USER_VERIFICATION_CODE_EXPIRED.value, ReturnConstant.USER_VERIFICATION_CODE_EXPIRED.desc),
    USER_USERNAME_INCORRECT(ReturnConstant.USER_USERNAME_INCORRECT.value, ReturnConstant.USER_USERNAME_INCORRECT.desc),
    USER_PASSWORD_INCORRECT(ReturnConstant.USER_PASSWORD_INCORRECT.value, ReturnConstant.USER_PASSWORD_INCORRECT.desc),
    USER_OLD_PASSWORD_INCORRECT(ReturnConstant.USER_OLD_PASSWORD_INCORRECT.value, ReturnConstant.USER_OLD_PASSWORD_INCORRECT.desc),
    USER_TOO_MANY_INTEREST(ReturnConstant.USER_TOO_MANY_INTEREST.value, ReturnConstant.USER_TOO_MANY_INTEREST.desc),
    USER_SAME_PASSWORD(ReturnConstant.USER_SAME_PASSWORD.value, ReturnConstant.USER_SAME_PASSWORD.desc),
    USER_SAME_PHONE_PASSWORD(ReturnConstant.USER_SAME_PHONE_PASSWORD.value, ReturnConstant.USER_SAME_PHONE_PASSWORD.desc),
    USER_SAME_PHONE(ReturnConstant.USER_SAME_PHONE.value, ReturnConstant.USER_SAME_PHONE.desc),
    USER_NO_PHONE(ReturnConstant.USER_NO_PHONE.value, ReturnConstant.USER_NO_PHONE.desc),
    USER_NO_PASSWORD(ReturnConstant.USER_NO_PASSWORD.value, ReturnConstant.USER_NO_PASSWORD.desc),
    USER_FAULT_PHONE(ReturnConstant.USER_FAULT_PHONE.value, ReturnConstant.USER_FAULT_PHONE.desc),
    USER_COLLEGE_REQUIRED(ReturnConstant.USER_COLLEGE_REQUIRED.value, ReturnConstant.USER_COLLEGE_REQUIRED.desc),
    USER_FAULT(ReturnConstant.USER_FAULT.value, ReturnConstant.USER_FAULT.desc),
    USER_ALREADY_AUTH(ReturnConstant.USER_ALREADY_AUTH.value, ReturnConstant.USER_ALREADY_AUTH.desc),
    USER_LOCK(ReturnConstant.USER_LOCK.value, ReturnConstant.USER_LOCK.desc),
    USER_THIRD_PARTY_ALREADY_EXIST(ReturnConstant.USER_THIRD_PARTY_ALREADY_EXIST.value, ReturnConstant.USER_THIRD_PARTY_ALREADY_EXIST.desc),
    USER_THIRD_PARTY_NOT_EXIST(ReturnConstant.USER_THIRD_PARTY_NOT_EXIST.value, ReturnConstant.USER_THIRD_PARTY_NOT_EXIST.desc),
    USER_UNBIND_PERMISSION_REQUIRED(ReturnConstant.USER_UNBIND_PERMISSION_REQUIRED.value, ReturnConstant.USER_UNBIND_PERMISSION_REQUIRED.desc),
    USER_RELATED_PERMISSION_REQUIRED(ReturnConstant.USER_RELATED_PERMISSION_REQUIRED.value, ReturnConstant.USER_RELATED_PERMISSION_REQUIRED.desc),
    USER_UN_AUTH(ReturnConstant.USER_UN_AUTH.value, ReturnConstant.USER_UN_AUTH.desc),
    USER_ALREADY_AUTH_ROLE(ReturnConstant.USER_ALREADY_AUTH_ROLE.value, ReturnConstant.USER_ALREADY_AUTH_ROLE.desc),

    USER_INVITE_NOT_EXIST(ReturnConstant.USER_INVITE_NOT_EXIST.value, ReturnConstant.USER_INVITE_NOT_EXIST.desc),
    USER_INVITE_ALREADY_EXIST(ReturnConstant.USER_INVITE_ALREADY_EXIST.value, ReturnConstant.USER_INVITE_ALREADY_EXIST.desc),
    USER_DATE_INPUT(ReturnConstant.USER_DATE_INPUT.value, ReturnConstant.USER_DATE_INPUT.desc),
    USER_HEIGHT_INPUT(ReturnConstant.USER_HEIGHT_INPUT.value, ReturnConstant.USER_HEIGHT_INPUT.desc),
    USER_WAIT_ACTIVE(ReturnConstant.USER_WAIT_ACTIVE.value, ReturnConstant.USER_WAIT_ACTIVE.desc),
    USER_ACTIVE_NOT_PHONE(ReturnConstant.USER_ACTIVE_NOT_PHONE.value, ReturnConstant.USER_ACTIVE_NOT_PHONE.desc),
    USER_ACTIVE_FAULT_STATUS(ReturnConstant.USER_ACTIVE_FAULT_STATUS.value, ReturnConstant.USER_ACTIVE_FAULT_STATUS.desc),
    USER_LOOK_PREFERENCE_NOT_ALLOW(ReturnConstant.USER_LOOK_PREFERENCE_NOT_ALLOW.value, ReturnConstant.USER_LOOK_PREFERENCE_NOT_ALLOW.desc),
    USER_APPEAL_EXIST(ReturnConstant.USER_APPEAL_EXIST.value, ReturnConstant.USER_APPEAL_EXIST.desc),
    USER_PASSWORD_EMPTY(ReturnConstant.USER_PASSWORD_EMPTY.value, ReturnConstant.USER_PASSWORD_EMPTY.desc),
    LOGIN_SUCCESS(ReturnConstant.LOGIN_SUCCESS.value, ReturnConstant.LOGIN_SUCCESS.desc),
    NICKNAME_FAULT_FORMAT(ReturnConstant.NICKNAME_FAULT_FORMAT.value, ReturnConstant.NICKNAME_FAULT_FORMAT.desc),
    STUDENT_HAVE_LOAD(ReturnConstant.STUDENT_HAVE_LOAD.value, ReturnConstant.STUDENT_HAVE_LOAD.desc),
    TEACHER_HAVE_BUSINESS(ReturnConstant.TEACHER_HAVE_BUSINESS.value, ReturnConstant.TEACHER_HAVE_BUSINESS.desc),
    CAN_NOT_DELETE_SELF(ReturnConstant.CAN_NOT_DELETE_SELF.value, ReturnConstant.CAN_NOT_DELETE_SELF.desc),
    MANAGER_CAN_NOT_BE_DELETE(ReturnConstant.MANAGER_CAN_NOT_BE_DELETE.value, ReturnConstant.MANAGER_CAN_NOT_BE_DELETE.desc),
    STUDENTNO_EXIST(ReturnConstant.STUDENTNO_EXIST.value, ReturnConstant.STUDENTNO_EXIST.desc),
    STUDENTNO_HAVE_AUTH(ReturnConstant.STUDENTNO_HAVE_AUTH.value, ReturnConstant.STUDENTNO_HAVE_AUTH.desc),
    USER_NICKNAME_EXIST(ReturnConstant.USER_NICKNAME_EXIST.value, ReturnConstant.USER_NICKNAME_EXIST.desc),
    USER_NEED_AUTH(ReturnConstant.USER_NEED_AUTH.value, ReturnConstant.USER_NEED_AUTH.desc),
    USER_USERNAME_ERROR(ReturnConstant.USER_USERNAME_ERROR.value, ReturnConstant.USER_USERNAME_ERROR.desc),

    // Lock: 10300
    LOCK_NOT_EXIST(ReturnConstant.LOCK_NOT_EXIST.value, ReturnConstant.LOCK_NOT_EXIST.desc),
    LOCK_HAVE_BINDED(ReturnConstant.LOCK_HAVE_BINDED.value, ReturnConstant.LOCK_HAVE_BINDED.desc),
    LOCK_NOT_BELONG_USER(ReturnConstant.LOCK_NOT_BELONG_USER.value, ReturnConstant.LOCK_NOT_BELONG_USER.desc),
    LOCK_HAVE_NOT_BINDED(ReturnConstant.LOCK_HAVE_NOT_BINDED.value, ReturnConstant.LOCK_HAVE_NOT_BINDED.desc),
    LOCK_HAVE_AUTHORIZE(ReturnConstant.LOCK_HAVE_AUTHORIZE.value, ReturnConstant.LOCK_HAVE_AUTHORIZE.desc),

    // management:10400
    BUSINESS_NOT_EXIST(ReturnConstant.BUSINESS_NOT_EXIST.value, ReturnConstant.BUSINESS_NOT_EXIST.desc),
    BUILDING_NOT_EXIST(ReturnConstant.BUILDING_NOT_EXIST.value, ReturnConstant.BUILDING_NOT_EXIST.desc),
    BUILDING_NOT_BELONG_TO_BUSINESS(ReturnConstant.BUILDING_NOT_BELONG_TO_BUSINESS.value, ReturnConstant.BUILDING_NOT_BELONG_TO_BUSINESS.desc),
    UPDATE_BUSINESS_BIND_PARAMS_SAME(ReturnConstant.UPDATE_BUSINESS_BIND_PARAMS_SAME.value, ReturnConstant.UPDATE_BUSINESS_BIND_PARAMS_SAME.desc),
    ADMIN_CAN_NOT_DELETE(ReturnConstant.ADMIN_CAN_NOT_DELETE.value, ReturnConstant.ADMIN_CAN_NOT_DELETE.desc),
    BUILDING_NAME_EXIST(ReturnConstant.BUILDING_NAME_EXIST.value, ReturnConstant.BUILDING_NAME_EXIST.desc),
    NO_PERMISSION(ReturnConstant.NO_PERMISSION.value, ReturnConstant.NO_PERMISSION.desc),

    // school 10300
    SCHOOL_NOT_EXIST(ReturnConstant.SCHOOL_NOT_EXIST.value, ReturnConstant.SCHOOL_NOT_EXIST.desc),
    COURSE_NOT_EXIST(ReturnConstant.COURSE_NOT_EXIST.value, ReturnConstant.COURSE_NOT_EXIST.desc),
    ACTIVITY_PARTICIPATE_NOFIT(ReturnConstant.ACTIVITY_PARTICIPATE_NOFIT.value, ReturnConstant.ACTIVITY_PARTICIPATE_NOFIT.desc),
    ACTIVITY_ENROLL_AT_TIME(ReturnConstant.ACTIVITY_ENROLL_AT_TIME.value, ReturnConstant.ACTIVITY_ENROLL_AT_TIME.desc),
    ACTIVITY_NOT_ENROLL(ReturnConstant.ACTIVITY_NOT_ENROLL.value, ReturnConstant.ACTIVITY_NOT_ENROLL.desc),
    ACTIVITY_SIGN_AT_TIME(ReturnConstant.ACTIVITY_SIGN_AT_TIME.value, ReturnConstant.ACTIVITY_SIGN_AT_TIME.desc),
    ACTIVITY_HAVE_ENROLL(ReturnConstant.ACTIVITY_HAVE_ENROLL.value, ReturnConstant.ACTIVITY_HAVE_ENROLL.desc),
    ACTIVITY_SIGN_AT_ADDRESS(ReturnConstant.ACTIVITY_SIGN_AT_ADDRESS.value, ReturnConstant.ACTIVITY_SIGN_AT_ADDRESS.desc),
    ACTIVITY_SIGN_LIMIT(ReturnConstant.ACTIVITY_SIGN_LIMIT.value, ReturnConstant.ACTIVITY_SIGN_LIMIT.desc),
    ACTIVITY_CAN_NOT_DELETE(ReturnConstant.ACTIVITY_CAN_NOT_DELETE.value, ReturnConstant.ACTIVITY_CAN_NOT_DELETE.desc),
    ACTIVITY_ENROLL_LIMIT(ReturnConstant.ACTIVITY_ENROLL_LIMIT.value, ReturnConstant.ACTIVITY_ENROLL_LIMIT.desc),
    ACTIVITY_ABDEFDFDF(ReturnConstant.ACTIVITY_ABDEFDFDF.value, ReturnConstant.ACTIVITY_ABDEFDFDF.desc),
    ACTIVITY_ENROLL_SUCESS(ReturnConstant.ACTIVITY_ENROLL_SUCESS.value, ReturnConstant.ACTIVITY_ENROLL_SUCESS.desc),
    COURSE_PARTICIPATE_NOFIT(ReturnConstant.COURSE_PARTICIPATE_NOFIT.value, ReturnConstant.COURSE_PARTICIPATE_NOFIT.desc),
    COURSE_ENROLL_AT_TIME(ReturnConstant.COURSE_ENROLL_AT_TIME.value, ReturnConstant.COURSE_ENROLL_AT_TIME.desc),
    COURSE_HAVE_ENROLL(ReturnConstant.COURSE_HAVE_ENROLL.value, ReturnConstant.COURSE_HAVE_ENROLL.desc),
    COURSE_NOT_ENROLL(ReturnConstant.COURSE_NOT_ENROLL.value, ReturnConstant.COURSE_NOT_ENROLL.desc),
    COURSE_SIGN_AT_TIME(ReturnConstant.COURSE_SIGN_AT_TIME.value, ReturnConstant.COURSE_SIGN_AT_TIME.desc),
    COURSE_SIGN_AT_ADDRESS(ReturnConstant.COURSE_SIGN_AT_ADDRESS.value, ReturnConstant.COURSE_SIGN_AT_ADDRESS.desc),
    COURSE_SCHEDULE_NOT_EXIST(ReturnConstant.COURSE_SCHEDULE_NOT_EXIST.value, ReturnConstant.COURSE_SCHEDULE_NOT_EXIST.desc),
    COURSE_CHOOSE_COUNT_LIMIT(ReturnConstant.COURSE_CHOOSE_COUNT_LIMIT.value, ReturnConstant.COURSE_CHOOSE_COUNT_LIMIT.desc),
    COURSE_HAVE_CHOOSE(ReturnConstant.COURSE_HAVE_CHOOSE.value, ReturnConstant.COURSE_HAVE_CHOOSE.desc),
    COURSE_SCHEDULE_HAVE_MEMBE(ReturnConstant.COURSE_SCHEDULE_HAVE_MEMBE.value, ReturnConstant.COURSE_SCHEDULE_HAVE_MEMBE.desc),
    COURSE_HAVE_START(ReturnConstant.COURSE_HAVE_START.value, ReturnConstant.COURSE_HAVE_START.desc),
    COURSE_OVER(ReturnConstant.COURSE_OVER.value, ReturnConstant.COURSE_OVER.desc),
    COURSE_SERIAL_HAVE_EXIST(ReturnConstant.COURSE_SERIAL_HAVE_EXIST.value, ReturnConstant.COURSE_SERIAL_HAVE_EXIST.desc),
    COURSE_HAVE_SIGN(ReturnConstant.COURSE_HAVE_SIGN.value, ReturnConstant.COURSE_HAVE_SIGN.desc),
    COURSE_CAN_NOT_CANCLE_AFTER_SIGN(ReturnConstant.COURSE_CAN_NOT_CANCLE_AFTER_SIGN.value, ReturnConstant.COURSE_CAN_NOT_CANCLE_AFTER_SIGN.desc),
    COURSE_MAX_CAPACITY(ReturnConstant.COURSE_MAX_CAPACITY.value, ReturnConstant.COURSE_MAX_CAPACITY.desc),

    // running
    ACTIVITY_NOT_EXIST(ReturnConstant.ACTIVITY_NOT_EXIST.value, ReturnConstant.ACTIVITY_NOT_EXIST.desc),
    RUNNING_ERROR(ReturnConstant.RUNNING_ERROR.value, ReturnConstant.RUNNING_ERROR.desc),
    RUNNING_RECORD_NOT_EXIST(ReturnConstant.RUNNING_RECORD_NOT_EXIST.value, ReturnConstant.RUNNING_RECORD_NOT_EXIST.desc),
    RUNNING_RECORD_INCOMPLETE(ReturnConstant.RUNNING_RECORD_INCOMPLETE.value, ReturnConstant.RUNNING_RECORD_INCOMPLETE.desc),
    APPEAL_STATUS_ERROR(ReturnConstant.APPEAL_STATUS_ERROR.value, ReturnConstant.APPEAL_STATUS_ERROR.desc),
    NODE_USER_ERROR(ReturnConstant.NODE_USER_ERROR.value, ReturnConstant.NODE_USER_ERROR.desc),
    RUNNINGRECORD_STATUS_ERROR(ReturnConstant.RUNNINGRECORD_STATUS_ERROR.value, ReturnConstant.RUNNINGRECORD_STATUS_ERROR.desc),
    RUNNING_RECORD_EXSIST(ReturnConstant.RUNNING_RECORD_EXSIST.value, ReturnConstant.RUNNING_RECORD_EXSIST.desc),
    NODE_STATUS_ERROR(ReturnConstant.NODE_STATUS_ERROR.value, ReturnConstant.NODE_STATUS_ERROR.desc),
    RUNNING_IS_NOT_EFFECTIVE(ReturnConstant.RUNNING_IS_NOT_EFFECTIVE.value, ReturnConstant.RUNNING_IS_NOT_EFFECTIVE.desc),
    NODE_FILE_QUERY_ERROR(ReturnConstant.NODE_FILE_QUERY_ERROR.value, ReturnConstant.NODE_FILE_QUERY_ERROR.desc),
    NODE_QUERY_FAIL(ReturnConstant.NODE_QUERY_FAIL.value, ReturnConstant.NODE_QUERY_FAIL.desc),


    //UserSchool
    USERSCHOOL_PARAMETER_MISS(ReturnConstant.USERSCHOOL_PARAMETER_MISS.value, ReturnConstant.USERSCHOOL_PARAMETER_MISS.desc),

    //rule
    RUNNING_RULE_EMPTY(ReturnConstant.RUNNING_RULE_EMPTY.value, ReturnConstant.RUNNING_RULE_EMPTY.desc),
    SCHOOL_RAIL_EMPTY(ReturnConstant.SCHOOL_RAIL_EMPTY.value, ReturnConstant.SCHOOL_RAIL_EMPTY.desc),
    DATA_POWER_ERROR(ReturnConstant.DATA_POWER_ERROR.value, ReturnConstant.DATA_POWER_ERROR.desc),
    RANDOM_NODE_EMPTY(ReturnConstant.RANDOM_NODE_EMPTY.value, ReturnConstant.RANDOM_NODE_EMPTY.desc),
    RANDOM_NODE_COUNAT_EMPTY(ReturnConstant.RANDOM_NODE_COUNAT_EMPTY.value, ReturnConstant.RANDOM_NODE_COUNAT_EMPTY.desc),
    AMUSING_RUNNING_IMG_EMPTY(ReturnConstant.AMUSING_RUNNING_IMG_EMPTY.value, ReturnConstant.AMUSING_RUNNING_IMG_EMPTY.desc),
    RUNNING_LINE_EMPTY(ReturnConstant.RUNNING_LINE_EMPTY.value, ReturnConstant.RUNNING_LINE_EMPTY.desc),
    EXAM_ITEM_EMPTY(ReturnConstant.EXAM_ITEM_EMPTY.value, ReturnConstant.EXAM_ITEM_EMPTY.desc),
    RUNNING_RULE_EXIST(ReturnConstant.RUNNING_RULE_EXIST.value, ReturnConstant.RUNNING_RULE_EXIST.desc),
    RUNNING_TYPE_MISS(ReturnConstant.RUNNING_TYPE_MISS.value, ReturnConstant.RUNNING_TYPE_MISS.desc),
    RANDOM_NODE_COUNAT_LESS(ReturnConstant.RANDOM_NODE_COUNAT_LESS.value, ReturnConstant.RANDOM_NODE_COUNAT_LESS.desc),
    EXAMITEM_NOT_NONE(ReturnConstant.EXAMITEM_NOT_NONE.value, ReturnConstant.EXAMITEM_NOT_NONE.desc),
    EXAMITEM_PASSSCORE_ERROR(ReturnConstant.EXAMITEM_PASSSCORE_ERROR.value, ReturnConstant.EXAMITEM_PASSSCORE_ERROR.desc),
    MORNINGRUNNING_TIME_ERROR(ReturnConstant.MORNINGRUNNING_TIME_ERROR.value, ReturnConstant.MORNINGRUNNING_TIME_ERROR.desc),
    MORNINGRUNNING_DATE_ERROR(ReturnConstant.MORNINGRUNNING_DATE_ERROR.value, ReturnConstant.MORNINGRUNNING_DATE_ERROR.desc),
    MORNINGRUNNING_COUNT_ERROR(ReturnConstant.MORNINGRUNNING_COUNT_ERROR.value, ReturnConstant.MORNINGRUNNING_COUNT_ERROR.desc),
    RUNNING_START_TIME_ERROR(ReturnConstant.RUNNING_START_TIME_ERROR.value, ReturnConstant.RUNNING_START_TIME_ERROR.desc),
    RUNNING_START_TIME_EMPTY(ReturnConstant.RUNNING_START_TIME_EMPTY.value, ReturnConstant.RUNNING_START_TIME_EMPTY.desc),

    REGISTERED_STUDENT_NO_MODIFY(ReturnConstant.REGISTERED_STUDENT_NO_MODIFY.value, ReturnConstant.REGISTERED_STUDENT_NO_MODIFY.desc),
    RUNNING_TYPE_ERROR(ReturnConstant.RUNNING_TYPE_ERROR.value, ReturnConstant.RUNNING_TYPE_ERROR.desc),
    COMPENSATE_PARAMS_TOO_LONG(ReturnConstant.COMPENSATE_PARAMS_TOO_LONG.value, ReturnConstant.COMPENSATE_PARAMS_TOO_LONG.desc),
    COMPENSATE_NOT_EXIST(ReturnConstant.COMPENSATE_NOT_EXIST.value, ReturnConstant.COMPENSATE_NOT_EXIST.desc),




    UPLOAD_ERROR(ReturnConstant.UPLOAD_ERROR.value, ReturnConstant.UPLOAD_ERROR.desc),
    NULL_POINT_ERROR(ReturnConstant.NULL_POINT_ERROR.value, ReturnConstant.NULL_POINT_ERROR.desc),
    SQL_ERROR(ReturnConstant.SQL_ERROR.value, ReturnConstant.SQL_ERROR.desc),
    FILE_NOT_FOUND_ERROR(ReturnConstant.FILE_NOT_FOUND_ERROR.value, ReturnConstant.FILE_NOT_FOUND_ERROR.desc),
    FILE_TYPE_ERROR(ReturnConstant.FILE_TYPE_ERROR.value, ReturnConstant.FILE_TYPE_ERROR.desc),
    OTHER_ERROR(ReturnConstant.OTHER_ERROR.value, ReturnConstant.OTHER_ERROR.desc),
    APP_MAST_UPDATE(ReturnConstant.APP_MAST_UPDATE.value, ReturnConstant.APP_MAST_UPDATE.desc),

    USERNAME_FORMAT_ERROR(ReturnConstant.USERNAME_FORMAT_ERROR.value,ReturnConstant.USERNAME_FORMAT_ERROR.desc),
    CLASSNAME_FORMAT_ERROR(ReturnConstant.CLASSNAME_FORMAT_ERROR.value,ReturnConstant.CLASSNAME_FORMAT_ERROR.desc),
    EFFECTIVE_SIGN_COUNT_EMPTY(ReturnConstant.EFFECTIVE_SIGN_COUNT_EMPTY.value,ReturnConstant.EFFECTIVE_SIGN_COUNT_EMPTY.desc),
    EFFECTIVE_SIGN_COUNT_EXCEED(ReturnConstant.EFFECTIVE_SIGN_COUNT_EXCEED.value,ReturnConstant.EFFECTIVE_SIGN_COUNT_EXCEED.desc),
    MOBILE__NUMBER_NOT_MISMATCH(ReturnConstant.MOBILE__NUMBER_NOT_MISMATCH.value,ReturnConstant.MOBILE__NUMBER_NOT_MISMATCH.desc);

    private final int value;
    private String description;

    ReturnStatus(int value, String description) {
        this.value = value;
        this.description = description;
    }

    public int getValue() {
        return value;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
