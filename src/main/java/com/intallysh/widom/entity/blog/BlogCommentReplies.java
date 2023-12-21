package com.intallysh.widom.entity.blog;

import java.sql.Timestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class BlogCommentReplies {
	@Id
	private String blogCommentRepliesId;
	@Column(nullable = false)
	private String blogCommentId;
	@Column(nullable = false)
	private String replyComment;
	@Column(nullable = false)
	private long userId;
	@Column(nullable = false)
	private Timestamp commentedOn;
	
	private Timestamp modifiedOn;
	private String modifiedBy;

}
