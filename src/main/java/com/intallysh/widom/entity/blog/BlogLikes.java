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
public class BlogLikes {
	@Id
	private String likeId;
	@Column(nullable = false)
	private String blogId;
	@Column(nullable = false)
	private long userId;
	@Column(nullable = false)
	private boolean isLiked;
	@Column(nullable = false)
	private Timestamp likedOn;
	
	private Timestamp modifiedOn;
	private String modifiedBy;

}
