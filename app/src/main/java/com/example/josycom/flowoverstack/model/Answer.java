package com.example.josycom.flowoverstack.model;

import com.google.gson.annotations.SerializedName;

public class Answer {

    @SerializedName("owner")
    private Owner owner;
    @SerializedName("down_vote_count")
    private Integer downVoteCount;
    @SerializedName("up_vote_count")
    private Integer upVoteCount;
    @SerializedName("is_accepted")
    private Boolean isAccepted;
    @SerializedName("score")
    private Integer score;
    @SerializedName("last_activity_date")
    private Integer lastActivityDate;
    @SerializedName("last_edit_date")
    private Integer lastEditDate;
    @SerializedName("creation_date")
    private Integer creationDate;
    @SerializedName("answer_id")
    private Integer answerId;
    @SerializedName("question_id")
    private Integer questionId;
    @SerializedName("link")
    private String link;
    @SerializedName("title")
    private String title;
    @SerializedName("body")
    private String body;

    public Owner getOwner() {
        return owner;
    }

    public void setOwner(Owner owner) {
        this.owner = owner;
    }

    public Integer getDownVoteCount() {
        return downVoteCount;
    }

    public void setDownVoteCount(Integer downVoteCount) {
        this.downVoteCount = downVoteCount;
    }

    public Integer getUpVoteCount() {
        return upVoteCount;
    }

    public void setUpVoteCount(Integer upVoteCount) {
        this.upVoteCount = upVoteCount;
    }

    public Boolean getIsAccepted() {
        return isAccepted;
    }

    public void setIsAccepted(Boolean isAccepted) {
        this.isAccepted = isAccepted;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public Integer getLastActivityDate() {
        return lastActivityDate;
    }

    public void setLastActivityDate(Integer lastActivityDate) {
        this.lastActivityDate = lastActivityDate;
    }

    public Integer getLastEditDate() {
        return lastEditDate;
    }

    public void setLastEditDate(Integer lastEditDate) {
        this.lastEditDate = lastEditDate;
    }

    public Integer getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Integer creationDate) {
        this.creationDate = creationDate;
    }

    public Integer getAnswerId() {
        return answerId;
    }

    public void setAnswerId(Integer answerId) {
        this.answerId = answerId;
    }

    public Integer getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Integer questionId) {
        this.questionId = questionId;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
