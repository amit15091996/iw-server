package com.intallysh.widom.entity.blog;

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
public class BlogComments {
	@Id
	private String blogCommentId;
	@Column(nullable = false)
	private String blogId;
	@Column(nullable = false)
	private String blogComment;
	@Column(nullable = false)
	private String blogCommentatorId;
	@Column(nullable = false)
	private String commentedOn;
	private String modifiedOn;
	private String modifiedBy;

}
