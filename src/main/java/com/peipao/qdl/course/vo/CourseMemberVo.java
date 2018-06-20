package com.peipao.qdl.course.vo;


import com.peipao.qdl.course.model.CourseMember;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;

/**
 * @author meteor.wu
 * @since 2017/7/4
 **/
@ApiModel("CourseMemberVo课程学员查询")
public class CourseMemberVo {
    @ApiModelProperty("报名总数")
    private Integer enrollCount;

    @ApiModelProperty("签到总数")
    private Integer signCount;

    @ApiModelProperty("学生成员List")
    private List<CourseMember> memberList;

    public Integer getEnrollCount() {
        return enrollCount;
    }

    public void setEnrollCount(Integer enrollCount) {
        this.enrollCount = enrollCount;
    }

    public Integer getSignCount() {
        return signCount;
    }

    public void setSignCount(Integer signCount) {
        this.signCount = signCount;
    }

    public List<CourseMember> getMemberList() {
        return memberList;
    }

    public void setMemberList(List<CourseMember> memberList) {
        this.memberList = memberList;
    }
}
