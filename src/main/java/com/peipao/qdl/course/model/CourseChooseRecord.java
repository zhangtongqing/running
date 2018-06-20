package com.peipao.qdl.course.model;


import java.io.Serializable;
import java.util.Date;

/**
 * @author meteor.wu
 * @since 2017/7/14
 **/
public class CourseChooseRecord  implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long courseChooseRecordId;
    private Long semesterId;
    private Long userId;
    private Long oldCourseId;
    private Long newCourseId;
    private Date createTime;

    public CourseChooseRecord(Long userId, Long semesterId, Long oldCourseId,  Long newCourseId){
        this.userId = userId;
        this.semesterId = semesterId;
        this.oldCourseId = oldCourseId;
        this.newCourseId = newCourseId;
    }

    public Long getCourseChooseRecordId() {
        return courseChooseRecordId;
    }

    public void setCourseChooseRecordId(Long courseChooseRecordId) {
        this.courseChooseRecordId = courseChooseRecordId;
    }

    public Long getOldCourseId() {
        return oldCourseId;
    }

    public void setOldCourseId(Long oldCourseId) {
        this.oldCourseId = oldCourseId;
    }

    public Long getNewCourseId() {
        return newCourseId;
    }

    public void setNewCourseId(Long newCourseId) {
        this.newCourseId = newCourseId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
