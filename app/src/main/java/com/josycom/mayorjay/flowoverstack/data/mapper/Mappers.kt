package com.josycom.mayorjay.flowoverstack.data.mapper

import com.josycom.mayorjay.flowoverstack.data.model.Answer
import com.josycom.mayorjay.flowoverstack.data.model.Owner
import com.josycom.mayorjay.flowoverstack.data.model.Question
import com.josycom.mayorjay.flowoverstack.data.model.Tag
import com.josycom.mayorjay.flowoverstack.data.remote.model.AnswerRemote
import com.josycom.mayorjay.flowoverstack.data.remote.model.OwnerRemote
import com.josycom.mayorjay.flowoverstack.data.remote.model.QuestionRemote
import com.josycom.mayorjay.flowoverstack.data.remote.model.TagRemote

fun QuestionRemote.toQuestion(): Question {
    return Question(
        tags = this.tags ?: emptyList(),
        body = this.body ?: "",
        owner = this.owner?.toOwner() ?: Owner(),
        isAnswered = this.isAnswered ?: false,
        viewCount = this.viewCount ?: 0,
        acceptedAnswerId = this.acceptedAnswerId ?: 0,
        answerCount = this.answerCount ?: 0,
        score = this.score ?: 0,
        lastActivityDate = this.lastActivityDate ?: 0,
        creationDate = this.creationDate ?: 0,
        lastEditDate = this.lastEditDate ?: 0,
        questionId = this.questionId ?: 0,
        link = this.link ?: "",
        title = this.title ?: ""
    )
}

fun OwnerRemote.toOwner(): Owner {
    return Owner(
        reputation = this.reputation ?: 0,
        userId = this.userId ?: 0,
        userType = this.userType ?: "",
        acceptRate = this.acceptRate ?: 0,
        profileImage = this.profileImage ?: "",
        displayName = this.displayName ?: "",
        link = this.link ?: ""
    )
}

fun AnswerRemote.toAnswer(): Answer {
    return Answer(
        owner = this.owner?.toOwner() ?: Owner(),
        isAccepted = this.isAccepted ?: false,
        score = this.score ?: 0,
        lastActivityDate = this.lastActivityDate ?: 0,
        creationDate = this.creationDate ?: 0,
        lastEditDate = this.lastEditDate ?: 0,
        answerId = this.answerId ?: 0,
        questionId = this.questionId ?: 0,
        body = this.body ?: "",
    )
}

fun TagRemote.toTag(): Tag {
    return Tag(
        name = this.name ?: "",
        count = this.count ?: 0L,
        hasSynonyms = this.hasSynonyms ?: false,
        isModeratorOnly = this.isModeratorOnly ?: false,
        isRequired = this.isRequired ?: false
    )
}