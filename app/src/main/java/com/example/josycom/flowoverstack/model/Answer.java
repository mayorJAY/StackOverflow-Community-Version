package com.example.josycom.flowoverstack.model;

import com.google.gson.annotations.SerializedName;

public class Answer {

    @SerializedName("owner")
    private Owner owner;
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
    @SerializedName("body")
    private String body;

    public Owner getOwner() {
        return owner;
    }

    public void setOwner(Owner owner) {
        this.owner = owner;
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

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
